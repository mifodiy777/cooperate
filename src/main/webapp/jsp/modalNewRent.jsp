<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    $(document).ready(function () {

        $("#yearRent").val('<c:out value="${year}"/>');
        $('#formModalRent').modal({
            backdrop: "static"
        });
        $("#rentForm").validate({
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    success: function (html) {
                        showSuccessMessage(html);
                        $('#formModalRent').modal('hide');
                    },
                    error: function (xhr) {
                        if (xhr.status == 409) {
                            showErrorMessage(xhr.responseText);
                            $('#formModalRent').modal('hide');
                        }
                    }
                });
            },
            errorPlacement: function (error, element) {
                validPlaceError(error, element);
            },
            success: function (label, element) {
                validPlaceSuccess(label, element);
            }
        });

        $('#formModalRent').on('hidden.bs.modal', function () {
            $('form input').tooltipster('hide');
        })

        $('#rentForm input').tooltipster({
            trigger: 'custom',
            onlyOne: false,
            position: 'top'
        });

        $('#submitNewRent').popConfirm({
            title: "Сохранить период?",
            content: "",
            placement: "bottom",
            yesBtn: "Да",
            noBtn: "Нет"
        });


        $("#rentForm").submit(function (e) {
            e.preventDefault();
            $(this).valid();
            return false;
        });
    })
</script>

<!-- Modal -->
<div id="formModalRent" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Введите суммы взносов за ${year} год</h4>
            </div>
            <form:form modelAttribute="rent" id="rentForm" method="post" action="saveRent">
                <div class="modal-body">

                    <form:hidden path="yearRent"/>
                    <div class="row">
                        <div class="col-md-8">
                            <div class="form-group input-group">
                                <label for="contributeMax" class="input-group-addon">Взнос*</label>
                                <form:input path="contributeMax" id="contributeMax"
                                            cssClass="required form-control"/>
                                <span class="input-group-addon">руб.</span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-8">
                            <div class="form-group input-group">
                                <label for="contLandMax" class="input-group-addon">За землю*</label>
                                <form:input path="contLandMax" id="contLandMax"
                                            cssClass="required form-control"/>
                                <span class="input-group-addon">руб.</span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-8">
                            <div class="form-group input-group">
                                <label for="contTargetMax" class="input-group-addon">Целевой взнос*</label>
                                <form:input path="contTargetMax" id="contTargetMax"
                                            cssClass="required form-control"/>
                                <span class="input-group-addon">руб.</span>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="modal-footer" align="center">
                    <button type="submit" class="btn btn-success" id="submitNewRent"><span
                            class="glyphicon glyphicon-ok"></span>
                        Сохранить
                    </button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <span class="glyphicon glyphicon-remove"></span> Закрыть
                    </button>

                </div>
            </form:form>
        </div>

    </div>
</div>