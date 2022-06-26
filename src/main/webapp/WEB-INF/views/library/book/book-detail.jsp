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
<%--@elvariable id="entity" type="io.lana.libman.core.book.Book"--%>

<layout:modal>
    <jsp:attribute name="title">Book Detail</jsp:attribute>
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
                                <a href="${pageContext.request.contextPath}/library/books/books"
                                   up-follow up-instant>Books</a>
                            </li>
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
                            <h5>Book Detail</h5>
                            <span>Detail of ${entity.info.name}</span>
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
                                    <img class="rounded" style="width: 110px"
                                         src="${(not empty entity.image ? entity.image : not empty entity.info.image ? entity.info.image : '/static/images/book-default.png')}"
                                         alt="image">
                                    <div class="w-100 d-flex justify-content-center pt-1 pt-sm-2">
                                        <span class="badge badge-primary">${entity.status}</span>
                                    </div>
                                </div>
                                <div class="mt-2 mt-sm-0">
                                    <div class="mb-1">Id <span class="small ms-2">${entity.id}</span></div>
                                    <div class="mb-1">Title <span class="ms-2">${entity.title}</span>
                                        <c:if test="${not empty entity.info.year}">(${entity.info.year})</c:if>
                                    </div>
                                    <c:if test="${not empty entity.seriesName}">
                                        <div class="mb-1">Series
                                            <a class="ms-2" up-layer="new" up-size="large"
                                               href="${pageContext.request.contextPath}/library/tags/series/${entity.info.series.id}/detail">
                                                    ${entity.seriesName}
                                            </a>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty entity.authorName}">
                                        <div class="mb-1">Author
                                            <a class="ms-2" up-layer="new" up-size="large"
                                               href="${pageContext.request.contextPath}/library/tags/authors/${entity.info.author.id}/detail">
                                                    ${entity.authorName}
                                            </a>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty entity.shelf}">
                                        <div class="mb-1">Shelf
                                            <a class="ms-2" up-layer="new" up-size="large"
                                               href="${pageContext.request.contextPath}/library/tags/shelf/${entity.shelf.id}/detail">
                                                    ${entity.shelf.name}
                                            </a>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty entity.note}">
                                         <div class="mb-1">Note <span class="font-primary ms-2">${entity.note}</span>
                                         </div>
                                      </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:modal>
