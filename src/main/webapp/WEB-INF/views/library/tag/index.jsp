<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="p" tagdir="/WEB-INF/tags/page/tag" %>
<jsp:useBean id="data" scope="request"
             type="org.springframework.data.domain.Page<io.lana.libman.core.tag.TaggedEntity>"/>
<jsp:useBean id="authority" scope="request" type="java.lang.String"/>
<jsp:useBean id="title" scope="request" type="java.lang.String"/>

<p:tag-index authority="${authority}" title="${title}" data="${data}"/>
