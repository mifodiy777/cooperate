<%--
  Created by IntelliJ IDEA.
  User: KuzminKA
  Date: 13.10.2015
  Time: 18:07:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="header.jsp"/>
<script type="text/javascript">

    $(document).ready(function () {
        $('#historyTable').DataTable({
            "order": [[ 0, "desc" ]],
            columnDefs: [
                { type: 'de_datetime', targets: 0 }
            ]
        });
    });

</script>
<div class="container">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h4 class="panel-title">История</h4>
        </div>
        <div class="panel-body">
            <table id="historyTable" class="table table-striped table-bordered">
                <thead>
                <tr>
                    <th>Дата</th>
                    <th>Событие</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${history}" var="h">
                    <tr>
                        <td><fmt:formatDate type="both" dateStyle="short" timeStyle="short"
                                            value="${h.dateEvent.time}"/></td>
                        <td>${h.evented}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

    </div>
    <jsp:include page="footer.jsp"/>
