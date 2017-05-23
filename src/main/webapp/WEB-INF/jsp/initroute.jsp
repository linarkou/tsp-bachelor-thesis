<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <script src="http://api-maps.yandex.ru/2.1/?load=package.full&lang=ru-RU" type="text/javascript"></script>
        
        <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
        
        <title>Ваш маршрут</title>
        
        <script type="text/javascript">
            var lengths = [];
            function getPaths() {
            ymaps.ready(function() {
                var myMap = new ymaps.Map ('myMap', {
                    center: [0, 0],
                    zoom: 11,
                });

                ymaps.route([${route}], {
                    mapStateAutoApply: true
                }).then(function (route) {
                    myMap.geoObjects.add(route);

                    route.getPaths().each(function (path) {
                        lengths.push(path.properties.getAll().RouterRouteMetaData.length);
                    
                    document.getElementById("lengths").value = lengths;
                    document.forms['kostil'].submit();
                    });
                }, function (err) {
                    alert('Попробуйте зайти позже');
                }, this);
            });
            }
        </script>
    </head>
    <body onload='getPaths()'>
        <div class="loading">
            Идет построение маршрута<br>Подождите пожалуйста...
        </div>
        <form method='post' action="${contextPath}/route" id="kostil">
            <input type="hidden" id="lengths" name="lengths" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        </script>
        <input type="hidden" id="myMap" style="width: 100px; height: 100px;"/>
    </body>
</html>


<!--$.ajax({
                    type:'post',
                    url:'${pageContext.request.contextPath}/route.do',
                    data: JSON.stringify({ lengths: lengths }),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function(data){alert(data);},
                    failure: function(errMsg) {
                        alert(errMsg);
                    }
                });-->

<!--var lengths = [];
        ymaps.ready();
        function initMap()
        {     
            var myMap = new ymaps.Map ('myMap', {
                center: [0, 0],
                zoom: 11,
                controls: ["default"]
            });
            
            ymaps.route([${route}], {
                mapStateAutoApply: true
            }).then(function (route) {
                myMap.geoObjects.add(route);
                
                route.getPaths().each(function (path) {
                    lengths.push(path.properties.getAll().RouterRouteMetaData.length);
                });
            }, function (err) {
                alert('Попробуйте зайти позже');
            }, this);
        }-->