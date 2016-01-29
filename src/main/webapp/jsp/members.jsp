<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp"/>
<script type="text/javascript">
    $(function() {
        $.scrollUp();

        var table = $('#membersTable').DataTable({
            "order": [
                [ 0, 'asc' ]
            ],
            "ajax": "members",
            "fnCreatedRow": function (nRow, aData) {
                $(nRow).attr('id', 'my' + aData.id);
            },
            "columns": [
                {"render": function(data, type, full) {
                    return '<a href=\"#\" onclick=\"editPerson(' + full.id + ')\">' +
                            full.lastName + " " + full.name + " " + full.fatherName + '</a>'

                }, 'title': 'ФИО'},
                {"data":"telephone", 'title': 'Телефон'},
                {"data": "address.address",  'title': 'Адрес'},
                {"data": "benefits",  'title': 'Льготы'}
            ]
        });

    });

    function editPerson(id) {
        if ($("#id").val() == id) {
            return null;
        }
        $("#editPanel").show();
        $("#personDiv").load("person/" + id);
        $("#addPersonButton").hide();
    }
</script>
<div class="container">
    <div id="editPanel" class="panel panel-success" style="display:none">
        <div id="typeDiv" class="panel-heading">
        </div>
        <div class="panel-body">
            <div id="personDiv"></div>
        </div>
    </div>
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h4>Члены правления ГК</h4>
        </div>
        <div class="panel-body">
            <table id="membersTable" class="table table-striped table-bordered" cellspacing="0" width="100%"></table>
        </div>
    </div>
</div>