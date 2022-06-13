<%@ tag description="Sorting component" body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ attribute name="labels" required="true" type="java.lang.String" %>
<%@ attribute name="values" required="true" type="java.lang.String" %>
<%@ attribute name="target" required="false" type="java.lang.String" %>
<%@ attribute name="up" required="false" type="java.lang.String" %>

<c:set var="_labels" value="${fn:split(labels, ';')}"/>
<c:set var="_values" value="${fn:split(values, ';')}"/>

<c:url var="url" value="">
    <c:forEach items="${param}" var="entry">
        <c:if test="${entry.key != 'page' && entry.key != 'sort'}">
            <c:param name="${entry.key}" value="${entry.value}"/>
        </c:if>
    </c:forEach>
</c:url>

<form method="get" ${up} up-history="true" up-autosubmit>
    <select class="form-select" name="sort"
    ${empty target ? '' : 'up-target=\"'.concat(target).concat('\"') }
            aria-label="Sort">
        <option value="" <c:if test="${empty param.sort}">selected</c:if>>
            No Sort
        </option>
        <c:forEach items="${_labels}" varStatus="loop">
            <option value="${_values[loop.index]}"
                    <c:if test="${_values[loop.index] == param.sort}">selected</c:if>>
                    ${_labels[loop.index]}
            </option>
        </c:forEach>
    </select>
</form>
