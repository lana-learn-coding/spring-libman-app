<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.time.LocalDate" %>

<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<%--@elvariable id="entity" type="io.lana.libman.core.book.BookBorrow"--%>
<%--@elvariable id="edit" type="java.lang.Boolean"--%>

<c:set var="op" value="${edit ? 'Update' : 'Create'}"/>

<layout:modal>
    <jsp:attribute name="title">${op} Borrow Ticket</jsp:attribute>
    <jsp:attribute name="body">
        <div class="container-fluid">
            <div class="page-header">
                <div class="row">
                    <div class="col-sm-8">
                        <h3>${op} Borrow</h3>
                        <ol class="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item">
                                    <a href="${pageContext.request.contextPath}/library/dashboard"
                                       up-follow up-instant>Home</a>
                                </li>
                                <li class="breadcrumb-item">
                                    <a href="${pageContext.request.contextPath}/library/borrows"
                                       up-follow up-instant>Borrows</a>
                                </li>
                                <li class="breadcrumb-item active">${op}</li>
                            </ol>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
       <div class="container-fluid">
           <div class="row">
               <div class="col-12 modal-p-0">
                   <form:form class="card text-start modal-m-0" method="post" up-submit="true"
                              modelAttribute="entity" up-layer="parent root" up-scroll="false">
                       <sec:csrfInput/>
                       <div class="up-active-overlay">
                           <div class="loader-box">
                               <div class="loader-3"></div>
                           </div>
                       </div>
                       <div class="card-header pb-0">
                           <h5>${op} Borrow</h5>
                           <c:if test="${edit}">
                               <span>Editing #${entity.id}</span>
                           </c:if>
                           <c:if test="${not edit}">
                                <span>Create new</span>
                          </c:if>
                       </div>
                       <div class="card-body">
                           <div class="theme-form" style="max-width: 1000px">
                               <c:if test="${edit}">
                                   <input type="hidden" name="book" value="${entity.book.id}">
                                   <input type="hidden" name="reader" value="${entity.reader.id}">
                                   <input type="hidden" name="ticket" value="${entity.ticketId}">
                               </c:if>
                               <c:if test="${not edit}">
                                   <div class="mb-3">
                                       <label class="col-form-label pt-0" for="book">Book</label>
                                       <form:select
                                               data-placeholder="Select book to borrow"
                                               select2="${pageContext.request.contextPath}/library/books/books/available/autocomplete"
                                               path="book" cssClass="form-select" data-option-show-id="true"
                                               data-selection-show-id="true"
                                               required="true" disabled="${edit}"
                                               cssErrorClass="form-select is-invalid">
                                               <c:if test="${not empty entity.book}">
                                                   <option value="${entity.book.id}"
                                                           selected>${entity.book.title}</option>
                                               </c:if>
                                           </form:select>
                                       <form:errors path="book" cssClass="invalid-feedback"/>
                                       <c:if test="${edit}">
                                        <small class="form-text text-muted">Cannot change reader after created</small>
                                       </c:if>
                                   </div>
                                   <div class="mb-3">
                                       <label class="col-form-label pt-0" for="reader">Reader</label>
                                       <form:select
                                               data-placeholder="Select reader"
                                               required="true" disabled="${edit}"
                                               select2="${pageContext.request.contextPath}/library/readers/autocomplete"
                                               path="reader" cssClass="form-select"
                                               cssErrorClass="form-select is-invalid">
                                           <c:if test="${not empty entity.reader}">
                                               <option value="${entity.reader.id}"
                                                       selected>${entity.reader.account.email}</option>
                                           </c:if>
                                       </form:select>
                                       <c:if test="${edit}">
                                           <small class="form-text text-muted">Cannot change reader after
                                               created</small>
                                       </c:if>
                                       <form:errors path="reader" cssClass="invalid-feedback"/>
                                   </div>
                                   <div class="mb-3">
                                       <label class="col-form-label pt-0" for="ticket">Ticket Id</label>
                                       <form:input path="ticketId" cssClass="form-control"
                                                   placeholder="Enter ticket id"
                                                   required="true" readonly="${edit}"
                                                   cssErrorClass="form-control is-invalid"/>
                                       <form:errors path="ticketId" cssClass="invalid-feedback"/>
                                       <c:if test="${edit}">
                                           <small class="form-text text-muted">Borrow is associated with
                                               ticket</small>
                                       </c:if>
                                   </div>
                               </c:if>
                               <div class="mb-3">
                                   <label class="col-form-label pt-0">Due Date</label>
                                   <form:input path="dueDate" cssClass="datepicker-here form-control digits"
                                               cssErrorClass="datepicker-here form-control digits is-invalid"
                                               placeholder="Select due date" type="text"
                                               data-start-date="${entity.dueDate}"
                                               required="true" readonly="true"/>
                                   <form:errors path="dueDate" cssClass="invalid-feedback"/>
                               </div>
                               <div class="mb-3">
                                   <label class="col-form-label pt-0"
                                          for="note">Note</label>
                                   <textarea class="form-control" id="note"
                                             placeholder="note">${entity.note}</textarea>
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
