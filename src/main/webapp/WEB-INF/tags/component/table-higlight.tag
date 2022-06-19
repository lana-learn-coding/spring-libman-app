<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="test" required="true" type="java.lang.Boolean" %>
<%@ attribute name="color" required="false" type="java.lang.String" %>

<c:set var="color" value="${empty color ? 'success' : color}"/>

<c:if test="${test}">
    data-remove-class="bg-${color}"
    style="transition: background-color 3s, color 3s"
    class="bg-${color}"
</c:if>
