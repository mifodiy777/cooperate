<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
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
            <div class="carousel-caption">
                <a class="btn btn-primary btn-lg" href="garagPage">Список гаражей</a>
            </div>
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
<div class="jumbotron">
    <div class="container">
        <h3>Система автоматизации ГК №23</h3>
    </div>
</div>

<div class="container">
    <div id="rent"></div>
    <div class="row footerMenu">
        <div class="col-md-4">
            <h2>Гаражи</h2>

            <p>Общий список гаражей</p>

            <p><a class="btn btn-primary" href="garagPage" role="button">Перейти »</a></p>
        </div>
        <div class="col-md-4">
            <h2>Владельцы</h2>

            <p>Список членов ГК</p>

            <p><a class="btn btn-primary" href="persons" role="button">Перейти »</a></p>
        </div>
        <div class="col-md-4">
            <h2>Оплата</h2>

            <p>Прием платежей членов ГК</p>

            <p><a class="btn btn-primary" href="payments" role="button">Перейти »</a></p>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</html>
