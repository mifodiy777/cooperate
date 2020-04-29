function showSuccessMessage(html) {
    $("#messages").removeClass("alert-danger").addClass("alert-info").html(html).show(800).delay(4000).hide(1000);
}

function showErrorMessage(html) {
    $("#messages").removeClass("alert-info").addClass("alert-danger").html(html).show(800).delay(4000).hide(1000);
}

function validPlaceError(error, element) {
    $(element).parent().addClass("has-error").removeClass("has-success");
    $(element).tooltipster('update', error.text());
    $(element).tooltipster('show');
}

function validPlaceSuccess(label, element) {
    $(element).parent().removeClass("has-error").addClass("has-success");
    $(element).tooltipster('hide');
}

function validError(error, element) {
    $(element).parents(".form-group").addClass("has-error").removeClass("has-success");
    $(element).parents(".form-group").find('.help-block').text($(error).text());
}

function validSuccess(label, element) {
    $(element).parents(".form-group").removeClass("has-error").addClass("has-success");
    $(element).parents(".form-group").find('.help-block').empty();
}

function searchPerson() {
    $('#searchPerson').on('keyup keypress', function (e) {
        if (e.which == 13) {
            e.preventDefault();
            loadOwner($(this).val());
        }
    });

    $("#searchPersonBtn").on("click", function () {
        if ($("#searchFormDiv").css('display') == 'none') {
            $("body").scrollTop(0);
            $("#addFormPersonDiv").hide();
            $("#searchFormDiv").show();
            $("#searchPerson").focus();
            $("#searchPersonBtn").text("Создать владельца");

        } else {
            $("#addFormPersonDiv").show();
            $("#searchFormDiv").hide();
            $("#lastName").focus();
            $("#searchPersonBtn").text("Найти владельца");
            $("#personResults").hide();
        }
    });
}

function openNewRent() {
    var now = new Date();
    $.ajax({
        method: "GET",
        url: "checkYearRent",
        data: {"year": now.getFullYear()},
        success: function (html) {
            $("#modalDiv").html(html);
        },
        error: function (xhr) {
            showSuccessMessage(xhr.responseText);
        }
    });
}

function loadOwner(pattern) {
    if (pattern.length > 3) {
        $.post("searchPerson", {pattern: pattern.trim()}).done(function (html) {
            $("#personResults").html(html).show();
            $(".choosePerson").click(function (e) {
                e.preventDefault();
                $.getJSON("getPerson", {personId: this.id}, function (person) {
                    $("#addFormPersonDiv").show();
                    $("#searchFormDiv").hide();
                    $("#searchPersonBtn").text("Найти владельца");
                    $("#deleteOldPerson").prop("checked", false).trigger("change");
                    $("#personResults").hide();
                    $("#personId").val(person.id);
                    $("#lastName").val(person.lastName);
                    $("#name").val(person.name);
                    $("#fatherName").val(person.fatherName);
                    $("#telephone").val(person.telephone);
                    $("#addressId").val(person.address.id);
                    $("#city").val(person.address.city);
                    $("#street").val(person.address.street);
                    $("#home").val(person.address.home);
                    $("#apartment").val(person.address.apartment);
                    $("#benefits").val(person.benefits);
                    $("#memberBoard").prop("checked", person.memberBoard == true);
                    $("#personId").trigger("change");
                });
            });
        });
    } else {
        $("#searchPerson").tooltip({placement: 'bottom', trigger: 'manual', font: '14px'}).tooltip('show');
    }
}

function hideTooltip() {
    $('#searchPerson').tooltip('destroy');
}

function resetPerson() {
    $(".person").each(function () {
        $(this).val('');
    });
    $("#memberBoard").prop("checked", false);

}

function emptyGarag() {
    $("#addFormPersonDiv").empty();
    $("#searchDivPerson").empty();
    $("#personBtn").hide();

}

function setOldContribute(id, year) {
    $.get('editContribute', {"idGarag": id, "year": year}, function (html) {
        $("#modalDiv").html(html);
    }).fail(function (xhr) {
        if (xhr.status == 409) {
            showErrorMessage(xhr.responseText);
        }
    });
}

function infGarag(id) {
    $("#formPanel").empty();
    $.get("garagInf", {"idGarag": id}, function (html) {
        $("#formPanel").html(html);
        initTR(id);
    }).fail(function (xhr) {
        if (xhr.status == 409) {
            showErrorMessage(xhr.responseText);
        }
    })
}

function payGarag(id, type) {
    $.get("payModal", {"idGarag": id, "type": type}, function (html) {
        $("#modalDiv").html(html);
    }).fail(function (xhr) {
        if (xhr.status == 409) {
            showErrorMessage(xhr.responseText);
        }
    })
}

function saveEntity(entity) {
    $("#formPanel").empty().load(entity);
    $(".addBtn").hide();
}

function editEntity(id, entity) {
    $("#formPanel").empty();
    $("#formPanel").load(entity + "/" + id);
    $(".addBtn").hide();
}

function initTR(id) {
    $("tr.info").removeClass("info");
    $("#garagTR_" + id).addClass("info");
    $("#personTR_" + id).addClass("info");
}

function closeForm() {
    $("#formPanel").empty();
    $('.addBtn').show();
    $("tr.info").removeClass("info");
}

function deleteEntity(id, entity) {
    $.ajax({
        url: entity + "/" + id,
        type: "post",
        success: function (html) {
            $(".cooperateTable").DataTable().ajax.reload(null, false);
            closeForm();
            showSuccessMessage(html);
        },
        error: function (xhr) {
            if (xhr.status == 409) {
                showErrorMessage(xhr.responseText);
            }
        }
    });

}

function deletePayment(id) {
    $.ajax({
        url: "deletePayment/" + id,
        type: "post",
        success: function (html) {
            showSuccessMessage(html);
            $("#paymentTable").DataTable().ajax.reload(null, false);
        },
        error: function (xhr) {
            if (xhr.status == 409) {
                showErrorMessage(xhr.responseText);
            }
        }
    });
}

function deleteAssign(garag, id) {
    $.post("deleteGaragInPerson", {idGarag: id}, function (html) {
        $(garag).parent().parent().remove();
        $(".cooperateTable").DataTable().ajax.reload(null, false);
        showSuccessMessage(html);
    });
}

function rangeDate(elementFrom, elementTo) {
    var FromEndDate = new Date();
    var ToEndDate = new Date();

    elementFrom.datepicker({
        format: "dd.mm.yyyy",
        weekStart: 1,
        endDate: FromEndDate,
        language: 'ru',
        autoclose: true,
        todayHighlight: true,
        todayBtn: 'linked'
    }).on('changeDate', function (selected) {
        startDate = new Date(selected.date.valueOf());
        startDate.setDate(startDate.getDate(new Date(selected.date.valueOf())));
        elementTo.datepicker('setStartDate', startDate);
        $(this).valid();
    });

    elementTo.datepicker({
        format: "dd.mm.yyyy",
        weekStart: 1,
        endDate: ToEndDate,
        language: 'ru',
        autoclose: true,
        todayHighlight: true,
        todayBtn: 'linked'
    }).on('changeDate', function (selected) {
        FromEndDate = new Date(selected.date.valueOf());
        FromEndDate.setDate(FromEndDate.getDate(new Date(selected.date.valueOf())));
        elementFrom.datepicker('setEndDate', FromEndDate);
        $(this).valid();
    });

}

function changePerson(idGarag) {
    $("#formPanel").load("changePerson/" + idGarag);
    initTR(idGarag);
    $(".addBtn").hide();
}
function openPage(href) {
    location.href = href;
}

function getHistoryGarag(id) {
    $("#reasonList").load("getHistoryGarag/" + id);
}

function deleteReason(id) {
    $.ajax({
        url: "deleteReason",
        data: {
            idReason: id
        },
        type: "post",
        success: function (html) {
            showSuccessMessage(html);
            $("#histTR_" + id).remove();
            if ($("[id^=histTR_]").size() == 0) {
                $('#histGarag').modal('hide');
                $('#historyBtn').parent().hide();
            }
        },
        error: function (xhr) {
            if (xhr.status == 409) {
                showErrorMessage(xhr.responseText);
            }
        }
    });
}

function messBuilder() {
    if ($("#allChange").length != 0) {
        $("#deleteOldPerson").prop("checked", false);
    }
    var idPastPerson = $("#idPastPerson").val();
    var serchPerson = idPastPerson != $("#personId").val();
    var delPerson = $("#deleteOldPerson").prop("checked");
    var countGarag = $("input[name='changeGaragAll']").prop("checked");
    setTextTip(serchPerson, delPerson, countGarag);
    $("#personId").on("change", function () {
        serchPerson = idPastPerson != $(this).val();
        setTextTip(serchPerson, delPerson, countGarag);
    });
    $("#deleteOldPerson").on("change", function () {
        delPerson = $(this).prop("checked");
        $("#oneChange").next('label').show();
        if (delPerson && $("#allChange").length != 0) {
            $("#oneChange").next('label').hide();
            $("input[name='changeGaragAll']").prop("checked", "checked")
            countGarag = $("input[name='changeGaragAll']").prop("checked");
        }
        setTextTip(serchPerson, delPerson, countGarag);
    });
    $("input[name='changeGaragAll']").on("change", function () {
        countGarag = $("input[name='changeGaragAll']").prop("checked");
        if (!countGarag && !serchPerson) {
            $("#deleteOldPerson").prop("checked", "checked");
            $("#deleteOldPerson").trigger("change");
        }
        setTextTip(serchPerson, delPerson, countGarag);
    });
}


function setTextTip(selectOne, selectTwo, selectThee) {
    var message = "Описание: ";
    var changeField = " Измените значения полей или найдите владельца в базе! ";
    var changePerson = " Новый владелец будет взят из базы! ";
    var pastPersonDelete = " Прошлый владелец удалится! ";
    var pastPersonNotDelete = " Прошлый владелец не удалится! ";
    var changeOneGarag = " Владелец заменится только у текущего гаража! ";
    var changeAllGarag = " Владелец заменится у всех гаражей! ";
    if (selectOne) {
        message += changePerson
    } else {
        message += changeField
    }
    if (selectTwo) {
        message += pastPersonDelete
    } else {
        message += pastPersonNotDelete
    }
    if (selectThee) {
        message += changeOneGarag
    } else {
        message += changeAllGarag
    }
    $("#alertPanel").show().text(message);
}

function promtReason(form, id) {
    $('#promtModal').modal({
        backdrop: "static",
        keyboard: false,
        show: true
    });
    $('#reason').on("focus", function () {
        $('#reason').parent().removeClass("has-error");
        $('#reason').parent().children(".help-block").empty();
    });
    $('#applyBtn').on("click", function () {
        if ($('#reason').val().length > 3) {
            $(form).ajaxSubmit({
                data: {
                    garag: id,
                    searchPerson: ($("#idPastPerson").val() != $("#personId").val()),
                    deletePerson: $("#deleteOldPerson").prop("checked"),
                    countGarag: $("input[name='changeGaragAll']").prop("checked"),
                    oldPerson: $("#idPastPerson").val(),
                    reason: $('#reason').val()

                },
                success: function (html) {
                    $('#promtModal').modal('hide');
                    $(".cooperateTable").DataTable().ajax.reload(null, false);
                    showSuccessMessage(html);
                    closeForm();
                    return false;
                }
            });
        } else {
            $('#reason').parent().addClass("has-error");
            $('#reason').parent().children(".help-block").text("Введите минимум 4 символа");
        }
    })
}



