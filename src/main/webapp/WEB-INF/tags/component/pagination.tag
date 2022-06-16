<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="pageMeta" required="true" type="org.springframework.data.domain.Page" %>
<%@ attribute name="target" required="false" type="java.lang.String" %>
<%@ attribute name="up" required="false" type="java.lang.String" %>

<c:url var="url" value="">
    <c:forEach items="${param}" var="entry">
        <c:if test="${entry.key != 'page'}">
            <c:param name="${entry.key}" value="${entry.value}"/>
        </c:if>
    </c:forEach>
</c:url>

<c:set var="totalPages" value="${pageMeta.totalPages == 0 ? 1 : pageMeta.totalPages - 1}"/>
<ul class="pagination pagination-primary" comp="pagination">
    <li class="page-item <c:if test="${pageMeta.pageable.pageNumber == 0}">disabled</c:if>">
        <a class="page-link"
        ${up} up-history="true"
        ${empty target ? '' : 'up-target=\"'.concat(target).concat('\"') } up-follow up-instant
           href="${(empty url ? "?page=" : url.concat("&page=")).concat(pageMeta.pageable.pageNumber - 1)}"
           aria-label="Previous">&laquo;</a>
    </li>
    <c:forEach var="i" begin="0" end="${totalPages}">
        <c:choose>
            <c:when test="${pageMeta.pageable.pageNumber == i}">
                <li class="page-item active">
                    <a class="page-link"
                        ${up} up-history="true"
                        ${empty target ? '' : 'up-target=\"'.concat(target).concat('\"') } up-follow up-instant
                       href="${(empty url ? "?page=" : url.concat("&page=")).concat(i)}">${i + 1}</a>
                </li>
            </c:when>

            <c:when test="${pageMeta.pageable.pageNumber < 4 && i < 5}">
                <li class="page-item">
                    <a class="page-link"
                        ${up} up-history="true"
                        ${empty target ? '' : 'up-target=\"'.concat(target).concat('\"') } up-follow up-instant
                       href="${(empty url ? "?page=" : url.concat("&page=")).concat(i)}">${i + 1}</a>
                </li>
            </c:when>

            <c:when test="${pageMeta.pageable.pageNumber > (totalPages - 4) && i > (totalPages - 5)}">
                <li class="page-item">
                    <a class="page-link"
                        ${up} up-history="true"
                        ${empty target ? '' : 'up-target=\"'.concat(target).concat('\"') } up-follow up-instant
                       href="${(empty url ? "?page=" : url.concat("&page=")).concat(i)}">${i + 1}</a>
                </li>
            </c:when>

            <c:when test="${i == 0 || i == totalPages || pageMeta.pageable.pageNumber == i - 1 || pageMeta.pageable.pageNumber == i + 1}">
                <li class="page-item">
                    <a class="page-link"
                        ${up} up-history="true"
                        ${empty target ? '' : 'up-target=\"'.concat(target).concat('\"') } up-follow up-instant
                       href="${(empty url ? "?page=" : url.concat("&page=")).concat(i)}">${i + 1}</a>
                </li>
            </c:when>

            <c:when test="${i == 1 || i == totalPages - 1}">
                <li class="page-item disabled">
                    <span class="page-link">...</span>
                </li>
            </c:when>
        </c:choose>
    </c:forEach>
    <li class="page-item <c:if test="${pageMeta.pageable.pageNumber == totalPages}">disabled</c:if>">
        <a class="page-link"
        ${up} up-history="true"
        ${empty target ? '' : 'up-target=\"'.concat(target).concat('\"') } up-follow up-instant
           href="${(empty url ? "?page=" : url.concat("&page=")).concat(pageMeta.pageable.pageNumber + 1)}"
           aria-label="Next">
            &raquo;
        </a>
    </li>
</ul>
