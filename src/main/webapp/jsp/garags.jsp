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
            "ajax": "allGarag?setSeries=${setSeries}",
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
                {"render": function(data, type, full) {
                  return '<a href=\"#\" onclick=\"editGarag(' + full.id + ')\">' + full.number + '</a>'
                },'title': 'Гараж',type: 'natural',className: "series"},
                {"render": function(data, type, full) {
                    if (full.person != null) {
                        return '<a href=\"#\" onclick=\"editPersonId(' + full.person.personId + ')\">' + full.person.fio + '</a>'
                    }
                    return ""
                }, 'title': 'ФИО'},
                {"data":"person.phone","defaultContent": "", 'title': 'Телефон'},
                {"data": "person.address","defaultContent": "",  'title': 'Адрес'},
                {"data": "person.benefits","defaultContent": "",  'title': 'Льготы'},
                {'title': 'Действия',className:"actionBtn","render": function (data, type, full) {
                    var del = "";
                    if ($("#role").val() == 1) {
                        del = "<a href=\"#\" class=\"btnTable deleteButton  btn btn-danger btn-sm\" title=\"Удалить гараж\" data-placement=\"top\" id=\"deleteGarag_" + full.id +
                                "\" onclick=\"deleteGarag('" + full.id + "');\"><span class=\"glyphicon glyphicon-trash\"/></span></a>"
                    }
                    return "<a href=\"#\" class=\"btnTable btn btn-info btn-sm\" title='Информация' onclick=\"infGarag(" + full.id +
                            ");\"><span class=\"glyphicon glyphicon-comment\"/></span></a><a href=\"#\" class=\"btnTable  btn btn-success btn-sm\" title='Оплатить' onclick=\"payGarag(" + full.id +
                            ");\"><span class=\"glyphicon glyphicon-shopping-cart\"/></span></a><a href=\"#\" class=\"btnTable deleteButton  btn btn-warning btn-sm\"  title=\"Удалить назначение\" data-placement=\"top\" onclick=\"assignDelete(" + full.id +
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
                    $("#garagTable").DataTable().ajax.url("allGarag?setSeries=${setSeries}").load(null, false);
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
                    $("#garagTable").DataTable().ajax.url("allGarag?setSeries=${setSeries}").load(null, false);
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
    <input type="hidden" id="seriesNumber" value="${setSeries}">

    <div class="panel with-nav-tabs panel-primary">
        <div class="panel-heading">
            <ul class="nav nav-tabs">
                <c:forEach items="${series}" var="number">
                    <li role='presentation' <c:if test="${setSeries eq number}">class="active"</c:if>>
                        <a class="seriesLink"
                           href="<c:url value="/garagPage">
                        <c:param name="series" value="${number}"/></c:url>">
                            <c:out value="${number}"/>
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="panel-body">
            <h3>Список гаражей ${setSeries} ряда</h3>
            Общее количество: <span id="count" class="badge"></span>
            <br>
            <table id="garagTable" class="table table-striped table-bordered" cellspacing="0" width="100%"></table>
        </div>


    </div>
    <jsp:include page="footer.jsp"/>
