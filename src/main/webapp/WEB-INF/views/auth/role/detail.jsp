<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="p" tagdir="/WEB-INF/tags/page/role" %>
<%--@elvariable id="authority" type="java.lang.String"--%>
<%--@elvariable id="title" type="java.lang.String"--%>
<%--@elvariable id="entity" type="io.lana.libman.support.data.DescriptiveEntity"--%>
<%--@elvariable id="relations" type="java.util.Collection<io.lana.libman.support.data.DescriptiveEntity>"--%>
<p:role-detail title="${title}" entity="${entity}" relations="${relations}"/>
