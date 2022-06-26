<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.time.LocalDate, io.lana.libman.support.data.Gender" %>

<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<%--@elvariable id="entity" type="io.lana.libman.core.user.User"--%>
<%--@elvariable id="edit" type="java.lang.Boolean"--%>

<c:set var="op" value="${edit ? 'Update' : 'Create'}"/>

<layout:modal>
    <jsp:attribute name="title">${op} User</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>${op} User</h3>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/dashboard"
                                   up-follow up-instant>Home</a>
                            </li>
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/authorities/users"
                                   up-follow up-instant>Users</a>
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
                                                 modelAttribute="entity" enctype="multipart/form-data"
                                                 up-layer="parent root">
                       <sec:csrfInput/>
                       <form:input type="hidden" path="id"/>
                       <div class="up-active-overlay">
                           <div class="loader-box">
                               <div class="loader-3"></div>
                           </div>
                       </div>
                       <div class="card-header pb-0">
                           <h5>${op} User</h5>
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
                                   <div class="row">
                                       <div class="col-12 col-sm-6">
                                           <label class="col-form-label pt-0" for="email">Username</label>
                                           <form:input path="username" cssClass="form-control"
                                                       cssErrorClass="form-control is-invalid"
                                                       type="username"
                                                       placeholder="Enter the username" required="true"/>
                                           <form:errors path="username" cssClass="invalid-feedback"/>
                                       </div>
                                       <div class="col-12 col-sm-6">
                                           <label class="col-form-label pt-0" for="password">Password</label>
                                           <spring:bind path="password">
                                               <input class="form-control ${status.error ? 'is-invalid' : ''}"
                                                      type="password" id="password" name="password"
                                                      value="${edit ? '' : entity.password}"
                                                      placeholder="Set new password">
                                           </spring:bind>
                                           <form:errors path="password" cssClass="invalid-feedback"/>
                                       </div>
                                   </div>
                               </div>
                               <div class="mb-3">
                                   <div class="row">
                                       <div class="col-12 col-sm-6">
                                           <label class="col-form-label pt-0" for="email">Email</label>
                                           <form:input path="email" cssClass="form-control"
                                                       cssErrorClass="form-control is-invalid"
                                                       type="email"
                                                       placeholder="Enter the email" required="true"
                                                       readonly="${edit}"/>
                                           <form:errors path="email" cssClass="invalid-feedback"/>
                                           <c:if test="${edit}">
                                                <small class="form-text text-muted">Email is associated to
                                                    account</small>
                                           </c:if>
                                       </div>
                                       <div class="col-12 col-sm-6">
                                           <label class="col-form-label pt-0" for="phone">Phone</label>
                                           <form:input path="phone" cssClass="form-control"
                                                       cssErrorClass="form-control is-invalid"
                                                       placeholder="Enter the phone"/>
                                           <form:errors path="phone" cssClass="invalid-feedback"/>
                                       </div>
                                   </div>
                               </div>
                               <div class="mb-3">
                                   <div class="row">
                                       <div class="col-12 col-sm-6">
                                           <label class="col-form-label pt-0" for="firstName">First Name</label>
                                           <form:input path="firstName" cssClass="form-control"
                                                       cssErrorClass="form-control is-invalid"
                                                       placeholder="Enter first name"/>
                                           <form:errors path="firstName" cssClass="invalid-feedback"/>
                                       </div>
                                       <div class="col-12 col-sm-6">
                                           <label class="col-form-label pt-0" for="lastName">Last Name</label>
                                           <form:input path="lastName" cssClass="form-control"
                                                       cssErrorClass="form-control is-invalid"
                                                       placeholder="Enter last name"/>
                                           <form:errors path="lastName" cssClass="invalid-feedback"/>
                                       </div>
                                   </div>
                               </div>
                               <div class="mb-3">
                                   <div class="row">
                                       <div class="col-12 col-sm-6">
                                           <label class="col-form-label pt-0" for="firstName">Gender</label>
                                           <form:select path="gender" cssClass="form-select"
                                                        cssErrorClass="form-select is-invalid" required="required">
                                               <form:options items="${Gender.values()}"/>
                                           </form:select>
                                           <form:errors path="gender" cssClass="invalid-feedback"/>
                                       </div>
                                       <div class="col-12 col-sm-6">
                                           <helper:format-date date="${entity.dateOfBirth}" var="birth"/>
                                           <label class="col-form-label pt-0">Date of Birth</label>
                                           <form:input path="dateOfBirth" cssClass="datepicker-here form-control digits"
                                                       cssErrorClass="datepicker-here form-control digits is-invalid"
                                                       placeholder="Select birthdate" type="text"
                                                       value="${birth}" data-start-date="${birth}"
                                                       readonly="true"/>
                                           <form:errors path="dateOfBirth" cssClass="invalid-feedback"/>
                                       </div>
                                   </div>
                               </div>
                               <div class="mb-3">
                                   <label class="col-form-label pt-0" for="roles">Roles</label>
                                   <form:select
                                           data-placeholder="Select Permissions"
                                           select2="${pageContext.request.contextPath}/authorities/roles/autocomplete"
                                           path="roles" cssClass="form-select"
                                           cssErrorClass="form-select is-invalid">
                                        <c:if test="${not empty entity.roles}">
                                             <form:options items="${entity.roles}" itemValue="id" itemLabel="name"
                                                           selected="true"/>
                                        </c:if>
                                   </form:select>
                                   <form:errors path="roles" cssClass="invalid-feedback"/>
                               </div>
                               <div class="mb-4">
                                   <label class="col-form-label pt-0"
                                          for="address">Address</label>
                                   <textarea class="form-control" id="address" rows="2"
                                             name="address"
                                             placeholder="Address">${entity.address}</textarea>
                               </div>
                               <div class="d-flex align-items-center mb-3">
                                   <img id="image" class="rounded-circle" style="width: 80px"
                                        src="${pageContext.request.contextPath}${(empty entity.avatar ? '/static/images/avatar/default.png' : entity.avatar)}"
                                        alt="avatar">
                                   <spring:bind path="avatar">
	                                    <div class="ms-2 ms-md-3 flex-grow-1">
                                            <label class="col-form-label pt-0" for="file">Avatar</label>
                                            <form:input type="hidden" path="avatar"/>
                                            <input id="file" name="file"
                                                   class="form-control ${status.error ? 'is-invalid' : ''}"
                                                   placeholder="Upload avatar" type="file" accept="image/*"/>
                                            <form:errors path="avatar" cssClass="invalid-feedback"/>
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
                           <a class="btn btn-secondary" href="${pageContext.request.contextPath}/authorities/users"
                              up-dismiss up-follow>Cancel</a>
                       </div>
                   </form:form>
               </div>
           </div>
       </div>
    </jsp:attribute>
</layout:modal>
