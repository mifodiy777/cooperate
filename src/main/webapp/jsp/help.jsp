<%--
  Created by IntelliJ IDEA.
  User: KuzminKA
  Date: 30.03.2016
  Time: 14:03:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>
<div class="container-fluid">
<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_1">
        <h4 class="panel-title">
            <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse_1"
               aria-expanded="true" aria-controls="collapse_1">
                Возможности
            </a>
        </h4>
    </div>
    <div id="collapse_1" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="heading_1">
        <div class="panel-body">
            <h4>ПО «Кооператив»</h4>
            <ul>
                <li>Хранение информации о гаражах и их владельцах;</li>
                <li>Проведение оплат по взносам членов кооператива;</li>
                <li>Хранение информации о долгах;</li>
                <li>Автоматическое начисление пеней;</li>
                <li>Формирование отчетов:
                    <ul>
                        <li>Общий список членов;</li>
                        <li>Список льготников;</li>
                        <li>Список должников;</li>
                        <li>Список платежей по выбранному диапазону дат;</li>
                        <li>Общий отчет для ревизионной комиссии;</li>
                    </ul>
                </li>
                <li>Хранение истории всех действий в программе;</li>
                <li>Хранение списка членов правления;</li>
            </ul>

            <p><b>При появление ошибок и вопросов свяжитесь с администратором системы</b></p>

            <p>Администратор: Кузьмин Кирилл Андреевич</p>

            <p><a href="mailto:mifodiy777@gmail.com?subject=Вопрос по Кооперативу">Задавайте
                вопросы по электронной почте</a></p>

            <p><a href="tel:+79379678666">+7(937)-967-86-66</a></p>

        </div>
    </div>
</div>

<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_2">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_2" aria-expanded="false" aria-controls="collapse_2">
                Добавление нового гаража с новым владельцем
            </a>
        </h4>
    </div>
    <div id="collapse_2" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_2">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Гаражи»</li>
                <li>Нажмите кнопку «Добавить гараж»</li>
                <li>Заполните все поля отмеченные звездочкой</li>
                <li>Номер гаража должен быть уникальным</li>
                <li>Внесите данные в остальные поля(не обязательно)</li>
                <li>Нажмите кнопку «Сохранить»</li>
                <li>При успешном выполнении появиться надпись «Гараж сохранен»</li>
            </ol>
            <div class="imgHelpDiv">
                <img src='<c:url value="/images/manual/addGarag.png"/>' alt="Добавление гаража"
                     class="img_help">
            </div>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_3">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_3" aria-expanded="false" aria-controls="collapse_3">
                Добавление нового гаража с владельцем имеющегося в базе
            </a>
        </h4>
    </div>
    <div id="collapse_3" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_3">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Гаражи»</li>
                <li>Нажмите кнопку «Добавить гараж»</li>
                <li>Заполните все поля отмеченные звездочкой</li>
                <li>Номер гаража должен быть уникальным</li>
                <li>Нажмите на кнопку «Найти владельца»</li>
                <li>Впишите ФИО владельца имеющегося в базе<i>(минимум 3 символа)</i></li>
                <li>Нажмите на кнопку «Найти»</li>
                <li>При появление списка владельцев нажмите на ФИО необходимого</li>
                <li>В поля раздела «Владелец» должны автоматически вписаться данные выбранного владельца</li>
                <li>Нажмите кнопку «Сохранить»</li>
                <li>При успешном выполнении появиться надпись «Гараж сохранен»</li>
            </ol>
            <div class="imgHelpDiv">
                <img src='<c:url value="/images/manual/changePersonInGarag.png"/>'
                     alt="Добавление нового гаража с владельцем имеющегося в базе"
                     class="img_help">
            </div>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_4">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_4" aria-expanded="false" aria-controls="collapse_4">
                Добавление нового владельца
            </a>
        </h4>
    </div>
    <div id="collapse_4" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_4">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Владельцы»</li>
                <li>Нажмите кнопку «Добавить владельца»</li>
                <li>Заполните все поля отмеченные звездочкой</li>
                <li>Внесите данные в остальные поля<i>(не обязательно)</i></li>
                <li>Нажмите кнопку «Сохранить»</li>
                <li>При успешном выполнении появиться надпись «Владелец сохранен»</li>
            </ol>
            <div class="imgHelpDiv">
                <img src='<c:url value="/images/manual/addPerson.png"/>' alt="Добавление нового владельца"
                     class="img_help">
            </div>
        </div>
    </div>
</div>

<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_6">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_6" aria-expanded="false" aria-controls="collapse_6">
                Редактирование гаража
            </a>
        </h4>
    </div>
    <div id="collapse_6" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_6">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Гаражи»</li>
                <li>Найдите в таблице гараж</li>
                <li>Нажмите на номер гаража</li>
                <li>Внесите изменения
                    <p style="color:red;"><b>Важно! При изменении данных у владельца, данные изменятся у всех гаражей
                        этого
                        владельца</b></p>
                </li>
                <li>Нажмите кнопку «Сохранить»</li>
                <li>При успешном выполнении появиться надпись «Гараж сохранен»</li>
            </ol>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_7">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_7" aria-expanded="false" aria-controls="collapse_7">
                Смена владельца
            </a>
        </h4>
    </div>
    <div id="collapse_7" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_7">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Гаражи»</li>
                <li>Найдите в таблице гараж</li>
                <li>Нажмите на кнопку <img src='<c:url value="/images/manual/changeBtn.png"/>'
                                           alt="Смена владельца"></li>
                </li>
                <li>Выберите режимы смены владельца</li>
                <li><b>Внимательно читайте описание</b></li>
                <li>Нажмите кнопку «Сохранить»</li>
                <li>При успешном выполнении появиться надпись «Владелец заменен!»</li>
            </ol>
            <div class="imgHelpDiv">
                <img src='<c:url value="/images/manual/changePerson.png"/>'
                     alt="Смена владельца"
                     class="img_help">
            </div>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_12">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_12" aria-expanded="false" aria-controls="collapse_12">
                Удаление владельца
            </a>
        </h4>
    </div>
    <div id="collapse_12" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_12">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Владельцы»</li>
                <li>Найдите необходимого владельца, там же можно проверить наличие назначений у этого владельца</li>
                <li>Нажмите кнопку «Урна»</li>
                <li>Владелец удалится
                    <p style="color:red;"><b>Важно! Если у этого владельца были гаражи, то у них просто удалится
                        владелец, сами гаражи останутся</b></p>

                </li>
            </ol>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_13">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_13" aria-expanded="false" aria-controls="collapse_13">
                Удаление гаража
            </a>
        </h4>
    </div>
    <div id="collapse_13" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_13">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Гаражи»</li>
                <li>Найдите в таблице гараж</li>
                <li>Нажмите на кнопку «Урана»</li>
                <li>Подтвердите действие</li>
                <li>Гараж удалится, Вы увидите сообщение об успешном удаление</li>
                <p style="color:red;"><b>Владелец при этом сохранится, у него просто удалится назначение</b></p>

                <p style="color:red;"><b>Удалятся все долги и платежи этого гаража</b></p>
            </ol>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_14">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_14" aria-expanded="false" aria-controls="collapse_14">
                Внесение долгов гаражу имеющего владельца
            </a>
        </h4>
    </div>
    <div id="collapse_14" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_14">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Гаражи»</li>
                <li>Найдите в таблице гараж</li>
                <li>Нажмите на номер гаража</li>
                <li>Если вы Администратор вы увидите раздел «Внесите старые долги»</li>
                <li>Выберите необходимый год
                    <i>"Года появляются в зависимости от количества периодов,
                        если период не создан – этого года в списке не будет"</i>.
                </li>
                <li>В появившемся окне внесите данные по долгу не превышая максимальных значений взносов для выбранного
                    периода
                </li>
                <li>При установки галочки «Льготный период» максимально значение за Аренду делиться пополам</li>
                <li>При нажатии на «Член правления» долг по членскому взносу обнуляется, макс значение взноса
                    обнуляется
                </li>
                <li>При нажатие на «Включить пени» программа начнет насчитывать пени по введенным правилам с даты
                    указанную в поле «Дата обновления пеней»
                </li>
                <li>При нажатии на кнопку «Полностью Максимальные значения переносятся в поля напротив, пени включаются,
                    Дата устанавливается на 01.07 текущего года
                </li>
                <li>При нажатии на «Ёлку» включаются пени, а дата устанавливается на 01.01. следующего года после
                    текущего
                </li>
                <li>После внесения всех параметров нажмите «Сохранить»</li>
                <li>Если у человека есть долги не облагаемые пенями, внесите их в поле «Долги прошлых лет» в разделе
                    «Гараж»
                </li>
                <li>При сохранение гаража пени пересчитываются автоматически</li>
            </ol>
            <div class="imgHelpDiv">
                <img src='<c:url value="/images/manual/editContribute.png"/>'
                     alt="Внесение долгов гаражу имеющего владельца"
                     class="img_help">
            </div>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_15">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_15" aria-expanded="false" aria-controls="collapse_15">
                Получение информации по гаражу
            </a>
        </h4>
    </div>
    <div id="collapse_15" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_15">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Гаражи»</li>
                <li>Найдите в таблице гараж</li>
                <li>Нажмите на кнопку <img src='<c:url value="/images/manual/infGaragBtn.png"/>'
                                           alt="Информации по гаражу"></li>
            </ol>
            <div class="imgHelpDiv">
                <img src='<c:url value="/images/manual/infGaragPanel.png"/>' alt="Получение информации по гаражу"
                     class="img_help">
            </div>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_16">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_16" aria-expanded="false" aria-controls="collapse_16">
                Проведение оплаты по гаражу
            </a>
        </h4>
    </div>
    <div id="collapse_16" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_16">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Гаражи»</li>
                <li>Найдите в таблице гараж</li>
                <li>Нажмите на кнопку <img src='<c:url value="/images/manual/infGaragBtn.png"/>'
                                           alt="Информации по гаражу"></li>
                <li>Нажмите на кнопку "Оплатить"</li>
                <li>Впишите сумму не менее 100 руб.</li>
                <li>Нажмите «Оплатить»</li>
                <li>Появится чек(отдельной вкладкой),распечатайте его «Ctrl + P»</li>
                <li>Закройте вкладку</li>
            </ol>
            <div class="imgHelpDiv">
                <img src='<c:url value="/images/manual/payPanel.png"/>' alt="Проведение оплаты по гаражу"
                     class="img_help">
            </div>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_17">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_17" aria-expanded="false" aria-controls="collapse_17">
                Оплата дополнительных взносов
            </a>
        </h4>
    </div>
    <div id="collapse_17" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_17">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Гаражи»</li>
                <li>Найдите в таблице гараж</li>
                <li>Нажмите на кнопку <img src='<c:url value="/images/manual/infGaragBtn.png"/>'
                                           alt="Информации по гаражу"></li>
                <li>Нажмите на кнопку «Дополнительный взнос»</li>
                <li>Введите сумму</li>
                <li>Появится чек(отдельной вкладкой), распечатайте его «Ctrl + P»</li>
                <li>Закройте вкладку</li>
            </ol>           
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_18">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_18" aria-expanded="false" aria-controls="collapse_18">
                Удаление чека
            </a>
        </h4>
    </div>
    <div id="collapse_18" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_18">
        <div class="panel-body">
            <ol>
                <li>Восстановите долги у гаража у которого хотите удалит чек,
                    данные по сумме возьмите из удаляемого чека
                </li>
                <li>Нажмите на вкладку «Оплата»</li>
                <li>Найдите в таблице необходимую платежку</li>
                <li>Нажмите на «Урну»</li>
                <li>Ответьте «Да»</li>
                <li>Появится сообщение об успешном удалении</li>
            </ol>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_19">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_19" aria-expanded="false" aria-controls="collapse_19">
                Формирование общего списка членов ГК
            </a>
        </h4>
    </div>
    <div id="collapse_19" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_19">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Отчеты»</li>
                <li>«Общий список»</li>
            </ol>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_20">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_20" aria-expanded="false" aria-controls="collapse_20">
                Формирование списка льготников
            </a>
        </h4>
    </div>
    <div id="collapse_20" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_20">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Отчеты»</li>
                <li>«Список должников»</li>
            </ol>

        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_21">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_21" aria-expanded="false" aria-controls="collapse_21">
                Формирование списка должников
            </a>
        </h4>
    </div>
    <div id="collapse_21" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_21">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Отчеты»</li>
                <li>«Список должников»</li>
            </ol>

        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_22">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_22" aria-expanded="false" aria-controls="collapse_22">
                Отчет по платежам за период
            </a>
        </h4>
    </div>
    <div id="collapse_22" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_22">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Отчеты»</li>
                <li>«Дополнительные отчеты»</li>
                <li>Отчет по платежам</li>
                <li>Выберите диапазон дат</li>
                <li>Подготовить</li>
            </ol>

        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_23">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_23" aria-expanded="false" aria-controls="collapse_23">
                Отчет по доходам, начислениям и долгам за период
            </a>
        </h4>
    </div>
    <div id="collapse_23" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_23">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Отчеты»</li>
                <li>«Дополнительные отчеты»</li>
                <li>Отчет по доходам</li>
                <li>Выберите диапазон дат</li>
                <li>По первой дате определяется период начисления</li>
                <li>Даты должны быть в разных годах</li>
                <li>Подготовить</li>
            </ol>

        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_24">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_24" aria-expanded="false" aria-controls="collapse_24">
                Создание нового периода
            </a>
        </h4>
    </div>
    <div id="collapse_24" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_24">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Администрирование»</li>
                <li>«Создать новый период»</li>
                <li>Год устанавливается текущий</li>
                <li>Впишите данные
                    <p style="color:red;"><b>Очень важно!!! Данные параметры не подлежат дальнейшему редактированию.
                        Будьте
                        внимательны! </b></p></li>
                <li>«Сохранить»</li>
                <li>Нажмите кнопку «Сохранить»</li>
                <li>При успешном выполнении появиться надпись «Гараж сохранен»</li>
            </ol>

        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_25">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_25" aria-expanded="false" aria-controls="collapse_25">
                Просмотр истории операций
            </a>
        </h4>
    </div>
    <div id="collapse_25" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_25">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Администрирование»</li>
                <li>«История»
                    <p style="color:red;"><b>Важно! Хранится в течении года</b></p></li>

            </ol>

        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_26">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_26" aria-expanded="false" aria-controls="collapse_26">
                Просмотр списка членов правления
            </a>
        </h4>
    </div>
    <div id="collapse_26" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_26">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Администрирование»</li>
                <li>«Члены правления»</li>
                <li>Можно редактировать владельцев при нажатии на ФИО</li>
            </ol>
            <p style="color:red;"><b>Необязательно член ГК</b></p>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_27">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_27" aria-expanded="false" aria-controls="collapse_27">
                Внесение прошлых периодов начисления
            </a>
        </h4>
    </div>
    <div id="collapse_27" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_27">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Администрирование»</li>
                <li>«Внести прошлые начисления»</li>
                <li>Впишите данные</li>
                <li>Нажмите кнопку «Сохранить»</li>
                <li>При успешном выполнении появиться надпись «Период сохранен»</li>
            </ol>
            <p style="color:red;"><b>Очень важно!!! Данные параметры не подлежат дальнейшему редактированию. Будьте
                внимательны!</b></p>

        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_28">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_28" aria-expanded="false" aria-controls="collapse_28">
                Обновление пеней
            </a>
        </h4>
    </div>
    <div id="collapse_28" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_28">
        <div class="panel-body">
            <ol>
                <li>Нажмите на вкладку «Обновить»</li>
            </ol>
            <p style="color:red;"><b>Пени пересчитываться автоматически при первом запуске программы
                Пени автоматически пересчитываются при сохранении гаража
            </b></p>

        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" role="tab" id="heading_29">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion"
               href="#collapse_29"
               aria-expanded="false" aria-controls="collapse_29">
                Требование к системе
            </a>
        </h4>
    </div>
    <div id="collapse_29" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading_29">
        <div class="panel-body">
            <ol>
                <li>Операционная система Microsoft Windows;</li>
                <li>Виртуальная машина java от версии 7 <a href="https://www.java.com/ru/download/">Перейти</a>
                </li>
                <li>Microsoft Excel (начиная с версии 2003);</li>
                <li>Сервер базы данных MySQL <a href="http://dev.mysql.com/downloads/mysql/">Перейти</a></li>
                <li>Контейнер приложений Apache Tomcat от версии 7 <a
                        href="http://tomcat.apache.org/download-70.cgi">Перейти</a></li>
            </ol>
        </div>
    </div>
</div>
</div>
<jsp:include page="footer.jsp"/>