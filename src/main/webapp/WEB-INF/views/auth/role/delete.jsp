<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%--@elvariable id="title" type="java.lang.String"--%>
<%--@elvariable id="id" type="java.lang.String"--%>
<%--@elvariable id="entity" type="io.lana.libman.core.tag.Named"--%>
<c:set var="uri" value="${title.toLowerCase()}"/>
<layout:modal>
    <jsp:attribute name="title">Delete ${title}</jsp:attribute>
    <jsp:attribute name="body">
        <div class="modal d-block">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-body d-flex flex-column align-items-center pt-4 pb-3">
                        <i data-feather="alert-triangle" class="txt-warning"
                           style="width: 100px; height: 100px; stroke-width: 1"></i>
                        <h3 class="f-w-600 mt-3">${entity.name}</h3>
                        <span>Are you sure delete this ${title}</span>
                    </div>
                    <div class="modal-footer">
                        <a href="${pageContext.request.contextPath}/authorities/${uri}"
                           class="btn btn-light" up-dismiss up-follow>Back</a>
                        <form action="${pageContext.request.contextPath}/authorities/${uri}/${id}/delete"
                              method="post" up-layer="parent root">
                            <sec:csrfInput/>
                            <button type="submit" class="btn btn-warning">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</layout:modal>
