<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script>
    $(document).ready(function () {

        $("#personForm").validate({
            submitHandler: function (form) {
                promtReason(form, '${garag.id}');
            },
            errorPlacement: function (error, element) {
                validPlaceError(error, element);
            },
            success: function (label, element) {
                validPlaceSuccess(label, element);
            }
        });


        $('#personForm input').tooltipster({
            trigger: 'custom',
            onlyOne: false,
            position: 'top'
        });

        $(".deleteAssign").popConfirm({
            title: "Вы хотите удалить назначение к этому гаражу?",
            content: "",
            placement: "right",
            yesBtn: "Да",
            noBtn: "Нет"
        });

        searchPerson();
        messBuilder();

    });

    function resetForm() {
        $("#personForm input:not(#personId)").val('');
    }

</script>
<div class="panel panel-success">
    <div class="panel-heading">
        <h4 class="panel-title"> Смена владельца: ${garag.fullName}</h4>
        <input type="hidden" id="idPastPerson" value="${person.id}">
    </div>
    <div class="panel-body">
        <div class="center">
            <h4>Режим смены владельца</h4>
        </div>
        <div class="row">
            <div class="col-md-5 col-md-offset-1">

                <input class="magic-radio" type="radio" name="changeGaragAll" id="oneChange" value="false"
                       checked="checked">
                <label for="oneChange"> Сменить только у текущего гаража</label>
                <c:if test="${fn:length(person.garagList)>1}">
                    <input class="magic-radio" type="radio" name="changeGaragAll" id="allChange" value="true">
                    <label for="allChange"> Сменить владельца у всех гаражей</label>
                </c:if>
            </div>
            <div class="col-md-6 center">
                <div id="deleteOwner" class="form-group input-group">
                    <input type="checkbox" id="deleteOldPerson" class="form-control"
                           name="fancy-checkbox-success" autocomplete="off" value="true" checked="checked"/>

                    <div class="[ btn-group ]">
                        <label for="deleteOldPerson" class="[ btn btn-danger ]">
                            <span class="[ glyphicon glyphicon-ok ]"></span>
                            <span> </span>
                        </label>
                        <label for="deleteOldPerson" class="[ btn btn-default ]">
                            Удалить прошлого владельца
                        </label>
                    </div>
                </div>
            </div>
        </div>
        <hr>
        <div class="center">
            <h4>Владелец</h4>
        </div>
        <div id="alertPanel" class="alert alert-info" role="alert" style="display:none; font-size:14pt"></div>
        <form:form modelAttribute="person" id="personForm" method="post" action="change">
            <form:hidden path="id" id="personId"/>
            <div id="addFormPersonDiv">
                <div class="col-md-6">
                    <div class="form-group input-group">
                        <label for="lastName" class="input-group-addon">Фамилия*</label>
                        <form:input path="lastName" id="lastName" cssClass="required form-control"/>
                    </div>
                    <div class="form-group input-group">
                        <label for="name" class="input-group-addon">Имя*</label>
                        <form:input path="name" id="name" cssClass="required form-control"/>
                    </div>
                    <div class="form-group input-group">
                        <label for="fatherName" class="input-group-addon">Отчество*</label>
                        <form:input path="fatherName" id="fatherName" cssClass="required form-control"/>
                    </div>
                    <div class="form-group input-group">
                        <label for="telephone" class="input-group-addon">Телефон*</label>
                        <form:input path="telephone" id="telephone" cssClass="required form-control"/>
                    </div>
                    <div class=" form-group input-group">
                        <label for="benefits" class="input-group-addon">Пенсионно удостоверение</label>
                        <form:input path="benefits" id="benefits" cssClass="form-control"/>
                    </div>
                </div>
                <div class="col-md-6">
                    <form:hidden path="address.id" id="addressId" cssClass="form-control"/>
                    <div class="form-group input-group">
                        <label for="city" class="input-group-addon">Город</label>
                        <form:input path="address.city" id="city" cssClass="form-control"/>
                    </div>
                    <div class="form-group input-group">
                        <label for="street" class="input-group-addon">Улица</label>
                        <form:input path="address.street" id="street" cssClass="form-control"/>
                    </div>
                    <div class="form-group input-group">
                        <label for="home" class="input-group-addon">Дом</label>
                        <form:input path="address.home" id="home" cssClass="form-control"/>
                    </div>
                    <div class="form-group input-group">
                        <label for="apartment" class="input-group-addon">Квартира</label>
                        <form:input path="address.apartment" id="apartment" cssClass="form-control"/>
                    </div>
                    <div class="form-group input-group">
                        <form:checkbox path="memberBoard" id="memberBoard"
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
                                        onclick="loadOwner($('#searchPerson').val());"
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
            <hr>
            <div style="text-align: center" class="col-md-12">
                <button id="buttonAdd" type="submit" class="btn btn-success buttonForm">
                    <span class="glyphicon glyphicon-ok"></span> Сохранить
                </button>
                <button type="reset" onclick="closeForm();" class="btn btn-danger buttonForm">
                    <span class="glyphicon glyphicon-remove"></span> Закрыть
                </button>
                <button type="button" onclick="resetForm();" class="btn btn-default buttonForm">Очистить
                </button>
            </div>
        </form:form>
    </div>
</div>


<div class="modal fade" tabindex="-1" role="dialog" id="promtModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" onclick="$('#reason').val('')"
                        aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Введите причину смены владельца?</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <input id="reason" name="reason" class=" form-control"/>
                    <span class="help-block"></span>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal" onclick="$('#reason').val('')">
                    <span class="glyphicon glyphicon-remove"></span> Отмена
                </button>
                <button type="submit" class="btn btn-success" id="applyBtn">
                    <span class="glyphicon glyphicon-ok"></span> Применить
                </button>
            </div>
        </div>
    </div>
</div>





