<#import "parts/common.ftlh" as c>

<@c.page>
    <!--Отфильтровать-->
    <#-- action - указывает обработчик, к которому обращаются данные формы при их отправке на сервер -->
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <#--   value="${filter!""}", говорит о том, что когда введём в поле тег,значение, то отобразим его в строке input -->
                <input type="text" name="filter" class="form-control" value="${filter!''}" placeholder="Search by tag">
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>

    <a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
        Add new message
    </a>
    <!--Добавить-->
    <div class="collapse <#if message??>show</#if>" id="collapseExample">
        <div class="form-group mt-3">
            <#-- Если action не указан, то данные будут отправлятся на обработку на текущую страницу -->
            <form method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <#--Выводим переменную textError, проверяем, что она существует
                    Будет возвращаться логическое значение, которое нужно привести к строке
                    Если у нас текстовое сообщение присутствует, то мы получим истинну и значит поле помечаем, как
                    is-invalid-->
                    <input type="text" class="form-control ${(textError??)?string('is-invalid', '')}"
                           value="<#if message??>${message.text}</#if>" name = "text" placeholder="Введите сообщение">
                    <#--Тут будет выводится ошибка-->
                    <#--Если будет ошибка в поле текст(true), то выведется сообщение из value, которое мы положили
                    в Map-->
                    <#if textError??>
                        <div class = "invalid-feedback">
                            ${textError}
                        </div>
                    </#if>
                </div>
                <div class="form-group">
                    <input type="text" class="form-control ${(tagError??)?string('is-invalid', '')}"
                           value="<#if message??>${message.tag}</#if>"
                           name = "tag" placeholder="Введите тэг">
                    <#--Тут будет выводится ошибка-->
                    <#if tagError??>
                        <div class = "invalid-feedback">
                            ${tagError}
                        </div>
                    </#if>
                </div>
                <div class="form-group">
                    <div class="custom-file">
                        <input type="file" name = "file" id = "customFile">
                        <label class="custom-file-label" for="customFile">Choose file</label>
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    </div>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Добавить</button>
                </div>
            </form>
        </div>
    </div>

    <!--Вывести-->
<#--    По очереди передаёт каждую строчку из БД-->
    <div class="card-columns">
        <#list messages as message>
            <div class="card my-3">
                <div>
                    <#if message.filename??>
                        <img src = "/img/${message.filename}" class="card-img-top" >
                    </#if>
                </div>
                <div class="m-2">
                    <span>${message.text}</span>
                    <i>${message.tag}</i>
                    <span> <a href="/delete/${message.id}">Delete</a> </span>
                </div>
                <!--
                authorName напрямую связан с getAuthorName()
                Всё методы getSomething могут быть заменены на обращение к полю (даже если оно не существует) с именем something
                -->
                <div class="card-footer text-muted">
                    ${message.authorName}
                </div>
            </div>
        <#else>
            No message
        </#list>
    </div>
</@c.page>