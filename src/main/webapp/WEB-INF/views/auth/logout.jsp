<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<layout:modal>
    <jsp:attribute name="title">Logout</jsp:attribute>
    <jsp:attribute name="body">
        <div class="modal d-block">
            <div class="modal-dialog modal-dialog-centered">
                <form class="modal-content" action="${pageContext.request.contextPath}/logout"
                      method="post">
                    <sec:csrfInput/>
                    <div class="modal-body d-flex flex-column align-items-center pt-4 pb-3">
                        <i data-feather="info" class="txt-info"
                           style="width: 100px; height: 100px; stroke-width: 1"></i>
                        <h3 class="f-w-600 mt-3">Log Out</h3>
                        <span>You are about to logout</span>
                    </div>
                    <div class="modal-footer">
                        <a href="${pageContext.request.contextPath}/" class="btn btn-light" up-dismiss>Back</a>
                        <button type="submit" class="btn btn-primary">Log out</button>
                    </div>
                </form>
            </div>
        </div>
    </jsp:attribute>
</layout:modal>
