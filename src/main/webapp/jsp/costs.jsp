<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp"/>
<script type="text/javascript">
    $(function () {
        $.scrollUp();

        $('#costsTable').DataTable({
            "order": [
                [0, 'desc']
            ],
            "ajax": "getCosts",
            "fnDrawCallback": function () {
                $('a.deleteButton').off("click");
                $('a.deleteButton').popConfirm({
                    title: "Удалить?",
                    content: "",
                    placement: "bottom",
                    yesBtn: "Да",
                    noBtn: "Нет"
                });

            },
            "columns": [
                {"data": "date", 'title': 'Дата', type: 'de_date', targets: 1, "searchable": false},
                {"data": "name", 'title': 'Наименование расхода'},
                {"data": "money", 'title': 'Сумма', "searchable": false},
                {"data": "description", 'title': 'Описание', "searchable": false, "orderable": false},
                {
                    'title': 'Удалить', "searchable": false, className: "btnCost",
                    "render": function (data, type, full) {
                        return "<a href=\"#\" class=\"deleteButton btn btn-danger btn-sm\" data-placement=\"top\" id=\"deleteCost_" + full.id +
                            '" onclick="deleteEntity(' + full.id + ',\'deleteCost\');\"><span class="glyphicon glyphicon-trash"/></span></a>'
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
            <h3 class="panel-title pull-left">Расходы</h3>
            <div class="btn-group pull-right">
                <button id="addCost" class="btn btn-success addBtn" onclick="saveEntity('cost')"><b><span
                        class="glyphicon glyphicon-plus"></span> Добавить</b></button>
                <a href="<c:url value="/costTypesPage" />" class="btn btn-default pull-right">Типы расходов</a>
            </div>
            <div class="clearfix"></div>
        </div>
        <div class="panel-body">
            <table id="costsTable" class="table table-striped table-bordered cooperateTable" cellspacing="0"
                   width="100%"></table>
        </div>
    </div>
    <jsp:include page="footer.jsp"/>
