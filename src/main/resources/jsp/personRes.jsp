<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${!empty persons}">
  <ul>
    <c:forEach items="${persons}" var="p">
      <li><a id="${p.id}" class="choosePerson">${p.lastName} ${p.name} ${p.fatherName}</a></li>
    </c:forEach>
  </ul>
</c:if>

<c:if test="${empty persons}">
  Не найдено...
</c:if>
