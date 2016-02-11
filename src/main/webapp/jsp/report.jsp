<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>
<script type="text/javascript">

    function reportProfit(year) {
        window.location.href = "reportProfit/" + year;
    }

    function reportPays(year) {
        window.location.href = "reportPayments/" + year;
    }
</script>
<div class="container">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h4>Отчеты</h4>
        </div>
        <div class="panel-body">

            <div class="row center">
                <div class="col-md-6">
                    <h3>Отчет по доходам</h3>
                    <div class="input-group form-group">
                <label for="yearProfit" class="input-group-addon">Год*</label>
                <select id="yearProfit" class="form-control ">
                    <c:forEach items="${rents}" var="rent">
                        <option value="${rent.yearRent}">${rent.yearRent}</option>
                    </c:forEach>
                </select>
                <span class="input-group-btn">
                    <button class="btn btn-primary" onclick='reportProfit($("#yearProfit").val());'>Подготовить</button>
                            </span>
            </div>
                </div>
                    <div class="col-md-6">
                    <h3>Отчет по платежам</h3>
                    <div class="input-group form-group">
                <label for="yearPay" class="input-group-addon">Год*</label>
                <select id="yearPay" class="form-control ">
                    <c:forEach items="${years}" var="year">
                        <option value="${year}">${year}</option>
                    </c:forEach>
                </select>
                <span class="input-group-btn">
                    <button class="btn btn-primary" onclick='reportPays($("#yearPay").val());'>Подготовить</button>
                            </span>
            </div>
                </div>
            </div>



        </div>

    </div>
    <jsp:include page="footer.jsp"/>
