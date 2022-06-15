<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%--@elvariable id="title" type="java.lang.String"--%>
<%--@elvariable id="entity" type="io.lana.libman.core.tag.TaggedEntity"--%>
<c:set var="uri" value="${title.toLowerCase()}"/>
<sec:authorize access="hasAnyAuthority('ADMIN', 'FORCE')" var="canForce"/>

<layout:base>
    <jsp:attribute name="title">Delete ${title}</jsp:attribute>
    <jsp:attribute name="body">
        <section>
            <div class="container-fluid p-0">
                <div class="row">
                    <div class="col-12">
                        <div class="login-card">
                            <div class="modal d-block">
                                <div class="modal-dialog modal-dialog-centered">
                                    <div class="modal-content" up-main="modal">
                                        <div class="modal-body d-flex flex-column align-items-center pt-4 pb-3">
                                            <c:choose>
                                                <c:when test="${entity.booksCount > 0 && !canForce}">
                                                    <i data-feather="alert-circle" class="txt-danger"
                                                       style="width: 100px; height: 100px; stroke-width: 1"></i>
                                                    <h3 class="f-w-600 mt-3">${entity.name}</h3>
                                                    <span>Cannot delete. Item have ${entity.booksCount} related book</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <i data-feather="alert-triangle" class="txt-warning"
                                                       style="width: 100px; height: 100px; stroke-width: 1"></i>
                                                    <h3 class="f-w-600 mt-3">${entity.name}</h3>
                                                    <span>Are you sure delete this item</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="modal-footer">
                                            <a href="${pageContext.request.contextPath}/library/tags/${uri}"
                                               class="btn btn-light" up-dismiss>Back</a>
                                            <c:if test="${entity.booksCount == 0}">
                                                <form action="${pageContext.request.contextPath}/library/tags/${uri}/delete"
                                                      method="post" up-layer="parent">
                                                    <sec:csrfInput/>
                                                    <input type="hidden" name="id" value="${entity.id}">
                                                    <button type="submit" class="btn btn-warning">Delete</button>
                                                </form>
                                            </c:if>

                                            <c:if test="${entity.booksCount > 0 && canForce}">
                                                <form action="${pageContext.request.contextPath}/library/tags/${uri}/force-delete"
                                                      method="post" up-layer="parent">
                                                    <sec:csrfInput/>
                                                    <input type="hidden" name="id" value="${entity.id}">
                                                    <button type="submit" class="btn btn-danger">Force Delete</button>
                                                </form>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </jsp:attribute>
</layout:base>
