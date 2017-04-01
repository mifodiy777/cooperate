<%--
  Created by IntelliJ IDEA.
  User: KuzminKA
  Date: 08.10.2015
  Time: 19:39:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Чек</title>
    <link rel="stylesheet" href="<c:url value="/css/order.css"/>" type="text/css">
</head>
<body>

<table>
    <thead>
    <tr>
        <th>Чек № ${pay.number}</th>
        <th colspan="3">Дата: <fmt:formatDate value="${pay.datePayment.time}" type="both"/></th>
        <th>Ряд: ${pay.garag.series} Гараж:${pay.garag.number}</th>
        <th colspan="3">ФИО:${pay.fio}</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <th colspan="8">Сумма платежа распределена на:</th>
    </tr>
    <tr>
        <th>Членский взнос</th>
        <th>Аренда земли</th>
        <th>Целевой взнос</th>
        <th colspan="2">Дополнительный взнос</th>
        <th>Пени</th>
        <th>Долги прошлых лет</th>
        <th>Оставшиеся средства</th>

    <tr>
        <td>${pay.contributePay} руб.</td>
        <td>${pay.contLandPay} руб.</td>
        <td>${pay.contTargetPay} руб.</td>
        <td colspan="2">${pay.additionallyPay} руб.</td>
        <td>${pay.finesPay} руб.</td>
        <td>${pay.oldContributePay} руб.</td>
        <td>${pay.pay} руб.</td>
    </tr>
    <tr>
        <th colspan="3">Сумма платежа:${pay.contributePay+pay.contLandPay+
                pay.contTargetPay+pay.additionallyPay+pay.finesPay+pay.oldContributePay+pay.pay} рублей
        </th>
        <th colspan="5">Сумма долга после оплаты: ${pay.debtPastPay} рублей</th>
    </tr>
    <tr>
        <td colspan="5" class="sign">
            Подпись плательщика:
        </td>
        <td colspan="3" class="sign">
            Подпись кассира:
            <div style="text-align:right; float:right;">
                М.П.
            </div>
        </td>
    </tr>
    </tbody>

</table>
<div class="line-cut"></div>
<table>
    <thead>
    <tr>
        <th>Чек № ${pay.number}</th>
        <th colspan="3">Дата: <fmt:formatDate value="${pay.datePayment.time}" type="both"/></th>
        <th>Ряд: ${pay.garag.series} Гараж:${pay.garag.number}</th>
        <th colspan="3">ФИО:${pay.fio}</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <th colspan="8">Сумма платежа распределена на:</th>
    </tr>
    <tr>
        <th>Членский взнос</th>
        <th>Аренда земли</th>
        <th>Целевой взнос</th>
        <th colspan="2">Дополнительный взнос</th>
        <th>Пени</th>
        <th>Долги прошлых лет</th>
        <th>Оставшиеся средства</th>

    <tr>
        <td>${pay.contributePay} руб.</td>
        <td>${pay.contLandPay} руб.</td>
        <td>${pay.contTargetPay} руб.</td>
        <td colspan="2">${pay.additionallyPay} руб.</td>
        <td>${pay.finesPay} руб.</td>
        <td>${pay.oldContributePay} руб.</td>
        <td>${pay.pay} руб.</td>
    </tr>
    <tr>
        <th colspan="3">Сумма платежа:${pay.contributePay+pay.contLandPay+
                pay.contTargetPay+pay.additionallyPay+pay.finesPay+pay.oldContributePay+pay.pay} рублей
        </th>
        <th colspan="5">Сумма долга после оплаты: ${pay.debtPastPay} рублей</th>
    </tr>
    <tr>
        <td colspan="5" class="sign">
            Подпись плательщика:
        </td>
        <td colspan="3" class="sign">
            Подпись кассира:
            <div style="text-align:right; float:right;">
                М.П.
            </div>
        </td>
    </tr>
    </tbody>

</table>

</body>
</html>