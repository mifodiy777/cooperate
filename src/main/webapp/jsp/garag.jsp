<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<security:authorize access="hasRole('ROLE_ADMIN')" var="isAdmin"/>
<security:authentication property="principal" var="user"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    $(document).ready(function () {

        $("#garagForm").validate({
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    success: function (html) {
                        $(".cooperateTable").DataTable().ajax.reload(null, false);
                        showSuccessMessage(html);
                        closeForm();
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

        $("")

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
            $("#searchPerson").focus();
            $("#searchPersonBtn").val("Создать владельца");

        } else {
            $("#addFormPersonDiv").show();
            $("#searchFormDiv").hide();
            $("#lastName").focus();
            $("#searchPersonBtn").val("Найти владельца");
            $("#personResults").hide();
        }
    });

    <c:if test="${isAdmin && isOldGarag}">
    function setOldContribute() {
        $.get('editContribute', {"idGarag":${garag.id}, "year":$("#year_select").val()}, function(html) {
            $("#modalDiv").html(html);
        });
    }
    </c:if>


</script>
<div class="panel panel-success">
    <div class="panel-heading">
        <h4 class="panel-title">${type}</h4>
    </div>
    <div class="panel-body">
        <form:form modelAttribute="garag" id="garagForm" method="post" action="saveGarag">
            <form:hidden path="id"/>
            <div class="divider"><h4> Гараж: </h4></div>
            <div class="row">
                <div class="col-md-4">
                    <div class="input-group">
                        <label for="series" class="input-group-addon">Ряд*</label>
                        <form:input path="series" id="series" cssClass="required form-control"/>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="input-group">
                        <label for="number" class="input-group-addon">Номер*</label>
                        <form:input path="number" id="number" cssClass="required form-control"/>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="input-group">
                        <label for="oldContribute" class="input-group-addon">Долг прошлых лет</label>
                        <form:input path="oldContribute" id="oldContribute" cssClass="form-control"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="input-group">
                        <label for="additionalInformation" class="input-group-addon">Дополнительная информация</label>
                        <form:input path="additionalInformation" id="additionalInformation" cssClass="form-control"/>
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
                            <form:input path="person.fatherName" id="fatherName"
                                        cssClass="required form-control person"/>
                        </div>
                    </div>
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
                            <form:input path="person.address.street" id="address.street"
                                        cssClass="form-control person"/>
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
                            <form:input path="person.address.apartment" id="address.apartment"
                                        cssClass="form-control person"/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="input-group">
                            <label for="benefits" class="input-group-addon">Пенсионно удостоверение</label>
                            <form:input path="person.benefits" id="benefits"
                                        cssClass="form-control person"/>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group input-group">
                            <form:checkbox path="person.memberBoard" id="memberBoard"
                                           cssClass="form-control"
                                           name="fancy-checkbox-success" autocomplete="off"/>
                            <div class="[ btn-group ]">
                                <label for="memberBoard" class="[ btn btn-info ]">
                                    <span class="[ glyphicon glyphicon-ok ]"></span>
                                    <span> </span>
                                </label>
                                <label for="memberBoard" class="[ btn btn-default ]">
                                    Член правления
                                </label>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
            <div id="searchDivPerson">

                <div class="col-md-12">
                    <button type="button" class="btn btn-primary" id="searchPersonBtn"><span
                            class="glyphicon glyphicon-search"></span> Найти владельца
                    </button>
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
            <c:if test="${isAdmin && isOldGarag}">
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
                        <button type="button" onclick="setOldContribute()" class="btn btn-info">Внести</button>
                    </span>
                        </div>
                    </div>
                </div>
            </c:if>
            <div id="personBtn" style="text-align: center; margin-top:20px" class="col-md-12">
                <button type="button" onclick="emptyGarag();"
                        class="btn btn-info"><span class="glyphicon glyphicon-repeat"></span> Создать пустой
                    гараж
                </button>
                <button type="button" onclick="resetPerson();"
                        class="btn btn-info"><span class="glyphicon glyphicon-repeat"></span> Очистить форму
                    владельца
                </button>
            </div>
            <div style="text-align: center; margin-top:20px" class="col-md-12">
                <button id="buttonAdd" class="btn btn-success">
                    <span class="glyphicon glyphicon-ok"></span> Сохранить
                </button>

                <button type="reset" onclick="closeForm('garag'); $('#addGaragButton').show();"
                        class="btn btn-danger"><span class="glyphicon glyphicon-remove"></span> Закрыть
                </button>
            </div>

        </form:form>
    </div>
</div>

