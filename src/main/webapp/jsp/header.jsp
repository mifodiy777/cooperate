<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>ГК №23</title>
    <link rel="shortcut icon" href="<c:url value="/images/favicon.ico"/>">
    <link rel="stylesheet" href="<c:url value="/css/cooperate.css"/>" type="text/css">
    <link rel="stylesheet" href="<c:url value="/css/bootstrap.min.css"/>" type="text/css">
    <link type="text/css" href="<c:url value="/css/datepicker.css"/>" rel="stylesheet"/>
    <link type="text/css" href="<c:url value="/css/dataTables.bootstrap.css"/>" rel="stylesheet"/>
    <link type="text/css" href="<c:url value="/css/tooltipster.css"/>" rel="stylesheet"/>
    <link type="text/css" href="<c:url value="/css/image.css"/>" rel="stylesheet"/>
    <link type="text/css" href="<c:url value="/css/bootstrap-modal.css"/>" rel="stylesheet"/>
    <link type="text/css" href="<c:url value="/css/levelNav.css"/>" rel="stylesheet"/>
    <link type="text/css" href="<c:url value="/css/magic-check.min.css"/>" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value="/js/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.validate.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.dataTables.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/validate.customMethod.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/bootstrap.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/bootstrap-datepicker.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.form.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.popconfirm.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.tooltipster.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/validate.messages_ru.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/dataTables.bootstrap.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.scrollUp.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/bootstrap-modal.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/bootstrap-modalmanager.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.cookie.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/formatted-numbers.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/de-date.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/locale/bootstrap-datepicker.ru.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/cooperate.js"/>"></script>
    <script type="text/javascript">
        $(document).ajaxStart(function () {
            $('html').css({'cursor': 'wait'});
        });

        $(document).ajaxStop(function () {
            $('html').css({'cursor': 'default'});
        });

        $.extend($.fn.dataTable.defaults, {
            "language": {
                "url": '<c:url value="/js/locale/dataTablesRu.json"/>'
            }
        });

    </script>
</head>
<div class="navbar navbar-custom navbar-static-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">ГК №23</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<c:url value="/"/>">ГК №23</a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="<c:if test="${pageContext.request.servletPath eq '/jsp/garags.jsp'}">active</c:if>">
                    <a href="<c:url value="/garagPage"/>"><span class="glyphicon glyphicon-home"></span> Гаражи</a>
                </li>
                <li class="<c:if test="${pageContext.request.servletPath eq '/jsp/persons.jsp'}">active</c:if>">
                    <a href="<c:url value="/persons"/>"><span class="glyphicon glyphicon-user"></span> Владельцы</a>
                </li>
                <li class="<c:if test="${pageContext.request.servletPath eq '/jsp/payments.jsp'}">active</c:if>">
                    <a href="<c:url value="/paymentsPage"/>"><span class="glyphicon glyphicon-credit-card"></span>
                        Платежи</a>
                </li>
                <li class="dropdown">
                    <a id="reportPageBtn" href="#" class="dropdown-toggle" data-toggle="dropdown"
                       role="button" aria-expanded="false"><span class="glyphicon glyphicon-stats"></span> Отчеты </a>
                    <ul class="dropdown-menu" role="menu">
                        <li>
                            <a href="<c:url value="/reportAllPerson"/>"><span
                                    class="glyphicon glyphicon-open-file"></span> Общий список</a>
                        </li>
                        <li>
                            <a href="<c:url value="/reportBenefitsPerson"/>"><span
                                    class="glyphicon glyphicon-open-file"></span> Список льготников</a>
                        </li>
                        <li>
                            <a href="<c:url value="/reportContribute"/>"><span
                                    class="glyphicon glyphicon-open-file"></span> Список должников</a>
                        </li>
                        <li>
                            <a href="<c:url value="/reportOther"/>">Дополнительные отчеты</a>
                        </li>
                    </ul>
                </li>
                <li class="<c:if test="${pageContext.request.servletPath eq '/jsp/members.jsp'}">active</c:if>">
                    <a href="<c:url value="/membersPage"/>"><span class="glyphicon glyphicon-tower"></span>
                        Члены правления</a>
                </li>
                <li>
                    <a href="#" onclick="openNewRent()"><span class="glyphicon glyphicon-plus-sign"></span>
                        Новый период</a>
                </li>

            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class=" <c:if test="${pageContext.request.servletPath eq '/jsp/help.jsp'}">active</c:if>">
                    <a href="<c:url value="jsp/help.jsp"/>"><span class="glyphicon glyphicon-question-sign"></span>
                        Помощь</a>
                </li>
            </ul>
        </div>
    </div>
</div>
<div id="messages" class="pull-right alert alert-info fade in " style="width: 20%; display: none"></div>
<div id="modalDiv"></div>


