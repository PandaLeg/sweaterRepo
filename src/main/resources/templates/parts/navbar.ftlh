<#include "security.ftlh">
<#import "login.ftlh" as l>
<#-- navbar-expand-lg - изменяется в зависимости от ширины экрана.
Пример таких размеров sm - маленький, md - средний, lg - большой, xl...-->
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">Sweater</a>
<#--    Тут описывается кнопка Menu, которая будет появляться на экране, меньше чем lg-->
    <button class="navbar-toggler" type="button" data -toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <#--Тут описывается menu -->
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/main">Messages</a>
            </li>
            <#if isAdmin>
            <li class="nav-item">
                <a class="nav-link" href="/user">User list</a>
            </li>
            </#if>
            <#--If user authorization(from security.ftlh) then true else false -->
            <#if name!= "unknown">
                <li class="nav-item">
                    <a class="nav-link" href="/user/profile">Profile</a>
                </li>
            </#if>
        </ul>
        <div class="navbar-text mr-3">${name}</div>
        <#if isCheck>
            <@l.logout/>
        </#if>
    </div>
</nav>