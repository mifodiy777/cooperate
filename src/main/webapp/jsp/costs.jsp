<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp"/>
<script name="text/javascript">
    $(function () {
        $.scrollUp();

        $('#costsTable').DataTable({
            "order": [
                [0, 'asc']
            ],
            "ajax": {
                "url": "getCosts",
                "type": "POST"
            },
            "fnCreatedRow": function (nRow, aData) {
                $(nRow).attr('id', 'costTR_' + aData.id);
            },
            "columns": [
                {"data": "name", 'title': 'Наименование расхода'},
                {"data": "date", 'title': 'Дата', "searchable": false},
                {"data": "description", 'title': 'Описание', "searchable": false}
            ]
        });

    });

</script>
<div class="container-fluid">
    <div id="formPanel"></div>
    <div class="panel panel-primary">
        <div class="panel-heading ">
            <h3 class="panel-title pull-left">Расходы ГК</h3>
            <button id="addCost" class="btn btn-success pull-right addBtn" onclick="saveEntity('cost')"><b><span
                    class="glyphicon glyphicon-plus"></span> Добавить</b></button>
            <div class="clearfix"></div>
        </div>
        <div class="panel-body">
            <table id="costsTable" class="table table-striped table-bordered cooperateTable" cellspacing="0"
                   width="100%"></table>
        </div>
    </div>
</div>