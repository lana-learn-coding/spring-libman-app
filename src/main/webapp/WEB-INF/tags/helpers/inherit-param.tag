<%@ tag body-content="empty" %>
<%@ attribute name="excludes" required="false" type="java.lang.String" %>
<%@ attribute name="defaults" required="false" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="excludes" value="${(empty excludes) ? '' : excludes}"/>
<c:set var="defaults" value="${(empty defaults) ? 'page' : defaults}"/>

<c:forEach items="${param}" var="entry">
    <c:if test="${not fn:contains(excludes, entry.key) and not fn:contains(defaults, entry.key)}">
        <input type="hidden" name="${entry.key}" value="${entry.value}">
    </c:if>
</c:forEach>
