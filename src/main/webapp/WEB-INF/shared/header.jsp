<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="page-main-header">
    <div class="main-header-right row m-0">
        <div class="main-header-left">
            <div class="logo-wrapper"><a href="index.tag"><img class="img-fluid" src="/static/images/logo/logo.png"
                                                               alt=""></a></div>
            <div class="dark-logo-wrapper"><a href="index.tag"><img class="img-fluid"
                                                                    src="/static/images/logo/dark-logo.png" alt=""></a>
            </div>
            <div class="toggle-sidebar"><i class="status_toggle middle" data-feather="align-center"
                                           id="sidebar-toggle"></i></div>
        </div>
        <div class="left-menu-header col">
            <ul>
                <li>
                    <form class="form-inline search-form">
                        <div class="search-bg"><i class="fa fa-search"></i>
                            <input class="form-control-plaintext" placeholder="Search here.....">
                        </div>
                    </form>
                    <span class="d-sm-none mobile-search search-bg"><i class="fa fa-search"></i></span>
                </li>
            </ul>
        </div>
        <div class="nav-right col pull-right right-menu p-0">
            <ul class="nav-menus">
                <li class="onhover-dropdown">
                    <div class="bookmark-box"><i data-feather="star"></i></div>
                    <div class="bookmark-dropdown onhover-show-div">
                        <div class="form-group mb-0">
                            <div class="input-group">
                                <div class="input-group-prepend"><span class="input-group-text"><i
                                        class="fa fa-search"></i></span></div>
                                <input class="form-control" type="text" placeholder="Search for bookmark...">
                            </div>
                        </div>
                        <ul class="m-t-5">
                            <li class="add-to-bookmark"><i class="bookmark-icon" data-feather="inbox"></i>Email<span
                                    class="pull-right"><i data-feather="star"></i></span></li>
                            <li class="add-to-bookmark"><i class="bookmark-icon"
                                                           data-feather="message-square"></i>Chat<span
                                    class="pull-right"><i data-feather="star"></i></span></li>
                            <li class="add-to-bookmark"><i class="bookmark-icon" data-feather="command"></i>Feather
                                Icon<span class="pull-right"><i data-feather="star"></i></span></li>
                            <li class="add-to-bookmark"><i class="bookmark-icon"
                                                           data-feather="airplay"></i>Widgets<span
                                    class="pull-right"><i data-feather="star">   </i></span></li>
                        </ul>
                    </div>
                </li>
                <li class="onhover-dropdown">
                    <div class="notification-box"><i data-feather="bell"></i><span class="dot-animated"></span>
                    </div>
                    <ul class="notification-dropdown onhover-show-div">
                        <li>
                            <p class="f-w-700 mb-0">You have 3 Notifications<span
                                    class="pull-right badge badge-primary badge-pill">4</span></p>
                        </li>
                        <li class="noti-primary">
                            <div class="media"><span class="notification-bg bg-light-primary"><i
                                    data-feather="activity"> </i></span>
                                <div class="media-body">
                                    <p>Delivery processing </p><span>10 minutes ago</span>
                                </div>
                            </div>
                        </li>
                        <li class="noti-secondary">
                            <div class="media"><span class="notification-bg bg-light-secondary"><i
                                    data-feather="check-circle"> </i></span>
                                <div class="media-body">
                                    <p>Order Complete</p><span>1 hour ago</span>
                                </div>
                            </div>
                        </li>
                        <li class="noti-success">
                            <div class="media"><span class="notification-bg bg-light-success"><i
                                    data-feather="file-text"> </i></span>
                                <div class="media-body">
                                    <p>Tickets Generated</p><span>3 hour ago</span>
                                </div>
                            </div>
                        </li>
                        <li class="noti-danger">
                            <div class="media"><span class="notification-bg bg-light-danger"><i
                                    data-feather="user-check"> </i></span>
                                <div class="media-body">
                                    <p>Delivery Complete</p><span>6 hour ago</span>
                                </div>
                            </div>
                        </li>
                    </ul>
                </li>
                <li>
                    <div class="mode"><i class="fa fa-moon-o"></i></div>
                </li>
                <li class="onhover-dropdown"><i data-feather="message-square"></i>
                    <ul class="chat-dropdown onhover-show-div">
                        <li>
                            <div class="media"><img class="img-fluid rounded-circle me-3"
                                                    src="/static/images/user/4.jpg" alt="">
                                <div class="media-body"><span>Ain Chavez</span>
                                    <p class="f-12 light-font">Lorem Ipsum is simply dummy...</p>
                                </div>
                                <p class="f-12">32 mins ago</p>
                            </div>
                        </li>
                        <li>
                            <div class="media"><img class="img-fluid rounded-circle me-3"
                                                    src="/static/images/user/1.jpg" alt="">
                                <div class="media-body"><span>Erica Hughes</span>
                                    <p class="f-12 light-font">Lorem Ipsum is simply dummy...</p>
                                </div>
                                <p class="f-12">58 mins ago</p>
                            </div>
                        </li>
                        <li>
                            <div class="media"><img class="img-fluid rounded-circle me-3"
                                                    src="/static/images/user/2.jpg" alt="">
                                <div class="media-body"><span>Kori Thomas</span>
                                    <p class="f-12 light-font">Lorem Ipsum is simply dummy...</p>
                                </div>
                                <p class="f-12">1 hr ago</p>
                            </div>
                        </li>
                        <li class="text-center"><a class="f-w-700" href="javascript:void(0)">See All </a></li>
                    </ul>
                </li>
                <li class="onhover-dropdown p-0">
                    <sec:authorize access="!isAuthenticated()">
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/login">
                            Login
                        </a>
                    </sec:authorize>
                    <sec:authorize access="isAuthenticated()">
                        <button class="btn btn-primary-light"
                                type="button"
                                data-bs-toggle="modal"
                                data-bs-target="#logout-modal">
                            <i data-feather="log-out"></i> Log out
                        </button>
                    </sec:authorize>
                </li>
            </ul>
        </div>
        <div class="d-lg-none mobile-toggle pull-right w-auto"><i data-feather="more-horizontal"></i></div>
    </div>
</div>
<sec:authorize access="isAuthenticated()">
    <div class="modal fade" tabindex="-1" id="logout-modal" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <form class="modal-content" action="${pageContext.request.contextPath}/logout"
                  method="post">
                <sec:csrfInput/>
                <div class="modal-body d-flex flex-column align-items-center pt-4 pb-3">
                    <i data-feather="info" class="txt-info" style="width: 100px; height: 100px; stroke-width: 1"></i>
                    <h3 class="f-w-600 mt-3">Log Out</h3>
                    <span>You are about to logout</span>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-light" data-bs-dismiss="modal">back</button>
                    <button type="submit" class="btn btn-primary">Log out</button>
                </div>
            </form>
        </div>
    </div>
</sec:authorize>
