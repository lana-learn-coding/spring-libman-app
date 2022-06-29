<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.time.LocalDate, io.lana.libman.support.data.Gender" %>
<%@ taglib prefix="p" tagdir="/WEB-INF/tags/page" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--@elvariable id="data" type="org.springframework.data.domain.Page<io.lana.libman.core.book.BookBorrow>"--%>
<%--@elvariable id="batch" type="io.lana.libman.core.book.controller.dto.BatchReturnDto"--%>
<%--@elvariable id="selected" type="java.util.Set<java.lang.String>"--%>
<%--@elvariable id="reader" type="io.lana.libman.core.reader.Reader"--%>
<%--@elvariable id="highlight" type="java.lang.String"--%>
<%--@elvariable id="total" type="java.lang.Double"--%>

<layout:librarian>
    <jsp:attribute name="title">Batch Return</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>Batch Return</h3>
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
                                <li class="breadcrumb-item">
                                    <a href="${pageContext.request.contextPath}/library/borrows/batch"
                                       up-follow up-instant>Batch</a>
                                </li>
                                <li class="breadcrumb-item">
                                        ${reader.account.email}
                                </li>
                            </ol>
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
                            <h5>Batch Return</h5>
                            <span>Select ticket to return bellow</span>
                        </div>
                        <div class="card-body" id="table">
                            <div class="row justify-content-between mb-3">
                                <div class="col-xs-12 col-md-6 my-1 d-flex">
                                    <form up-layer="new" method="get" up-history="false"
                                          action="${pageContext.request.contextPath}/library/borrows/batch/return">
                                        <c:forEach items="${batch.ids}" var="id">
                                            <input type="hidden" name="ids" value="${id}">
                                        </c:forEach>
                                        <button class="btn btn-primary me-2" type="submit"
                                                <c:if test="${empty selected}"> disabled </c:if>>Return Selected
                                        </button>
                                    </form>
                                    <div>
                                        <button up-href="${pageContext.request.contextPath}/library/borrows/batch/${reader.id}/return"
                                                class="btn btn-danger me-2" up-instant up-layer="new"
                                                up-history="false">
                                            Return All
                                        </button>
                                    </div>
                                    <div class="d-none d-lg-block">
                                        <a href="${pageContext.request.contextPath}/library/borrows/batch"
                                           class="btn btn-light" up-instant up-back up-follow>
                                            Back
                                        </a>
                                    </div>
                                </div>
                                <form class="col-xs-12 col-md-6 my-1" up-target="#table, nav .pagination"
                                      up-transition='cross-fade' method="get">
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
                                    <c:forEach items="${batch.ids}" var="id">
                                        <input type="hidden" name="ids" value="${id}">
                                    </c:forEach>
                                </form>
                            </div>
                            <form class="table-responsive" up-target="#table">
                                <input type="hidden" value="${param.query}" name="query">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th scope="col"></th>
                                        <th scope="col">#</th>
                                        <th scope="col">Id</th>
                                        <th scope="col">Title</th>
                                        <th scope="col">Borrow</th>
                                        <th scope="col">Cost</th>
                                        <th scope="col">Updated At</th>
                                    </tr>
                                    </thead>
                                    <c:if test="${not empty selected}">
                                        <tbody>
                                        <p:batch-edit-rows batch="${batch}" content="${selected}"
                                                           highlight="${highlight}"/>
                                        <tr>
                                            <th colspan="5" class="text-end">Total</th>
                                            <th colspan="1">
                                                <fmt:formatNumber value="${total}" type="currency"
                                                                  maxFractionDigits="2"/>
                                            </th>
                                            <th colspan="1">
                                                    ${selected.size()} Books
                                            </th>
                                        </tr>
                                        </tbody>
                                    </c:if>
                                    <tbody>
                                    <p:batch-edit-rows batch="${batch}" content="${data}" highlight="${highlight}"/>
                                    </tbody>
                                </table>
                                <c:if test="${empty data && empty selected}">
                                    <component:empty/>
                                </c:if>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:librarian>
