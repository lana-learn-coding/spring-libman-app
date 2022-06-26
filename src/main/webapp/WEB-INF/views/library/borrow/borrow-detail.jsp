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
<%--@elvariable id="entity" type="io.lana.libman.core.book.BookBorrow"--%>

<layout:modal>
    <jsp:attribute name="title">Book Detail</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>Info Detail</h3>
                        <ol class="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item">
                                    <a href="${pageContext.request.contextPath}/library/dashboard"
                                       up-follow up-instant>Home</a>
                                </li>
                                <li class="breadcrumb-item">
                                    <a href="${pageContext.request.contextPath}/library/borrows"
                                       up-follow up-instant>Borrows</a>
                                </li>
                                <li class="breadcrumb-item active">
                                    Detail
                                </li>
                            </ol>
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
                            <h5>Borrow <c:if test="${entity.returned}">History</c:if> Detail</h5>
                            <span>Detail of #${entity.id}</span>
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
                                <div class="me-3 me-md-4 d-flex align-items-center align-items-sm-start flex-column">
                                    <img class="rounded" style="width: 120px"
                                         src="${(not empty entity.book && not empty entity.book.image ? entity.book.image : '/static/images/book-default.png')}"
                                         alt="image">
                                    <div class="w-100 d-flex justify-content-center pt-2 pt-sm-3">
                                        <c:if test="${entity.returned}">
                                            <span class="badge badge-primary">Returned</span>
                                        </c:if>
                                        <c:if test="${not entity.returned}">
                                            <span class="badge badge-warning">Borrowing</span>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="mt-2 mt-sm-0">
                                    <div class="mb-1">Ticket <span class="small ms-2">${entity.ticket}</span></div>
                                    <div class="mb-1">Title
                                        <c:if test="${empty entity.book}">
                                            <span class="ms-2">${entity.title}</span>
                                        </c:if>
                                        <c:if test="${not empty entity.book}">
                                            <a class="ms-2 d-inline"
                                               href="${pageContext.request.contextPath}/library/books/infos/${entity.book.info.id}/detail"
                                               up-follow>${entity.title}</a>
                                        </c:if>
                                        <c:if test="${not empty entity.releaseYear}">(${entity.releaseYear})</c:if>
                                    </div>
                                    <c:if test="${not empty entity.seriesName}">
                                        <div class="mb-1">Series
                                            <c:if test="${empty entity.book}">
                                            <span class="ms-2">${entity.seriesName}</span>
                                            </c:if>
                                            <c:if test="${not empty entity.book}">
                                            <a class="ms-2 d-inline"
                                               href="${pageContext.request.contextPath}/library/tags/series/${entity.book.info.series.id}/detail"
                                               up-layer="new" up-size="large">${entity.title}</a>
                                            </c:if>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty entity.book}">
                                        <div class="mb-1">Shelf
                                            <a class="ms-2" up-layer="new" up-size="large"
                                               href="${pageContext.request.contextPath}/library/tags/shelf/${entity.book.shelf.id}/detail">
                                                    ${entity.book.shelf.name}
                                            </a>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty entity.reader}">
                                        <hr class="my-2">
                                        <div class="mb-1">Borrower
                                            <a class="ms-2" up-follow
                                               href="${pageContext.request.contextPath}/library/readers/${entity.reader.id}/detail">
                                                    ${entity.reader.account.email}
                                            </a>
                                        </div>
                                        <div class="mb-1">Full Name
                                            <div class="ms-2 d-inline">
                                                <c:if test="${not empty entity.reader.account.firstName}">
                                                    <span class="me-1">${entity.reader.account.firstName}</span>
                                                </c:if>
                                                <c:if test="${not empty entity.reader.account.firstName}">
                                                    <span>${entity.reader.account.lastName}</span>
                                                </c:if>
                                            </div>
                                        </div>
                                    </c:if>
                                    <div class="mb-1">
                                        <span class="me-1">
                                            From ${entity.borrowDate} to ${entity.dueDate}
                                        </span>
                                        <c:if test="${not empty entity.returnDate}">
                                            <span class="txt-primary">(${entity.returnDate})</span>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                            <c:if test="${not empty entity.note}">
                                <div class="mt-3 ms-1">
                                    <h6 class="mb-1">Note</h6>
                                    <span class="">${entity.note}</span>
                                </div>
                            </c:if>
                        </div>
                        <div class="card-footer">
                            <c:if test="${empty param.history}">
                                 <c:if test="${not entity.returned}">
                                    <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOKBORROW_UPDATE')">
                                    <button up-href="${pageContext.request.contextPath}/library/borrows/${id}/return"
                                            class="btn btn-primary me-2" up-instant up-layer="new" up-history="false">
                                        Return
                                    </button>
                                    </sec:authorize>
                                    <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOKBORROW_DELETE')">
                                    <button up-href="${pageContext.request.contextPath}/library/borrows/${id}/delete"
                                            class="btn btn-danger me-2" up-instant up-layer="new" up-history="false">
                                        Delete
                                    </button>
                                    </sec:authorize>
                                 </c:if>
                            </c:if>
                            <c:if test="${entity.returned}">
                                 <sec:authorize
                                         access="hasAnyAuthority('ADMIN', 'BOOKBORROW_DELETE') && hasAnyAuthority('FORCE', 'ADMIN')">
                                <button up-href="${pageContext.request.contextPath}/library/borrows/history/${id}/delete"
                                        class="btn btn-danger me-2" up-instant up-layer="new" up-history="false">
                                    Delete (History)
                                </button>
                                </sec:authorize>
                            </c:if>
                            <a href="${pageContext.request.contextPath}/library/borrows"
                               class="btn btn-light" up-back up-dismiss up-back>Back</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:modal>
