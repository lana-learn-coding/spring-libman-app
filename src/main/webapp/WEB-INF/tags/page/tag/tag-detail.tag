<%@tag description="Page layout" pageEncoding="UTF-8" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ attribute name="uri" required="false" type="java.lang.String" %>
<%@ attribute name="data" required="false" type="org.springframework.data.domain.Page" %>
<%@ attribute name="entity" required="false" type="io.lana.libman.core.tag.TaggedEntity" %>

<%@ attribute name="table" fragment="true" %>
<%@ attribute name="detail" fragment="true" %>

<%--@elvariable id="data" type="org.springframework.data.domain.Page<io.lana.libman.support.data.NamedEntity>"--%>

<c:set var="uri" value="${(empty uri) ? title.toLowerCase() : uri}"/>

<layout:librarian>
    <jsp:attribute name="title">${title} Detail</jsp:attribute>
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
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/tags/${uri}"
                                   up-follow up-instant>${title}
                                </a>
                            </li>
                            <li class="breadcrumb-item">${entity.name}</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-12">
                    <div class="card text-start" up-main="modal">
                        <div class="card-header pb-0">
                            <h5>${title} Detail</h5>
                            <span>Detail of ${entity.name}</span>
                        </div>
                        <c:choose>
                            <c:when test="${detail != null}">
                               <jsp:invoke fragment="detail"/>
                            </c:when>
                            <c:otherwise>
                                 <div class="card-body pt-1">
                                     <div class="mb-3 text-muted">
                                         <div>
                                             <i class="fa fa-calendar-o"></i>
                                             <span class="ms-1"><helper:formatZonedDateTime
                                                     date="${entity.updatedAt}"/></span>
                                         </div>
                                         <c:if test="${not empty entity.updatedBy}">
                                            <div class="ms-2 d-none d-md-block">
                                                <i class="fa fa-user"></i>
                                                <span class="ms-1">${entity.updatedBy}</span>
                                            </div>
                                        </c:if>
                                     </div>
                                     <div class="mb-1">Name <span class="font-primary ms-2">${entity.name}</span></div>
                                     <c:if test="${not empty entity.about}">
                                        <div class="mb-1">About <span class="font-primary ms-2">${entity.about}</span>
                                        </div>
                                     </c:if>
                                 </div>
                            </c:otherwise>
                        </c:choose>

                        <hr class="my-0">
                        <div class="card-header pb-0">
                            <h5>Related Books</h5>
                            <span>List books of ${title}: ${entity.name} (Total of ${entity.booksCount})</span>
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
                                            labels="Updated At;Updated By;Id"
                                            values="updatedAt,desc;updatedBy,id"/>
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
                                                 <th scope="col">Updated At</th>
                                                 <th scope="col">Updated By</th>
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
                                                     <td><helper:formatZonedDateTime date="${item.updatedAt}"/></td>
                                                     <td>${ item.updatedBy }</td>
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
        <div class="modal fade" tabindex="-1" id="delete-modal" aria-hidden="true"
             _="on load remove .show from <.modal-backdrop/> then remove <.modal-backdrop/>">
            <div class="modal-dialog modal-dialog-centered">
                <form class="modal-content" action="${pageContext.request.contextPath}/library/tags/${uri}/delete"
                      method="post"
                      up-target="#table, #delete-modal" up-scroll="false">
                    <sec:csrfInput/>
                    <input id="delete-id" type="hidden" name="id" value="">
                    <div class="modal-body d-flex flex-column align-items-center pt-4 pb-3">
                        <i data-feather="alert-triangle" class="txt-warning"
                           style="width: 100px; height: 100px; stroke-width: 1"></i>
                        <h3 class="f-w-600 mt-3">Delete item</h3>
                        <span>Are you sure delete this item</span>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-light" data-bs-dismiss="modal">back</button>
                        <button type="submit" class="btn btn-warning">Delete</button>
                    </div>
                </form>
            </div>
        </div>
    </jsp:attribute>
</layout:librarian>
