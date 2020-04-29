<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: KuzminKA
  Date: 25.08.2016
  Time: 19:07:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    $(document).ready(function() {
        $('#histGarag').modal('show');
        $('a.deleteReason').popConfirm({
            title: "Удалить?",
            content: "",
            placement: "bottom",
            yesBtn: "Да",
            noBtn: "Нет"
        });
    })
</script>
<div class="modal fade" id="histGarag" tabindex="-1" role="dialog">
    <div class="modal-dialog  modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">История смены владельцев</h4>
            </div>
            <div class="modal-body">
                <table class="table table-striped table-bord">
                    <thead>
                    <tr class="success">
                        <th>Дата</th>
                        <th>ФИО прошлого владельца</th>
                        <th>Причина смены</th>
                        <th>&nbsp;</th>
                    </tr>

                    </thead>
                    <c:forEach items="${garag.historyGarags}" var="hist">
                        <tr id="histTR_${hist.id}">
                            <td><fmt:formatDate type="both" dateStyle="short"
                                                value="${hist.dateRecord.time}"/></td>
                            <td>${hist.fioLastPerson}</td>
                            <td>${hist.reason}</td>
                            <td><a href="#" class="btn btn-danger deleteReason" onclick="deleteReason('${hist.id}');">
                                <span class="glyphicon glyphicon-trash"></span>
                            </a></td>
                        </tr>
                    </c:forEach>

                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
            </div>
        </div>
    </div>
</div>