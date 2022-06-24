<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="p" tagdir="/WEB-INF/tags/page/role" %>
<%--@elvariable id="authority" type="java.lang.String"--%>
<%--@elvariable id="title" type="java.lang.String"--%>
<%--@elvariable id="data" type="org.springframework.data.domain.Page<io.lana.libman.core.tag.DescriptiveEntity>"--%>
<%--@elvariable id="highlight" type="java.lang.String"--%>
<p:role-index authority="${authority}" title="${title}" data="${data}" highlight="${highlight}"/>
