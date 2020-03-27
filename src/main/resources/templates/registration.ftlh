<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>

<@c.page>
    <div class = "mb-1">Add new user</div>
<#--  Восклицательный знак - это дефолт значение, на которое будет ссылаться ftlh, если не задан параметр message.
Иначе будет ошибка.-->
    ${message!" "}
    <@l.login "/registration" true/>
</@c.page>