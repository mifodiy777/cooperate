<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    $(document).ready(function () {

        $('#formModalContribute').modal({
            backdrop: "static"
        });


        $("#contributionForm").validate({
            submitHandler: function (form) {
                $('#contributionForm input.number').each(function () {
                    var val = $(this).val();
                    if (val.length == 0) {
                        $(this).val(0);
                    }
                });
                $(form).ajaxSubmit({
                    success: function (html) {
                        $('#formModalContribute').modal('hide');
                        showSuccessMessage(html);
                        return false;
                    },
                    error: function (xhr) {
                        if (xhr.status == 409) {
                            showErrorMessage(xhr.responseText);
                        }
                    }
                });
            },
            rules: {
                contTarget: {
                    max: ${max.contTargetMax}
                }

            },
            errorPlacement: function (error, element) {
                validPlaceError(error, element);
            },
            success: function (label, element) {
                validPlaceSuccess(label, element);
            }
        });

        $("#contributionForm input[type=text]").focusout(function () {
            $("#contributionForm input[type=text]").each(function (key, value) {
                $(value).val($(value).val().trim())
            })
        })


        $("#memberBoardOn").on("change", function () {
            if ($("#memberBoardOn").prop("checked")) {
                $("#memberBoardOnHide").val(0);
                $(".maxContribute").text("MAX 0 руб.");
            } else {
                $("#memberBoardOnHide").val(${max.contributeMax});
                $(".maxContribute").text("MAX ${max.contributeMax} руб.");
            }
        });


        $("#benefitsOn").on("click", function () {
            if ($("#benefitsOn:checked").val()) {
                $("#benefitsOnHide").val(${max.contLandMax/2});
                $(".maxLand").html("MAX ${max.contLandMax/2} руб.");

            } else {
                $("#benefitsOnHide").val(${max.contLandMax});
                $(".maxLand").html("MAX ${max.contLandMax} руб.");

            }
        });

        $('#formModalContribute').on('hidden.bs.modal', function () {
            $('form input').tooltipster('hide');
        });


        $('#contributionForm input').tooltipster({
            trigger: 'custom',
            onlyOne: false,
            position: 'top'
        });

        $("#finesLastUpdate").datepicker({
            format: "dd.mm.yyyy",
            defaultViewDate: {year:${contribution.year}, month: 6, day: 1},
            startDate: '-10y',
            endDate: "-0d",
            language: 'ru',
            todayBtn: 'linked',
            todayHighlight: true
        }).on('changeDate', function () {
            $(this).valid();
        });

    });

    function setNextYear() {
        $("#finesLastUpdate").val("01.01.${contribution.year+1}");
        $("#finesOn").prop("checked", true);
    }

    function fullSetDebt() {
        $("#contribute").val(${max.contributeMax});
        $("#contTarget").val(${max.contTargetMax});
        if ($("#benefitsOn:checked").val()) {
            $("#contLand").val(${max.contLandMax/2});
        } else {
            $("#contLand").val(${max.contLandMax});
        }
        $("#finesLastUpdate").val("01.07.${contribution.year}");
        $("#finesOn").prop("checked", true);
        if ($("#memberBoardOn").prop("checked")) {
            $("#contribute").val(0);
        }

    }

</script>

<!-- Modal -->
<div id="formModalContribute" class="modal fade" role="dialog">
    <div class="modal-dialog" style="width:700px">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Введите долг за ${contribution.year} год</h4>
                <c:if test="${garag.person.memberBoard}"><span id="membersPerson" class="label label-warning">Член правления</span> </c:if>
            </div>
            <form:form modelAttribute="contribution" id="contributionForm" method="post" action="saveContribute">
            <div class="modal-body">
                <form:hidden path="id"/>
                <form:hidden path="year"/>
                <input type="hidden" id="idGarag" name="idGarag" value="${garag.id}">

                <div class="row">
                    <div class="col-md-8">
                        <div class="form-group input-group">
                            <label for="contribute" class="input-group-addon">Членский взнос</label>
                            <form:input path="contribute" id="contribute"
                                        cssClass="number form-control maxContributeVal"/>
                            <span class="input-group-addon">руб.</span>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <span class="maxValueCont maxContribute">
                            <c:choose>
                                <c:when test="${garag.person.memberBoard}">MAX 0 руб.</c:when>
                                <c:otherwise>MAX ${max.contributeMax} руб.</c:otherwise>
                            </c:choose>
                          </span>
                    </div>
                    <input type="hidden" id="memberBoardOnHide" value="
                    <c:choose>
                                <c:when test="${garag.person.memberBoard}">0</c:when>
                                <c:otherwise>${max.contributeMax}</c:otherwise>
                            </c:choose>">
                </div>
                <div class="row">
                    <div class="col-md-8">
                        <div class="form-group input-group">
                            <label for="contLand" class="input-group-addon">Аренда земли</label>
                            <form:input path="contLand" id="contLand"
                                        cssClass="number form-control maxLandVal"/>
                            <span class="input-group-addon">руб.</span>
                        </div>
                    </div>
                    <div class="col-md-4">
                            <span class="maxValueCont maxLand">MAX
                                <c:choose>
                                    <c:when test="${contribution.benefitsOn}">${max.contLandMax/2}</c:when>
                                    <c:otherwise>${max.contLandMax}</c:otherwise>
                                </c:choose> руб.</span>
                    </div>
                    <input type="hidden" id="benefitsOnHide" value="
                    <c:choose>
                                    <c:when test="${contribution.benefitsOn}">${max.contLandMax/2}</c:when>
                                    <c:otherwise>${max.contLandMax}</c:otherwise>
                                </c:choose>">
                </div>
                <div class="row">
                    <div class="col-md-8">
                        <div class="form-group input-group">
                            <label for="contTarget" class="input-group-addon">Целевой взнос</label>
                            <form:input path="contTarget" id="contTarget"
                                        cssClass="number form-control"/>
                            <span class="input-group-addon">руб.</span>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <span class="maxValueCont">MAX ${max.contTargetMax} руб.</span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-8">
                        <div class="form-group input-group">
                            <label for="fines" class="input-group-addon">Пени</label>
                            <form:input path="fines" id="fines"
                                        cssClass="number form-control"/>
                            <span class="input-group-addon">руб.</span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-8">
                        <div class="form-group input-group">
                            <label for="finesLastUpdate" class="input-group-addon">Дата обновления пеней</label>
                            <form:input path="finesLastUpdate" id="finesLastUpdate"
                                        cssClass="form-control dateRU"/>

                        </div>

                    </div>
                    <div class="col-md-4">
                        <div class="input-group">
                            <button type="button" class="btn btn-success btnNextYear" onclick="setNextYear()"
                                    title="Установить дату пеней на начало следующего года">
                                <span class="glyphicon glyphicon-tree-conifer"></span></button>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4">
                        <div class="form-group input-group">
                            <form:checkbox path="finesOn" id="finesOn"
                                           cssClass="form-control"
                                           name="fancy-checkbox-success" autocomplete="off"/>
                            <div class="[ btn-group ]">
                                <label for="finesOn" class="[ btn btn-info ]">
                                    <span class="[ glyphicon glyphicon-ok ]"></span>
                                    <span> </span>
                                </label>
                                <label for="finesOn" class="[ btn btn-default ]">
                                    Включить пени
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group input-group">
                            <form:checkbox path="benefitsOn" id="benefitsOn"
                                           cssClass="form-control"
                                           name="fancy-checkbox-success" autocomplete="off"/>
                            <div class="[ btn-group ]">
                                <label for="benefitsOn" class="[ btn btn-info ]">
                                    <span class="[ glyphicon glyphicon-ok ]"></span>
                                    <span> </span>
                                </label>
                                <label for="benefitsOn" class="[ btn btn-default ]">
                                    Льготный период
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group input-group">
                            <form:checkbox path="memberBoardOn" id="memberBoardOn"
                                           cssClass="form-control"
                                           name="fancy-checkbox-success" autocomplete="off"/>
                            <div class="[ btn-group ]">
                                <label for="memberBoardOn" class="[ btn btn-info ]">
                                    <span class="[ glyphicon glyphicon-ok ]"></span>
                                    <span> </span>
                                </label>
                                <label for="memberBoardOn" class="[ btn btn-default ]">
                                    Член правления
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" align="center">
                    <button type="submit" class="btn btn-success"><span class="glyphicon glyphicon-ok"></span>
                        Сохранить
                    </button>
                    <button type="button" class="btn btn-danger" onclick="fullSetDebt()"><span
                            class="glyphicon glyphicon-signal"></span> Полностью
                    </button>
                    <button type="button" data-dismiss="modal" class="btn btn-default"><span
                            class="glyphicon glyphicon-remove"></span> Закрыть
                    </button>

                </div>
                </form:form>
            </div>

        </div>
    </div>
</div>