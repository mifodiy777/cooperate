<%--
  Created by IntelliJ IDEA.
  User: velievvm
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ include file="header.jsp" %>
<style>
    body {
        background-color: #F3F3F3;
        background-image: url(<c:url value="/images/404.jpg"/>);
        background-repeat: no-repeat;
        background-size: 100%;
        background-color: black;
    }

    .vertical-and-horizontal-offset {
        padding-top: 30%;
        padding-left: 65%;
    }

    input:-webkit-autofill, input:-webkit-autofill:hover, input:-webkit-autofill:focus {
        -webkit-box-shadow: 0 0 0px 1000px white inset;
    }

</style>

<body>
<div id="wrap">
    <div class="container">
        <div class="row vertical-and-horizontal-offset">
            <h1 style="color: #ffffff;">Cтраница не найдена</h1>
            <button class="btn btn-lg btn-primary" onclick="window.history.back();">Вернуться назад</button>
        </div>
    </div>
</div>
</body>
<jsp:include page="footer.jsp"/>
