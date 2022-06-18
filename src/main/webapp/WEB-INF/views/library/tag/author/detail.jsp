<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="p" tagdir="/WEB-INF/tags/page/tag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="helper" tagdir="/WEB-INF/tags/helpers" %>
<%--@elvariable id="authority" type="java.lang.String"--%>
<%--@elvariable id="title" type="java.lang.String"--%>
<%--@elvariable id="data" type="org.springframework.data.domain.Page<io.lana.libman.support.data.AuditableEntity>"--%>
<%--@elvariable id="entity" type="io.lana.libman.core.tag.Author"--%>
<p:tag-detail title="${title}" data="${data}" entity="${entity}">
    <jsp:attribute name="detail">
    <div class="card-body pt-1">
        <div class="mb-4 text-muted">
            <div>
                <i class="fa fa-calendar-o"></i>
                <span class="ms-1"><helper:format-datetime date="${entity.updatedAt}"/></span>
            </div>
            <c:if test="${not empty entity.updatedBy}">
                <div class="ms-2 d-none d-md-block">
                    <i class="fa fa-user"></i>
                    <span class="ms-1">${entity.updatedBy}</span>
                </div>
            </c:if>
        </div>

        <div class="d-flex flex-column flex-sm-row">
            <div class="me-3 me-md-4 d-flex justify-content-center justify-content-sm-start align-items-baseline">
                <img class="rounded-circle" style="width: 110px"
                     src="${pageContext.request.contextPath}${(empty entity.image ? '/static/images/avatar/default.png' : entity.image)}"
                     alt="avatar">
            </div>
            <div class="mt-2">
                <div class="mb-1">Name <span class="font-primary ms-2">${entity.name}</span></div>

                <c:if test="${empty entity.dateOfDeath && empty entity.dateOfBirth}">
                    <div class="mb-1">Born - Died <span class="font-primary ms-2">Unknown</span></div>
                </c:if>
                <c:if test="${not empty entity.dateOfDeath && not empty entity.dateOfBirth}">
                <div class="mb-1">Born - Died
                    <span class="font-primary ms-2 me-1">
                        <helper:format-date date="${entity.dateOfBirth}"/>
                    </span>
                    -
                    <span class="font-primary m1-2">
                        <helper:format-date date="${entity.dateOfDeath}"/>
                    </span>
                </div>
                 </c:if>
                <c:if test="${not empty entity.dateOfBirth && empty entity.dateOfDeath}">
                <div class="mb-1">Born
                    <span class="font-primary ms-2 me-1">
                        <helper:format-date date="${entity.dateOfBirth}"/>
                    </span>
                </div>
                </c:if>
                <c:if test="${empty entity.dateOfBirth && not empty entity.dateOfDeath}">
                    <div class="mb-1">Died
                        <span class="font-primary ms-2 me-1">
                        <helper:format-date date="${entity.dateOfDeath}"/>
                        </span>
                    </div>
                </c:if>
                <div class="mb-1">Books <span class="font-primary ms-2">${entity.booksCount}</span></div>
                <c:if test="${not empty entity.about}">
                    <div class="mb-1">About <span class="font-primary ms-2">${entity.about}</span></div>
                </c:if>
            </div>
        </div>
    </div>
    </jsp:attribute>
</p:tag-detail>
