<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp"/>
<script type="text/javascript">

    $(document).ready(function () {

        var fio = "";

        $.scrollUp();

        $('.cooperateTable').DataTable({
            "searching": false,
            "paging": false,
            "ajax": {
                "url": "allPerson",
                data: function (d) {
                    d.fio = fio;
                }
            },
            "fnCreatedRow": function (nRow, aData) {
                $(nRow).attr('id', 'personTR_' + aData.id);
            },
            "fnDrawCallback": function () {
                $('a.deleteButton').off("click");
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
                {
                    "render": function (data, type, full) {
                        var vip = '';
                        if (full.memberBoard) {
                            vip = ' <span class="label label-warning">ЧП</span>';
                        }
                        return '<a href=\"#\" onclick=\"initTR(' + full.id + ');editEntity(' + full.id + ',\'person\')\">' + full.fio + vip + '</a>'
                    }, 'title': 'ФИО'
                },
                {"data": "phone", 'title': 'Телефон', "searchable": false},
                {"data": "address", 'title': 'Адрес', "searchable": false},
                {"data": "benefits", 'title': 'Льготы', "searchable": false},
                {
                    "data": "garags", 'title': 'Гаражи', "searchable": false, "render": function (data, type, full) {
                    var linkGarag = "";
                    data.forEach(function (item, i, arr) {
                        linkGarag += "<a href='<c:url value="linkGarag"/>?id=" + item.garagId + "&series=" + item.series + "' title='Найти гараж'>" + item.garag + "</a><br>"
                    });
                    return linkGarag;
                }
                },
                {
                    'title': 'Удалить',
                    "searchable": false,
                    className: "deletePerson",
                    "render": function (data, type, full) {
                        return "<a href=\"#\" class=\"deleteButton btn btn-danger btn-sm\" data-placement=\"top\" id=\"deletePerson_" + full.id +
                            '" onclick="deleteEntity(' + full.id + ',\'deletePerson\');\"><span class="glyphicon glyphicon-trash"/></span></a>'
                    }
                }
            ]
        });

        $('#search-field').keypress(function (e) {
            if (e.which == 13) {
                var input = $(this).val();
                if (input.length > 0) {
                    fio = input;
                    $(".cooperateTable").DataTable().ajax.reload(null, false);
                }
                console.log(new Date() + " fio = " + fio);
            }
        })

    });

    function emptyPerson() {
        var table = $('.cooperateTable').DataTable();
        var btn = $('#emptyPerson');
        if (btn.text() === 'Показать без гаражей') {
            btn.html('Показать всех');
            table.ajax.url("emptyPersons").load();
        } else {
            btn.html('Показать без гаражей');
            table.ajax.url("allPerson").load();
        }
    }


</script>
<div class="container-fluid">
    <button class="btn btn-success addBtn pull-left" onclick="saveEntity('person')">
        <span class="glyphicon glyphicon-plus"></span> Добавить владельца
    </button>
    <button id="emptyPerson" class="btn btn-warning pull-right" onclick="emptyPerson()">Показать без гаражей</button>
    <div class="clearfix"></div>
    <div id="formPanel"></div>
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h4>Список членов ГК</h4>
            Общее количество: <span id="count" class="badge"></span></div>
        <br>

        <div class="panel-body">
            <div class="form-group input-group-lg pull-right">
                <input id="search-field" name="search" class="form-control" placeholder="Введите ФИО и нажмите Enter">
            </div>
            <table class="table table-striped table-bordered cooperateTable" cellspacing="0" width="100%"></table>
        </div>
    </div>

</div>
<jsp:include page="footer.jsp"/>
