<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>

<jsp:useBean id="data" scope="request" type="org.springframework.data.domain.Page<io.lana.libman.core.tag.Genre>"/>

<layout:librarian>
    <jsp:attribute name="title">Series Manage</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>Layout Light</h3>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/dashboard"
                                   up-follow up-instant>Home</a>
                            </li>
                            <li class="breadcrumb-item">Books</li>
                            <li class="breadcrumb-item">Tags</li>
                            <li class="breadcrumb-item active">Series</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-12">
                    <div class="card">
                        <div class="card-header">
                            <h5>Series Manage</h5>
                            <span>Manage list of Genres information and related books</span>
                        </div>
                        <div class="card-body" id="table">
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">Name</th>
                                        <th scope="col">Total Books</th>
                                        <th scope="col">Updated At</th>
                                        <th scope="col">Updated By</th>
                                        <th scope="col">Action</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="item" items="${data.content}" varStatus="loop">
                                        <tr>
                                            <th scope="row">${loop.index + 1}</th>
                                            <td>${ item.name }</td>
                                            <td>${ item.booksCount }</td>
                                            <td><helper:formatZonedDateTime date="${item.updatedAt}"/></td>
                                            <td>${ item.updatedBy }</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <nav class="d-flex justify-content-end mt-3">
                                <component:pagination pageMeta="${data}" target="#table" up="up-scroll='layer'"/>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:librarian>
