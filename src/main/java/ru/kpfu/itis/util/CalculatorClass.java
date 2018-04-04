package ru.kpfu.itis.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.*;

public class CalculatorClass {

    public double tf(String[] request, String term) {
        double result = 0;
        for (String word : request) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result / request.length;
    }

    //считается tfidf для запроса
    public List<Double> tfIdf(String[] request, List<String> idf_doc, List<String> terms) {
        double idf;
        double tfidf;
        List<Double> tfIdf = new ArrayList<>();
        for (int i = 0; i < terms.size(); i++) {
            for (int k = 0; k < idf_doc.size(); k++) {
                if (terms.indexOf(i) == idf_doc.indexOf(k)) {
                    String term = terms.get(i);
                    idf = Double.parseDouble(idf_doc.get(k));
                    tfidf = tf(request, term) * idf;
                    tfIdf.add(tfidf);
                }
                k = idf_doc.size();
            }
        }
        return tfIdf;
    }

    private double similarity(List<Double> a, List<Double> b) {
        double similarity = 0;
        if (a.size() == b.size()) {
            double numerator = 0;
            double denominator = 0;
            double denominator_sum1 = 0;
            double denominator_sum2 = 0;
            for (int i = 0; i < a.size(); i++) {
                numerator += a.get(i) * b.get(i);
                denominator_sum1 += a.get(i) * a.get(i);
                denominator_sum2 += b.get(i) * b.get(i);
            }

            denominator = Math.sqrt(denominator_sum1) * Math.sqrt(denominator_sum2);
            similarity = numerator / denominator;
            return similarity;

        } else {
            System.out.println("Ошибка! Длины векторов не совпадают");
            return -1;
        }
    }

    public static Map<String, Double> calculate(String request) throws IOException {
        CalculatorClass calculator = new CalculatorClass();

        String[] words = request.split(" "); // разбиение запроса на слова

        //загружаем список терминов
        List<String> terms = new ArrayList<>();
        ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{});
        Resource resource = appContext.getResource("information/terms1.txt");
        FileInputStream fstream = new FileInputStream(resource.getFile());
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF-8"));
        String strLine;
        while ((strLine = br.readLine()) != null) {
            strLine = strLine.substring(0, strLine.length() - 1);
            terms.add(strLine);
        }

        //лемматизация слова запроса
        Porter porter = new Porter();
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll("\\,|\\.|\\?|\\-|\\=|\\+|\\(|\\)|\\*|\\/|\\!|\"", "");
            words[i] = porter.stem(words[i]);
        }

        //загрузка idf_вектора терминов
        List<String> idf_doc = new ArrayList<>();
        resource = appContext.getResource("information/idf_result.txt");
        fstream = new FileInputStream(resource.getFile());
        br = new BufferedReader(new InputStreamReader(fstream));
        while ((strLine = br.readLine()) != null) {
            String[] idf_array = strLine.split("  ");
            idf_doc.addAll(Arrays.asList(idf_array));
        }

        //подсчет tfidf для запроса
        List<Double> a = calculator.tfIdf(words, idf_doc, terms);
        //System.out.println(a);

        //получение списка документов, в которых есть слова из запроса
        List<String> docs = new ArrayList<>();
        for (String word : words) {
            try {
                resource = appContext.getResource("information/invert/" + word + ".txt");
                fstream = new FileInputStream(resource.getFile());
            } catch (FileNotFoundException e) {
                System.out.println("Файла с таким именем не существует");
            }
            br = new BufferedReader(new InputStreamReader(fstream));
            while ((strLine = br.readLine()) != null) {
                strLine = strLine.substring(0, strLine.length() - 1);
                String[] d = strLine.split(" ");
                for (String doc : d) {
                    if (!docs.contains(doc)) {
                        docs.add(doc);
                    }
                }
            }
        }

        // подсчет косинуса для каждого слова
        Map<String, Double> similarityMapByDoc = new HashMap<>();
        for (String doc : docs) {
            List<String> elements = new ArrayList<>();
            try {
                resource = appContext.getResource("information/tfidf/" + doc + ".txt");
                fstream = new FileInputStream(resource.getFile());
            } catch (FileNotFoundException e) {
                System.out.println("Файла с таким именем не существует");
            }
            br = new BufferedReader(new InputStreamReader(fstream));
            while ((strLine = br.readLine()) != null) {
                strLine = strLine.substring(0, strLine.length() - 1);
                String[] numbers = strLine.split(" ");
                elements.addAll(Arrays.asList(numbers));
            }
            List<Double> b = new ArrayList<>();
            for (String element : elements) {
                b.add(Double.parseDouble(element));
            }

            double similarity = calculator.similarity(a, b);
            similarityMapByDoc.put(doc, similarity);
        }

        Answer.checking(similarityMapByDoc);
        System.out.println(similarityMapByDoc);
        return similarityMapByDoc;
    }

}