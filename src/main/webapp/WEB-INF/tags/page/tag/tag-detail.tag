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
<%@ attribute name="entity" required="false" type="io.lana.libman.core.tag.Tagged" %>

<%@ attribute name="table" fragment="true" %>
<%@ attribute name="detail" fragment="true" %>

<%--@elvariable id="data" type="org.springframework.data.domain.Page<io.lana.libman.support.data.NamedEntity>"--%>

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
                            <li class="breadcrumb-item">Books</li>
                            <li class="breadcrumb-item">Tags</li>
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/tags/${uri}"
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
                        <c:choose>
                             <c:when test="${detail != null}">
                                <jsp:invoke fragment="detail"/>
                             </c:when>
                             <c:otherwise>
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
                                <div class="col-xs-12 col-md-3 col-lg-2 my-1">
                                     <component:sorting
                                             target="#table, [comp=sorting]"
                                             up="up-scroll='#table' up-transition='cross-fade'"
                                             labels="Updated At;Updated By;Id"
                                             values="updatedAt,desc;updatedBy;id,desc"/>
                                </div>
                                <form class="col-xs-12 col-md-6 my-1"
                                      up-target="#table, nav .pagination" method="get">
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
                                                    <th scope="row"><component:index pageMeta="${data}"
                                                                                     i="${loop.index}"/></th>
                                                    <td>${ item.id }</td>
                                                    <td>${ item.name }</td>
                                                    <td><helper:format-instant date="${item.updatedAt}"/></td>
                                                    <td>${ not empty item.updatedBy ? item.updatedBy : item.createdBy }</td>
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
</layout:modal>
