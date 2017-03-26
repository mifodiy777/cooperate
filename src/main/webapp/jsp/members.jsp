<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp"/>
<script type="text/javascript">
    $(function () {
        $.scrollUp();

        $('.cooperateTable').DataTable({
            "order": [
                [0, 'asc']
            ],
            "ajax": "members",
            "fnCreatedRow": function (nRow, aData) {
                $(nRow).attr('id', 'personTR_' + aData.personId);
            },
            "columns": [
                {"render": function (data, type, full) {
                        return '<a href=\"#\" onclick=\"editEntity(' + full.personId +',\'person\')\">' + full.fio + '</a>'
                    }, 'title': 'ФИО'
                },
                {"data": "phone", 'title': 'Телефон', "searchable": false},
                {"data": "address", 'title': 'Адрес', "searchable": false},
                {"data": "benefits", 'title': 'Льготы', "searchable": false}
            ]
        });

    });

</script>
<div class="container-fluid">
    <div id="formPanel"></div>
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h4 class="panel-title">Члены правления ГК</h4>
        </div>
        <div class="panel-body">
            <table id="membersTable" class="table table-striped table-bordered cooperateTable" cellspacing="0" width="100%"></table>
        </div>
    </div>
</div>