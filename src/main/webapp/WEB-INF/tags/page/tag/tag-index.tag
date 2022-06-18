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
<%@ attribute name="data" required="false"
              type="org.springframework.data.domain.Page<io.lana.libman.core.tag.TaggedEntity>" %>

<%@ attribute name="table" fragment="true" %>

<c:set var="uri" value="${(empty uri) ? title.toLowerCase() : uri}"/>

<layout:librarian>
    <jsp:attribute name="title">${title} Manage</jsp:attribute>
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
                            <span>Manage list of ${title} information and related books</span>
                        </div>
                        <div class="card-body" id="table">
                            <div class="row justify-content-between mb-3">
                                <form class="col-xs-12 col-md-6 my-1"
                                      up-target="#table table, nav .pagination" method="get">
                                    <helper:inherit-param excludes="query"/>
                                    <div class="input-group">
                                        <span class="input-group-text bg-primary"><i class="icon-search"></i></span>
                                        <input type="text"
                                               style="max-width: 500px"
                                               class="form-control form-control-lg"
                                               name="query"
                                               placeholder="Search for item"
                                               aria-label="query"
                                               value="${param.query}"
                                               up-autosubmit
                                               up-delay="400">
                                    </div>
                                </form>
                                <div class="col-xs-12 col-md-3 col-lg-2 my-1">
                                    <component:sorting
                                            target="#table, [comp=sorting]" up="up-scroll='layer'"
                                            labels="Name;Books Count;Updated At;Updated By;Id"
                                            values="name;booksCount,desc;updatedAt,desc;updatedBy,id"/>
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
                                                 <th scope="col">Total Books</th>
                                                 <th scope="col">Updated At</th>
                                                 <th scope="col">Updated By</th>
                                                 <th scope="col">Action</th>
                                             </tr>
                                             </thead>
                                             <tbody>
                                             <c:set var="content" value="${empty data ? [] : data.content}"/>
                                             <c:forEach var="item" items="${data.content}" varStatus="loop">
                                                 <tr>
                                                     <th scope="row">${loop.index + 1}</th>
                                                     <td class="text-truncate"
                                                         style="max-width: 180px">${ item.id }</td>
                                                     <td>${ item.name }</td>
                                                     <td>${ item.booksCount }</td>
                                                     <td><helper:formatZonedDateTime date="${item.updatedAt}"/></td>
                                                     <td>${ item.updatedBy }</td>
                                                     <td>
                                                         <a href="${pageContext.request.contextPath}/library/tags/${uri}/${item.id}/detail?size=5"
                                                            class="mx-1 txt-primary" up-layer="new" up-size="large">
                                                             <i data-feather="external-link"
                                                                style="width: 20px; height: 20px"></i>
                                                         </a>
                                                         <sec:authorize
                                                                 access="hasAnyAuthority('ADMIN', '${authority.concat(\'_UPDATE\')}')">
                                                                <a href="#" class="mx-1 txt-primary">
                                                                    <i data-feather="edit"
                                                                       style="width: 20px; height: 20px"></i>
                                                                </a>
                                                            </sec:authorize>
                                                         <sec:authorize
                                                                 access="hasAnyAuthority('ADMIN', '${authority.concat(\'_DELETE\')}')">
                                                                <a href="${pageContext.request.contextPath}/library/tags/${uri}/${item.id}/delete"
                                                                   up-layer="new" up-history="false"
                                                                   class="mx-1 txt-danger">
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
                            <nav class="d-flex justify-content-end mt-3">
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
