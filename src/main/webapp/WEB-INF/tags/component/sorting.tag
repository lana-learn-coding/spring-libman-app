<%@ tag description="Sorting component" body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ attribute name="labels" required="true" type="java.lang.String" %>
<%@ attribute name="values" required="true" type="java.lang.String" %>
<%@ attribute name="target" required="false" type="java.lang.String" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="up" required="false" type="java.lang.String" %>
<c:set var="target" value="[comp=sorting], ${not empty target ? target : ''}"/>

<c:set var="_labels" value="${fn:split(labels, ';')}"/>
<c:set var="_values" value="${fn:split(values, ';')}"/>

<form method="get" ${up} up-history="true" up-autosubmit comp="sorting"
${empty target ? '' : 'up-target=\"'.concat(target).concat('\"') }>
    <c:forEach items="${param}" var="entry">
        <c:if test="${entry.key != 'page' && entry.key != 'sort'}">
            <input type="hidden" name="${entry.key}" value="${entry.value}">
        </c:if>
    </c:forEach>
    <select class="form-select ${cssClass}" name="sort"
            aria-label="Sort">
        <c:forEach items="${_labels}" varStatus="loop">
            <option value="${_values[loop.index]}"
                    <c:if test="${_values[loop.index] == param.sort}">selected</c:if>>
                    ${_labels[loop.index]}
            </option>
        </c:forEach>
        <option value="" <c:if test="${empty param.sort}">selected</c:if>>
            No Sort
        </option>
    </select>
</form>
