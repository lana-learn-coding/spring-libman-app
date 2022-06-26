<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="p" tagdir="/WEB-INF/tags/page/tag" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--@elvariable id="data" type="org.springframework.data.domain.Page<io.lana.libman.core.book.Book>"--%>
<%--@elvariable id="highlight" type="java.lang.String"--%>


<layout:librarian>
    <jsp:attribute name="title">Book Manage</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>Book Manage</h3>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/dashboard"
                                   up-follow up-instant>Home</a>
                            </li>
                            <li class="breadcrumb-item active">Books</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-12">
                    <div class="card">
                        <div class="card-header pb-0">
                            <h5>Book Manage</h5>
                            <span>Manage list of book (<component:total pageMeta="${data}"/>)</span>
                        </div>
                        <form class="card-body row" up-target="#table, nav .pagination" method="get">
                            <helper:inherit-param excludes="query,shelfId"/>
                            <div class="col-12 col-sm-6 col-md-8 col-lg-9 mb-2">
                                <div class="input-group">
                                    <span class="input-group-text bg-primary"><i class="icon-search"></i></span>
                                    <input type="text"
                                           class="form-control form-control-lg"
                                           name="query"
                                           placeholder="Search for item"
                                           aria-label="query"
                                           value="${param.query}"
                                           up-autosubmit
                                           up-delay="400">
                                </div>
                            </div>
                            <div class="col-12 col-sm-6 col-md-4 col-lg-3 mb-2">
                                <select name="shelfId"
                                        class="form-select"
                                        up-autosubmit
                                        data-placeholder="Filter shelf"
                                        select2="${pageContext.request.contextPath}/library/tags/shelf/autocomplete"
                                        data-allow-clear="true">
                                    <option selected="selected"></option>
                                </select>
                            </div>
                        </form>
                        <hr class="my-0">
                        <div class="card-body" id="table">
                            <div class="row justify-content-between">
                                <div class="col-6 col-md-3 col-lg-2 mb-3">
                                    <component:sorting
                                            target="#table" up="up-scroll='layer'"
                                            labels="Newest;Title;Shelf;Borrowed;Updated At;Updated By;Id"
                                            values="createdAt,desc;info.title;shelf;ticket;updatedAt,desc;updatedBy;id,desc"/>
                                </div>
                                <div class="col-6 col-sm-6 d-flex justify-content-end align-items-start">
                                    <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOK_CREATE')">
                                        <button up-href="${pageContext.request.contextPath}/library/books/books/create"
                                                class="btn btn-primary" up-instant up-layer="new"
                                                up-dismissable="button">
                                            <i class="fa fa-plus-square-o fa-lg pe-2"></i>
                                            Create
                                        </button>
                                    </sec:authorize>
                                </div>
                            </div>
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">Id</th>
                                        <th scope="col">Image</th>
                                        <th scope="col">Shelf</th>
                                        <th scope="col">Title</th>
                                        <th scope="col">Borrow</th>
                                        <th scope="col">Updated At</th>
                                        <th scope="col">Action</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:set var="content" value="${empty data ? [] : data.content}"/>
                                    <c:forEach var="item" items="${data.content}" varStatus="loop">
                                        <c:set var="isHighlight" value="${item.id == highlight}"/>
                                        <tr>
                                            <th scope="row" <component:table-higlight
                                                    test="${isHighlight}"/>>${loop.index + 1}</th>
                                            <td <component:table-higlight test="${isHighlight}"/>>
                                                <div style="max-width: 120px">
                                                        ${ item.id }
                                                </div>
                                            </td>
                                            <td <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <img class="round-box mx-auto" style="width: 70px"
                                                     src="${(not empty item.image ? item.image : not empty item.info.image ? item.info.image : '/static/images/book-default.png')}"
                                                     alt="book cover">
                                            </td>
                                            <td <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                    ${item.shelf.name}
                                            </td>
                                            <td <component:table-higlight test="${isHighlight}"/>>
                                                <a href="${pageContext.request.contextPath}/library/books/infos/${item.info.id}/detail"
                                                   up-follow>
                                                        ${item.info.title}
                                                    <c:if test="${not empty item.info.year}">
                                                        <span class="ms-1">(${item.info.year})</span>
                                                    </c:if>
                                                </a>
                                                <c:if test="${not empty item.seriesName}">
                                                     <div class="small">
                                                         In Series: ${ item.seriesName }
                                                     </div>
                                                </c:if>
                                            </td>
                                            <td <component:table-higlight test="${isHighlight}"/>>
                                                <c:if test="${not empty item.ticket}">
                                                    <div>Borrowed by
                                                        <a up-follow up-instant class="ms-1"
                                                           href="${pageContext.request.contextPath}/library/readers/${item.ticket[0].reader.id}/detail">
                                                                ${item.ticket[0].reader.account.email}</a>
                                                    </div>
                                                    <div>From <span class="ms-1">
                                                        <helper:format-date date="${item.ticket[0].borrowDate}"/></span>
                                                    </div>
                                                    <div>To <span class="ms-1">
                                                        <helper:format-date date="${item.ticket[0].dueDate}"/></span>
                                                    </div>
                                                </c:if>
                                                <c:if test="${empty item.ticket}">
                                                    NONE
                                                </c:if>
                                            </td>
                                            <td <component:table-higlight test="${isHighlight}"/>>
                                                <div><helper:format-instant date="${item.updatedAt}"/></div>
                                                <div>
                                                    by ${ not empty item.updatedBy ? item.updatedBy : item.createdBy }</div>
                                            </td>
                                            <td <component:table-higlight test="${isHighlight}"/>>
                                                <a href="${pageContext.request.contextPath}/library/books/books/${item.id}/detail"
                                                   up-instant up-layer="new" class="mr-1 txt-primary">
                                                    <i data-feather="external-link"
                                                       style="width: 20px; height: 20px"></i>
                                                </a>
                                                <sec:authorize
                                                        access="hasAnyAuthority('ADMIN', 'BOOK_UPDATE')">
                                                    <a href="${pageContext.request.contextPath}/library/books/books/${item.id}/update"
                                                       class="mr-1 txt-primary" up-instant up-layer="new"
                                                       up-history="false"
                                                       up-dismissable="button">
                                                        <i data-feather="edit"
                                                           style="width: 20px; height: 20px"></i>
                                                    </a>
                                                </sec:authorize>
                                                <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOK_DELETE')">
                                                    <a href="${pageContext.request.contextPath}/library/books/books/${item.id}/delete"
                                                       up-history="false" up-layer="new" up-instant
                                                       up-dismissable="button"
                                                       class="txt-danger">
                                                        <i data-feather="trash" style="width: 20px; height: 20px"></i>
                                                    </a>
                                                </sec:authorize>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <c:if test="${empty content}">
                                            <component:empty/>
                                        </c:if>
                            </div>
                            <nav class="d-flex justify-content-between mt-3">
                                <component:pagination pageMeta="${data}" target="#table"
                                                      up="up-scroll='layer' up-transition='cross-fade'"/>
                                <div class="d-none d-md-block pt-2">
                                    <component:total pageMeta="${data}" verbose="true"/>
                                </div>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:librarian>
