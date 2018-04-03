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

        <p>Найдено</p>
    <div align="justify">
        <#list results as result>
            <p><a href="${(result)!}">${(result)!}</a></p>
        </#list>
    </div>
    <#else><p>${(answer)!}</p>
    </#if>

</div>
</#macro>