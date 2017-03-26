<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>
<header id="myCarousel" class="carousel slide">
    <!-- Indicators -->
    <ol class="carousel-indicators">
        <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
    </ol>
    <!-- Wrapper for Slides -->
    <div class="carousel-inner">
        <div class="item active">
            <!-- Set the first background image using inline CSS below. -->
            <div class="fill" style="background-image:url('images/background.jpg');
            background-size: 100%; background-repeat: no-repeat"></div>
        </div>
    </div>

    <!-- Controls -->
    <a class="left carousel-control" href="#myCarousel" data-slide="prev">
        <span class="icon-prev"></span>
    </a>
    <a class="right carousel-control" href="#myCarousel" data-slide="next">
        <span class="icon-next"></span>
    </a>
</header>
<div class="container-fluid">
    <br>

    <div class="row footerMenu">
        <div class="col-md-4">
            <div class="thumbnail openPage" onclick="openPage('garagPage')">
                <img src="<c:url value="/images/garag.png"/>" alt="Гаражи">

                <div class="caption">
                    <h3>Гаражи</h3>
                    <p>Общий список гаражей</p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="thumbnail openPage" onclick="openPage('persons')">
                <img src="<c:url value="/images/man.png"/>" alt="Владельцы">

                <div class="caption">
                    <h3>Владельцы</h3>
                    <p>Список членов ГК</p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="thumbnail openPage" onclick="openPage('paymentsPage')">
                <img src="<c:url value="/images/pay.png"/>" alt="Платежи">
                <div class="caption">
                    <h3>Платежи</h3>
                    <p>Список платежей членов ГК</p>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</html>
