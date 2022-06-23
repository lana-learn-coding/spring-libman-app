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
                                    <div class="mb-1">Title <span class="ms-2">${entity.title}</span></div>
                                    <c:if test="${not empty entity.seriesName}">
                                        <div class="mb-1">Series
                                            <a class="ms-2" up-layer="new" up-size="large"
                                               href="${pageContext.request.contextPath}/library/tags/series/${entity.series.id}/detail?size=5">
                                                    ${entity.seriesName}
                                            </a>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty entity.authorName}">
                                        <div class="mb-1">Author
                                            <a class="ms-2" up-layer="new" up-size="large"
                                               href="${pageContext.request.contextPath}/library/tags/authors/${entity.author.id}/detail?size=5">
                                                    ${entity.authorName}
                                            </a>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty entity.genres}">
                                        <div class="mb-1">Genres
                                            <div class="ms-2 d-inline">
                                                <c:forEach items="${entity.genres}" var="genre">
                                                    <span class="badge badge-primary"
                                                          up-href="${pageContext.request.contextPath}/library/tags/genres/${genre.id}/detail?size=5"
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
                                               href="/library/tags/publishers/${entity.publisher.id}/detail?size=5">
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
                            <div class="row justify-content-between mb-3">
                                <div class="col-xs-12 col-md-3 col-lg-2 my-1">
                                     <component:sorting
                                             target="#table, [comp=sorting]"
                                             up="up-scroll='#table' up-transition='cross-fade'"
                                             labels="Available;Status;Borrow Date;Due Date;Updated At;Updated By;Id"
                                             values="status;status,desc;ticket.borrowDate,desc;ticket.dueDate,desc;updatedAt,desc;updatedBy,id"/>
                                </div>
                                <form class="col-xs-12 col-md-6 my-1"
                                      up-target="#table table, nav .pagination" up-transition='cross-fade' method="get">
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
                                            <th scope="row" <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <component:index pageMeta="${data}" i="${loop.index}"/>
                                            </th>
                                            <td <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <img class="round-box mx-auto" style="width: 70px"
                                                     src="${(empty item.image ? '/static/images/book-default.png' : item.image)}"
                                                     alt="book cover">
                                            </td>
                                            <td <component:table-higlight test="${isHighlight}"/>>
                                                <c:if test="${item.status == 'AVAILABLE'}">
                                                    <span class="badge badge-success">${item.status}</span>
                                                </c:if>
                                                <c:if test="${item.status != 'AVAILABLE'}">
                                                    <span class="badge badge-warning">${item.status}</span>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${not empty item.ticket}">
                                                    <div>Borrowed by
                                                        <a up-follow up-instant class="ms-1"
                                                           href="${pageContext.request.contextPath}/library/readers/${item.ticket[0].reader.id}/detail?size=8">
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
                                            <td>
                                                <div><helper:format-instant date="${item.updatedAt}"/></div>
                                                <div>
                                                    by ${ not empty item.updatedBy ? item.updatedBy : item.createdBy }</div>
                                            </td>
                                            <td></td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <c:if test="${empty content}">
                                            <component:empty/>
                                </c:if>
                            </div>
                            <nav class="d-flex justify-content-start mt-3">
                                <component:pagination pageMeta="${data}"
                                                      target="#table, [comp=pagination]"
                                                      up="up-scroll='#table' up-transition='cross-fade'"/>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:librarian>
