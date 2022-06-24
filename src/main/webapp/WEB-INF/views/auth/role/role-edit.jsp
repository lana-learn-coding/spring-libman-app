<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="p" tagdir="/WEB-INF/tags/page/role" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="edit" type="java.lang.Boolean"--%>
<%--@elvariable id="title" type="java.lang.String"--%>
<%--@elvariable id="entity" type="io.lana.libman.core.user.role.Role"--%>
<p:role-edit title="${title}" entity="${entity}" edit="${edit}">
<jsp:attribute name="form">
    <div class="mb-3">
        <label class="col-form-label pt-0" for="permissions">Permission</label>
        <form:select
                data-placeholder="Select Permissions"
                select2="${pageContext.request.contextPath}/authorities/permissions/autocomplete"
                path="permissions" cssClass="form-select"
                cssErrorClass="form-select is-invalid">
            <c:if test="${not empty entity.permissions}">
                 <form:options items="${entity.permissions}" itemValue="id" itemLabel="name" selected="true"/>
            </c:if>
        </form:select>
        <form:errors path="permissions" cssClass="invalid-feedback"/>
    </div>
</jsp:attribute>
</p:role-edit>
