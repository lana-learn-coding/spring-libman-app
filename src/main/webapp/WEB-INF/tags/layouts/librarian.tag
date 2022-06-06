<%@tag description="Page layout" pageEncoding="UTF-8" %>
<%@attribute name="scripts" fragment="true" %>
<%@attribute name="body" fragment="true" %>
<%@attribute name="head" fragment="true" %>
<%@attribute name="title" fragment="true" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" href="/static/images/favicon.png" type="image/x-icon">
    <link rel="shortcut icon" href="/static/images/favicon.png" type="image/x-icon">
    <title>
        <jsp:invoke fragment="title"/>
    </title>
    <!-- Base -->
    <jsp:include page="/WEB-INF/presets/core.head.jsp"/>
    <jsp:include page="/WEB-INF/presets/theme.head.jsp"/>
    <jsp:invoke fragment="head"/>
</head>
<body>
<!-- Loader starts-->
<div class="loader-wrapper">
    <div class="theme-loader">
        <div class="loader-p"></div>
    </div>
</div>
<!-- Loader ends-->
<!-- page-wrapper Start-->
<div class="page-wrapper" id="pageWrapper">
    <jsp:include page="/WEB-INF/shared/header.jsp"/>
    <!-- Page Body Start-->
    <div class="page-body-wrapper horizontal-menu">
        <jsp:include page="/WEB-INF/shared/nav.jsp"/>

        <div class="page-body">
            <jsp:invoke fragment="body"/>
        </div>
        <jsp:include page="/WEB-INF/shared/footer.jsp"/>
    </div>
</div>

<jsp:invoke fragment="scripts"/>
</body>
</html>
