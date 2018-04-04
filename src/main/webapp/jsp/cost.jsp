<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script name="text/javascript">
    $(document).ready(function () {

        $("#costForm").validate({
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
                validPlaceError(error, element);
            },
            success: function (label, element) {
                validPlaceSuccess(label, element);
            }
        });

        $("#date").datepicker({
            format: "dd.mm.yyyy",
            endDate: "-0d",
            language: 'ru',
            todayBtn: 'linked',
            todayHighlight: true
        }).on('changeDate', function () {
            $(this).valid();
        });

        $('#costForm input').tooltipster({
            trigger: 'custom',
            onlyOne: false,
            position: 'top'
        });

        $("#costForm").submit(function (e) {
            e.preventDefault();
            $(this).valid();
            return false;
        });
    });


</script>
<div class="panel panel-success">
    <div class="panel-heading">
        <h4 class="panel-title">Режим добавления расходов</h4>
    </div>
    <div class="panel-body">
        <form:form modelAttribute="cost" id="costForm" method="post" action="saveCost">
            <form:hidden path="id"/>
            <div class="divider"><h4> Расходная накладная: </h4></div>
            <div class="row">
                <form:hidden path="type.id" id="typeId"/>
                <div class="col-md-4">
                    <div class="form-group input-group">
                        <label for="name" class="input-group-addon">Наименование*</label>
                        <form:input path="type.name" id="name" cssClass="required form-control"/>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="form-group input-group">
                        <label for="date" class="input-group-addon">Дата*</label>
                        <form:input path="date" id="date" cssClass="required form-control dateRU"/>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="form-group input-group">
                        <label for="description" class="input-group-addon">Дополнительная информация</label>
                        <form:input path="description" id="description" cssClass="form-control"/>
                    </div>
                </div>
            </div>
            <div style="text-align: center; margin-top:20px" class="col-md-12">
                <button id="buttonAdd" class="btn btn-success buttonForm">
                    <span class="glyphicon glyphicon-ok"></span> Сохранить
                </button>

                <button name="reset" onclick="closeForm('cost'); $('#addCost').show();"
                        class="btn btn-danger buttonForm"><span class="glyphicon glyphicon-remove"></span> Закрыть
                </button>
            </div>
        </form:form>
    </div>
</div>

