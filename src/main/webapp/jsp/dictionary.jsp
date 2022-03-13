<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp"/>
<script type="text/javascript">
    $(function () {
        $.scrollUp();

        $('#dictionaryTable').DataTable({
            "order": [
                [0, 'asc']
            ],
            "ajax": "getDictionary",
            "columns": [
                {"data": "name", 'title': 'Наименование типа'},
                {"data": "value", 'title': 'Значение типа'},
                {
                    'title': 'Действия', "orderable": false, "searchable": false, className: "btnCost",
                    "render": function (data, type, full) {
                        var btnEdit = "<a href=\"#\" class=\"btn btn-primary btn-sm btnTable\" id=\"editDictionary_" + full.id +
                            '" onclick="editEntity(' + full.id + ',\'dictionary\');\"><span class="glyphicon glyphicon-pencil"/></span></a>';
                        return btnEdit;
                    }
                }
            ]
        });

    });

</script>
<div class="container-fluid">
    <div id="formPanel"></div>
    <div class="panel panel-primary">
        <div class="panel-heading ">
            <h3 class="panel-title pull-left">Справочники</h3>
            <div class="btn-group pull-right">
                <button id="addDictionary" class="btn btn-success addBtn" onclick="saveEntity('dictionary')"><b><span
                        class="glyphicon glyphicon-plus"></span> Добавить</b></button>
            </div>
            <div class="clearfix"></div>
        </div>
        <div class="panel-body">
            <table id="dictionaryTable" class="table table-striped table-bordered cooperateTable" cellspacing="0"
                   width="100%"></table>
        </div>
    </div>
    <jsp:include page="footer.jsp"/>
