<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>
<script type="text/javascript">

    $(function() {

        rangeDate();

        $('.reportForm input').tooltipster({
            animation: 'slide',
            trigger: 'custom',
            onlyOne: false,
            position: 'top'
        });


        $("#reportProfitForm").validate({
            submitHandler: function (form) {
                var data = $(form).serialize();
                window.open($(form).attr("action") + "?" + data)
            },
            errorPlacement: function (error, element) {
                $(element).tooltipster('update', $(error).text());
                $(element).tooltipster('show');
            },
            success: function (label, element) {
                $(element).tooltipster('hide');
            }
        });

        $("#reportPaymentsForm").validate({
            submitHandler: function (form) {
                var data = $(form).serialize();
                window.open($(form).attr("action") + "?" + data)
            },
            errorPlacement: function (error, element) {
                $(element).tooltipster('update', $(error).text());
                $(element).tooltipster('show');
            },
            success: function (label, element) {
                $(element).tooltipster('hide');
            }
        });
    })

</script>
<div class="container-fluid">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h4 class="panel-title">Отчеты</h4>
        </div>
        <div class="panel-body ">
            <div class="row center">
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <h4>Отчет по доходам</h4>

                            <form action="reportProfit" id="reportProfitForm" class="reportForm" method="post">
                                <div class="row">
                                    <div class="col-md-4">
                                        <div class="input-group form-group">
                                            <label for="profitDateStart" class="input-group-addon">C </label>
                                            <input id="profitDateStart" name="profitDateStart"
                                                   class="from_date required form-control dateRU">
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="input-group form-group">
                                            <label for="profitDateEnd" class="input-group-addon"> ПО </label>
                                            <input id="profitDateEnd" name="profitDateEnd"
                                                   class="to_date required form-control dateRU">
                                        </div>
                                    </div>
                                    <div class="col-md-2">
                                        <div class="input-group form-group">
                                            <button type="submit" class="btn btn-primary"><span
                                                    class="glyphicon glyphicon-open-file"></span> Подготовить
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <h4>Отчет по платежам</h4>

                            <form action="reportPayments" id="reportPaymentsForm" class="reportForm" method="post">
                                <div class="row">
                                    <div class="col-md-4">
                                        <div class="input-group form-group">
                                            <label for="paymentDateStart" class="input-group-addon">C </label>
                                            <input id="paymentDateStart" name="paymentDateStart"
                                                   class="from_date required form-control dateRU">
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="input-group form-group">
                                            <label for="paymentDateEnd" class="input-group-addon"> ПО </label>
                                            <input id="paymentDateEnd" name="paymentDateEnd"
                                                   class="to_date required form-control dateRU">
                                        </div>
                                    </div>
                                    <div class="col-md-2">
                                        <div class="input-group form-group">
                                            <button type="submit" class="btn btn-primary"><span
                                                    class="glyphicon glyphicon-open-file"></span> Подготовить
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
