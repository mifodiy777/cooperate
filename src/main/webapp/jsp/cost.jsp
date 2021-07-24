<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript" src="<c:url value="/js/typeahead.js"/>"></script>
<script type="text/javascript">
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
                validError(error, element);
            },
            success: function (label, element) {
                validSuccess(label, element);
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

        var costTypeData = [];
        var costTypeIds = new Object();

        function getCostType() {
            $.post("getTypes", function (data) {
                costTypeData = $.map(data, function (type) {
                    costTypeIds[type.name] = type.id;
                    return type.name;
                });
                costType.clear();
                costType.add(costTypeData);
            });
        }

        getCostType();

        var costType = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.whitespace,
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            local: costTypeData
        });

        $('#typeName').typeahead({
                minLength: 3
            },
            {
                limit: 15,
                name: 'costType',
                source: costType
            }).bind('typeahead:select', function (ev, suggestion) {
            $("#typeId").val(costTypeIds[suggestion]);
            $("#statusType").text("Существующий тип").attr('class', 'label label-warning');
        }).keypress(function (e) {
            $("#statusType").text("Новый тип").attr('class', 'label label-primary');
            $("#typeId").val("");
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
                <div class="col-md-3">
                    <form:hidden path="type.id" id="typeId" cssClass="select-typehead"/>
                    <div class="form-group">
                        <label for="typeName">Наименование*</label>
                        <form:input path="type.name" id="typeName" placeholder="Найти"
                                    cssClass="form-control typeahead"/>
                        <span class="help-block"></span>
                    </div>
                </div>
                <div class="col-md-1 status">
                    <h2><span id="statusType" class="label label-primary">Новый тип</span></h2>
                </div>
            </div>
            <div class="row">
                <div class="col-md-2">
                    <div class="form-group">
                        <label for="date">Дата*</label>
                        <form:input path="date" id="date" cssClass="required form-control dateRU"/>
                        <span class="help-block"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-2">
                    <div class="form-group">
                        <label for="date">Сумма/Руб*</label>
                        <form:input path="money" id="money" cssClass="required digits form-control"/>
                        <span class="help-block"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="form-group">
                        <label for="description">Дополнительная информация</label>
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

