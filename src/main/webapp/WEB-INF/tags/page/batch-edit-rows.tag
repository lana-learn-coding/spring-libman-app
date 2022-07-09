<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ attribute name="batch" required="true" type="io.lana.libman.core.book.controller.dto.BatchReturnDto" %>
<%@ attribute name="highlight" required="false" type="java.lang.String" %>
<%@ attribute name="content" required="true" type="java.util.Collection<io.lana.libman.core.book.BookBorrow>" %>

<c:forEach var="item" items="${content}" varStatus="loop">
    <c:set var="isHighlight" value="${item.id == highlight}"/>
    <tr>
        <td <component:table-higlight test="${isHighlight}"/>>
            <div class="checkbox checkbox-primary text-center">
                <input id="${item.id}" type="checkbox" name="ids" value="${item.id}"
                       <c:if test="${batch.ids.contains(item.id)}">checked</c:if>
                       up-autosubmit>
                <label for="${item.id}"></label>
            </div>
        </td>
        <th scope="row" <component:table-higlight
                test="${isHighlight}"/>>
                ${loop.index + 1}
        </th>
        <td <component:table-higlight
                test="${isHighlight}"/>>
            <div style="max-width: 180px">
                    ${ item.id }
            </div>
        </td>
        <td  <component:table-higlight
                test="${isHighlight}"/>>
            <a href="${pageContext.request.contextPath}/library/books/infos/${item.book.info.id}/detail"
               up-follow>${item.title}
            </a>
        </td>
        <td <component:table-higlight
                test="${isHighlight}"/>>
            <div <c:if test="${item.isOverDue()}">class="txt-danger"</c:if>>
                <div>From <span class="ms-1">
                    <helper:format-date date="${item.borrowDate}"/></span>
                </div>
                <div>To <span class="ms-1">
                    <helper:format-date date="${item.dueDate}"/></span>
                </div>
            </div>
        </td>
        <td <component:table-higlight
                test="${isHighlight}"/>>
            <fmt:formatNumber value="${item.totalCost}" type="currency"
                              maxFractionDigits="2"/>
        </td>
        <td <component:table-higlight
                test="${isHighlight}"/>>
            <div><helper:format-instant
                    date="${item.updatedAt}"/></div>
            <div>by ${ not empty item.updatedBy ? item.updatedBy : item.createdBy }</div>
        </td>
    </tr>
</c:forEach>
