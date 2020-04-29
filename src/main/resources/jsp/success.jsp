<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Author: LemeshkinMO
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="success_message" onclick="$('#messages').hide();" style="display:none">
    <a href="#" class="close">&times;</a>
    <span><c:out value="${message}"/></span>
</div>
<script type="text/javascript">
    $("#success_message").show(800);
</script>
