<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: KuzminKA
  Date: 15.10.2015
  Time: 17:57:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp"/>
<script type="text/javascript">
    $(document).ready(function () {

        $("#rentForm").validate({
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    success: function (html) {
                        showSuccessMessage(html);
                    },
                    error: function (xhr) {
                        if (xhr.status == 409) {
                            showErrorMessage(xhr.responseText);

                        }
                    }
                });
            },
            errorPlacement: function (error, element) {
                validPlaceError(error, element);
            },
            success: function (label, element) {
                validPlaceSuccess(label, element);
            },
            rules: {
                yearRent: {
                    required: true,
                    rangelength: [4, 4]
                }
            }
        });

        $('#submitOldRent').popConfirm({
            title: "Сохранить период?",
            content: "",
            placement: "bottom",
            yesBtn: "Да",
            noBtn: "Нет"
        });

        $('#rentForm input').tooltipster({
            trigger: 'custom',
            onlyOne: false,
            position: 'right'
        });


        $("#rentForm").submit(function (e) {
            e.preventDefault();
            $(this).valid();
            return false;
        });
    })
</script>
<div class="container-fluid">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h4 class="panel-title">Введите прошлые периоды</h4>
        </div>
        <div class="panel-body">
            <div class="col-md-4 col-md-offset-4">
                <form:form modelAttribute="rent" id="rentForm" method="post" action="saveOldRent">
                    <div class="form-group input-group">
                        <label for="yearRent" class="input-group-addon">Год(в формате yyyy)*</label>
                        <form:input path="yearRent" id="yearRent" cssClass="form-control"/>
                    </div>


                    <div class="form-group input-group">
                        <label for="contributeMax" class="input-group-addon">Взнос*</label>
                        <form:input path="contributeMax" id="contributeMax"
                                    cssClass="required number form-control"/>
                        <span class="input-group-addon">руб.</span>
                    </div>

                    <div class="form-group input-group">
                        <label for="contLandMax" class="input-group-addon">За землю*</label>
                        <form:input path="contLandMax" id="contLandMax"
                                    cssClass="required number form-control"/>
                        <span class="input-group-addon">руб.</span>
                    </div>

                    <div class="form-group input-group">
                        <label for="contTargetMax" class="input-group-addon">Целевой взнос*</label>
                        <form:input path="contTargetMax" id="contTargetMax"
                                    cssClass="required number form-control"/>
                        <span class="input-group-addon">руб.</span>
                    </div>

                    <div align="center">
                        <button id="submitOldRent" type="submit" class="btn btn-success"><span
                                class="glyphicon glyphicon-ok"></span>
                            Сохранить
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>

<jsp:include page="footer.jsp"/>