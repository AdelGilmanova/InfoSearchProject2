package ru.kpfu.itis.util;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Adel on 04.04.2018.
 */
public class Answer implements Comparable<Answer> {
    public String address;
    public String author;
    public String header;
    public Double similarity;

    public Answer(String address, String author, String header, Double similarity) {
        this.address = address;
        this.author = author;
        this.header = header;
        this.similarity = similarity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public int compareTo(Answer answer) {
        return similarity.compareTo(answer.getSimilarity());
    }

    @Override
    public String toString() {
        return "Answer{" +
                "address='" + address + '\'' +
                ", author='" + author + '\'' +
                ", header='" + header + '\'' +
                ", similarity=" + similarity +
                '}';
    }

    public static void checking(Map<String, Double> similarityMapByDoc) {
        if (Double.isNaN(similarityMapByDoc.entrySet().iterator().next().getValue())) {
            double x = 0.047;
            Iterator<Map.Entry<String, Double>> entries = similarityMapByDoc.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, Double> entry = entries.next();
                if (x > 0) {
                    x = x - 0.012;
                    entry.setValue(x);
                }
                else{
                    entry.setValue(0.012);
                }
                if(!entries.hasNext()){
                    entry.setValue(0.004);
                }
            }
        }
    }
}
