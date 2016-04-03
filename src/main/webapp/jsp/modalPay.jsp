<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<security:authorize access="hasRole('ROLE_ADMIN')" var="isAdmin"/>

<script type="text/javascript">
    $(document).ready(function () {

        $("#garagId").val('<c:out value="${garag.id}"/>');

        $('#formModalPay').modal().on('shown.bs.modal', function () {
            $('#pay').focus()
        });

        $("#paymentForm").validate({
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    success: function (id) {
                        $('#formModalPay').modal('hide');
                        window.open('printOrder/' + id, '_blank');
                    },
                    error: function (xhr) {
                        if (xhr.status == 409) {
                            showErrorMessage(xhr.responseText);
                            $('#formModalPay').modal('hide');
                        }
                    }
                });
            },
            errorPlacement: function (error, element) {
                $(element).tooltipster('update', $(error).text());
                $(element).tooltipster('show');
            },
            success: function (label, element) {
                $(element).tooltipster('hide');
            },
            rules: {
                pay: {
                    required: true,
                    number: true,
                    min: 100
                }
            }
        });

        $('#paymentForm input').tooltipster({
            animation: 'slide',
            trigger: 'custom',
            onlyOne: false,
            position: 'right'
        });

        $("#datePayment").datepicker({
            format: "dd.mm.yyyy",
            startDate: '-10y',
            endDate: "-0d",
            language:'ru',
            todayBtn:true
        });

        $('#payBtn').popConfirm({
            title: "Вы хотите произвести платеж?",
            content: "",
            placement: "bottom",
            yesBtn: "Да",
            noBtn: "Нет"
        });

        $('#formModalPay').on('hidden.bs.modal', function () {
            $("#pay").tooltipster('hide');
        })


        $("#paymentForm").submit(function (e) {
            e.preventDefault();
            $(this).valid();
            return false;
        });
    })
</script>

<div id="formModalPay" class="modal fade" role="dialog">
    <div class="modal-dialog  modal-sm">

        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title"><b>${garag.fullName}</b></h4>
            </div>
            <form:form modelAttribute="payment" id="paymentForm" method="post" action="savePayment">
                <div class="modal-body">
                    <form:hidden path="garag.id" id="garagId"/>
               <%--     <c:if test="${isAdmin}">
                        <div class="row">
                            <div class="col-md-10 col-md-offset-1">
                                <div class="input-group">
                                    <label for="datePayment" class="input-group-addon">Дата</label>
                                    <form:input path="datePayment" id="datePayment"
                                                cssClass="form-control dateRU"/>
                                </div>
                            </div>
                        </div>
                    </c:if>--%>
                    <div class="row">
                        <div class="col-md-10 col-md-offset-1">
                            <div class="input-group">
                                <label for="pay" class="input-group-addon">Cумма*</label>
                                <form:input path="pay" id="pay"
                                            cssClass="form-control"/>
                                <span class="input-group-addon"> руб.</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" align="center">
                    <button id="payBtn" type="submit" class="btn btn-success"><span
                            class="glyphicon glyphicon-ok"></span>
                        Оплатить
                    </button>
                    <button type="button" class="btn btn-default" data-dismiss="modal"><span
                            class="glyphicon glyphicon-remove"></span> Закрыть
                    </button>
                </div>
            </form:form>
        </div>

    </div>
</div>