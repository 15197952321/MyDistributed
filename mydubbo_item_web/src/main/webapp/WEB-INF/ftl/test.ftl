<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
freemark 模版<br>
1:取值
${item.title}<br>
${item.updated?date}<br>
${item.updated?datetime}<br>
${item.updated?string("yyyy-MM-dd HH:mm:ss")}<br>
${obj1?string[",000.00"]}<br>
2:判断和遍历<br>
<#if list?? && (list?size>0)>
        <#list list as item>
            ${item.title}<br>
        </#list>
    <#else>
       为空
</#if>
</body>
</html>