<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.time.LocalDate" %>
<%@ taglib prefix="p" tagdir="/WEB-INF/tags/page/tag" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--@elvariable id="data" type="org.springframework.data.domain.Page<io.lana.libman.core.book.BookBorrow>"--%>
<%--@elvariable id="highlight" type="java.lang.String"--%>


<layout:librarian>
    <jsp:attribute name="title">Borrow Ticket Manage</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>Borrow Manage</h3>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/dashboard"
                                   up-follow up-instant>Home</a>
                            </li>
                            <li class="breadcrumb-item active">Borrows</li>
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
                            <h5>Borrow Ticket Manage</h5>
                            <span>Manage borrow tickets and history (<component:total pageMeta="${data}"/>). Choose batch mode for group by reader</span>
                        </div>
                        <form class="card-body row" up-target="#table, nav .pagination" method="get">
                            <helper:inherit-param excludes="query,reader"/>
                            <div class="col-12 col-sm-6 mb-2">
                                <div class="input-group">
                                    <span class="input-group-text bg-primary"><i class="icon-book"></i></span>
                                    <input type="text"
                                           class="form-control form-control-lg"
                                           name="query"
                                           placeholder="Search for book"
                                           aria-label="query"
                                           value="${param.query}"
                                           up-autosubmit
                                           up-delay="400">
                                </div>
                            </div>
                            <div class="col-12 col-sm-6 mb-2">
                                <div class="input-group">
                                    <span class="input-group-text bg-primary"><i class="icon-user"></i></span>
                                    <input type="text"
                                           class="form-control form-control-lg"
                                           name="reader"
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
                                       href="${pageContext.request.contextPath}/library/borrows"
                                       up-alias="${pageContext.request.contextPath}/library/borrows?*"
                                       up-follow>Individual</a>
                                </li>
                                <sec:authorize access="hasAnyAuthority('ADMIN', 'READER_READ')">
                                    <li class="nav-item">
                                        <a class="nav-link"
                                           href="${pageContext.request.contextPath}/library/borrows/batch"
                                           up-alias="${pageContext.request.contextPath}/library/borrows/batch?*"
                                           up-follow>Batch</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                            <div class="tab-content mt-4">
                                <div class="tab-pane fade show active">
                                    <div class="row justify-content-between">
                                        <div class="col-6 col-md-3 col-lg-2 mb-3">
                                    <component:sorting
                                            target="#table"
                                            up="up-scroll='layer' up-transition='cross-fade'"
                                            labels="Email;Borrowed Date;Due Date;Title;Updated At;Updated By;Id"
                                            values="reader.account.email;borrowDate,desc;dueDate;book.info.title;updatedAt,desc;updatedBy;id,desc"/>
                                        </div>
                                        <div class="col-6 col-sm-6 d-flex justify-content-end align-items-start">
                                    <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOKBORROW_CREATE')">
                                        <button up-href="${pageContext.request.contextPath}/library/borrows/create"
                                                class="btn btn-primary" up-instant up-layer="new" up-size="large"
                                                up-history="false"
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
                                                <th scope="col">Book</th>
                                                <th scope="col">Contact</th>
                                                <th scope="col">Borrow</th>
                                                <th scope="col">Cost</th>
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
                                                        <c:if test="${not empty item.releaseYear}">
                                                        <span class="ms-1">(${item.releaseYear})</span>
                                                    </c:if>
                                                    </a>
                                                    <small class="d-block text-muted">${item.book.id}</small>
                                                </c:if>
                                                <c:if test="${empty item.book}">
                                                    <div up-follow>${item.title}
                                                        <c:if test="${not empty item.releaseYear}">
                                                            <span class="ms-1">(${item.releaseYear})</span>
                                                        </c:if>
                                                        <span class="ms-1">(Deleted)</span>
                                                    </div>
                                                </c:if>
                                                <c:if test="${not empty item.seriesName}">
                                                     <div class="small">
                                                         In Series: ${ item.seriesName }
                                                     </div>
                                                </c:if>
                                            </td>
                                            <td  <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <div
                                                        <c:if test="${item.reader.overDueBooksCount > 0}">class="txt-danger"</c:if>>
                                                    <c:if test="${not empty item.reader.account.firstName}">
                                                        <span class="me-1">${item.reader.account.firstName}</span>
                                                    </c:if>
                                                    <c:if test="${not empty item.reader.account.lastName}">
                                                        <span>${item.reader.account.lastName}</span>
                                                    </c:if>
                                                </div>
                                                <a class="txt-primary" up-follow up-layer="parent root"
                                                   href="${pageContext.request.contextPath}/library/readers/${item.reader.id}/detail#table">
                                                        ${  item.reader.account.email }
                                                </a>
                                                <c:if test="${not empty item.reader.account.phone}">
                                                    <div>Phone: ${item.reader.account.phone}</div>
                                                </c:if>
                                            </td>
                                            <td <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <div
                                                        <c:if test="${item.isOverDue()}">class="txt-danger"</c:if>>
                                                    <div class="text-nowrap">+ <span class="ms-1">
                                                        <helper:format-date date="${item.borrowDate}"/></span>
                                                    </div>
                                                    <div class="text-nowrap">+ <span class="ms-1">
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
                                                <div>
                                                    by ${ not empty item.updatedBy ? item.updatedBy : item.createdBy }</div>
                                            </td>
                                            <td <component:table-higlight test="${isHighlight}"/>>
                                                <a href="${pageContext.request.contextPath}/library/borrows/${item.id}/detail"
                                                   up-instant up-layer="new" up-history="false"
                                                   class="mr-1 txt-primary">
                                                    <i data-feather="external-link"
                                                       style="width: 20px; height: 20px"></i>
                                                </a>
                                                <sec:authorize
                                                        access="hasAnyAuthority('ADMIN', 'BOOKBORROW_UPDATE')">
                                                    <a href="${pageContext.request.contextPath}/library/borrows/${item.id}/return"
                                                       class="mr-1 txt-primary" up-instant up-layer="new"
                                                       up-history="false">
                                                        <i data-feather="check-square"
                                                           style="width: 20px; height: 20px"></i>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/library/borrows/${item.id}/update"
                                                       class="mr-1 txt-primary" up-instant up-layer="new"
                                                       up-history="false"
                                                       up-dismissable="button">
                                                        <i data-feather="edit"
                                                           style="width: 20px; height: 20px"></i>
                                                    </a>
                                                </sec:authorize>
                                                <c:if test="${LocalDate.now().isAfter(item.borrowDate)}">
                                                    <sec:authorize
                                                            access="hasAnyAuthority('ADMIN', 'BOOKBORROW_DELETE') && hasAnyAuthority('ADMIN', 'FORCE')">
                                                        <a href="${pageContext.request.contextPath}/library/borrows/${item.id}/delete"
                                                           up-history="false" up-layer="new" up-instant
                                                           up-dismissable="button"
                                                           class="txt-danger">
                                                            <i data-feather="trash"
                                                               style="width: 20px; height: 20px"></i>
                                                        </a>
                                                    </sec:authorize>
                                                </c:if>
                                                <c:if test="${!LocalDate.now().isAfter(item.borrowDate)}">
                                                    <sec:authorize
                                                            access="hasAnyAuthority('ADMIN', 'BOOKBORROW_DELETE')">
                                                        <a href="${pageContext.request.contextPath}/library/borrows/${item.id}/delete"
                                                           up-history="false" up-layer="new" up-instant
                                                           up-dismissable="button"
                                                           class="txt-danger">
                                                            <i data-feather="trash"
                                                               style="width: 20px; height: 20px"></i>
                                                        </a>
                                                    </sec:authorize>
                                                </c:if>
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
            </div>
        </div>
    </jsp:attribute>
</layout:librarian>
