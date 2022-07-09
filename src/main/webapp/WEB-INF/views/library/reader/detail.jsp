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
<%--@elvariable id="data" type="org.springframework.data.domain.Page<io.lana.libman.core.book.BookBorrow>"--%>
<%--@elvariable id="entity" type="io.lana.libman.core.reader.Reader"--%>

<layout:librarian>
    <jsp:attribute name="title">Reader Detail</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>Reader Detail</h3>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/dashboard"
                                   up-follow up-instant>Home</a>
                            </li>
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/readers"
                                   up-follow up-instant>Readers</a>
                            </li>
                            <li class="breadcrumb-item active">${entity.account.email}</li>
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
                            <h5>Reader Detail</h5>
                            <span>Detail of ${entity.account.email}</span>
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
                                    <img class="rounded" style="width: 120px"
                                         src="${(empty entity.account.avatar ? '/static/images/avatar/default.png' : entity.account.avatar)}"
                                         alt="image">
                                </div>
                                <div class="mt-2 mt-sm-0">
                                    <div class="mb-1">Id <span class="small ms-2">${entity.id}</span></div>
                                    <div class="mb-1">Email
                                        <span class="ms-2 txt-primary">${entity.account.email}</span>
                                    </div>
                                    <c:if test="${not empty entity.account.phone}">
                                        <div class="mb-1">Phone
                                            <span class="ms-2 txt-primary">${entity.account.phone}</span>
                                        </div>
                                    </c:if>
                                    <div class="mb-1">Name
                                        <div class="ms-2 txt-primary d-inline">
                                            <c:if test="${not empty entity.account.firstName}">
                                                <span class="me-2">${entity.account.firstName}</span>
                                            </c:if>
                                            <c:if test="${not empty entity.account.firstName}">
                                                <span>${entity.account.lastName}</span>
                                            </c:if>
                                        </div>
                                    </div>
                                    <c:if test="${not empty entity.account.gender}">
                                        <div class="mb-1">Gender
                                            <span class="ms-2 txt-primary">${entity.account.gender.name().toLowerCase()}</span>
                                        </div>
                                    </c:if>
                                    <div>
                                        <div class="mb-1 me-3 d-inline">Borrowing
                                            <span class="ms-2">${ entity.borrowingBooksCount }</span>
                                            -
                                            <span class="txt-danger">${entity.overDueBooksCount}</span>
                                        </div>
                                        <div class="mb-1 d-inline">Limit
                                            <span class="ms-2 txt-primary">${entity.borrowLimit}</span>
                                        </div>
                                    </div>
                                    <c:if test="${not empty entity.account.dateOfBirth}">
                                        <div class="mb-1">Birthdate
                                            <span class="ms-2 txt-primary">
                                                <helper:format-date date="${entity.account.dateOfBirth}"/>
                                            </span>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                            <c:if test="${not empty entity.account.address}">
                                <div class="mt-3 ms-1">
                                    <h6 class="mb-1">Address</h6>
                                    <span class="">${entity.account.address}</span>
                                </div>
                            </c:if>
                        </div>
                        <div class="card-footer">
                            <sec:authorize access="hasAnyAuthority('ADMIN', 'READER_UPDATE')">
                                <button up-href="${pageContext.request.contextPath}/library/readers/${id}/update"
                                        class="btn btn-primary me-2" up-instant up-layer="new" up-size="large"
                                        up-dismissable="button" up-history="false">
                                    Update
                                </button>
                            </sec:authorize>
                            <sec:authorize access="hasAnyAuthority('ADMIN', 'READER_DELETE')">
                                <button up-href="${pageContext.request.contextPath}/library/readers/${id}/delete"
                                        class="btn btn-danger me-2" up-instant up-layer="new" up-history="false">
                                    Delete
                                </button>
                            </sec:authorize>
                            <a href="${pageContext.request.contextPath}/library/readers"
                               up-follow up-instant class="btn btn-light me-2 d-none d-lg-inline-block">Manage</a>
                            <a href="${pageContext.request.contextPath}/library/readers"
                               up-back class="btn btn-light me-2 d-none d-lg-inline-block">Back</a>
                        </div>
                    </div>
                </div>
            </div>
            <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOKBORROW_READ')">
                <div class="row" id="borrow">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header pb-0">
                                <h5>Borrowing Book</h5>
                                <span>List borrowing books of: ${entity.account.email} (Total of ${entity.borrowingBooksCount})</span>
                            </div>
                            <div class="card-body" id="table">
                                <div class="row">
                                    <div class="col-12 mb-3 d-flex justify-content-end">
                                    <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOKBORROW_READ')">
                                        <button up-href="${pageContext.request.contextPath}/library/readers/${entity.id}/history"
                                                class="btn btn-primary me-2" up-instant up-size="large" up-layer="new"
                                                up-dismissable="button" up-history="false">
                                            <i class="fa fa-clock-o fa-lg pe-2"></i>
                                            Borrow History
                                        </button>
                                    </sec:authorize>
                                        <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOKBORROW_CREATE')">
                                        <button up-href="${pageContext.request.contextPath}/library/borrows/create?parentId=${id}"
                                                class="btn btn-primary me-2" up-instant up-layer="new"
                                                up-history="false"
                                                up-dismissable="button">
                                            <i class="fa fa-plus-square-o fa-lg pe-2"></i>
                                            Borrow
                                        </button>
                                    </sec:authorize>
                                        <sec:authorize access="hasAnyAuthority('ADMIN', 'BOOKBORROW_UPDATE')">
                                        <button up-href="${pageContext.request.contextPath}/library/borrows/batch/${id}/edit"
                                                class="btn btn-primary" up-instant up-follow>
                                            <i class="fa fa-check fa-lg pe-2"></i>
                                            Batch
                                        </button>
                                        <c:if test="${entity.overDueBooksCount > 0}">
                                            <button up-href="${pageContext.request.contextPath}/library/readers/${id}/remind"
                                                    class="btn btn-warning ms-2 d-none d-md-block" up-instant
                                                    up-layer="new"
                                                    up-history="false">
                                                Remind (${entity.overDueBooksCount})
                                            </button>
                                        </c:if>
                                    </sec:authorize>
                                    </div>
                                </div>
                                <div class="row justify-content-between mb-3">
                                    <div class="col-xs-12 col-md-3 col-lg-2 my-1">
                                    <component:sorting
                                            target="#table"
                                            up="up-scroll='#table' up-transition='cross-fade'"
                                            labels="Title;Borrow Date;Due Date;Updated At;Updated By;Id"
                                            values="book.info.title,borrowDate,desc;dueDate,asc;updatedAt,desc;updatedBy;id,desc"/>
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
                                            <td <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <img class="round-box mx-auto"
                                                     style="width: 70px"
                                                     src="${(empty item.book.image ? '/static/images/book-default.png' : item.book.image)}"
                                                     alt="book cover">
                                            </td>
                                            <td  <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <a href="${pageContext.request.contextPath}/library/books/infos/${item.book.info.id}/detail#${item.book.id}"
                                                   up-follow>${item.title}
                                                </a>
                                                <small class="d-block text-muted">${item.book.id}</small>
                                            </td>
                                            <td <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <div <c:if test="${item.isOverDue()}">class="txt-danger"</c:if>>
                                                    <div>From <span class="ms-1">
                                                        <helper:format-date date="${item.borrowDate}"/></span>
                                                    </div>
                                                    <div>To <span class="ms-1">
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
                                    <c:if test="${empty content}"><component:empty/></c:if>
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
            </sec:authorize>
        </div>
    </jsp:attribute>
</layout:librarian>
