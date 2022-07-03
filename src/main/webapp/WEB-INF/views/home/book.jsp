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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--@elvariable id="highlight" type="java.lang.String"--%>
<%--@elvariable id="entity" type="io.lana.libman.core.book.BookInfo"--%>

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
                                <a href="${pageContext.request.contextPath}/home"
                                   up-follow up-instant>Home</a>
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
                        <div class="card-header pb-0 modal-d-none">
                            <h5>Book Detail</h5>
                            <span>Detail of ${entity.title}</span>
                        </div>
                        <div class="card-body p-4 row">
                            <div class="product-img col-lg-6">
                                <img class="img-fluid"
                                     src="${(empty entity.image ? '/static/images/book-default.png' : entity.image)}"
                                     alt="book cover">
                            </div>
                            <div class="product-details col-lg-6 text-start">
                                <h4>${entity.title} </h4>
                                <div class="product-price">
                                    <fmt:formatNumber value="${entity.borrowCost}" maxFractionDigits="2"
                                                      type="currency"/>
                                    <span class="small">per day</span>
                                </div>
                                <hr>
                                <div class="product-view">
                                    <h6 class="f-w-600">About</h6>
                                    <c:if test="${not empty entity.about}">
                                        <p class="mb-0">${entity.about}</p>
                                    </c:if>
                                    <div class="mt-3"></div>
                                    <c:if test="${not empty entity.seriesName}">
                                        <div class="mb-1">Series
                                            <span class="txt-primary">${entity.seriesName}</span>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty entity.year}">
                                        <div class="mb-1">Published in
                                            <span class="txt-primary">${entity.year}</span>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty entity.authorName}">
                                        <div class="mb-1">Author
                                            <span class="txt-primary">${entity.authorName}</span>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty entity.genres}">
                                        <div class="mb-1">Genres
                                            <div class="ms-2 d-inline">
                                                <c:forEach items="${entity.genres}" var="genre">
                                                    <span class="badge badge-primary">${genre.name}</span>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty entity.publisherName}">
                                        <div class="mb-1">Publisher
                                            <span class="txt-primary">${entity.publisherName}</span>
                                        </div>
                                    </c:if>
                                </div>
                                <hr>
                                <div class="product-qnty">
                                    <h6 class="f-w-600">Status</h6>
                                    <div>
                                        <span class="txt-warning">${entity.booksCount - entity.availableBooksCount}</span>
                                        Borrowed,
                                        <span class="txt-primary">${entity.availableBooksCount}</span>
                                        Available,
                                        <span>${entity.booksCount}</span> Total
                                    </div>
                                </div>
                                <div class="addcart-btn">
                                    <a class="btn btn-primary mt-4" href=${pageContext.request.contextPath}/home"
                                       up-follow up-back up-dismiss>Back</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    </jsp:attribute>
</layout:modal>
