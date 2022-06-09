<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%-- Font --%>
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&amp;display=swap"
      rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&amp;display=swap"
      rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Rubik:ital,wght@0,400;0,500;0,600;0,700;0,800;0,900;1,300;1,400;1,500;1,600;1,700;1,800;1,900&amp;display=swap"
      rel="stylesheet">

<%-- Core --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/unpoly.min.css">
<script src="${pageContext.request.contextPath}/static/js/core/jquery-3.5.1.min.js" defer></script>
<script src="${pageContext.request.contextPath}/static/js/core/popper.min.js" defer></script>
<script src="${pageContext.request.contextPath}/static/js/core/bootstrap.min.js" defer></script>
<script src="${pageContext.request.contextPath}/static/js/core/unpoly.min.js" defer></script>
<script src="${pageContext.request.contextPath}/static/js/core/unpoly-bootstrap5.min.js" defer></script>

<%-- App styles and colors --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.min.css">
<sec:authorize access="!hasAuthority('LIBRARIAN')" var="isAdmin">
    <link id="color" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/color-1.min.css"
          media="screen">
</sec:authorize>
<sec:authorize access="isAuthenticated() && !hasAuthority('ADMIN')" var="isAdmin">
    <link id="color" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/color-4.min.css"
          media="screen">
</sec:authorize>
<sec:authorize access="isAuthenticated() && hasAuthority('ADMIN')" var="isAdmin">
    <link id="color" rel="stylesheet" href="${pageContext.request.contextPath}/static/css/color-2.min.css"
          media="screen">
</sec:authorize>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/responsive.min.css">

<%-- Icon --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/fontawesome.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/icofont.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/themify.min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/feather-icon.min.css">
<script src="${pageContext.request.contextPath}/static/js/icons/feather.min.js" defer></script>
<script src="${pageContext.request.contextPath}/static/js/icons/feather-icon.js" defer></script>

<script>
    // Defer script call hack
    const queue = window._deferQueue || [];
    window.defer = (func) => {
        queue.push(func);
    }
    document.addEventListener('DOMContentLoaded', () => {
        while (queue.length) queue.shift().call();
    });
</script>
