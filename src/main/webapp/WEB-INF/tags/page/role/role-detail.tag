<%@tag description="Page layout" pageEncoding="UTF-8" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ attribute name="uri" required="false" type="java.lang.String" %>
<%@ attribute name="entity" required="false" type="io.lana.libman.support.data.DescriptiveEntity" %>
<%@ attribute name="relations" required="false"
              type="java.util.Collection<io.lana.libman.support.data.DescriptiveEntity>" %>
<%@ attribute name="detail" fragment="true" %>

<c:set var="uri" value="${(empty uri) ? title.toLowerCase() : uri}"/>

<layout:modal>
    <jsp:attribute name="title">${title} Detail</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>${title} Detail</h3>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/dashboard"
                                   up-follow up-instant>Home</a>
                            </li>
                            <li class="breadcrumb-item">Authorities</li>
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/authorities/${uri}"
                                   up-follow up-instant>${title}
                                </a>
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
                        <div class="card-header pb-0">
                            <h5>${title} Detail</h5>
                            <span>Detail of ${entity.name}</span>
                        </div>
                        <div class="card-body pt-1">
                            <div class="mb-3 text-muted">
                                <div>
                                    <i class="fa fa-calendar-o"></i>
                                    <span class="ms-1"><helper:format-instant
                                            date="${entity.updatedAt}"/></span>
                                </div>
                                <c:if test="${not empty entity.updatedBy}">
                                             <div class="ms-2 d-none d-md-block">
                                                 <i class="fa fa-user"></i>
                                                 <span class="ms-1">${entity.updatedBy}</span>
                                             </div>
                                         </c:if>
                            </div>
                            <div class="mb-1">Id <span class="font-primary ms-2">${entity.id}</span></div>
                            <div class="mb-1">Name <span class="font-primary ms-2">${entity.name}</span></div>
                            <c:if test="${not empty entity.description}">
                                <div class="mb-1">Description <span
                                        class="font-primary ms-2">${entity.description}</span>
                                </div>
                            </c:if>
                            <c:if test="${not empty relations}">
                                <div class="mb-1">Relations
                                    <div class="d-inline ms-1">
                                        <c:forEach items="${relations}" var="relation">
                                            <span class="badge badge-info">${relation.name}</span>
                                        </c:forEach>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:modal>
