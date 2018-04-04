<#include "temp/mainTemplate.ftl">
<@main_template title="Поиск"/>

<#macro body>

<div class="allContent">
    <h3>Поиск 1</h3>
    <h3><a href="/search2">Поиск 2</a></h3>
    <br>

    <form action="/search1" method="POST">
        <input type="text" name="text" value="${(text)!}"/>
        <button type="submit">Поиск</button>
    </form>
    <#if results?has_content>
        <br>

        <p>Найдено</p>
        <#list results as result>
            <p><a href="${(result)!}">${(result)!}</a></p>
        </#list>
    <#else>
        <br>
        <p>${(answer)!}</p>
    </#if>


</div>
</#macro>