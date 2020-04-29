<%--
  Created by IntelliJ IDEA.
  User: velievvm
  Date: 31.07.15
  Time: 13:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>

<style>
    body {
        background-color: #eee;
        background-image: url(<c:url value="/images/furley_bg.png"/>);
        background-size: 100%;
        background-color: darkgray;
    }

    .vertical-and-horizontal-offset {
        padding-top: 20%;
    }
</style>

<div class="container">
    <div class="vertical-and-horizontal-offset">
        <h1 style="text-align: center;">Доступ запрещен!</h1>
        <p style="color: #f3a02c; text-align: center">К сожалению, у Вас нет прав доступа к запрашиваемой странице</p>
        <p style="text-align: center;"><a onclick="window.history.back();">Вернуться на предыдущую страницу</a></p>
    </div>
</div>




