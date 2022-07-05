<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.time.LocalDate" %>
<%@ taglib prefix="p" tagdir="/WEB-INF/tags/page/tag" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--@elvariable id="data" type="org.springframework.data.domain.Page<io.lana.libman.core.book.Income>"--%>
<%--@elvariable id="summary" type="java.util.Map"--%>


<layout:librarian>
    <jsp:attribute name="title">Income Manage</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>History Manage</h3>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/dashboard"
                                   up-follow up-instant>Home</a>
                            </li>
                            <li class="breadcrumb-item">History</li>
                            <li class="breadcrumb-item active">Income</li>
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
                            <h5>Income History</h5>
                            <span>History of income from borrowed books (<component:total pageMeta="${data}"/>)</span>
                        </div>
                        <form class="card-body row" up-target="#table, nav .pagination" method="get">
                            <helper:inherit-param excludes="query,reader"/>
                            <div class="col-12 mb-2">
                                <div class="input-group">
                                    <span class="input-group-text bg-primary"><i class="icon-user"></i></span>
                                    <input type="text"
                                           class="form-control form-control-lg"
                                           name="query"
                                           placeholder="Search by reader"
                                           aria-label="reader"
                                           value="${param.reader}"
                                           up-autosubmit
                                           up-delay="400">
                                </div>
                            </div>
                        </form>
                        <hr class="my-0">
                        <div class="card-body" id="table">
                            <ul class="nav nav-pills nav-primary">
                                <li class="nav-item">
                                    <a class="nav-link"
                                       href="${pageContext.request.contextPath}/library/history"
                                       up-alias="${pageContext.request.contextPath}/library/history?*"
                                       up-follow>History</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link"
                                       href="${pageContext.request.contextPath}/library/history/income"
                                       up-alias="${pageContext.request.contextPath}/library/history/income?*"
                                       up-follow>Income</a>
                                </li>
                            </ul>
                            <div class="tab-content mt-4">
                                <div class="tab-pane fade show active">
                                    <div class="row justify-content-between">
                                        <div class="col-6 col-md-3 col-lg-2 mb-3">
                                    <component:sorting
                                            target="#table"
                                            up="up-scroll='layer' up-transition='cross-fade'"
                                            labels="Total Cost;Borrowed Date;Due Date;Updated At;Updated By;Id"
                                            values="totalCost,desc;borrowDate,desc;dueDate;updatedAt,desc;updatedBy;id,desc"/>
                                        </div>
                                        <div class="col-6 col-sm-6 d-flex justify-content-end align-items-start">
                                        </div>
                                    </div>
                                    <div class="row flex-md-row-reverse">
                                        <div class="col-12 col-md-3 col-xl-2">
                                            <form class="row">
                                                <div class="col-6 col-md-12">
                                                    <input type="text" readonly
                                                           class="datepicker-here form-control digits mb-3"
                                                           name="from" placeholder="From date"
                                                           data-max-date="${empty param.to ? LocalDate.now() : param.to}"
                                                           value="${param.from}" up-autosubmit data-clear-button="true">
                                                </div>
                                                <div class="col-6 col-md-12">
                                                    <input type="text" readonly
                                                           class="datepicker-here form-control digits mb-3"
                                                           name="to" placeholder="To date"
                                                           data-min-date="${param.from}"
                                                           data-max-date="${LocalDate.now()}"
                                                           value="${param.to}" up-autosubmit data-clear-button="true">
                                                </div>
                                            </form>
                                        </div>
                                        <div class="col-12 col-md-9 col-xl-10">
                                            <div class="table-responsive">
                                                <table class="table">
                                                    <thead>
                                                    <tr>
                                                        <th scope="col">Active Reader</th>
                                                        <th scope="col">Total Borrows</th>
                                                        <th scope="col">Total Income</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <tr class="text-end" style="font-size: 1rem">
                                                        <td>${summary.get('readerCount')} Readers Active</td>
                                                        <td>${summary.get('borrowsCount')} Borrows</td>
                                                        <td class="txt-primary fw-bold">
                                                            <fmt:formatNumber value="${summary.get('totalCost')}"
                                                                              type="currency"
                                                                              maxFractionDigits="2"/>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    <hr class="my-4">
                                    <div class="table-responsive">
                                        <table class="table">
                                            <thead>
                                            <tr>
                                                <th scope="col">#</th>
                                                <th scope="col">Id</th>
                                                <th scope="col">Contact</th>
                                                <th scope="col">Borrow</th>
                                                <th scope="col">Books</th>
                                                <th scope="col">Cost</th>
                                                <th scope="col">Updated At</th>
                                                <th scope="col">Action</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:set var="content" value="${empty data ? [] : data.content}"/>
                                            <c:forEach var="item" items="${data.content}" varStatus="loop">
                                                <tr>
                                                    <th scope="row">
                                                        <component:index pageMeta="${data}" i="${loop.index}"/>
                                                    </th>
                                                    <td>
                                                        <div style="max-width: 120px">
                                                                ${ item.id }
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <c:if test="${not empty item.reader}">
                                                            <div>
                                                                <c:if test="${not empty item.reader.account.firstName}">
                                                                    <span class="me-1">${item.reader.account.firstName}</span>
                                                                </c:if>
                                                                <c:if test="${not empty item.reader.account.lastName}">
                                                                    <span>${item.reader.account.lastName}</span>
                                                                </c:if>
                                                            </div>
                                                            <a class="txt-primary" up-follow up-layer="parent root"
                                                               href="${pageContext.request.contextPath}/library/readers/${item.reader.id}/detail">
                                                                    ${  item.reader.account.email }
                                                            </a>
                                                            <c:if test="${not empty item.reader.account.phone}">
                                                                <div>Phone: ${item.reader.account.phone}</div>
                                                            </c:if>
                                                        </c:if>
                                                        <c:if test="${empty item.reader}">
                                                            <div>READER OR TICKET DELETED</div>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <div class="text-nowrap">+ <span class="ms-1">
                                                                <helper:format-date date="${item.borrowDate}"/></span>
                                                        </div>
                                                        <div class="text-nowrap">+ <span class="ms-1">
                                                                <helper:format-date date="${item.dueDate}"/></span>
                                                        </div>
                                                        <c:if test="${not empty item.returnDate}">
                                                             <div><span class="txt-primary">
                                                                <helper:format-date date="${item.returnDate}"/></span>
                                                             </div>
                                                        </c:if>
                                                    </td>
                                                    <td>${item.borrowsCount}</td>
                                                    <td>
                                                        <div class="text-nowrap">+ <span class="ms-1">
                                                                <fmt:formatNumber value="${item.totalBorrowCost}"
                                                                                  type="currency"
                                                                                  maxFractionDigits="2"/>
                                                        </div>
                                                        <div class="text-nowrap">+ <span class="ms-1">
                                                                <fmt:formatNumber
                                                                        value="${item.totalOverDueAdditionalCost}"
                                                                        type="currency"
                                                                        maxFractionDigits="2"/>
                                                        </div>
                                                        <div class="txt-primary">
                                                            <span>
                                                                <fmt:formatNumber value="${item.totalCost}"
                                                                                  type="currency"
                                                                                  maxFractionDigits="2"/>
                                                            </span>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <div><helper:format-instant
                                                                date="${item.updatedAt}"/></div>
                                                        <div>
                                                            by ${ not empty item.updatedBy ? item.updatedBy : item.createdBy }</div>
                                                    </td>
                                                    <td>
                                                        <a href="${pageContext.request.contextPath}/library/history/income/${item.id}/detail"
                                                           up-instant up-follow="new" class="mr-1 txt-primary"
                                                           up-history="true">
                                                            <i data-feather="external-link"
                                                               style="width: 20px; height: 20px"></i>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                        <c:if test="${empty content}"><component:empty/></c:if>
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
            </div>
        </div>
    </jsp:attribute>
</layout:librarian>
