<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="pageMeta" required="true" type="org.springframework.data.domain.Page" %>
<%@ attribute name="verbose" required="false" type="java.lang.Boolean" %>

<c:if test="${empty verbose or not verbose}">
    <span comp="total">Total of ${pageMeta.totalElements} results</span>
</c:if>

<c:if test="${not empty verbose and verbose}">
    <c:set var="to" value="${pageMeta.pageable.offset + pageMeta.pageable.pageSize + 1}"/>
    <span comp="total">
        Showing ${pageMeta.pageable.offset + 1} to ${to < pageMeta.totalElements ? to : pageMeta.totalElements} of ${pageMeta.totalElements} results
    </span>
</c:if>
