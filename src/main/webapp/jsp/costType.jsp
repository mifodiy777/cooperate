<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript" src="<c:url value="/js/typeahead.js"/>"></script>
<script type="text/javascript">
    $(document).ready(function () {

        $("#costTypeForm").validate({
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    success: function (html) {
                        $(".cooperateTable").DataTable().ajax.reload(null, false);
                        showSuccessMessage(html);
                        closeForm();
                        return false;
                    },
                    error: function (xhr) {
                        if (xhr.status == 409) {
                            showErrorMessage(xhr.responseText);
                        }
                    }
                });
            },
            errorPlacement: function (error, element) {
                validError(error, element);
            },
            success: function (label, element) {
                validSuccess(label, element);
            }
        });
    });

</script>
<div class="panel panel-success">
    <div class="panel-heading">
        <h4 class="panel-title">Режим изменения типа расходов</h4>
    </div>
    <div class="panel-body">
        <form:form modelAttribute="costType" id="costTypeForm" method="post" action="saveType">
            <form:hidden path="id"/>
            <div class="row">
                <div class="col-md-4">
                    <div class="form-group">
                        <label for="typeName">Наименование*</label>
                        <form:input path="name" id="typeName" cssClass="required form-control"/>
                        <span class="help-block"></span>
                    </div>
                </div>
            </div>
            <div style="text-align: left; margin-top:20px" class="col-md-12">
                <button id="buttonAdd" class="btn btn-success buttonForm">
                    <span class="glyphicon glyphicon-ok"></span> Сохранить
                </button>

                <button name="reset" onclick="closeForm('cost'); $('#addCost').show();"
                        class="btn btn-danger buttonForm"><span class="glyphicon glyphicon-remove"></span> Закрыть
                </button>
            </div>
        </form:form>
    </div>
</div>

