<%--
  Created by IntelliJ IDEA.
  User: KuzminKA
  Date: 13.10.2015
  Time: 11:03:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Информация по гаражу</title>
    <link rel="stylesheet" href="<c:url value="/css/order.css"/>" type="text/css">
</head>
<body>
<h2 align="center">Информация по гаражу</h2>

<table>
    <thead>
    <tr>
        <th colspan="3">Дата: <fmt:formatDate value="${now}" type="both"/></th>
        <th colspan="2">Ряд: ${garag.series} Гараж:${garag.number}</th>
        <th colspan="5">ФИО: ${fio}</th>
    </tr>
    </thead>
    <tbody>

    <tr>
        <th colspan="10">Долги</th>
    </tr>
    <tr>       
        <th colspan="10">Долги прошлых лет: ${garag.oldContribute} руб.</th>
    </tr>
    <tr>
        <th>Год</th>
        <th colspan="2">Членский взнос</th>
        <th>Аренда земли</th>
        <th>Целевой взнос</th>
        <th colspan="2">Пени</th>
        <th colspan="3">Годовой долг</th>

    </tr>
    <c:forEach items="${garag.contributions}" var="c">
        <c:if test="${(c.contribute+c.contLand+c.contTarget+c.fines) !=0}">
            <tr>
                <td>${c.year}</td>
                <td colspan="2">${c.contribute} руб.</td>
                <td>${c.contLand} руб.</td>
                <td>${c.contTarget} руб.</td>
                <td colspan="2">${c.fines} руб.</td>
                <td colspan="3">${c.contribute+c.contLand+c.contTarget+c.fines} руб.</td>
            </tr>
        </c:if>
    </c:forEach>
    <tr>        
        <th colspan="10"> Итого: ${contributionAll} руб.</th>
    </tr>
    <tr>
        <th colspan="10">Оплаты</th>
    </tr>
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
    </tr>
    <c:forEach items="${garag.payments}" var="p" varStatus="loop">
        <tr>
            <td><fmt:formatDate type="both" dateStyle="full"
                                value="${p.datePayment.time}"/></td>
            <td>${p.number}</td>
            <td><fmt:formatNumber type="number"
                                  maxFractionDigits="2"
                                  value="${p.pay+p.contributePay+p.contLandPay+p.contTargetPay+p.additionallyPay+p.finesPay+p.oldContributePay}"/>
                руб.
            </td>
            <td>${p.contributePay} руб.</td>
            <td>${p.contLandPay} руб.</td>
            <td>${p.contTargetPay} руб.</td>
            <td>${p.additionallyPay} руб.</td>
            <td>${p.finesPay} руб.</td>
            <td>${p.oldContributePay} руб.</td>
            <td>${p.pay} руб.</td>
        </tr>
    </c:forEach>
    </tbody>

</table>

</body>
</html>