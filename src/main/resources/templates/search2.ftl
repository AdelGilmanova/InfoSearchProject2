<#-- @ftlvariable name="results" type="java.util.TreeSet<ru.kpfu.itis.util.Answer>" -->
<#include "temp/mainTemplate.ftl">
<@main_template title="Поиск"/>

<#macro body>

<div class="allContent">
    <h3><a href="/search1">Поиск 1</a></h3>
    <h3>Поиск 2</h3>
<br>

    <form action="/search2" method="POST">
        <input type="text" name="text" value="${(text)!}"/>
        <button type="submit">Поиск</button>
    </form>
    <#if results?has_content>
        <br>


    <div align="justify">
        <#list results as result>
            <p>Автор: ${(result.getAuthor())!}</p>
            <p><a href="${(result.getAddress())!}">${(result.getHeader())!}</a></p>
            <p>${(result.getSimilarity())!}</p>
            <hr>
        </#list>
    </div>
    <#else>
        <br>
        <p>${(answer)!}</p>
    </#if>

</div>
</#macro>