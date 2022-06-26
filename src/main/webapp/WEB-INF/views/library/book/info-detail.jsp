<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="io.lana.libman.core.book.Book.Status" %>

<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--@elvariable id="highlight" type="java.lang.String"--%>
<%--@elvariable id="data" type="org.springframework.data.domain.Page<io.lana.libman.core.book.Book>"--%>
<%--@elvariable id="entity" type="io.lana.libman.core.book.BookInfo"--%>

<layout:librarian>
    <jsp:attribute name="title">Book Info Detail</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>Info Detail</h3>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/dashboard"
                                   up-follow up-instant>Home</a>
                            </li>
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/books/infos"
                                   up-follow up-instant>Books</a>
                            </li>
                            <li class="breadcrumb-item">Info</li>
                            <li class="breadcrumb-item active">${entity.name}</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row">
                <div class="col-12 modal-p-0">
                    <div class="card text-start modal-m-0">
                        <div class="card-header pb-0">
                            <h5>Book Info Detail</h5>
                            <span>Detail of ${entity.name}</span>
                        </div>
                        <div class="card-body pt-1">
                            <div class="mb-4 text-muted">
                                <div class="d-inline">
                                    <i class="fa fa-calendar-o"></i>
                                    <span class="ms-1"><helper:format-instant date="${entity.updatedAt}"/></span>
                                </div>
                                <c:if test="${not empty entity.updatedBy}">
                                    <div class="ms-2 d-none d-md-inline">
                                        <i class="fa fa-user"></i>
                                        <span class="ms-1">${entity.updatedBy}</span>
                                    </div>
                                </c:if>
                            </div>
                            <div class="d-flex flex-column flex-sm-row">
                                <div class="me-3 me-md-4 d-flex justify-content-center justify-content-sm-start align-items-baseline">
                                    <img class="rounded" style="width: 110px"
                                         src="${(empty entity.image ? '/static/images/book-default.png' : entity.image)}"
                                         alt="image">
                                </div>
                                <div class="mt-2 mt-sm-0">
                                    <div class="mb-1">Title <span class="ms-2">${entity.title}</span>
                                        <c:if test="${not empty entity.year}">(${entity.year})</c:if>
                                    </div>
                                    <c:if test="${not empty entity.seriesName}">
                                        <div class="mb-1">Series
                                            <a class="ms-2" up-layer="new" up-size="large"
                                               href="${pageContext.request.contextPath}/library/tags/series/${entity.series.id}/detail">
                                                    ${entity.seriesName}
                                            </a>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty entity.authorName}">
                                        <div class="mb-1">Author
                                            <a class="ms-2" up-layer="new" up-size="large"
                                               href="${pageContext.request.contextPath}/library/tags/authors/${entity.author.id}/detail">
                                                    ${entity.authorName}
                                            </a>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty entity.genres}">
                                        <div class="mb-1">Genres
                                            <div class="ms-2 d-inline">
                                                <c:forEach items="${entity.genres}" var="genre">
                                                    <span class="badge badge-primary"
                                                          up-href="${pageContext.request.contextPath}/library/tags/genres/${genre.id}/detail"
                                                          up-layer="new" up-size="large"
                                                          up-clickable>${genre.name}</span>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </c:if>
                                    <div class="mb-1">Books
                                        <span class="txt-info ms-2">${entity.availableBooksCount}</span>
                                        /
                                        <span>${ entity.booksCount }</span>
                                    </div>
                                    <c:if test="${not empty entity.publisherName}">
                                        <div class="mb-1">Publisher
                                            <a class="ms-2" up-layer="new" up-size="large"
                                               href="/library/tags/publishers/${entity.publisher.id}/detail">
                                                    ${entity.publisherName}
                                            </a>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                            <c:if test="${not empty entity.about}">
                                <div class="mt-3 ms-1">
                                    <h6 class="mb-1">About the Book</h6>
                                    <span class="">${entity.about}</span>
                                </div>
                            </c:if>
                        </div>
                        <div class="card-footer">
                            <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOKINFO_UPDATE')">
                                <button up-href="${pageContext.request.contextPath}/library/books/infos/${id}/update"
                                        class="btn btn-primary me-2" up-instant up-layer="new" up-size="large"
                                        up-dismissable="button" up-history="false">
                                    Update
                                </button>
                            </sec:authorize>
                            <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOKINFO_DELETE')">
                                <button up-href="${pageContext.request.contextPath}/library/books/infos/${id}/delete"
                                        class="btn btn-danger me-2" up-instant up-layer="new" up-history="false">
                                    Delete
                                </button>
                            </sec:authorize>
                            <a href="${pageContext.request.contextPath}/library/books/infos"
                               up-follow up-instant class="btn btn-light me-2">Manage</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-header pb-0">
                            <h5>Books List</h5>
                            <span>List books of: ${entity.name} (Total of ${entity.booksCount})</span>
                        </div>
                        <div class="card-body" id="table">
                            <div class="row">
                                <div class="col-12 mb-3 d-flex justify-content-end">
                                    <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOKBORROW_READ')">
                                        <button up-href="${pageContext.request.contextPath}/library/books/infos/${id}/history"
                                                class="btn btn-primary me-2" up-instant up-layer="new" up-size="large"
                                                up-dismissable="button" up-history="false">
                                            <i class="fa fa-clock-o fa-lg pe-2"></i>
                                            Borrow History
                                        </button>
                                    </sec:authorize>
                                    <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOK_CREATE')">
                                        <button up-href="${pageContext.request.contextPath}/library/books/books/create?parentId=${id}"
                                                class="btn btn-primary" up-instant up-layer="new"
                                                up-dismissable="button">
                                            <i class="fa fa-plus-square-o fa-lg pe-2"></i>
                                            Create
                                        </button>
                                    </sec:authorize>
                                </div>
                            </div>
                            <div class="row justify-content-between mb-3">
                                <div class="col-xs-12 col-md-3 col-lg-2 my-1">
                                     <component:sorting
                                             target="#table"
                                             up="up-scroll='#table' up-transition='cross-fade'"
                                             labels="Available;Status;Borrow Date;Due Date;Updated At;Updated By;Id"
                                             values="status;status,desc;ticket.borrowDate,desc;ticket.dueDate,desc;updatedAt,desc;updatedBy;id,desc"/>
                                </div>
                                <form class="col-xs-12 col-md-6 my-1"
                                      up-target="#table, nav .pagination" up-transition='cross-fade' method="get">
                                    <helper:inherit-param excludes="query"/>
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
                                </form>
                            </div>
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">Id</th>
                                        <th scope="col">Image</th>
                                        <th scope="col">Shelf</th>
                                        <th scope="col">Status</th>
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
                                                     src="${(empty item.image ? '/static/images/book-default.png' : item.image)}"
                                                     alt="book cover">
                                            </td>
                                            <th scope="row" <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                    ${item.shelf.name}
                                            </th>
                                            <td <component:table-higlight test="${isHighlight}"/>>
                                                <c:if test="${item.status == 'AVAILABLE'}">
                                                    <span class="badge badge-success">${item.status}</span>
                                                </c:if>
                                                <c:if test="${item.status != 'AVAILABLE'}">
                                                    <span class="badge badge-warning">${item.status}</span>
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
                                                <c:if test="${not empty item.ticket}">
                                                    <sec:authorize
                                                            access="hasAnyAuthority('ADMIN', 'BOOKBORROW_UPDATE')">
                                                    <a href="${pageContext.request.contextPath}/library/borrows/${item.ticket[0].id}/return"
                                                       class="mr-1 txt-primary" up-instant up-layer="new"
                                                       up-history="false">
                                                        <i data-feather="check-square"
                                                           style="width: 20px; height: 20px"></i>
                                                    </a>
                                                </sec:authorize>
                                                </c:if>
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
                                <component:pagination pageMeta="${data}"
                                                      target="#table"
                                                      up="up-scroll='#table' up-transition='cross-fade'"/>
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
