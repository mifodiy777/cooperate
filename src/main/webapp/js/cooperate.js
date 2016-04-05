function showSuccessMessage(html) {
    $("#messages").removeClass("alert-danger");
    $("#messages").addClass("alert-info");
    $("#messages").html(html).show(800).delay(4000).hide(1000);
}

function showErrorMessage(html) {
    $("#messages").removeClass("alert-info");
    $("#messages").addClass("alert-danger");
    $("#messages").html(html).show(800).delay(4000).hide(1000);
}

function openNewRent() {
    var now = new Date();
    $.ajax({
        method: "GET",
        url: "checkYearRent",
        data:{"year":now.getFullYear()},
        success: function(html) {
            $("#modalDiv").html(html);
        },
        error:function(xhr) {
            showSuccessMessage(xhr.responseText);
        }
    });
}

function updateFines() {
    $.ajax({
        method: "POST",
        url: "updateFines",
        success: function(html) {
            showSuccessMessage(html);
        },
        error:function(xhr) {
            showErrorMessage(xhr.responseText);
        }
    })
}

function loadPerson(pattern) {
    if (pattern.length > 3) {
        $.post("searchPerson", {pattern: pattern.trim()}).done(function (html) {
            $("#personResults").html(html).show();
            $(".choosePerson").click(function (e) {
                e.preventDefault();
                $.getJSON("getPerson", {personId:this.id}, function(person) {
                    $("#addFormPersonDiv").show();
                    $("#searchFormDiv").hide();
                    $("#searchPersonBtn").val("Найти владельца");
                    $("#personResults").hide();
                    $("input[name='person.id']").val(person.id);
                    $("#lastName").val(person.lastName);
                    $("#name").val(person.name);
                    $("#fatherName").val(person.fatherName);
                    $("#telephone").val(person.telephone);
                    $("input[name='person.address.id']").val(person.address.id);
                    $("input[name='person.address.city']").val(person.address.city);
                    $("input[name='person.address.street']").val(person.address.street);
                    $("input[name='person.address.home']").val(person.address.home);
                    $("input[name='person.address.apartment']").val(person.address.apartment);
                    $("input[name='person.benefits']").val(person.benefits);
                    $("#memberBoard").prop("checked", person.memberBoard == true);
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
    $(".person").each(function() {
        $(this).val('');
    });
    $("#memberBoard").prop("checked", false);

}

function emptyGarag() {
    $("#addFormPersonDiv").empty();
    $("#searchDivPerson").empty();
    $("#personBtn").hide();

}

function infGarag(id) {
    $("tr.info").removeClass("info");
    $("#formPanel").empty();
    $.get("garagInf", {"idGarag":id}, function(html) {
        $("#formPanel").html(html);
        $("#garagTR_" + id).addClass("info");
    }).fail(function(xhr) {
        if (xhr.status == 409) {
            showErrorMessage(xhr.responseText);
        }
    })
}

function payGarag(id, type) {
    $.get("payModal", {"idGarag":id,"type":type}, function(html) {
        $("#modalDiv").html(html);
    }).fail(function(xhr) {
        if (xhr.status == 409) {
            showErrorMessage(xhr.responseText);
        }
    })
}

function saveEntity(entity) {
    $("#formPanel").empty();
    $("#formPanel").load(entity);
    $(".addBtn").hide();
}

function editEntity(id, entity) {
    $("tr.info").removeClass("info");
    $("#formPanel").empty();
    $("#formPanel").load(entity + "/" + id);
    $("#" + entity + "TR_" + id).addClass("info");
    $(".addBtn").hide();
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

function deleteAssign(garag, id) {
    $.post("deleteGaragInPerson", {idGarag:id}, function(html) {
        $(garag).parent().parent().remove();
        $(".cooperateTable").DataTable().ajax.reload(null, false);
        showSuccessMessage(html);
    });
}


