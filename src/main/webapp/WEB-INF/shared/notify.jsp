<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="uiToast" value="${sessionScope['ui.toast']}"/>

<div id="notify-container" class="toast-container position-fixed top-0 end-0 card-body"
     style="z-index: 999; margin-top: 60px" up-hungry>
    <c:if test="${not empty uiToast}">
        <c:remove var="ui.toast" scope="session"/>
        <div class="toast fade" style="background: transparent; border: 0; border-radius: 0"
             aria-live="assertive" aria-atomic="true">
                <%--@elvariable id="uiToast" type="io.lana.libman.support.ui.Toast"--%>
            <div class="alert alert-${uiToast.color} ${uiToast.variant} alert-dismissible fade show">
                <c:if test="${not empty uiToast.icon}">
                    <i data-feather="${uiToast.icon}"></i>
                </c:if>
                <p>${uiToast.message}</p>
                <button class="btn-close" type="button" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </div>
    </c:if>
</div>
