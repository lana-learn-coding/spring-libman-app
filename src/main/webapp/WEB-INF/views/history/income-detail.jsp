<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.time.LocalDate" %>

<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--@elvariable id="highlight" type="java.lang.String"--%>
<%--@elvariable id="entity" type="io.lana.libman.core.book.Income"--%>
<%--@elvariable id="data" type="org.springframework.data.domain.Page<io.lana.libman.core.book.BookBorrow>"--%>

<layout:modal>
    <jsp:attribute name="title">Income Detail</jsp:attribute>
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
                            <li class="breadcrumb-item">Income</li>
                            <li class="breadcrumb-item active">Detail</li>
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
                            <h5>Income Detail</h5>
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
                            <c:if test="${not empty entity.reader}">
                                <div class="d-flex flex-column flex-sm-row">
                                    <div class="me-3 me-md-4 d-flex justify-content-center justify-content-sm-start align-items-baseline">
                                        <img class="rounded" style="width: 100px"
                                             src="${(empty entity.reader.account.avatar ? '/static/images/avatar/default.png' : entity.reader.account.avatar)}"
                                             alt="image">
                                    </div>
                                    <div class="mt-2 mt-sm-0">
                                        <div class="mb-1">Email
                                            <span class="ms-2 txt-primary">${entity.reader.account.email}</span>
                                        </div>
                                        <c:if test="${not empty entity.reader.account.phone}">
                                        <div class="mb-1">Phone
                                            <span class="ms-2 txt-primary">${entity.reader.account.phone}</span>
                                        </div>
                                        </c:if>
                                        <div class="mb-1">Name
                                            <div class="ms-2 txt-primary d-inline">
                                            <c:if test="${not empty entity.reader.account.firstName}">
                                                <span class="me-2">${entity.reader.account.firstName}</span>
                                            </c:if>
                                                <c:if test="${not empty entity.reader.account.firstName}">
                                                <span>${entity.reader.account.lastName}</span>
                                            </c:if>
                                            </div>
                                        </div>
                                        <div class="mb-1">
                                            Total Cost
                                            <span class="me-1 ms-2 txt-primary">
                                             <fmt:formatNumber value="${entity.totalCost}" type="currency"
                                                               maxFractionDigits="2"/>
                                            </span>
                                            (<span class="me-1">
                                                <fmt:formatNumber value="${entity.totalBorrowCost}" type="currency"
                                                                  maxFractionDigits="2"/>
                                            </span>
                                            <c:if test="${entity.totalOverDueAdditionalCost > 0}">
                                               <span>
                                                  + <fmt:formatNumber value="${entity.totalOverDueAdditionalCost}"
                                                                      type="currency"
                                                                      maxFractionDigits="2"/>
                                              </span>
                                            </c:if>)
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                        <hr class="my-3">
                        <div class="card-body" id="table">
                            <div class="row justify-content-between mb-3">
                                <div class="col-xs-12 col-md-3 col-lg-2 my-1">
                                     <component:sorting
                                             target="#table"
                                             up="up-scroll='#table' up-transition='cross-fade'"
                                             labels="Title;Borrow Date;Due Date;Updated At;Updated By;Id"
                                             values="book.info.title,borrowDate,desc;dueDate,desc;updatedAt,desc;updatedBy;id,desc"/>
                                </div>
                                <form class="col-xs-12 col-md-6 my-1"
                                      up-target="#table, nav .pagination" method="get">
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
                                        <th scope="col">Title</th>
                                        <th scope="col">Borrow</th>
                                        <th scope="col">Cost</th>
                                        <th scope="col">Updated At</th>
                                        <th scope="col">Action</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:set var="content" value="${empty data ? [] : data.content}"/>
                                    <c:forEach var="item" items="${data.content}" varStatus="loop">
                                        <c:set var="isHighlight"
                                               value="${item.id == highlight}"/>
                                        <tr>
                                            <th scope="row" <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <component:index pageMeta="${data}" i="${loop.index}"/>
                                            </th>
                                            <td <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <div style="max-width: 120px">
                                                        ${ item.id }
                                                </div>
                                            </td>
                                            <td  <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <c:if test="${not empty item.book}">
                                                    <a href="${pageContext.request.contextPath}/library/books/infos/${item.book.info.id}/detail#${item.book.id}"
                                                       up-follow>${item.title}
                                                    </a>
                                                    <small class="d-block text-muted">${item.book.id}</small>
                                                </c:if>
                                                <c:if test="${empty item.book}">
                                                    <div>${item.title} (Deleted)</div>
                                                </c:if>
                                            </td>
                                            <td <component:table-higlight
                                                    test="${isHighlight}"/>>
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
                                            <td <component:table-higlight
                                                    test="${isHighlight}"/>>
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
                                            <td <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <div><helper:format-instant
                                                        date="${item.updatedAt}"/></div>
                                                <div>
                                                    by ${ not empty item.updatedBy ? item.updatedBy : item.createdBy }</div>
                                            </td>
                                            <td <component:table-higlight test="${isHighlight}"/>>
                                                <a href="${pageContext.request.contextPath}/library/borrows/${item.id}/detail?history=true"
                                                   up-instant up-layer="new" class="mr-1 txt-primary"
                                                   up-history="false">
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
                                <component:pagination
                                        href="${pageContext.request.contextPath}/library/history/income/${entity.id}/detail"
                                        pageMeta="${data}" target="#table"
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
</layout:modal>
