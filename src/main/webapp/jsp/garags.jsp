<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp"/>
<script type="text/javascript">

    $(document).ready(function () {

        $.scrollUp();

        var table = $('#garagTable').DataTable({
            "order": [
                [ 0, 'asc' ]
            ],
            "ajax": "allGarag",
            "fnCreatedRow": function (nRow, aData) {
                $(nRow).attr('id', 'my' + aData.id);
            },
            "fnDrawCallback": function () {
                $('a.deleteButton').popConfirm({
                    title: "Удалить?",
                    content: "",
                    placement: "bottom",
                    yesBtn: "Да",
                    noBtn: "Нет"
                });
                var oSettings = this.fnSettings();
                var iTotalRecords = oSettings.fnRecordsTotal();
                $("#count").html(iTotalRecords);
            },
            "columns": [
                {"data": "number",'title': 'Гараж',type: 'natural'},
                {"render": function(data, type, full) {
                    return '<a href=\"#\" onclick=\"editPersonId(' + full.personId + ')\">' + full.fio + "</a>"
                }, 'title': 'ФИО'},
                {"render": function (data, type, full) {
                    return full.phone;
                }, 'title': 'Телефон'},
                {"render": function (data, type, full) {
                    if (full.city == "") {
                        return "";
                    } else {
                        if (full.appartment == "") {
                            return "г." + full.city + " ул." + full.street + " д." + full.home;
                        } else {
                            return "г." + full.city + " ул." + full.street + " д." + full.home + " кв." + full.appartment;
                        }
                    }
                }, 'title': 'Адрес'},
                {"render": function (data, type, full) {
                    return full.benefits;
                }, 'title': 'Льготы'},
                {'title': 'Действия',className:"actionBtn","render": function (data, type, full) {
                    var del = "";
                    if ($("#role").val() == 1) {
                        del = "<a href=\"#\" class=\"btnTable deleteButton  btn btn-danger btn-sm\" title=\"Удалить гараж\" data-placement=\"top\" id=\"deleteGarag_" + full.id +
                                "\" onclick=\"deleteGarag('" + full.id + "');\"><span class=\"glyphicon glyphicon-trash\"/></span></a>"
                    }
                    return "<a href=\"#\" class=\"btnTable btn btn-info btn-sm\" title='Информация' onclick=\"infGarag(" + full.id +
                            ");\"><span class=\"glyphicon glyphicon-comment\"/></span></a><a href=\"#\" class=\"btnTable  btn btn-success btn-sm\" title='Оплатить' onclick=\"payGarag(" + full.id +
                            ");\"><span class=\"glyphicon glyphicon-shopping-cart\"/></span></a><a href=\"#\" class=\"btnTable  btn btn-primary btn-sm\" title='Редактировать' onclick=\"editGarag(" + full.id +
                            ");\"><span class=\"glyphicon glyphicon-pencil\"/></span></a><a href=\"#\" class=\"btnTable deleteButton  btn btn-warning btn-sm\"  title=\"Удалить назначение\" data-placement=\"top\" onclick=\"assignDelete(" + full.id +
                            ");\"><span class=\"glyphicon glyphicon-trash\"/></span></a>" + del;
                }}
            ]
        });

    });

    function closeForm(formName) {
        $("#editPanel").hide();
        $("#" + formName + "Div").empty();
    }

    function saveGarag() {
        $("#editPanel").show();
        $("#garagDiv").load('garag');
        $("#addGaragButton").hide();
    }

    function infGarag(id) {
        $.get("infModal", {"idGarag":id}, function(html) {
            $("#modalDiv").html(html);
        }).fail(function(xhr) {
            if (xhr.status == 409) {
                showErrorMessage(xhr.responseText);
            }
        })
    }

    function payGarag(id) {
        $.get("payModal", {"idGarag":id}, function(html) {
            $("#modalDiv").html(html);
        }).fail(function(xhr) {
            if (xhr.status == 409) {
                showErrorMessage(xhr.responseText);
            }
        })
    }

    function editPersonId(id) {
        if ($("#id").val() == id) {
            return null;
        }
        $("#editPanel").show();
        $("#personDiv").load("person/" + id);
        $("#addGaragButton").hide();
    }

    function editGarag(id) {
        if ($("#id").val() == id) {
            return null;
        }
        $("#editPanel").show();
        $("#garagDiv").load("garag/" + id);
        $("#addGaragButton").hide();
    }

    function assignDelete(id) {
        if (id == "") {
            showErrorMessage("Не найден ID !");
        } else {
            $.ajax({
                url: "assignDelete/" + id,
                type: "post",
                success: function (html) {
                    if ($("#id").val() == id) {
                        $("#editPanel").hide();
                        $("#personDiv").empty();
                        $("#garagDiv").empty();

                    }
                    showSuccessMessage(html);
                    $("#garagTable").DataTable().ajax.url("allGarag").load(null, false);
                },
                error: function (xhr) {
                    if (xhr.status == 409) {
                        showErrorMessage(xhr.responseText);
                    }
                }
            });
        }
    }

    function deleteGarag(id) {
        if (id == "") {
            showErrorMessage("Не найден ID !");
        } else {
            $.ajax({
                url: "deleteGarag/" + id,
                type: "post",
                success: function (html) {
                    if ($("#id").val() == id) {
                        $("#editPanel").hide();
                        $("#garagDiv").empty();
                        $("#personDiv").empty();
                    }
                    showSuccessMessage(html);
                    $("#garagTable").DataTable().ajax.url("allGarag").load(null, false);
                },
                error: function (xhr) {
                    if (xhr.status == 409) {
                        showErrorMessage(xhr.responseText);
                    }
                }
            });
        }
    }


</script>
<style type="text/css">
    .container-full {
        margin: 15px;
        width: 98%;
    }

    th, td {
        font-size: 15px
    }
</style>
<div class="container">
    <button id="addGaragButton" class="btn btn-success" onclick="saveGarag()">
        <span class="glyphicon glyphicon-plus"></span> Добавить гараж
    </button>
    <div id="editPanel" class="panel panel-success" style="display:none">
        <div id="typeDiv" class="panel-heading">

        </div>
        <div class="panel-body">
            <div id="garagDiv"></div>
            <div id="personDiv"></div>
        </div>
    </div>
    <div class="panel panel-default">
        <div class="panel-heading"><h3>Общий список</h3>
            Общее количество: <span id="count" class="badge"></span></div>
        <br>

        <div class="panel-body">
            <table id="garagTable" class="table table-striped table-bordered" cellspacing="0" width="100%"></table>
        </div>
    </div>

</div>
<jsp:include page="footer.jsp"/>
