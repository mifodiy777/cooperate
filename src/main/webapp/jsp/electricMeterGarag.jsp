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
            "ajax": "electricMeters",
            "columns": [
                {"render": function (data, type, full) {
                        return '<a href=\"#\" onclick=\"editEntity(' + full.id +',\'garag\')\">' + full.series +' - '+ full.number + '</a>'
                    }, 'title': 'Гараж'
                },
                {"data": "person.fio", "defaultContent": "", "searchable": false, 'title': 'ФИО'},
                {"data": "person.phone", "defaultContent": "", "searchable": false, 'title': 'Телефон'},
                {"data": "person.address", "defaultContent": "", "searchable": false, 'title': 'Адрес'},
                {"data": "person.benefits", "defaultContent": "", "searchable": false, 'title': 'Льготы'},
            ]
        });

    });

</script>
<div class="container-fluid">
    <div id="formPanel"></div>
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h4 class="panel-title">Гаражи имеющие счетчики</h4>
        </div>
        <div class="panel-body">
            <table id="electricMetersTable" class="table table-striped table-bordered cooperateTable" cellspacing="0" width="100%"></table>
        </div>
    </div>
</div>