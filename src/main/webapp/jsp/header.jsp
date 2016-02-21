<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<security:authorize access="hasRole('ROLE_ADMIN')" var="isAdmin"/>
<security:authentication property="principal" var="user"/>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>ГК №23</title>
    <link rel="shortcut icon" href="<c:url value="/images/favicon.ico"/>">
    <link rel="stylesheet" href="<c:url value="/css/cooperate.css"/>" type="text/css">
    <link type="text/css" href="<c:url value="/css/jquery-ui.min.css"/>" rel="stylesheet"/>
    <link rel="stylesheet" href="<c:url value="/css/bootstrap.min.css"/>" type="text/css">
    <link type="text/css" href="<c:url value="/css/datepicker.css"/>" rel="stylesheet"/>
    <link type="text/css" href="<c:url value="/css/dataTables.bootstrap.css"/>" rel="stylesheet"/>
    <link type="text/css" href="<c:url value="/css/tooltipster.css"/>" rel="stylesheet"/>
    <link type="text/css" href="<c:url value="/css/pill.css"/>" rel="stylesheet"/>
    <link type="text/css" href="<c:url value="/css/bootstrap-modal.css"/>" rel="stylesheet"/>
    <link type="text/css" href="<c:url value="/css/levelNav.css"/>" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value="/js/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery-ui.min.js"/>"></script>
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
    <script type="text/javascript" src="<c:url value="/js/locale/bootstrap-datepicker.ru.js"/>"></script>


    <script type="text/javascript">

        $(document).ajaxStart(function() {
            $('html').css({'cursor' : 'wait'});
        });

        $(document).ajaxStop(function() {
            $('html').css({'cursor' : 'default'});
        });

        $.extend($.fn.dataTable.defaults, {
            "language": {
                "url": '<c:url value="/js/locale/dataTablesRu.json"/>'
            }
        });

        function showSuccessMessage(html) {
            $("#messages").removeClass("alert-danger");
            $("#messages").addClass("alert-info");
            $("#messages").html(html).show(800).delay(4000).hide(1000);
        }


        function showErrorMessage(html) {
            $("#messages").removeClass("alert-info");
            $("#messages").addClass("alert-danger");
            $("#messages").html(html).show(800).delay(4000).hide(1000);
        }
        function openNewRent() {
            var now = new Date();
            if (now.getMonth() == 3) {
                $.ajax({
                    method: "GET",
                    url: "checkYearRent",
                    data:{"year":now.getFullYear()},
                    success: function(html) {
                        $("#modalDiv").html(html);
                    },
                    error:function(xhr) {
                        showSuccessMessage(xhr.responseText);
                    }
                });
            }
        }

        function updateFines() {
            var now = new Date();
            $('#modalLoading').modal();
            $.ajax({
                method: "POST",
                url: "updateFines",
                success: function(html) {
                    $('#modalLoading').modal('hide');
                    showSuccessMessage(html);
                },
                error:function(xhr) {
                    $('#modalLoading').modal('hide');
                    showErrorMessage(xhr.responseText);
                }
            })
        }

        $(document).ready(function () {
            var now = new Date();
            if ($.cookie('day_sync') == null) {
                openNewRent();
                updateFines();
            }
        })
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
                        Оплата</a>
                </li>
                <li class="dropdown">
                    <a id="reportPageBtn" href="#" class="dropdown-toggle" data-toggle="dropdown"
                       role="button" aria-expanded="false"><span class="glyphicon glyphicon-stats"></span> Отчеты </a>
                    <ul class="dropdown-menu" role="menu">
                        <li>
                            <a href="<c:url value="/reportAllPerson"/>"><span class="glyphicon glyphicon-open-file"></span> Общий список</a>
                        </li>
                        <li>
                            <a href="<c:url value="/reportBenefitsPerson"/>"><span class="glyphicon glyphicon-open-file"></span> Список льготников</a>
                        </li>
                        <li>
                            <a href="<c:url value="/reportContribute"/>"><span class="glyphicon glyphicon-open-file"></span> Список должников</a>
                        </li>
                        <li>
                             <a href="<c:url value="/reportOther"/>">Дополнительные отчеты</a>
                        </li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a id="adminPageBtn" href="#" class="dropdown-toggle" data-toggle="dropdown"
                       role="button" aria-expanded="false"><span class="glyphicon glyphicon-cog"></span>
                        Администрирование </a>
                    <ul class="dropdown-menu" role="menu">
                        <li class="<c:if test="${pageContext.request.servletPath eq '/jsp/history.jsp'}">active</c:if>">
                            <a href="<c:url value="/historyPage"/>"><span class="glyphicon glyphicon-header"></span>
                                История</a>
                        </li>
                        <li class="<c:if test="${pageContext.request.servletPath eq '/jsp/members.jsp'}">active</c:if>">
                            <a href="<c:url value="/membersPage"/>"><span class="glyphicon glyphicon-tower"></span>
                                Члены правления</a>
                        </li>
                        <c:if test="${isAdmin}">
                            <li class=" <c:if test="${pageContext.request.servletPath eq '/jsp/fileUploadPage.jsp'}">active</c:if>">
                                <a href="<c:url value="/fileUploadPage"/>"><span
                                        class="glyphicon glyphicon-save"></span>
                                    Загрузка</a>
                            </li>
                        </c:if>
                        <c:if test="${isAdmin}">
                            <li class=" <c:if test="${pageContext.request.servletPath eq '/jsp/oldRent.jsp'}">active</c:if>">
                                <a href="<c:url value="/oldRentPage"/>"><span class="glyphicon glyphicon-usd"></span>
                                    Внести прошлые начисления</a>
                            </li>
                        </c:if>
                    </ul>
                </li>
                <li>
                    <a href="#" onclick="updateFines()"><span class="glyphicon glyphicon-refresh"></span>
                        Обновить</a>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a style="cursor:default"> <c:out value="${user.username}"/> </a>
                </li>
                <li>
                    <a href="<c:url value="/j_spring_security_logout"/>" title="Выйти">
                        <span class="glyphicon glyphicon-off" aria-hidden="true"></span>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
<c:if test="${isAdmin}">
    <input type="hidden" id="role" value="1"/>
</c:if>
<c:if test="${!isAdmin}">
    <input type="hidden" id="role" value="0"/>
</c:if>
<div id="messages" class="pull-right alert alert-info fade in " style="width: 20%; display: none"></div>
<div id="modalDiv"></div>
<div id="loadingDiv">
    <div id="modalLoading" class="modal fade" role="dialog">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-body">
                    <img src="<c:url value="/images/ajax_blue_.gif"/>" id="loading-indicator"/>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="wrap">


