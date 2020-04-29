<%--
  Created by IntelliJ IDEA.
  User: KuzminKA
  Date: 15.09.2015
  Time: 19:31:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>
<script type="text/javascript">

    $(document).ready(function () {

        $.scrollUp();

        var now = new Date();
        $("#year").val(now.getFullYear());

        $('#paymentTable').DataTable({
            "order": [
                [0, 'desc']
            ],
            "ajax": {
                url: "payments",
                data: {
                    setYear: "${setYear}"
                }
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
                {"data": "number", 'title': 'Платеж', className: "series"},
                {"data": "datePay", 'title': 'Дата', type: 'de_date', targets: 1, "searchable": false},
                {"data": "garag", 'title': 'Гараж', "searchable": false},
                {"data": "fio", 'title': 'ФИО'},
                {"data": "pay", 'title': 'Сумма', className: "pay_style", "searchable": false},
                {
                    'title': 'Действия',
                    className: "btnPayments",
                    "searchable": false,
                    "render": function (data, type, full) {
                        var del = "<a href=\"#\" class=\"deleteButton btn btn-danger btn-sm\" data-placement=\"top\" id=\"deletePayment_" + full.id +
                            "\" onclick=\"deletePayment('" + full.id + "');\"><span class=\"glyphicon glyphicon-trash\"/></span></a>"
                        return "<a href=\"printOrder/" + full.id +
                            "\" class=\"btn btn-info btn-sm\" target=\"_blank\"><span class=\"glyphicon glyphicon-print\"/></span></a>" + del;


                    }
                }
            ]
        });
    });

</script>
<div class="container-fluid">
    <input type="hidden" id="setYear" value="${setYear}">

    <div class="panel with-nav-tabs panel-primary">
        <div class="panel-heading">
            <ul class="nav nav-tabs nav-justified">
                <c:forEach items="${years}" var="year">
                    <li role='presentation' <c:if test="${setYear eq year}">class="active"</c:if>>
                        <a class="seriesLink"
                           href="<c:url value="/paymentsPage">
                        <c:param name="year" value="${year}"/></c:url>">
                            <c:out value="${year}"/>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-md-3">
                    <h3>Платежи ${setYear} года</h3>
                    Общее количество: <span id="count" class="badge"></span>
                </div>
            </div>
            <table id="paymentTable" class="table table-striped table-bordered" cellspacing="0" width="100%"></table>
        </div>
    </div>
    <jsp:include page="footer.jsp"/>
