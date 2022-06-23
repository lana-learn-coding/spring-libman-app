<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="pageMeta" required="true" type="org.springframework.data.domain.Page" %>
<%@ attribute name="i" required="true" type="java.lang.Integer" %>

<c:out value="${pageMeta.pageable.offset + i + 1}"/>
