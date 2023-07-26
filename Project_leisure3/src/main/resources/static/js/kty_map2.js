  var map;
		    var marker;
		    var selectedElement = null;

		    function initializeMap(latitude, longitude) {
		        var mapContainer = document.getElementById('map');
		        var mapOption = {
		            center: new kakao.maps.LatLng(latitude, longitude), // 지도의 중심좌표
		            level: 3 // 지도의 확대 레벨
		        };

		        map = new kakao.maps.Map(mapContainer, mapOption);

		        var mapTypeControl = new kakao.maps.MapTypeControl();
		        map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

		        var zoomControl = new kakao.maps.ZoomControl();
		        map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

		        var geocoder = new kakao.maps.services.Geocoder();

		        $(document).on('click', '.output-item', function () {
		            $(this).css('display', 'block');

		            var tour = $(this).find('.touristSite').text();
		            var address = $(this).find('.address').text();
		            var tel = $(this).find('.tel').text();
		            var d = $(this).find('.course').text();
		            var home = $(this).find('.homepage').text();

		            if (marker) {
		                marker.setMap(null);
		            }

		            if (address === "대구광역시 중구 일대") {
		                address = '대구광역시 중구 서성로 10';
		            }

		            if (selectedElement !== null) {
		                selectedElement.removeClass('selected');
		            }
		            $(this).addClass('selected');
		            selectedElement = $(this);

		            geocoder.addressSearch(address, function (result, status) {
		                if (status === kakao.maps.services.Status.OK) {
		                    var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
		                    if (marker) {
		                        marker.setMap(null);
		                    }
		                    marker = new kakao.maps.Marker({
		                        map: map,
		                        position: coords,
		                        clickable: true
		                    });

		                    var linkHtml = '';
		                    if (home) {
		                        linkHtml = '<a href="' + home + '" target="_blank" class="link">홈페이지</a>';
		                    }
		                    var tourImage = '/img/tour/' + tour.replace(/\s/g, '') + '.jpg';
		                    var infowindowContent = '<div class="wrap">' +
		                        '    <div class="info">' +
		                        '        <div class="title">' +
		                        tour +
		                        '            <div class="close" onclick="closeOverlay()" title="닫기"></div>' +
		                        '        </div>' +
		                        '        <div class="body">' +
		                        '            <div class="img">' +
		                        '                <img src="' + tourImage + '" width="73" height="70">' +
		                        '           </div>' +
		                        '            <div class="desc">' +
		                        address +
		                        '                <div class="jibun ellipsis">' + tel + '</div>' +
		                        '<a href="https://map.kakao.com/?q=' + address + '" target="_blank" class="link">길찾기  </a>' + linkHtml + '<br>' +
		                        '            </div>' +
		                        '        </div>' +
		                        '    </div>' +
		                        '</div>';
		                    var overlay = new kakao.maps.CustomOverlay({
		                        content: infowindowContent,
		                        map: map,
		                        position: marker.getPosition(),
		                    });

		                    kakao.maps.event.addListener(marker, 'click', function () {
		                        overlay.setMap(map);
		                    });
		                    map.setCenter(coords);
		                }
		            });
		        });

		        function getCurrentPosition() {
		            if ("geolocation" in navigator) {
		                navigator.geolocation.getCurrentPosition(function (position) {
		                    var latitude = position.coords.latitude;
		                    var longitude = position.coords.longitude;

		                    if (marker) {
		                        marker.setMap(null);
		                    }
		                    initializeMap(latitude, longitude);
		                });
		            }
		        }

		        document.getElementById('current_me_position').addEventListener('click', function () {
		            getCurrentPosition();
		        });
		        getCurrentPosition();

		        $(document).on('click', '.close', function () {
		            $('.wrap').css('display', 'none');
		        });

		        function closeOverlay() {
		            $('.wrap').css('display', 'none');
		        }
		    }

		    // Initialize the map with a default location
		    initializeMap(33.451475, 126.570528);