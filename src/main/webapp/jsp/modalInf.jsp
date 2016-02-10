<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type="text/javascript">
    $(document).ready(function () {
        $('#modalInf').modal();
    });

    function openAddingCount() {
        $("#setAddingDiv").show();
    }

    function setAddingCount() {
        var id = $("#idGarag").val();
        var count = $("#setAdding").val();
        if (count == "" || !$.isNumeric(count)) {
            $("#setAdding").tooltip({placement: 'bottom', trigger: 'manual', font: '14px'}).tooltip('show');
        } else {
            $.get("saveAddingCount", {id:id,count:count}, function(html) {
                $("#setAddingDiv").hide();
                $('#modalInf').modal('hide');
                showSuccessMessage(html);
                window.open('printNewOrder/' + id, '_blank');
            })
        }

    }

    function hideTooltipAdding() {
        $('#setAdding').tooltip('destroy');
    }

</script>

<div id="modalInf" class="modal fade" role="dialog">
    <div class="modal-dialog" style="width:95%;">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <input id="idGarag" type="hidden" value="${garag.id}">
                <h4 class="modal-title">Гараж ${garag.name}</h4>
            </div>
            <div class="modal-body">
                <div id="setAddingDiv" style="display:none">
                    <h4>Введите сумму дополнительного взноса</h4>
                    <input type="hidden" id="yearAdding">

                    <div class="form-group input-group">
                        <label for="setAdding" class="input-group-addon">Сумма</label>
                        <input id="setAdding" rel="tooltip"
                               data-original-title="Введите сумму"
                               onkeypress="hideTooltipAdding();" onclick="hideTooltipAdding();" class="form-control">
                        <span class="input-group-btn">
                            <button onclick="setAddingCount()" class="btn btn-info">Записать</button>
                        </span>
                    </div>
                </div>
                <h4>${fio} <c:if test="${garag.person.memberBoard}"><span class="label label-warning">Член правления</span> </c:if></h4>
                <h4>Долги</h4>
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Год</th>
                        <th>Годовой долг</th>
                        <th>Членский взнос</th>
                        <th>Аренда земли</th>
                        <th>Целевой взнос</th>
                        <th>Пени</th>
                        <th>Оставшиеся средства</th>                      
                    </tr>
                    </thead>
                    <c:forEach items="${garag.contributions}" var="c">
                        <%--  Проверить--%>
                        <c:if test="${(c.contribute+c.contLand+c.contTarget+c.fines) !=0}">
                            <tr>
                                <td>${c.year}</td>
                                <td>${c.contribute+c.contLand+c.contTarget+c.fines} руб.</td>
                                <td>${c.contribute} руб.</td>
                                <td>${c.contLand} руб.
                                    <c:if test="${c.benefitsOn}"> <span
                                            class="glyphicon glyphicon-heart-empty"></span></c:if>
                                </td>
                                <td>${c.contTarget} руб.</td>
                                <td>${c.fines} руб. <c:if test="${c.finesOn}">
                                    <span class="glyphicon glyphicon-fire"></span></c:if></td>
                                <td>${c.balance} руб.</td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>
                <h4>Общий долг: ${contributionAll}</h4>
                <hr>
                <h4>Платежи</h4>
                <button class="btn btn-primary" onclick="openAddingCount()">
                    <span class="glyphicon glyphicon-shopping-cart"></span> Дополнительный взнос
                </button>
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Дата</th>
                        <th>№</th>
                        <th>Сумма</th>
                        <th>Членский взнос</th>
                        <th>Аренда земли</th>
                        <th>Целевой взнос</th>
                        <th>Дополнительный взнос</th>
                        <th>Пени</th>
                        <th>Оставшиеся средства</th>
                        <th>Печать чека</th>
                    </tr>
                    </thead>
                    <c:forEach items="${garag.payments}" var="p" varStatus="loop">
                        <c:if test="${loop.index<10}">
                            <tr>
                                <td><fmt:formatDate type="both" dateStyle="full"
                                                    value="${p.datePayment.time}"/></td>
                                <td>${p.number}</td>
                                <td>${p.pay+p.contributePay+p.contLandPay+p.contTargetPay+p.additionallyPay+p.finesPay}
                                    руб.
                                </td>
                                <td>${p.contributePay} руб.</td>
                                <td>${p.contLandPay} руб.</td>
                                <td>${p.contTargetPay} руб.</td>
                                <td>${p.additionallyPay} руб.</td>
                                <td>${p.finesPay} руб.</td>
                                <td>${p.pay} руб.</td>
                                <td><a href="printOrder/${p.id}" target="_blank">
                                    <span class="glyphicon glyphicon-print"></span></a></td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>
            </div>
            <div class="modal-footer" align="center">
                <a href="infGarag/${garag.id}" target="_blank" class="btn btn-success"><span
                        class="glyphicon glyphicon-print"></span>
                    Распечатать
                </a>
                <button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
            </div>

        </div>

    </div>
</div>