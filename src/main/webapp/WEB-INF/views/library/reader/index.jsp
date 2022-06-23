<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="p" tagdir="/WEB-INF/tags/page/tag" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--@elvariable id="data" type="org.springframework.data.domain.Page<io.lana.libman.core.reader.Reader>"--%>
<%--@elvariable id="highlight" type="java.lang.String"--%>


<layout:librarian>
    <jsp:attribute name="title">Reader Manage</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>Reader Manage</h3>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/dashboard"
                                   up-follow up-instant>Home</a>
                            </li>
                            <li class="breadcrumb-item active">Readers</li>
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
                            <h5>Readers Manage</h5>
                            <span>Manage list of library readers</span>
                        </div>
                        <form class="card-body row" up-target="#table table, nav .pagination" method="get">
                            <helper:inherit-param excludes="query,email"/>
                            <div class="col-12 col-sm-6 col-md-8 col-lg-9 mb-2">
                                <div class="input-group">
                                    <span class="input-group-text bg-primary"><i class="icon-search"></i></span>
                                    <input type="text"
                                           class="form-control form-control-lg"
                                           name="query"
                                           placeholder="Search for reader"
                                           aria-label="query"
                                           value="${param.query}"
                                           up-autosubmit
                                           up-delay="400">
                                </div>
                            </div>
                            <div class="col-12 col-sm-6 col-md-4 col-lg-3 mb-2">
                                <div class="input-group">
                                    <span class="input-group-text bg-primary"><i class="icon-email"></i></span>
                                    <input type="text"
                                           class="form-control form-control-lg"
                                           name="email"
                                           placeholder="Search by email"
                                           aria-label="email"
                                           value="${param.email}"
                                           up-autosubmit
                                           up-delay="400">
                                </div>
                            </div>
                        </form>
                        <hr class="my-0">
                        <div class="card-body" id="table">
                            <div class="row justify-content-between">
                                <div class="col-6 col-md-3 col-lg-2 mb-3">
                                    <component:sorting
                                            target="#table, [comp=sorting]"
                                            up="up-scroll='layer' up-transition='cross-fade'"
                                            labels="Email;First Name;Last Name;Book Borrowed;Book Overdue;Limit;Updated At;Updated By;Id"
                                            values="account.email;account.firstName;account.lastName;borrowingBooksCount,desc;overDueBooksCount,desc;borrowLimit,desc;updatedAt,desc;updatedBy,id"/>
                                </div>
                                <div class="col-6 col-sm-6 d-flex justify-content-end align-items-start">
                                    <button up-href="${pageContext.request.contextPath}/library/readers/create"
                                            class="btn btn-primary" up-instant up-layer="new" up-size="large"
                                            up-dismissable="button">
                                        <i class="fa fa-plus-square-o fa-lg pe-2"></i>
                                        Create
                                    </button>
                                </div>
                            </div>
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">Id</th>
                                        <th scope="col">Image</th>
                                        <th scope="col">Full Name</th>
                                        <th scope="col">Contact</th>
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
                                                    test="${isHighlight}"/>>
                                                <component:index pageMeta="${data}" i="${loop.index}"/>
                                            </th>
                                            <td <component:table-higlight test="${isHighlight}"/>>
                                                <div style="max-width: 120px">
                                                        ${ item.id }
                                                </div>
                                            </td>
                                            <td <component:table-higlight
                                                    test="${isHighlight}"/>>
                                                <img class="rounded-circle" style="width: 80px"
                                                     src="${(empty item.account.avatar ? '/static/images/avatar/default.png' : item.account.avatar)}"
                                                     alt="avatar">
                                            </td>
                                            <td <component:table-higlight test="${isHighlight}"/>>
                                                <c:if test="${not empty item.account.firstName}">
                                                    <span class="me-1">${item.account.firstName}</span>
                                                </c:if>
                                                <c:if test="${not empty item.account.lastName}">
                                                    <span>${item.account.lastName}</span>
                                                </c:if>
                                                <c:if test="${empty item.account.lastName and empty item.account.firstName}">
                                                    <div>Unknown</div>
                                                </c:if>
                                            </td>
                                            <td <component:table-higlight test="${isHighlight}"/>>
                                                <div class="txt-primary">${ not empty item.account.email ? item.account.email : item.account.username }</div>
                                                <c:if test="${not empty item.account.phone}">
                                                    <div>Phone: ${item.account.phone}</div>
                                                </c:if>
                                            </td>
                                            <td <component:table-higlight test="${isHighlight}"/>>
                                                <span>${ item.borrowingBooksCount }</span>
                                                /
                                                <span>${ item.borrowLimit }</span>
                                                -
                                                <span class="txt-danger">${ item.overDueBooksCount }</span>
                                            </td>
                                            <td <component:table-higlight test="${isHighlight}"/>>
                                                <div><helper:format-instant date="${item.account.updatedAt}"/></div>
                                                <div>
                                                    by ${ not empty item.updatedBy ? item.updatedBy : item.createdBy }</div>
                                            </td>
                                            <td <component:table-higlight test="${isHighlight}"/>>
                                                <a href="${pageContext.request.contextPath}/library/readers/${item.id}/detail"
                                                   up-instant up-follow class="mr-1 txt-primary">
                                                    <i data-feather="external-link"
                                                       style="width: 20px; height: 20px"></i>
                                                </a>
                                                <sec:authorize
                                                        access="hasAnyAuthority('ADMIN', 'READER_UPDATE')">
                                                    <a href="${pageContext.request.contextPath}/library/books/infos/${item.id}/update"
                                                       class="mr-1 txt-primary" up-instant up-layer="new"
                                                       up-size="large" up-dismissable="button">
                                                        <i data-feather="edit"
                                                           style="width: 20px; height: 20px"></i>
                                                    </a>
                                                </sec:authorize>
                                                <sec:authorize access="hasAnyAuthority('ADMIN', 'READER_DELETE')">
                                                    <a href="${pageContext.request.contextPath}/library/books/infos/delete"
                                                       up-history="false" up-layer="new" up-instant
                                                       up-dismissable="button"
                                                       class="txt-danger">
                                                        <i data-feather="trash" style="width: 20px; height: 20px"></i>
                                                    </a>
                                                </sec:authorize>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <c:if test="${empty content}">
                                    <component:empty/>
                                </c:if>
                            </div>
                            <nav class="d-flex justify-content-start mt-3">
                                <component:pagination pageMeta="${data}" target="#table, [comp=pagination]"
                                                      up="up-scroll='layer' up-transition='cross-fade'"/>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:librarian>
