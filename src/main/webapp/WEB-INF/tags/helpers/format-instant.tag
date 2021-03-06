<%@ tag body-content="empty" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="date" required="true" type="java.time.Instant" %>
<%@ attribute name="pattern" required="false" type="java.lang.String" %>
<%@ attribute name="var" required="false" type="java.lang.String" %>

<c:if test="${empty pattern}">
    <c:set var="pattern" value="yyyy-MM-dd HH:mm:ss"/>
</c:if>

<jsp:useBean id="parsedDate" class="java.util.Date"/>
<c:set target="${parsedDate}" property="time" value="${date.toEpochMilli()}"/>
<c:if test="${not empty var}">
    <fmt:formatDate value="${parsedDate}" type="both" pattern="${pattern}" var="value"/>
    <c:set target="${requestScope}" property="${var}" value="${value}"/>
</c:if>
<c:if test="${empty var}">
    <fmt:formatDate value="${parsedDate}" type="both" pattern="${pattern}"/>
</c:if>
