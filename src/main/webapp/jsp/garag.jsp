<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<security:authorize access="hasRole('ROLE_ADMIN')" var="isAdmin"/>
<security:authentication property="principal" var="user"/>
<%--
  Created by IntelliJ IDEA.
  User: Кирилл
  Date: 25.07.2015
  Time: 17:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<script type="text/javascript">
    $(document).ready(function () {

        $("#typeDiv").html('<h4><c:out value="${type}"/></h4>');

        $("#garagForm").validate({
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    success: function (html) {
                        $("#garagTable").DataTable().ajax.url("allGarag").load(null, false);
                        showSuccessMessage(html);
                        $("#editPanel").hide();
                        $("#garagDiv").empty();
                        $("#addGaragButton").show();
                        return false;
                    },
                    error: function (xhr) {
                        if (xhr.status == 409) {
                            showErrorMessage(xhr.responseText);
                        }
                    }
                });
            },
            errorPlacement: function (error, element) {
                $(element).tooltipster('update', $(error).text());
                $(element).tooltipster('show');
            },
            success: function (label, element) {
                $(element).tooltipster('hide');
            }
        });


        $('#garagForm input').tooltipster({
            animation: 'slide',
            trigger: 'custom',
            onlyOne: false,
            position: 'top'
        });

        $('#searchPerson').on('keyup keypress', function (e) {
            if (e.which == 13) {
                e.preventDefault();
                loadPerson($(this).val());
            }
        });


    });

    $("#garagForm").submit(function (e) {
        e.preventDefault();
        $(this).valid();
        return false;
    });

    $("#searchPersonBtn").on("click", function() {
        if ($("#searchFormDiv").css('display') == 'none') {
            $("body").scrollTop(0);
            $("#addFormPersonDiv").hide();
            $("#searchFormDiv").show();
            $("#searchPersonBtn").val("Создать владельца");
        } else {
            $("#addFormPersonDiv").show();
            $("#searchFormDiv").hide();
            $("#searchPersonBtn").val("Найти владельца");
            $("#personResults").hide();
        }
    });

    function resetPerson() {
        $(".person").clearInputs();
    }

    function emptyGarag() {
        $("#addFormPersonDiv").empty();
        $("#searchDivPerson").empty();
        $("#personBtn").hide();

    }
    <c:if test="${isAdmin && editContribute}">
    function setOldContribute() {
        $.get('editContribute', {"idGarag":${garag.id}, "year":$("#year_select").val()}, function(html) {
            $("#modalDiv").html(html);
        });
    }
    </c:if>

    function loadPerson(pattern) {
        if (pattern.length > 3) {
            $.post("searchPerson", {pattern: pattern.trim()}).done(function (html) {
                $("#personResults").html(html).show();
                $(".choosePerson").click(function (e) {
                    e.preventDefault();
                    $.getJSON("getPerson", {personId:this.id}, function(person) {
                        $("#addFormPersonDiv").show();
                        $("#searchFormDiv").hide();
                        $("#searchPersonBtn").val("Найти владельца");
                        $("#personResults").hide();
                        $("input[name='person.id']").val(person.id);
                        $("#lastName").val(person.lastName);
                        $("#name").val(person.name);
                        $("#fatherName").val(person.fatherName);
                        $("#additionalInformation").val(person.additionalInformation);
                        $("#telephone").val(person.telephone);
                        $("input[name='person.address.id']").val(person.address.id);
                        $("input[name='person.address.city']").val(person.address.city);
                        $("input[name='person.address.street']").val(person.address.street);
                        $("input[name='person.address.home']").val(person.address.home);
                        $("input[name='person.address.apartment']").val(person.address.apartment);
                        $("input[name='person.benefits']").val(person.benefits);
                    });
                });
            });
        } else {
            $("#searchPerson").tooltip({placement: 'bottom', trigger: 'manual', font: '14px'}).tooltip('show');
        }
    }

    function checkDublPerson() {
        var pattern = $("#lastName").val() + " " + $("#name").val() + " " + $("#fatherName").val();
        if (pattern.length > 3) {
            $.post("searchPerson", {pattern: pattern.trim()}).done(function (html) {
                $("#personResults").empty();
                $("#personResults").html(html).hide();
                if ($("li a.choosePerson").size() != 0) {
                    $("#nameChecker").removeClass("btn-info btn-success");
                    $("#nameChecker").addClass("btn-danger");
                } else {
                    $("#nameChecker").removeClass("btn-info btn-danger");
                    $("#nameChecker").addClass("btn-success");
                }
            });
        } else {
            $("#nameChecker").addClass("btn-danger");
        }
    }


    function hideTooltip() {
        $('#searchPerson').tooltip('destroy');
    }

</script>
<c:if test="${isAdmin && editContribute}">
    <div class="row">
        <div class="col-md-6 col-md-offset-3">
            <h4 align="center">Внести старые долги</h4>

            <div class="form-group input-group">
                <label for="year_select" class="input-group-addon">Год</label>
                <select id="year_select" class="form-control">
                    <c:forEach items="${rents}" var="rent">
                        <option value="${rent.yearRent}">${rent.yearRent}</option>
                    </c:forEach>
                </select>
                    <span class="input-group-btn">
                        <button onclick="setOldContribute()" class="btn btn-info">Внести</button>
                    </span>
            </div>
        </div>

    </div>
</c:if>

<form:form modelAttribute="garag" id="garagForm" method="post" action="saveGarag">
    <form:hidden path="id"/>
    <div class="divider"><h4> Гараж: </h4></div>
    <div class="row">
        <div class="col-md-6">
            <div class="input-group">
                <label for="series" class="input-group-addon">Ряд</label>
                <form:input path="series" id="series" cssClass="required form-control"/>
            </div>
        </div>
        <div class="col-md-6">
            <div class="input-group">
                <label for="number" class="input-group-addon">Номер</label>
                <form:input path="number" id="number" cssClass="required form-control"/>
            </div>
        </div>
    </div>
    <div id="addFormPersonDiv">
        <div class="divider"><h4> Владелец: </h4></div>
        <div class="row">
            <form:hidden path="person.id" cssClass="person"/>
            <div class="col-md-4">
                <div class="input-group">
                    <label for="lastName" class="input-group-addon">Фамилия*</label>
                    <form:input path="person.lastName" id="lastName" cssClass="required form-control person"/>
                </div>
            </div>
            <div class="col-md-4">
                <div class="input-group">
                    <label for="name" class="input-group-addon">Имя*</label>
                    <form:input path="person.name" id="name" cssClass="required form-control person"/>
                </div>
            </div>
            <div class="col-md-4">
                <div class="input-group">
                    <label for="fatherName" class="input-group-addon">Отчество*</label>
                    <form:input path="person.fatherName" id="fatherName" cssClass="required form-control person"/>
                </div>
            </div>
        </div>
        <div class="row" style="text-align:center;">
            <button id="nameChecker" type="button" class="btn btn-info" onclick="checkDublPerson()">Проверить</button>
        </div>
        <div class="input-group">
            <label for="additionalInformation" class="input-group-addon">Дополнительная информация</label>
            <form:input path="person.additionalInformation" id="additionalInformation" cssClass="form-control person"/>
        </div>
        <div class="input-group">
            <label for="telephone" class="input-group-addon">Телефон*</label>
            <form:input path="person.telephone" id="telephone" cssClass="required form-control person"/>
        </div>
        <div class="row">
            <div class="col-md-6">
                <form:hidden path="person.address.id" id="address.id" cssClass="form-control person"/>
                <div class="input-group">
                    <label for="address.city" class="input-group-addon">Город</label>
                    <form:input path="person.address.city" id="address.city" cssClass="form-control person"/>
                </div>
            </div>
            <div class="col-md-6">
                <div class="input-group">
                    <label for="address.street" class="input-group-addon">Улица</label>
                    <form:input path="person.address.street" id="address.street" cssClass="form-control person"/>
                </div>
            </div>

        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="input-group">
                    <label for="address.home" class="input-group-addon">Дом</label>
                    <form:input path="person.address.home" id="address.home" cssClass="form-control person"/>
                </div>
            </div>
            <div class="col-md-6">
                <div class="input-group">
                    <label for="address.apartment" class="input-group-addon">Квартира</label>
                    <form:input path="person.address.apartment" id="address.apartment" cssClass="form-control person"/>
                </div>
            </div>
        </div>
        <div class="input-group">
            <label for="benefits" class="input-group-addon">Пенсионно удостоверение</label>
            <form:input path="person.benefits" id="benefits"
                        cssClass="form-control person"/>
        </div>
    </div>
    <div id="searchDivPerson">

        <div class="col-md-12">
            <input type="button" class="btn btn-primary" id="searchPersonBtn" value="Найти владельца">
        </div>

        <div class="row">
            <div id="searchFormDiv" class="col-md-6" style=" display:none;">
                <div class="input-group" id="searchDiv">
                    <label for="searchPerson">Владелец</label>

                    <div>
                        <input id="searchPerson" type="text" rel="tooltip"
                               data-original-title="Введите более 3 символов" onkeypress="hideTooltip();"
                               onclick="hideTooltip();" class="form-control"/>
                        <button type="button" id="buttonSearch"
                                onclick="loadPerson($('#searchPerson').val());"
                                class="btn btn-warning buttonCheck">
                            <span class="glyphicon glyphicon-search"></span> Найти
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div id="personResults" class="col-md-6">

            </div>
        </div>
    </div>
    <div id="personBtn" style="text-align: center; margin-top:20px" class="col-md-12">
        <button type="button" onclick="emptyGarag();"
                class="btn btn-info btn-lg"><span class="glyphicon glyphicon-repeat"></span> Создать пустой гараж
        </button>
        <button type="button" onclick="resetPerson();"
                class="btn btn-info btn-lg"><span class="glyphicon glyphicon-repeat"></span> Очистить форму владельца
        </button>
    </div>
    <div style="text-align: center; margin-top:20px" class="col-md-12">
        <button id="buttonAdd" class="btn btn-success btn-lg">
            <span class="glyphicon glyphicon-ok"></span> Сохранить
        </button>

        <button type="reset" onclick="closeForm('garag'); $('#addGaragButton').show();"
                class="btn btn-danger btn-lg"><span class="glyphicon glyphicon-remove"></span> Закрыть
        </button>
    </div>

</form:form>
