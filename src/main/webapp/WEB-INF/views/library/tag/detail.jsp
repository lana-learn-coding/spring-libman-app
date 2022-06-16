<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="p" tagdir="/WEB-INF/tags/page/tag" %>
<%--@elvariable id="authority" type="java.lang.String"--%>
<%--@elvariable id="title" type="java.lang.String"--%>
<%--@elvariable id="data" type="org.springframework.data.domain.Page<io.lana.libman.support.data.AuditableEntity>"--%>
<%--@elvariable id="entity" type="io.lana.libman.core.tag.TaggedEntity"--%>
<p:tag-detail title="${title}" data="${data}" entity="${entity}"/>
