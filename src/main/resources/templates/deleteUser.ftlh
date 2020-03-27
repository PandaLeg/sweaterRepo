<#import "parts/common.ftlh" as c>

<@c.page>
    DeleteUser
    <form action="/user/delete" method="post">
        <input type="hidden" name="userId" value="${user.id}">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" class="btn btn-primary">Delete</button>
    </form>
</@c.page>