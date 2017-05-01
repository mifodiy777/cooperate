<%--
  Created by IntelliJ IDEA.
  User: velievvm
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ include file="header.jsp" %>

    <div class="container">
        <div class="text-center">
            <div class="alert alert-danger" role="alert">
                <h1>Произошла ошибка, свяжитесь с администратором системы.</h1>

                <h3>${textError}</h3>
                <br>
                <button class="btn btn-lg btn-primary" onclick="window.history.back();">Вернуться назад</button>
            </div>
        </div>
    </div>
<jsp:include page="footer.jsp"/>