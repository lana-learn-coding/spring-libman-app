<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.time.LocalDate, io.lana.libman.support.data.Gender" %>

<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<%--@elvariable id="entity" type="io.lana.libman.core.reader.Reader"--%>
<%--@elvariable id="edit" type="java.lang.Boolean"--%>

<c:set var="op" value="${edit ? 'Update' : 'Create'}"/>

<layout:modal>
    <jsp:attribute name="title">${op} Reader</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>${op} Book Info</h3>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/dashboard"
                                   up-follow up-instant>Home</a>
                            </li>
                            <li class="breadcrumb-item">Readers</li>
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/readers"
                                   up-follow up-instant>Info</a>
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
                       <form:input type="hidden" path="account.id"/>
                       <div class="up-active-overlay">
                           <div class="loader-box">
                               <div class="loader-3"></div>
                           </div>
                       </div>
                       <div class="card-header pb-0">
                           <h5>${op} Book Info</h5>
                           <c:if test="${edit}">
                               <span>Editing ${entity.account.name}</span>
                           </c:if>
                           <c:if test="${not edit}">
                                <span>Create new</span>
                          </c:if>
                       </div>
                       <div class="card-body">
                           <div class="theme-form" style="max-width: 1000px">
                               <div class="mb-3">
                                   <label class="col-form-label pt-0" for="account.email">Email</label>
                                   <form:input path="account.email" cssClass="form-control"
                                               cssErrorClass="form-control is-invalid"
                                               type="email"
                                               placeholder="Enter the email" required="true"
                                               readonly="${edit}"/>
                                   <form:errors path="account.email" cssClass="invalid-feedback"/>
                                   <c:if test="${edit}">
                                       <small class="form-text text-muted">Cannot change email as it associated to
                                           account</small>
                                   </c:if>
                               </div>
                               <div class="mb-3">
                                   <label class="col-form-label pt-0" for="account.phone">Phone</label>
                                   <form:input path="account.phone" cssClass="form-control"
                                               cssErrorClass="form-control is-invalid"
                                               placeholder="Enter the phone"
                                               readonly="${edit}"/>
                                   <form:errors path="account.phone" cssClass="invalid-feedback"/>
                                   <c:if test="${edit}">
                                       <small class="form-text text-muted">Cannot change phone as it associated to
                                           account</small>
                                   </c:if>
                               </div>
                               <div class="mb-3">
                                   <div class="row">
                                       <div class="col-12 col-sm-6">
                                           <label class="col-form-label pt-0" for="account.firstName">First Name</label>
                                           <form:input path="account.firstName" cssClass="form-control"
                                                       cssErrorClass="form-control is-invalid"
                                                       placeholder="Enter first name"/>
                                           <form:errors path="account.firstName" cssClass="invalid-feedback"/>
                                       </div>
                                       <div class="col-12 col-sm-6">
                                           <label class="col-form-label pt-0" for="account.lastName">Last Name</label>
                                           <form:input path="account.lastName" cssClass="form-control"
                                                       cssErrorClass="form-control is-invalid"
                                                       placeholder="Enter last name"/>
                                           <form:errors path="account.lastName" cssClass="invalid-feedback"/>
                                       </div>
                                   </div>
                               </div>
                               <div class="mb-3">
                                   <div class="row">
                                       <div class="col-12 col-sm-6">
                                           <label class="col-form-label pt-0" for="account.firstName">Gender</label>
                                           <form:select path="account.gender" cssClass="form-select"
                                                        cssErrorClass="form-select is-invalid" required="required">
                                               <form:options items="${Gender.values()}"/>
                                           </form:select>
                                           <form:errors path="account.gender" cssClass="invalid-feedback"/>
                                       </div>
                                       <div class="col-12 col-sm-6">
                                           <label class="col-form-label pt-0" for="account.lastName">Limit</label>
                                           <form:input path="borrowLimit" cssClass="form-control"
                                                       cssErrorClass="form-control is-invalid"
                                                       placeholder="Enter limit" type="number" min="1" step="1"/>
                                           <form:errors path="borrowLimit" cssClass="invalid-feedback"/>
                                       </div>
                                   </div>
                               </div>
                               <div class="mb-4">
                                   <label class="col-form-label pt-0"
                                          for="address">Address</label>
                                   <textarea class="form-control" id="address" rows="2"
                                             name="account.address"
                                             placeholder="Address">${entity.account.address}</textarea>
                               </div>
                               <div class="d-flex align-items-center mb-3">
                                   <img id="image" class="rounded-circle" style="width: 80px"
                                        src="${pageContext.request.contextPath}${(empty entity.account.avatar ? '/static/images/avatar/default.png' : entity.account.avatar)}"
                                        alt="avatar">
                                   <spring:bind path="account.avatar">
	                                    <div class="ms-2 ms-md-3 flex-grow-1">
                                            <label class="col-form-label pt-0" for="file">Avatar</label>
                                            <input id="file" name="file"
                                                   class="form-control ${status.error ? 'is-invalid' : ''}"
                                                   placeholder="Upload avatar" type="file" accept="image/*"/>
                                            <form:errors path="account.avatar" cssClass="invalid-feedback"/>
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
                           <a class="btn btn-secondary" href="${pageContext.request.contextPath}/library/books/infos"
                              up-dismiss>Cancel</a>
                       </div>
                   </form:form>
               </div>
           </div>
       </div>
    </jsp:attribute>
</layout:modal>
