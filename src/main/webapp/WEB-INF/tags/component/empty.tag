<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="message" required="false" type="java.lang.String" %>
<%@ attribute name="icon" required="false" type="java.lang.String" %>

<c:set var="message" value="${empty message ? 'No Data' : message}"/>
<c:set var="icon" value="${empty icon ? 'inbox' : icon}"/>


<div class="d-flex flex-column align-items-center my-5 txt-light">
    <i data-feather="inbox" style="width: 100px; height: 100px; stroke-width: 0.5px"></i>
    <span>${message}</span>
</div>
