 $.getJSON('https://api.openweathermap.org/data/2.5/weather?q=Daegu&appid=46f3bb4ee2063d7abd826c2a6ebbfd0d&units=metric', function (result) {
            $('.weather-icon').html('<img src="https://openweathermap.org/img/wn/' + result.weather[0].icon + '.png" alt="' + result.weather[0].description + '">');
            $('.weather-temp').text(result.main.temp + '°C');
            $('.lowtemp').text(result.main.temp_min);
            $('.hightemp').text(result.main.temp_max);
        });

        $('.weather-temp').on('click', function () {
            $('.weather-details').toggle();
        });