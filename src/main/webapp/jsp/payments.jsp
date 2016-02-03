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

        var table = $('#paymentTable').DataTable({
            "order": [
                [ 0, 'desc' ]
            ],
            "ajax": "payments?setYear=${setYear}",
            "fnDrawCallback": function () {
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
                {"data": "number", 'title': 'Платеж'},
                {"render": function(data, type, full) {
                    return  full.datePayment.dayOfMonth + " /" + (full.datePayment.month + 1) + "/" +
                            full.datePayment.year;

                }, 'title': 'Дата'},
                {"render": function(data, type, full) {
                    return  full.garag.series + "-" + full.garag.number;

                }, 'title': 'Гараж'},
                {"data": "fio", 'title': 'ФИО'},
                {"render": function(data, type, full) {
                    return  (full.contributePay + full.contLandPay + full.contTargetPay +
                            full.finesPay + full.additionallyPay + full.pay).toFixed(2) + " руб.";

                }, 'title': 'Сумма'},
                { 'title': 'Действия', "render": function (data, type, full) {
                    var del = "";
                    if ($("#role").val() == 1) {
                        del = "<a href=\"#\" class=\"btnTable deleteButton btn btn-danger btn-sm\" data-placement=\"top\" id=\"deletePayment_" + full.id +
                                "\" onclick=\"deletePayment('" + full.id + "');\"><span class=\"glyphicon glyphicon-trash\"/></span></a>"
                    }
                    return "<a href=\"printOrder/" + full.id +
                            "\" class=\"btnTable btn btn-info btn-sm\" target=\"_blank\"><span class=\"glyphicon glyphicon-print\"/></span></a>" + del;


                }}
            ]
        });
    });

    function deletePayment(id) {
        if (id == "") {
            showErrorMessage("Не найден ID !");
        } else {
            $.ajax({
                url: "deletePayment/" + id,
                type: "post",
                success: function (html) {
                    showSuccessMessage(html);
                    $("#paymentTable").DataTable().ajax.url("payments?setYear=${setYear}").load(null, false);
                },
                error: function (xhr) {
                    if (xhr.status == 409) {
                        showErrorMessage(xhr.responseText);
                    }
                }
            });
        }
    }

    function reportProfit(year) {
        window.location.href = "reportProfit/" + year;
    }


</script>
<div class="container">
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

                <div class="col-md-3 - col-md-offset-6 center">
                    <h3>
                        <button class="btn btn-primary" onclick='$("#profitDiv").toggle()'>Отчет о доходах</button>
                    </h3>
                    <div id="profitDiv" style="display:none;">
                        <div class="input-group form-group">
                            <label for="year" class="input-group-addon">Год*</label>
                            <select id="year" class="form-control ">
                                <c:forEach items="${rents}" var="rent">
                                    <option value="${rent.yearRent}">${rent.yearRent}</option>
                                </c:forEach>
                            </select>
                            <span class="input-group-btn">
                                <button class="btn btn-primary" onclick='reportProfit($("#year").val());'>Подготовить
                                </button>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
            <table id="paymentTable" class="table table-striped table-bordered" cellspacing="0" width="100%"></table>
        </div>
    </div>
    <jsp:include page="footer.jsp"/>
