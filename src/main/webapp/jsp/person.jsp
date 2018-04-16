<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script>
    $(document).ready(function () {

        $("#personForm").validate({
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    success: function (html) {
                        $(".cooperateTable").DataTable().ajax.reload(null, false);
                        showSuccessMessage(html);
                        closeForm();
                        return false;
                    }
                });
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


    });

</script>
<div class="panel panel-success">
    <div class="panel-heading">
        <h4 class="panel-title">${type}</h4>
    </div>
    <div class="panel-body">
        <form:form modelAttribute="person" id="personForm" method="post" action="savePerson">
            <form:hidden path="id"/>
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
                    <label for="benefits" class="input-group-addon">Пенсионное удостоверение или другие льготы</label>
                    <form:input path="benefits" id="benefits" cssClass="form-control"/>
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
            <div class="col-md-6">
                <h4> Адрес </h4>
                <form:hidden path="address.id" id="address.id" cssClass="form-control"/>
                <div class="form-group input-group">
                    <label for="address.city" class="input-group-addon">Город</label>
                    <form:input path="address.city" id="address.city" cssClass="form-control"/>
                </div>
                <div class="form-group input-group">
                    <label for="address.street" class="input-group-addon">Улица</label>
                    <form:input path="address.street" id="address.street" cssClass="form-control"/>
                </div>
                <div class="form-group input-group">
                    <label for="address.home" class="input-group-addon">Дом</label>
                    <form:input path="address.home" id="address.home" cssClass="form-control"/>
                </div>
                <div class="form-group input-group">
                    <label for="address.apartment" class="input-group-addon">Квартира</label>
                    <form:input path="address.apartment" id="address.apartment" cssClass="form-control"/>
                </div>
            </div>
            <div class="row">
                <div class="col-md-4">
                    <table class="table">
                        <tbody>
                        <c:forEach items="${person.garagList}" var="garag">
                            <tr>
                                <th><b>Гараж: </b><c:out value="${garag.fullName}"/></th>
                                <td>
                                    <button class="btn btn-danger deleteAssign"
                                            onclick="deleteAssign(this,${garag.id})">
                                        Удалить назначение</button>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <div style="text-align: center" class="col-md-12">
                <button id="buttonAdd" type="submit" class="btn btn-success buttonForm">
                    <span class="glyphicon glyphicon-ok"></span> Сохранить
                </button>
                <button type="reset" onclick="closeForm();" class="btn btn-danger buttonForm">
                    <span class="glyphicon glyphicon-remove"></span> Закрыть
                </button>
            </div>
        </form:form>
    </div>
</div>


