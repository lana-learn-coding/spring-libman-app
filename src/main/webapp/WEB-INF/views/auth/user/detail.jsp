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
<%--@elvariable id="entity" type="io.lana.libman.core.user.User"--%>

<layout:modal>
    <jsp:attribute name="title">User Detail</jsp:attribute>
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
                                <a href="${pageContext.request.contextPath}/authorities/users"
                                   up-follow up-instant>Users</a>
                            </li>
                            <li class="breadcrumb-item active">${entity.email}</li>
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
                            <h5>User Detail</h5>
                            <span>Detail of ${entity.email}</span>
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
                                         src="${(empty entity.avatar ? '/static/images/avatar/default.png' : entity.avatar)}"
                                         alt="image">
                                    <div class="w-100 d-flex justify-content-center pt-2 pt-sm-3">
                                        <c:if test="${entity.isReader()}">
                                            <span class="badge badge-success">Reader</span>
                                        </c:if>
                                        <sec:authorize access="hasAnyAuthority('ADMIN', 'READER_CREATE')">
                                            <c:if test="${!entity.isReader()}">
                                            <form action="${pageContext.request.contextPath}/authorities/users/${entity.id}/link-reader"
                                                  method="post" up-submit up-layer="parent root">
                                                <sec:csrfInput/>
                                                <button type="submit" class="btn btn-sm btn-primary">Link Reader
                                                </button>
                                            </form>
                                        </c:if>
                                        </sec:authorize>
                                    </div>
                                </div>
                                <div class="mt-2 mt-sm-0">
                                    <div class="mb-1">Username
                                        <span class="ms-2 txt-primary">${entity.username}</span>
                                    </div>
                                    <div class="mb-1">Email
                                        <span class="ms-2 txt-primary">${entity.email}</span>
                                    </div>
                                    <c:if test="${not empty entity.phone}">
                                        <div class="mb-1">Phone
                                            <span class="ms-2 txt-primary">${entity.phone}</span>
                                        </div>
                                    </c:if>
                                    <div class="mb-1">Name
                                        <div class="ms-2 txt-primary d-inline">
                                            <c:if test="${not empty entity.firstName}">
                                                <span class="me-2">${entity.firstName}</span>
                                            </c:if>
                                            <c:if test="${not empty entity.firstName}">
                                                <span>${entity.lastName}</span>
                                            </c:if>
                                        </div>
                                    </div>
                                    <c:if test="${not empty entity.gender}">
                                        <div class="mb-1">Gender
                                            <span class="ms-2 txt-primary">${entity.gender.name().toLowerCase()}</span>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty entity.dateOfBirth}">
                                        <div class="mb-1">Birthdate
                                            <span class="ms-2 txt-primary">
                                                <helper:format-date date="${entity.dateOfBirth}"/>
                                            </span>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty entity.roles}">
                                        <div class="mb-1">Roles
                                            <div class="ms-2 d-inline">
                                                <c:forEach items="${entity.roles}" var="item">
                                                    <span class="badge badge-primary">${item.name}</span>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                            <c:if test="${not empty entity.address}">
                                <div class="mt-3 ms-1">
                                    <h6 class="mb-1">Address</h6>
                                    <span class="">${entity.address}</span>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:modal>
