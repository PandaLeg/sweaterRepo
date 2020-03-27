<#import "parts/common.ftlh" as c>

<@c.page>
    <form action="/delete" method="post">
        <input type="hidden" name="messageId" value="${message.id}">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" class="btn btn-primary">Delete this message</button>
    </form>
</@c.page>