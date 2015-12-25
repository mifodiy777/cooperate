<%--
  Author: LemeshkinMO
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>
<div id="modalLoadingParse" class="modal fade" role="dialog">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-body">
                <img src="<c:url value="/images/ajax_blue_.gif"/>" id="loading-indicator"/>
            </div>
        </div>
    </div>
</div>
<div class="container">
    <h1>Конвертация</h1>

    <form method="POST" enctype="multipart/form-data"
          action="upload">
        <div class="form-group input-group">
            <label for="file" class="input-group-addon"> Конвертировать: </label>
            <input type="file" id="file" name="file" class="form-control">
            <span class="input-group-btn">
            <button type="submit" class="btn btn-primary">Загрузить</button>
        </span>
        </div>

        <div class="col-md-8">
            <div class="form-group input-group">
                <input type="checkbox" id="benefits"  class="form-control" name="benefits" value="true" autocomplete="true"/>

                <div class="[ btn-group ]">
                    <label for="benefits" class="[ btn btn-info ]">
                        <span class="[ glyphicon glyphicon-ok ]"></span>
                        <span> </span>
                    </label>
                    <label for="benefits" class="[ btn btn-default active ]">
                        Конвертер льготников
                    </label>
                </div>
            </div>
        </div>

    </form>

</div>
<jsp:include page="footer.jsp"/>