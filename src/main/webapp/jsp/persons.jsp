<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp"/>
<script type="text/javascript">

    $(document).ready(function () {

        $.scrollUp();

        $('#listPerson').DataTable({
            "ajax": "allPerson",
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
                {"data": "fio", 'title': 'ФИО'},
                {"data": "phone", 'title': 'Телефон'},
                {"data": "address", 'title': 'Адрес'},
                {"data": "benefits", 'title': 'Льготы'},
                {"data": "garags[,<br>].garag", 'title': 'Гаражи'},
                {'title': 'Действия', "render": function (data, type, full) {
                    return "<a href=\"#\" class=\"btnTable  btn btn-primary btn-sm\" onclick=\"editPerson(" + full.id +
                            ");\"><span class=\"glyphicon glyphicon-pencil\"/></span></a>" +
                            "<a href=\"#\" class=\"btnTable deleteButton btn btn-danger btn-sm\" data-placement=\"top\" id=\"deletePerson_" + full.id +
                            "\" onclick=\"deletePerson('" + full.id + "');\"><span class=\"glyphicon glyphicon-trash\"/></span></a>"
                }
                }
            ]
        });
    });

    function closeForm(formName) {
        $("#editPanel").hide();
        $("#" + formName + "Div").empty();
    }

    function savePerson() {
        $("#editPanel").show();
        $("#personDiv").load('person');
        $("#addPersonButton").hide();
    }

    function editPerson(id) {
        if ($("#id").val() == id) {
            return null;
        }
        $("#editPanel").show();
        $("#personDiv").load("person/" + id);
        $("#addPersonButton").hide();
    }

    function deletePerson(id) {
        if (id == "") {
            showErrorMessage("Не найден ID !");
        } else {
            $.ajax({
                url: "deletePerson/" + id,
                type: "post",
                success: function (html) {
                    showSuccessMessage(html);
                    $("#listPerson").DataTable().ajax.url("allPerson").load(null, false);
                    if ($("#id").val() == id) {
                        $("#editPanel").hide();
                        $("#personDiv").empty();
                    }
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
<div class="container">
    <button id="addPersonButton" class="btn btn-success" onclick="savePerson()">
        <span class="glyphicon glyphicon-plus"></span> Добавить владельца
    </button>
    <div id="editPanel" class="panel panel-success" style="display:none">
        <div id="typeDiv" class="panel-heading">
        </div>
        <div class="panel-body">
            <div id="personDiv"></div>
        </div>
    </div>
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h4>Список членов ГК</h4>
            Общее количество: <span id="count" class="badge"></span></div>
        <br>

        <div class="panel-body">
            <table id="listPerson" class="table table-striped table-bordered" cellspacing="0" width="100%"></table>
        </div>
    </div>

</div>
<jsp:include page="footer.jsp"/>
