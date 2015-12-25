<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type="text/javascript">
    $(document).ready(function () {

        $("#garagId").val('<c:out value="${garag.id}"/>');
        $('#formModalPay').modal();

        $("#paymentForm").validate({
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    success: function (html) {
                        $('#formModalPay').modal('hide');
                        showSuccessMessage(html);
                        window.open('printNewOrder/${garag.id}', '_blank');
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
            rules:{
                pay:{
                    required:true,
                    number:true,
                    min:100
                }
            }
        });

        $('#paymentForm input').tooltipster({
            animation: 'slide',
            trigger: 'custom',
            onlyOne: false,
            position: 'right'
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

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <c:if test="${garag.person.id==null}">
                    <div class="modal-body">
                        <h4>Гаражу не назначен владелец</h4>
                    </div>
                </c:if>
                <c:if test="${garag.person.id!=null}">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Введите сумму платежа</h4>
                </c:if>
            </div>
            <form:form modelAttribute="payment" id="paymentForm" method="post" action="savePayment">
                <c:if test="${garag.person.id!=null}">
                    <div class="modal-body">
                        <form:hidden path="garag.id" id="garagId"/>
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
                </c:if>
                <div class="modal-footer" align="center">
                    <c:if test="${garag.person.id!=null}">
                        <button type="submit" class="btn btn-success"><span class="glyphicon glyphicon-ok"></span>
                            Оплатить
                        </button>
                    </c:if>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>

                </div>

            </form:form>
        </div>

    </div>
</div>