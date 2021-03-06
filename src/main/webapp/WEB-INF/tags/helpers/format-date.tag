<%@ tag body-content="empty" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="date" required="true" type="java.time.LocalDate" %>
<%@ attribute name="pattern" required="false" type="java.lang.String" %>
<%@ attribute name="var" required="false" type="java.lang.String" %>

<c:if test="${empty pattern}">
    <c:set var="pattern" value="yyyy-MM-dd"/>
</c:if>

<jsp:useBean id="parsedDate" class="java.util.Date"/>
<fmt:parseDate value="${date.toString()}" type="date" var="parsedDate" pattern="${pattern}"/>
<c:if test="${not empty var}">
    <fmt:formatDate value="${parsedDate}" type="date" pattern="${pattern}" var="value"/>
    <c:set target="${requestScope}" property="${var}" value="${value}"/>
</c:if>
<c:if test="${empty var}">
    <fmt:formatDate value="${parsedDate}" type="date" pattern="${pattern}"/>
</c:if>
