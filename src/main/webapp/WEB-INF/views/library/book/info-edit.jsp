<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.time.LocalDate" %>

<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<%--@elvariable id="entity" type="io.lana.libman.core.book.BookInfo"--%>
<%--@elvariable id="edit" type="java.lang.Boolean"--%>

<c:set var="op" value="${edit ? 'Update' : 'Create'}"/>

<layout:modal>
    <jsp:attribute name="title">${op} Book Info</jsp:attribute>
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
                            <li class="breadcrumb-item">Books</li>
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/library/books/infos"
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
                       <div class="up-active-overlay">
                           <div class="loader-box">
                               <div class="loader-3"></div>
                           </div>
                       </div>
                       <jsp:include page="/WEB-INF/presets/editor.head.jsp"/>
                       <div class="card-header pb-0">
                           <h5>${op} Book Info</h5>
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
                                   <label class="col-form-label pt-0" for="title">Book Title</label>
                                   <form:input path="title" cssClass="form-control"
                                               cssErrorClass="form-control is-invalid"
                                               placeholder="Enter the title" required="true"/>
                                   <form:errors path="title" cssClass="invalid-feedback"/>
                               </div>
                               <div class="mb-3">
                                   <div class="row">
                                       <div class="col-12 col-sm-9">
                                           <label class="col-form-label pt-0" for="series">Book Series</label>
                                           <form:select
                                                   data-placeholder="Select series"
                                                   data-allow-clear="true"
                                                   select2="${pageContext.request.contextPath}/library/tags/series/autocomplete"
                                                   path="series" cssClass="form-select"
                                                   cssErrorClass="form-select is-invalid">

                                               <c:if test="${not empty entity.seriesName}">
                                                   <option value="${entity.series.id}"
                                                           selected>${entity.seriesName}</option>
                                               </c:if>
                                           </form:select>
                                           <form:errors path="series" cssClass="invalid-feedback"/>
                                       </div>
                                       <div class="col-12 col-sm-3">
                                           <label class="col-form-label pt-0" for="year">Release</label>
                                           <form:input path="year" cssClass="form-control"
                                                       cssErrorClass="form-control is-invalid"
                                                       placeholder="Enter release year" min="0"
                                                       step="1"/>
                                           <form:errors path="year" cssClass="invalid-feedback"/>
                                       </div>
                                   </div>
                               </div>
                               <div class="mb-3 row">
                                   <c:if test="${not edit}">
                                       <div class="col-12 col-sm-6">
                                           <label class="col-form-label pt-0" for="numberOfBooks">Number of
                                               Books</label>
                                           <form:input path="numberOfBooks" cssClass="form-control"
                                                       cssErrorClass="form-control is-invalid"
                                                       placeholder="Enter number of books" required="true" min="0"/>
                                           <form:errors path="numberOfBooks" cssClass="invalid-feedback"/>
                                       </div>
                                   </c:if>
                                   <div class="col-12 col-sm-6">
                                       <label class="col-form-label pt-0" for="borrowCost">Borrow cost (per
                                           day)</label>
                                       <form:input path="borrowCost" cssClass="form-control"
                                                   cssErrorClass="form-control is-invalid"
                                                   placeholder="Enter borrow cost per day" required="true" min="0"
                                                   step="0.02"/>
                                       <form:errors path="borrowCost" cssClass="invalid-feedback"/>
                                   </div>
                               </div>
                               <div class="mb-3">
                                   <div class="row">
                                       <div class="col-12 col-sm-6">
                                           <label class="col-form-label pt-0" for="author">Author</label>
                                           <form:select
                                                   data-placeholder="Select author"
                                                   data-allow-clear="true"
                                                   select2="${pageContext.request.contextPath}/library/tags/authors/autocomplete"
                                                   path="author" cssClass="form-select"
                                                   cssErrorClass="form-select is-invalid">
                                                <c:if test="${not empty entity.authorName}">
                                                    <option value="${entity.author.id}"
                                                            selected>${entity.authorName}</option>
                                                </c:if>
                                           </form:select>
                                           <form:errors path="author" cssClass="invalid-feedback"/>
                                       </div>
                                       <div class="col-12 col-sm-6">
                                           <label class="col-form-label pt-0" for="publisher">Publishers</label>
                                           <form:select
                                                   data-placeholder="Select publisher"
                                                   data-allow-clear="true"
                                                   select2="${pageContext.request.contextPath}/library/tags/publishers/autocomplete"
                                                   path="publisher" cssClass="form-select"
                                                   cssErrorClass="form-select is-invalid"
                                                   placeholder="Enter number of books">
                                                <c:if test="${not empty entity.publisherName}">
                                                    <option value="${entity.publisher.id}"
                                                            selected>${entity.publisherName}</option>
                                                </c:if>
                                           </form:select>
                                           <form:errors path="publisher" cssClass="invalid-feedback"/>
                                       </div>
                                   </div>
                               </div>
                               <div class="mb-3">
                                   <label class="col-form-label pt-0" for="genres">Book Genres</label>
                                   <form:select
                                           data-placeholder="Select genres"
                                           data-allow-clear="true"
                                           select2="${pageContext.request.contextPath}/library/tags/genres/autocomplete"
                                           path="genres" cssClass="form-select" cssErrorClass="form-select is-invalid"
                                           multiple="multiple">
                                       <c:if test="${not empty entity.genres}">
                                               <form:options items="${entity.genres}" itemValue="id" itemLabel="name"
                                                             selected="true"/>
                                       </c:if>
                                   </form:select>
                                   <form:errors path="genres" cssClass="invalid-feedback"/>
                               </div>
                               <div class="mb-3">
                                   <label class="col-form-label pt-0"
                                          for="about">About The Book</label>
                                   <textarea class="form-control" id="about" data-editor="true"
                                             placeholder="About">${entity.about}</textarea>
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
                           <a class="btn btn-secondary" href="${pageContext.request.contextPath}/library/books/infos"
                              up-dismiss up-follow>Cancel</a>
                       </div>
                   </form:form>
               </div>
           </div>
       </div>
    </jsp:attribute>
</layout:modal>
