<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp"/>
<script type="text/javascript">
    $(function () {
        $.scrollUp();

        $('#costsTable').DataTable({
            "order": [
                [0, 'asc']
            ],
            "ajax": "getCostTypes",
            "fnDrawCallback": function () {
                $('a.deleteButton').off("click");
                $('a.deleteButton').popConfirm({
                    title: "Удалить? Будут удалены все записи этого типа.",
                    content: "",
                    placement: "bottom",
                    yesBtn: "Да",
                    noBtn: "Нет"
                });

            },
            "columns": [
                {"data": "name", 'title': 'Наименование типа расхода'},
                {
                    'title': 'Действия', "orderable": false, "searchable": false, className: "btnCost",
                    "render": function (data, type, full) {
                        var btnEdit = "<a href=\"#\" class=\"btn btn-primary btn-sm btnTable\" id=\"editCost_" + full.id +
                            '" onclick="editEntity(' + full.id + ',\'costType\');\"><span class="glyphicon glyphicon-pencil"/></span></a>';
                        var btnDelete = "<a href=\"#\" class=\"deleteButton btn btn-danger btn-sm btnTable\" data-placement=\"top\" id=\"deleteCost_" + full.id +
                            '" onclick="deleteEntity(' + full.id + ',\'deleteCostType\');\"><span class="glyphicon glyphicon-trash"/></span></a>';

                        return btnEdit + btnDelete;
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
            <h3 class="panel-title  pull-left">Типы расходов</h3>
            <div class="pull-right">
                <a href="<c:url value="/costsPage" />" class="btn btn-default pull-right"><span
                        class="glyphicon glyphicon-chevron-left"></span> Вернуться</a>
            </div>
            <div class="clearfix"></div>
        </div>
        <div class="panel-body">
            <table id="costsTable" class="table table-striped table-bordered cooperateTable" cellspacing="0"
                   width="100%"></table>
        </div>
    </div>
    <jsp:include page="footer.jsp"/>
