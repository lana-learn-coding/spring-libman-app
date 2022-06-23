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
                                            <span class="ms-2 txt-primary">${entity.account.dateOfBirth}</span>
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
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-header pb-0">
                            <h5>Borrowing Book</h5>
                            <span>List borrowing books of: ${entity.account.email} (Total of ${entity.borrowingBooksCount})</span>
                        </div>
                        <div class="card-body" id="table">
                            <div class="row justify-content-between mb-3">
                                <div class="col-xs-12 col-md-3 col-lg-2 my-1">
                                    <component:sorting
                                            target="#table, [comp=sorting]"
                                            up="up-scroll='#table' up-transition='cross-fade'"
                                            labels="Title;Borrow Date;Due Date;Updated At;Updated By;Id"
                                            values="book.info.title,borrowDate,desc;dueDate,desc;updatedAt,desc;updatedBy,id"/>
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
                                        <th scope="col">Ticket</th>
                                        <th scope="col">Image</th>
                                        <th scope="col">Title</th>
                                        <th scope="col">Borrow</th>
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
                                                <div style="max-width: 120px">
                                                        ${ item.ticket }
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
                                                <a href="${pageContext.request.contextPath}/library/books/infos/${item.book.info.id}/detail"
                                                   up-follow>${item.title}
                                                </a>
                                            </td>
                                            <td <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <div>From <span class="ms-1">
                                                        <helper:format-date date="${item.borrowDate}"/></span>
                                                </div>
                                                <div>To <span class="ms-1">
                                                        <helper:format-date date="${item.dueDate}"/></span>
                                                </div>
                                            </td>
                                            <td <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <div><helper:format-instant
                                                        date="${item.updatedAt}"/></div>
                                                <div>
                                                    by ${ not empty item.updatedBy ? item.updatedBy : item.createdBy }</div>
                                            </td>
                                            <td>

                                            </td>
                                        </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                                <c:if test="${empty content}"><component:empty/></c:if>
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