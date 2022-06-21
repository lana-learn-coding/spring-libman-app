<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.time.LocalDate" %>
<%@ taglib prefix="p" tagdir="/WEB-INF/tags/page/tag" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--@elvariable id="edit" type="java.lang.Boolean"--%>
<%--@elvariable id="title" type="java.lang.String"--%>
<%--@elvariable id="entity" type="io.lana.libman.core.tag.Author"--%>
<p:tag-edit title="${title}" entity="${entity}" edit="${edit}" enctype="multipart/form-data">
    <jsp:attribute name="form">
        <jsp:include page="/WEB-INF/presets/datepicker.head.jsp"/>
        <div class="mb-3">
            <label class="col-form-label pt-0" for="name">Name</label>
            <form:input path="name" cssClass="form-control"
                        cssErrorClass="form-control is-invalid"
                        placeholder="Enter the name" required="true"/>
            <form:errors path="name" cssClass="invalid-feedback"/>
        </div>
        <helper:format-date date="${LocalDate.now()}" var="now"/>
        <div class="mb-3">
            <helper:format-date date="${entity.dateOfBirth}" var="birth"/>
            <label class="col-form-label pt-0">Date of Birth</label>
            <form:input path="dateOfBirth" cssClass="datepicker-here form-control digits"
                        cssErrorClass="datepicker-here form-control digits is-invalid"
                        placeholder="Select birth of date" type="text" readonly="true"
                        data-max-date="${now}"
                        value="${birth}"/>
            <form:errors path="dateOfBirth" cssClass="invalid-feedback"/>
        </div>
        <div class="mb-3">
            <helper:format-date date="${entity.dateOfDeath}" var="death"/>
            <label class="col-form-label pt-0">Date of Death</label>
            <form:input path="dateOfDeath" cssClass="datepicker-here form-control digits"
                        cssErrorClass="datepicker-here form-control digits is-invalid"
                        placeholder="Select birth of date" type="text" readonly="true"
                        data-max-date="${now}"
                        value="${death}"/>
            <form:errors path="dateOfDeath" cssClass="invalid-feedback"/>
        </div>
        <div class="mb-3">
            <label class="col-form-label pt-0"
                   for="about">About</label>
            <textarea class="form-control" id="about" data-editor="true"
                      placeholder="About">${entity.about}</textarea>
        </div>
        <div class="mb-3">
            <label class="col-form-label pt-0">Author image</label>
            <div class="d-flex align-items-center">
                <img id="image" class="rounded-circle" style="width: 70px"
                     src="${pageContext.request.contextPath}${(empty entity.image ? '/static/images/avatar/default.png' : entity.image)}"
                     alt="avatar">
                <spring:bind path="image">
	                <div class="ms-3 flex-grow-1">
                        <input name="file" class="form-control ${status.error ? 'is-invalid' : ''}"
                               placeholder="Upload image" type="file" accept="image/*"/>
                        <form:errors path="image" cssClass="invalid-feedback"/>
                    </div>
                </spring:bind>
            </div>
        </div>
    </jsp:attribute>
</p:tag-edit>
