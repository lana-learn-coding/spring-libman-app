<%@tag description="Page layout" pageEncoding="UTF-8" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ attribute name="authority" required="true" type="java.lang.String" %>
<%@ attribute name="uri" required="false" type="java.lang.String" %>
<%@ attribute name="highlight" required="false" type="java.lang.String" %>
<%@ attribute name="data" required="false"
              type="org.springframework.data.domain.Page<io.lana.libman.support.data.DescriptiveEntity>" %>

<%@ attribute name="table" fragment="true" %>

<c:set var="uri" value="${(empty uri) ? title.toLowerCase() : uri}"/>
<layout:librarian>
    <jsp:attribute name="title">${title} Manage</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>${title}</h3>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/dashboard"
                                   up-follow up-instant>Home</a>
                            </li>
                            <li class="breadcrumb-item">Authorities</li>
                            <li class="breadcrumb-item active">${title}</li>
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
                            <h5>${title} Manage</h5>
                            <span>Manage list of ${title}</span>
                        </div>
                        <form class="card-body row" up-target="#table, nav .pagination" method="get">
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
                        <hr class="my-0">
                        <div class="card-body" id="table">
                            <div class="row justify-content-between">
                                <div class="col-6 col-md-3 col-lg-2 mb-3">
                                    <component:sorting
                                            target="#table, [comp=sorting]" up="up-scroll='layer'"
                                            labels="Newest;Name;Updated At;Updated By;Id"
                                            values="createdAt,desc;name;updatedAt,desc;updatedBy,id"/>
                                </div>
                                <div class="col-6 col-sm-6 d-flex justify-content-end align-items-start">
                                    <button up-href="${pageContext.request.contextPath}/authorities/${uri}/create"
                                            class="btn btn-primary" up-instant up-layer="new" up-dismissable="button">
                                        <i class="fa fa-plus-square-o fa-lg pe-2 d-none d-sm-inline"></i>
                                        Create
                                    </button>
                                </div>
                            </div>
                            <div class="table-responsive">
                                <c:choose>
                                    <c:when test="${table != null}">
                                        <jsp:invoke fragment="table"/>
                                    </c:when>
                                    <c:otherwise>
                                         <table class="table">
                                             <thead>
                                             <tr>
                                                 <th scope="col">#</th>
                                                 <th scope="col">Id</th>
                                                 <th scope="col">Name</th>
                                                 <th scope="col">Description</th>
                                                 <th scope="col">Updated At</th>
                                                 <th scope="col">Updated By</th>
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
                                                             test="${isHighlight}"/>>${ item.id }</td>
                                                     <td <component:table-higlight
                                                             test="${isHighlight}"/>>${ item.name }</td>
                                                     <td <component:table-higlight
                                                             test="${isHighlight}"/>>${ item.description }</td>
                                                     <td <component:table-higlight test="${isHighlight}"/>>
                                                         <helper:format-instant date="${item.updatedAt}"/></td>
                                                     <td <component:table-higlight
                                                             test="${isHighlight}"/>>${ not empty item.updatedBy ? item.updatedBy : item.createdBy }</td>
                                                     <td <component:table-higlight test="${isHighlight}"/>>
                                                         <a href="${pageContext.request.contextPath}/authorities/${uri}/${item.id}/detail"
                                                            class="mr-1 txt-primary" up-layer="new"
                                                            up-instant>
                                                             <i data-feather="external-link"
                                                                style="width: 20px; height: 20px"></i>
                                                         </a>
                                                         <sec:authorize
                                                                 access="hasAnyAuthority('ADMIN', '${authority.concat(\'_UPDATE\')}')">
                                                                <a href="${pageContext.request.contextPath}/authorities/${uri}/${item.id}/update"
                                                                   class="mr-1 txt-primary" up-instant
                                                                   up-layer="new"
                                                                   up-dismissable="button">
                                                                    <i data-feather="edit"
                                                                       style="width: 20px; height: 20px"></i>
                                                                </a>
                                                            </sec:authorize>
                                                         <sec:authorize
                                                                 access="hasAnyAuthority('ADMIN', '${authority.concat(\'_DELETE\')}')">
                                                                <a href="${pageContext.request.contextPath}/authorities/${uri}/${item.id}/delete"
                                                                   up-layer="new" up-history="false" up-instant
                                                                   up-dismissable="button"
                                                                   class="txt-danger">
                                                                    <i data-feather="trash"
                                                                       style="width: 20px; height: 20px"></i>
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
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <nav class="d-flex justify-content-start mt-3">
                                <component:pagination pageMeta="${data}" target="#table, [comp=pagination]"
                                                      up="up-scroll='#table' up-transition='cross-fade'"/>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:librarian>
