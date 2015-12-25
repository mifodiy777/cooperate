<%--
  Created by IntelliJ IDEA.
  User: velievvm
  Date: 16.07.15
  Time: 19:47
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html>
<head>
    <title>Cooperate</title>
    <link rel="shortcut icon" href="<c:url value="/images/favicon.ico"/>">
    <link type="text/css" href="<c:url value='/css/bootstrap.min.css'/>" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/js/bootstrap.min.js'/>"></script>
   <style>
        body {
            background-color: #F3F3F3;
            background-image: url(<c:url value="/images/login_back.jpg"/>);
        }

        .vertical-offset-100 {
            padding-top: 150px;
        }

        input:-webkit-autofill, input:-webkit-autofill:hover, input:-webkit-autofill:focus {
            -webkit-box-shadow: 0 0 0px 1000px white inset;
        }

        .panel-footer {
            background-color: white;
        }
    </style>
</head>
<body>
<div id="login-box" class="container">
    <div class="row vertical-offset-100">
        <div class="col-md-4 col-md-offset-4">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Войдите</h3>
                </div>
                <div class="panel-body">
                    <form accept-charset="UTF-8" role="form" id='loginForm' name='loginForm'
                          action="<c:url value='/j_spring_security_check' />" method='POST'>
                        <fieldset>
                            <div class="form-group <c:if test="${not empty error}">has-error</c:if>">
                                <input class="form-control" placeholder="Логин" name="username" type="text"
                                       required="" autofocus="">
                            </div>
                            <div class="form-group <c:if test="${not empty error}">has-error</c:if>">
                                <input class="form-control" placeholder="Пароль" name="password" type="password"
                                       value="" required="">
                            </div>
                            <button type="submit" class="btn btn-success btn-block">Войти</button>
                            <button type="reset" class="btn btn-default btn-block">Сбросить</button>
                        </fieldset>
                    </form>
                </div>
                <c:if test="${not empty error}">
                    <div class="panel-footer">
                        <div id="loginError">${error}</div>
                    </div>
                </c:if>
                <c:if test="${not empty msg}">
                    <div class="panel-footer">
                        <div id="loginMsg">${msg}</div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
</html>