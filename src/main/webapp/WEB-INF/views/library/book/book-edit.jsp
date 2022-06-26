<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.time.LocalDate" %>

<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<%--@elvariable id="entity" type="io.lana.libman.core.book.Book"--%>
<%--@elvariable id="edit" type="java.lang.Boolean"--%>

<c:set var="op" value="${edit ? 'Update' : 'Create'}"/>

<layout:modal>
    <jsp:attribute name="title">${op} Book</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>${op} Book</h3>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/dashboard"
                                   up-follow up-instant>Home</a>
                            </li>
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/books/books"
                                   up-follow up-instant>Books</a>
                            </li>
                            <li class="breadcrumb-item active">${op}</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
       <div class="container-fluid">
           <div class="row">
               <div class="col-12 modal-p-0">
                   <form:form class="card text-start modal-m-0" method="post" up-submit="true"
                              modelAttribute="entity" enctype="multipart/form-data" up-layer="parent root">
                       <sec:csrfInput/>
                       <div class="up-active-overlay">
                           <div class="loader-box">
                               <div class="loader-3"></div>
                           </div>
                       </div>
                       <div class="card-header pb-0">
                           <h5>${op} Book</h5>
                           <c:if test="${edit}">
                               <span>Editing ${entity.name}</span>
                           </c:if>
                           <c:if test="${not edit}">
                                <span>Create new</span>
                          </c:if>
                       </div>
                       <div class="card-body">
                           <div class="theme-form" style="max-width: 1000px">
                               <div class="mb-3">
                                   <label class="col-form-label pt-0" for="info">Book Info</label>
                                   <form:select
                                           data-placeholder="Select info"
                                           select2="${pageContext.request.contextPath}/library/books/infos/autocomplete"
                                           path="info" cssClass="form-select"
                                           required="true"
                                           cssErrorClass="form-select is-invalid">
                                               <c:if test="${not empty entity.info}">
                                                   <option value="${entity.info.id}"
                                                           selected>${entity.info.title}</option>
                                               </c:if>
                                           </form:select>
                                   <form:errors path="info" cssClass="invalid-feedback"/>
                               </div>
                               <c:if test="${not edit}">
                                   <div class="mb-3">
                                       <label class="col-form-label pt-0" for="id">Id</label>
                                       <form:input path="id" cssClass="form-control"
                                                   placeholder="Enter book id"
                                                   required="true"
                                                   cssErrorClass="form-control is-invalid"/>
                                       <form:errors path="id" cssClass="invalid-feedback"/>
                                   </div>
                               </c:if>
                               <div class="mb-3">
                                   <label class="col-form-label pt-0" for="info">Shelf</label>
                                   <form:select
                                           data-placeholder="Select shelf"
                                           required="true"
                                           select2="${pageContext.request.contextPath}/library/tags/shelf/autocomplete"
                                           path="shelf" cssClass="form-select"
                                           cssErrorClass="form-select is-invalid">
                                               <c:if test="${not empty entity.shelf}">
                                                   <option value="${entity.shelf.id}"
                                                           selected>${entity.shelf.name}</option>
                                               </c:if>
                                           </form:select>
                                   <form:errors path="shelf" cssClass="invalid-feedback"/>
                               </div>
                               <div class="mb-3">
                                   <label class="col-form-label pt-0"
                                          for="note">Note</label>
                                   <textarea class="form-control" id="note"
                                             placeholder="note">${entity.note}</textarea>
                               </div>
                               <div class="d-flex align-items-center mb-3">
                                   <img id="image" class="rounded" style="width: 80px"
                                        src="${pageContext.request.contextPath}${(empty entity.image ? '/static/images/book-default.png' : entity.image)}"
                                        alt="avatar">
                                   <spring:bind path="image">
	                                    <div class="ms-3 ms-md-4 flex-grow-1">
                                            <label class="col-form-label pt-0" for="file">Image</label>
                                            <input id="file" name="file"
                                                   class="form-control ${status.error ? 'is-invalid' : ''}"
                                                   placeholder="Upload image" type="file" accept="image/*"/>
                                            <form:input path="image" type="hidden"/>
                                            <form:errors path="image" cssClass="invalid-feedback"/>
                                        </div>
                                   </spring:bind>
                               </div>
                               <div class="mb-3">
                                   <div class="row">
                                       <div class="col-12 col-sm-6">
                                           <label class="col-form-label pt-0" for="user">
                                                   ${op}d by
                                           </label>
                                           <input class="form-control"
                                                  id="user"
                                                  value="<sec:authentication property="principal.username"/>"
                                                  type="text"
                                                  readonly>
                                       </div>
                                       <div class="col-12 col-sm-6">
                                           <label class="col-form-label pt-0" for="now">
                                                   ${op}d at
                                           </label>
                                           <input class="form-control"
                                                  id="now"
                                                  value="<helper:format-date date="${LocalDate.now()}"/>" type="text"
                                                  readonly>
                                       </div>
                                   </div>
                               </div>
                           </div>
                       </div>
                       <div class="card-footer">
                           <button class="btn btn-primary" type="submit">Submit</button>
                           <a class="btn btn-secondary" href="${pageContext.request.contextPath}/library/books/books"
                              up-dismiss up-follow>Cancel</a>
                       </div>
                   </form:form>
               </div>
           </div>
       </div>
    </jsp:attribute>
</layout:modal>
