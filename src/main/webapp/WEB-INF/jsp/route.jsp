<!--<%@page contentType="text/html" pageEncoding="UTF-8"%>-->
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">        
        <script src="http://api-maps.yandex.ru/2.1/?load=package.full&lang=ru-RU" type="text/javascript"></script>
        <title>Ваш маршрут</title>
    </head>
    <body>
        <h1>${name}</h1>
        <input id="routelength">
        <script type="text/javascript">
        ymaps.ready(initMap);
        
        function initMap()
        {     
            var myMap = new ymaps.Map ('myMap', {
                center: [0, 0],
                zoom: 11,
                controls: ["default"]
            });
            
            ymaps.route([${route}]).then(function (route) {
                route.options.set("mapStateAutoApply", true);
                myMap.geoObjects.add(route);
                document.getElementById("routelength").value = route.getLength();
            }, function (err) {
                alert('Невозможно построить маршрут');
            }, this);
        }
        
        
        </script>
        <div id="myMap" style="width: 700px; height: 700px;"></div>
    </body>
</html>
