<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="sumAll" scope="page" value="0.0"/>
<c:set var="sumContribute" scope="page" value="0.0"/>
<c:set var="sumContLand" scope="page" value="0.0"/>
<c:set var="sumContTarget" scope="page" value="0.0"/>
<c:set var="sumFines" scope="page" value="0.0"/>
<div id="informationPanel" class="panel panel-success">
    <div class="panel-heading">
        <input id="idGarag" type="hidden" value="${garag.id}">
        <h4 class="modal-title">Информация: ${garag.fullName}</h4>
    </div>
    <div class="panel-body">
        <h4>${fio} <c:if test="${garag.person.memberBoard}"><span
                class="label label-warning">Член правления</span> </c:if></h4>
        <c:if test="${not empty garag.additionalInformation}">
            <div class="alert alert-info addingInf" role="alert">${garag.additionalInformation}</div>
        </c:if>
        <h4><b>Долги</b></h4>
        <table class="table table-striped table-bordered">
            <thead>
            <tr>
                <th>Год</th>
                <th>Годовой долг</th>
                <th>Членский взнос</th>
                <th>Аренда земли</th>
                <th>Целевой взнос</th>
                <th>Пени</th>
            </tr>
            </thead>
            <c:forEach items="${garag.contributions}" var="c">
                <%--  Проверить--%>
                <c:if test="${(c.contribute+c.contLand+c.contTarget+c.fines) !=0}">
                    <tr>
                        <td>${c.year}</td>
                        <td>${c.contribute+c.contLand+c.contTarget+c.fines} руб.</td>
                        <c:set var="sumAll" scope="page"
                               value="${sumAll+c.contribute+c.contLand+c.contTarget+c.fines}"/>
                        <td>${c.contribute} руб.</td>
                        <c:set var="sumContribute" scope="page" value="${sumContribute+c.contribute}"/>
                        <td>${c.contLand} руб.
                            <c:if test="${c.benefitsOn}"> <span
                                    class="glyphicon glyphicon-heart-empty"></span></c:if>
                        </td>
                        <c:set var="sumContLand" scope="page" value="${sumContLand+c.contLand}"/>
                        <td>${c.contTarget} руб.</td>
                        <c:set var="sumContTarget" scope="page" value="${sumContTarget+c.contTarget}"/>
                        <td>${c.fines} руб. <c:if test="${c.finesOn}">
                            <span class="glyphicon glyphicon-fire"></span></c:if></td>
                        <c:set var="sumFines" scope="page" value="${sumFines+c.fines}"/>
                    </tr>
                </c:if>
            </c:forEach>
            <tr>
                <th>Итого:</th>
                <th>${sumAll} руб.</th>
                <th>${sumContribute} руб.</th>
                <th>${sumContLand} руб.</th>
                <th>${sumContTarget} руб.</th>
                <th>${sumFines} руб.</th>
            </tr>
        </table>

        <c:if test="${garag.oldContribute > 0.0}">
            <span class="label label-danger">Долги прошлых лет: ${garag.oldContribute} руб.</span>
        </c:if>

        <h3><b>Общий долг: ${contributionAll} руб.</b></h3>
        <hr>
        <h4><b>Платежи</b></h4>
        <table class="table table-striped table-bordered">
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
                <th>Долги прошлых лет</th>
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
                        <td>${p.oldContributePay} руб.</td>
                        <td>${p.pay} руб.</td>
                        <td align="center"><a href="printOrder/${p.id}" target="_blank">
                            <span class="glyphicon glyphicon-print"></span></a></td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
        <c:if test="${not empty garag.historyGarags }">
            <div class="pull-left">
                <button id="historyBtn" class="btn btn-info" type="button"
                        onclick="getHistoryGarag(${garag.id}); return false">
                    <span class="glyphicon glyphicon-time"></span> История
                </button>
            </div>
        </c:if>

        <div class="pull-right">
            <button class="btn btn-success" type="button" onclick="payGarag(${garag.id},'default')">
                <span class="glyphicon glyphicon-shopping-cart"></span> Оплатить
            </button>
            <button id="openAddingCount" class="btn btn-warning" onclick="payGarag(${garag.id},'adding')">
                <span class="glyphicon glyphicon-shopping-cart"></span> Дополнительный взнос
            </button>
            <a href="infPrint/${garag.id}" target="_blank" class="btn btn-primary"><span
                    class="glyphicon glyphicon-print"></span>
                Распечатать
            </a>
            <button type="button" class="btn btn-default" onclick="closeForm()"><span
                    class="glyphicon glyphicon-remove"></span> Закрыть
            </button>
        </div>

    </div>
</div>
<div id="reasonList"></div> 