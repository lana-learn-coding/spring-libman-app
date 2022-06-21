<%@tag description="Page layout" pageEncoding="UTF-8" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ attribute name="edit" required="false" type="java.lang.Boolean" %>
<%@ attribute name="uri" required="false" type="java.lang.String" %>
<%@ attribute name="entity" required="false" type="io.lana.libman.core.tag.Tagged" %>
<%@ attribute name="enctype" required="false" type="java.lang.String" %>
<%@ attribute name="form" fragment="true" %>

<%--@elvariable id="data" type="org.springframework.data.domain.Page<io.lana.libman.support.data.NamedEntity>"--%>
<c:set var="op" value="${edit ? 'Update' : 'Create'}"/>
<c:set var="uri" value="${(empty uri) ? title.toLowerCase() : uri}"/>
<c:set var="enctype" value="${not empty enctype ? enctype : 'application/x-www-form-urlencoded'}"/>

<layout:modal>
    <jsp:attribute name="title">${title}</jsp:attribute>
    <jsp:attribute name="body">
       <form:form class="card text-start" method="post" up-main="modal" up-submit="true" modelAttribute="entity"
                  up-layer="parent" enctype="${enctype}">
           <sec:csrfInput/>
           <div class="up-active-overlay">
               <div class="loader-box">
                   <div class="loader-3"></div>
               </div>
           </div>
           <jsp:include page="/WEB-INF/presets/editor.head.jsp"/>
           <div class="card-header pb-0">
               <h5>${op} ${title}</h5>
               <c:if test="${edit}">
                   <span>Editing ${entity.name}</span>
               </c:if>
               <c:if test="${not edit}">
                    <span>Create new</span>
              </c:if>
           </div>
           <div class="card-body">
               <div class="theme-form" style="max-width: 900px">
               <c:choose>
                   <c:when test="${form != null}">
                       <jsp:invoke fragment="form"/>
                   </c:when>
                   <c:otherwise>
                       <div class="mb-3">
                           <label class="col-form-label pt-0" for="name">Name</label>
                           <form:input path="name" cssClass="form-control"
                                       cssErrorClass="form-control is-invalid"
                                       placeholder="Enter the name" required="true"/>
                           <form:errors path="name" cssClass="invalid-feedback"/>
                       </div>
                       <div class="mb-3">
                           <label class="col-form-label pt-0"
                                  for="about">About</label>
                           <textarea class="form-control" id="about" data-editor="true"
                                     placeholder="About">${entity.about}</textarea>
                       </div>
                   </c:otherwise>
               </c:choose>
                   <div class="mb-3">
                       <label class="col-form-label pt-0" for="user">
                               ${op}d by
                       </label>
                       <input class="form-control"
                              id="user"
                              value="<sec:authentication property="principal.username"/>" type="text"
                              disabled>
                   </div>
               </div>
           </div>
           <div class="card-footer">
               <button class="btn btn-primary" type="submit">Submit</button>
               <a class="btn btn-secondary" href="${pageContext.request.contextPath}/library/tags/${uri}"
                  up-dismiss>Cancel</a>
           </div>
       </form:form>
    </jsp:attribute>
</layout:modal>
