<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp"/>
<script type="text/javascript">

    $(document).ready(function () {

        $.scrollUp();

       $('.cooperateTable').DataTable({
            "order": [
                [ 0, 'asc' ]
            ],
            "ajax": {
                url:"allGarag",
                data:{
                    setSeries: "${setSeries}"
                }
            },
            "fnCreatedRow": function (nRow, aData) {
                $(nRow).attr('id', 'garagTR_' + aData.id);
            },
            "fnDrawCallback": function () {
                $('a.deleteButton').off("click");
                $('a.deleteButton').popConfirm({
                    title: "Удалить?",
                    content: "",
                    placement: "bottom",
                    yesBtn: "Да",
                    noBtn: "Нет"
                });
                var oSettings = this.fnSettings();
                var iTotalRecords = oSettings.fnRecordsTotal();
                $("#count").html(iTotalRecords);
            },
            "columns": [
                {data:"number", "render": function(data, type, full) {
                    return '<a href=\"#\" onclick=\"editEntity(' + full.id +',\'garag\')\">' + full.number + '</a>'
                },'title': 'Гараж',type: 'natural',className: "series"},
                {"render": function(data, type, full) {
                    var vip = '';
                    if (full.person != null) {
                        if (full.person.memberBoard) {
                            vip = ' <span class="label label-warning">ЧП</span>';
                        }
                        return '<a href=\"#\" onclick=\"editEntity(' + full.person.personId +',\'person\')\">' + full.person.fio + vip + '</a>'
                    }
                    return ""
                }, "searchable": false, 'title': 'ФИО'},
                {"data":"person.phone","defaultContent": "","searchable": false, 'title': 'Телефон'},
                {"data": "person.address","defaultContent": "","searchable": false,  'title': 'Адрес'},
                {"data": "person.benefits","defaultContent": "","searchable": false,  'title': 'Льготы'},
                {'title': 'Действия',"searchable": false,className:"actionBtn","render": function (data, type, full) {
                    var del = "";
                    if ($("#roleAdmin").val()) {
                        del = '<a href="#" class="btnTable deleteButton  btn btn-danger btn-sm" title="Удалить гараж" data-placement="top" id="deleteGarag_' + full.id +
                                '" onclick="deleteEntity(' + full.id + ',\'deleteGarag\');"><span class="glyphicon glyphicon-trash"/></span></a>'
                    }
                    var actionsBtn = "";
                    if (full.person != null) {
                        actionsBtn = "<a href=\"#\" class=\"btnTable btn btn-info btn-sm\" title='Информация' onclick=\"infGarag(" + full.id +
                                ");\"><span class=\"glyphicon glyphicon-comment\"/></span></a><a href=\"#\" class=\"btnTable  btn btn-success btn-sm\" title='Оплатить' onclick=\"payGarag(" + full.id +",'default'" +
                                ");\"><span class=\"glyphicon glyphicon-shopping-cart\"/></span></a>" +
                                "<a href=\"#\" class=\"btnTable deleteButton  btn btn-warning btn-sm\"  title=\"Удалить назначение\" data-placement=\"top\" onclick=\"deleteEntity(" + full.id +
                                ',\'assignDelete\');\"><span class=\"glyphicon glyphicon-scissors\"/></span></a>';
                    }
                    return actionsBtn + del;
                }}
            ]
        });

    });


</script>
<style type="text/css">
    th, td {
        font-size: 15px
    }
</style>
<div class="container">
    <button class="btn btn-success addBtn" onclick="saveEntity('garag')">
        <span class="glyphicon glyphicon-plus"></span> Добавить гараж
    </button>
    <div id="formPanel"></div>
    <div class="panel with-nav-tabs panel-primary">
        <div class="panel-heading">
            <ul class="nav nav-tabs nav-justified">
                <c:forEach items="${series}" var="number">
                    <li role='presentation' <c:if test="${setSeries eq number}">class="active"</c:if>>
                        <a class="seriesLink"
                           href="<c:url value="/garagPage">
                        <c:param name="series" value="${number}"/></c:url>">
                            <c:out value="${number}"/>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="panel-body">
            <h3>Список гаражей ${setSeries} ряда</h3>
            Общее количество: <span id="count" class="badge"></span>
            <br>
            <table class="table table-striped table-bordered cooperateTable" cellspacing="0" width="100%"></table>
        </div>


    </div>
    <jsp:include page="footer.jsp"/>
