<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<script>
    $(document).ready(function () {

        $("#typeDiv").html('<h4><c:out value="${type}"/></h4>');


        $("#personForm").validate({
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    success: function (html) {
                        $("#listPerson").DataTable().ajax.url("allPerson").load(null, false);
                        if ($('#garagTable').length != 0) {
                            $("#garagTable").DataTable().ajax.url("allGarag?setSeries=" + $("#seriesNumber").val()).load(null, false);
                        }
                        showSuccessMessage(html);
                        $("#editPanel").hide();
                        $("#personDiv").empty();
                        $("#addPersonButton").show();
                        $('#addGaragButton').show();
                        return false;
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

        $('#personForm input').tooltipster({
            animation: 'slide',
            trigger: 'custom',
            onlyOne: false,
            position: 'top'
        });

        $(".buttonDelete").click(function() {
            var garag = this;
            $.post("deleteGaragInPerson", {idGarag:this.id}, function(html) {
                $(garag).parent().remove();
                $("#listPerson").DataTable().ajax.url("allPerson").load(null, false);
                if ($('#garagTable').length != 0) {
                    $("#garagTable").DataTable().ajax.url("allGarag?setSeries=" + $("#seriesNumber").val()).load(null, false);
                }
                showSuccessMessage(html);
            });

        })
    });

</script>

<form:form modelAttribute="person" id="personForm" method="post" action="savePerson">
    <form:hidden path="id"/>
    <div class="col-md-6">
        <div class="input-group">
            <label for="lastName" class="input-group-addon">Фамилия*</label>
            <form:input path="lastName" id="lastName" cssClass="required form-control"/>
        </div>
        <div class="input-group">
            <label for="name" class="input-group-addon">Имя*</label>
            <form:input path="name" id="name" cssClass="required form-control"/>
        </div>
        <div class="input-group">
            <label for="fatherName" class="input-group-addon">Отчество*</label>
            <form:input path="fatherName" id="fatherName" cssClass="required form-control"/>
        </div>
        <div class="input-group">
            <label for="telephone" class="input-group-addon">Телефон*</label>
            <form:input path="telephone" id="telephone" cssClass="required form-control"/>
        </div>
        <div class="input-group">
            <label for="benefits" class="input-group-addon">Пенсионно удостоверение</label>
            <form:input path="benefits" id="benefits" cssClass="form-control"/>
        </div>
    </div>
    <div class="col-md-6">
        <h4> Адрес </h4>
        <form:hidden path="address.id" id="address.id" cssClass="form-control"/>
        <div class="input-group">
            <label for="address.city" class="input-group-addon">Город</label>
            <form:input path="address.city" id="address.city" cssClass="form-control"/>
        </div>
        <div class="input-group">
            <label for="address.street" class="input-group-addon">Улица</label>
            <form:input path="address.street" id="address.street" cssClass="form-control"/>
        </div>
        <div class="input-group">
            <label for="address.home" class="input-group-addon">Дом</label>
            <form:input path="address.home" id="address.home" cssClass="form-control"/>
        </div>
        <div class="input-group">
            <label for="address.apartment" class="input-group-addon">Квартира</label>
            <form:input path="address.apartment" id="address.apartment" cssClass="form-control"/>
        </div>
    </div>
    <div style="text-align: center" class="col-md-12">
        <button id="buttonAdd" type="submit" class="btn btn-success btn-lg buttonForm">
            <span class="glyphicon glyphicon-ok"></span> Сохранить
        </button>
        <button type="reset" onclick="closeForm('person'); $('#addPersonButton').show();
        $('#addGaragButton').show();"
                class="btn btn-danger btn-lg buttonForm"><span class="glyphicon glyphicon-remove"></span> Закрыть
        </button>
    </div>
</form:form>
<div class="col-md-6">
    <c:forEach items="${person.garagList}" var="garag" varStatus="index">
        <div class="input-group garagRow">
            <input type="hidden" name="garagList[${index.index}].id" value="<c:out value="${garag.id}"/>"/>
            <h4><b>Гараж: </b><c:out value="Ряд: ${garag.series} Номер: ${garag.number}"/>
                <span id="${garag.id}" title="Удалить" class="glyphicon glyphicon-remove buttonDelete"></span>
            </h4>

        </div>
    </c:forEach>
</div>
