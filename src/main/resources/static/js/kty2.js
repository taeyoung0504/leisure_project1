fetchData();

			function fetchData() {
				var myurl = 'https://api.odcloud.kr/api/15087234/v1/uddi:927f8b4b-0241-4461-9924-f8dfffbe5394?page=1&perPage=36&serviceKey=OzZTUnMJGM1btfj8YuaBUFh5ZYDn85xrzW1n3S82xZEHM5pZI7D%2FVYzTXPfmQtxC5iY8pE9vmfFPf5qzo%2FKQnQ%3D%3D';
				$.ajax({
					url : myurl,
						}).done(
								function(response) {
									console.log(response);
									var items = response.data;
									var output = '';
									for (var i = 0; i < items.length; i++) {
										var item = items[i];
										var touristSite = item.관광지;
										var tel = item.전화번호;
										var address = item.주소;
										var region = item.지역;
										var homepage = item.홈페이지;
										var c = item["코스 주제"];
										var d = item["코스 주제 설명"];
										var e = item["코스설명2"];

										output += '<div class="output-item">';

										output += '<strong>관광지:</strong> <span class="touristSite"> '
												+ touristSite + '</span><br>';

										if (tel == null) {
											output += '<strong>전화번호:</strong> <span class="tel">053-803-4911</span><br>';
										} else {
											output += '<strong>전화번호:</strong> <span class="tel">'
													+ tel + '</span><br>';
										}
										if (address == null) {
											output += '<strong>주소:</strong><span class="address">경상북도 고령군 개진면 인안리</span><br>';
										}

										else if (address === "대구 동구 갓바위로 팔공산 갓바위상가번영회") {
											output += '<strong>주소:</strong><span class="address">대구시 동구 갓바위로 254</span>(갓바위상가번영회)<br>';
										} else if (address === "대구광역시 달성군 유가면 휴양림길 230 자연휴양림 내 해발 590m") {
											output += '<strong>주소:</strong><span class="address">대구광역시 달성군 유가면 230</span><br>';
										} else if (address === "대구광역시 달성군 구지면 구지서로 726") {
											output += '<strong>주소:</strong><span class="address">대구광역시 달성군 구지면 도동서원로 1</span><br>';
										} else if (address === "대구광역시 중구 동성로2길 27") {
											output += '<strong>주소:</strong><span class="address">대구 중구 용덕동 12</span><br>';
										
										} else {
											output += '<strong>주소:</strong><span class="address">'
													+ address + '</span><br>';
										}

										if (homepage != null) {
											if (homepage === "http://83tower.kr") {
												output += '<strong>홈페이지:</strong></strong> <span class="homepage">'
														+ 'https:/eworld.kr/tower'
														+ '</span><br>';
											} else if (homepage === "http://www.sanghwa.or.kr") {
												output += '<strong>홈페이지:</strong></strong><span class="homepage">'
														+ 'https://korean.visitkorea.or.kr'
														+ '</span><br>';

											} else if (homepage === "http://www.dgca.or.kr/") {
												output += '<strong>홈페이지:</strong><span class="homepage">'
														+ 'https://tour.daegu.go.kr/'
														+ '</span><br>';
											} else if (homepage === "https://www.daegu.go.kr/cts/index.do?menu_id=00000910") {
												output += '<strong>홈페이지:</strong><span class="homepage">'
														+ 'https://tour.daegu.go.kr/'
														+ '</span><br>';
											} else if (homepage === "http://www.riverguide.go.kr/kor/page.do?menuIdx=381") {
												output += '<strong>홈페이지:</strong><span class="homepage">'
														+ 'http://www.riverguide.go.kr'
														+ '</span><br>';
											} else {
												output += '<strong>홈페이지:</strong></strong><span class="homepage">'
														+ homepage
														+ '</span><br>';
											}
										} else {
											output += '<strong>홈페이지:</strong></strong> 제공되는 홈페이지가 없습니다. <br>';
										}

										if (e.includes("우뚝")) {
											output += '<br><span class="course";>도심 속에 우뚝 솟은 대구의 랜드마크인 두류공원(頭流公園) 내에 위치한 유럽형 테마공원에 조성된 83타워!</span><br>';

										}
										if (e.includes("3.1만세운동길")) {
											output += '<br><span class="course";>타임머신 타고 대구 근대골목으로! 역사의 숨결을 느껴보세요!</span><br>';
										}
										if (e.includes("동성로축제는")) {
											output += '<br><span class="course";>대구의 열정과 다채로움이 만나는 절묘한 조화! 함께 춤추고 놀아요!!</span><br>';
										}
										if (e.includes("비로암")) {
											output += '<br><span class="course";>희망과 신비, 역사의 미소가 어우러진 곳. 영원히 기억될 마법같은 체험을 누려보세요!!</span><br>';
										}

										if (e.includes("9년(1658년)")) {
											output += '<br><span class="course";>매년 5월초에는 대구약령시축제를 개최하여 약령시의 문화적 가치를 전국적으로 홍보함으로써 약령시와 한의약에 대한 국민적 관심과 사랑을 유도하는 약령시 축제!</span><br>';
										}

										if (e.includes("소원의")) {
											output += '<br><span class="course";>팔공산과 대구 시내 전경이 한눈에  6인승 케이블카를 타고 해발 820m까지 펀안하게 올라가보자!</span><br>';
										}
										if (e.includes("상실한다고")) {
											output += '<br><span class="course";>대구 출신의 독립운동가를 만난다 일제강점기에 민족의 광복을 위해 저항 정신의 횃불을 밝힌 시를 지은 그의 시향이 이상화고택에 남아 있다.</span><br>';
										}
										if (e.includes("고딕양식의")) {
											output += '<br><span class="course";>이국적인 성당을 거닐며 100년의 역사를 돌아보는  경상도에서 가장 오래된 성당!</span><br>';
										}

										if (e.includes("제29호로")) {
											output += '<br><span class="course";>1990년 12월 15일 대구광역시 유형문화재 제29호로 지정되었다. 프랑스 루르드 동굴을 본떠서 만든, 전국적으로 유명한 천주교 성지를 방문해보자.</span><br>';
										}
										if (e.includes("벨벳의")) {
											output += '<br><span class="course";>우아하고 아름다운 직물인 벨벳의 모든 것  벨벳으로 만든 의류, 홈데코, 가구, 벽지 등 벨벳의 무한 가능성을 만나는 벨벳 갤러리</span><br>';
										}
										if (e.includes("삼성그룹의")) {
											output += '<br><span class="course";>1938년 호암 이병철 회장이 삼성그룹의 모태인 삼성상회를 창업해 처음 사업을 시작했던 곳, 대구오페라하우스 앞에는 호암 동상이 자리해 있다.</span><br>';
										}
										if (e.includes("김광석의")) {
											output += '<br><span class="course";>김광석의 음악 인생이 녹아있는 골목에서 벽화 감상하기  방천시장 뒷골목, 350m 이어진 골목 담벼락에 김광석의 삶과 노래를 주제로 한 벽화가 실감나게 펼쳐진다.</span><br>';
										}
										if (e.includes("상설시장")) {
											output += '<br><span class="course";>대구 최대의 상설시장, 야시장도 인기  조선시대 평양장, 강경장과 함께 전국 3대 장터를 방문해보자</span><br>';
										}
										if (e.includes("앞산케이블카")) {
											output += '<br><span class="course";>대구 시내를 조망할 수 있는 으뜸 명소 앞산케이블카를 타고 S자로 흐르는 낙동강을 시작으로 도시를 감싸는 산자락이 겹겹이 펼쳐진 대구를 둘러보자!</span><br>';
										}
										if (e.includes("한일월드컵")) {
											output += '<br><span class="course";>한일월드컵 세계육상선수권대회 등 여러 대회를 유치하고 수려한 자연환경과 깨끗한 시설로 많은 관광객이 찾아오는 대구의 새로운 명소!</span><br>';
										}
										if (e.includes("김충선")) {
											output += '<br><span class="course";>조선으로 귀화한 일본 장수 김충선을 기리는 서원 임진왜란 때 귀화한 김충선을 추모하기 위해 건립한 서원! 귀화 후 그의 활약상을 보여주는 역사관, 체험실 등이 있다</span><br>';
										}
										if (e.includes("3전시장")) {
											output += '<br><span class="course";>대구미술관은 국내외 근·현대미술의 전시는 물론, 미술 강좌 등 다양한 프로그램으로 시민의 문화예술욕구에 부응하는 문화공간으로서의 역할을 담당하고 있다.</span><br>';
										}
										if (e.includes("메타세쿼이어숲")) {
											output += '<br><span class="course";>사계절 내내 친환경 레저 체험을 즐길 수 있는 자연 속 놀거리와 체험거리가 한데 모여 있다 여러 체험거리로 지루할 틈이 없다!</span><br>';
										}
										if (e.includes("워터파크")) {
											output += '<br><span class="course";>주암산 기슭 청정자연에서 물놀이와 온천, 워터파크 와 동물원과 식물원이 한 자리에 있는 복합테마파크 등 여러 시설을 갖춘 테마파크를 즐길 수 있다!</span><br>';
										}
										if (e.includes("버드나무")) {
											output += '<br><span class="course";>낮과 밤 모두 아름다운 대구의 대표 관광명소 수성못을 방문해보자!</span><br>';
										}
										if (e.includes("참꽃축제")) {
											output += '<br><span class="course";>흙산이면서도 산세가 장중하고,  비슬산 자연휴양림이 있어 삼림욕과 휴식을 즐길 수 있으며 계절마다 다양한 축제가 개최되는 비슬산으로 가보자!</span><br>';
										}
										if (e.includes("디아크")) {
											output += '<br><span class="course";>물과 자연과 문화가 오롯이 담긴 건축예술품  강과 물, 자연을 모티브로 건축한 디아크는 예술적인 외관으로 사랑받는 장소</span><br>';
										}
										if (e.includes("수목원")) {
											output += '<br><span class="course";>쓰레기 매립장이 청정 수목원으로 재탄생  쓰레기 매립장이었던 장소를 활용해 만든 전국 최초의 도심형 수목원!</span><br>';
										}
										if (e.includes("우체통")) {
											output += '<br><span class="course";>옛 농촌 풍경이 마을 토담과 벽면을 가득 채운 정겨운 벽화마을  마을길을 따라 1960~70년대 정겨운 농촌으로 볼거리가 다양한 그 시절로 시간여행을 떠나보자!</span><br>';
										}
										if (e.includes("김굉필")) {
											output += '<br><span class="course";>아름드리 은행나무와 아름다운 토담을 품은 고즈넉한 서원  조선 초기의 성리학자 김굉필을 배향한 서원, 조선 중기 서원 건축의 특징을 살필 수 있다!</span><br>';
										}
										if (e.includes("원두막에서")) {
											output += '<br><span class="course";>옛 나루터의 정취 가득한 먹을거리, 볼거리 많은 주막촌  과거 나루터에 초가집, 원두막 운치 있고 주변 경관이 잘 가꿔진 서문진 주막촌으로 가자!</span><br>';
										}
										if (e.includes("전설을")) {
											output += '<br><span class="course";> 갓바위는 1,365개의 돌계단을 오르면 도달 할 수 있으며 정성껏 기도하는 사람의 소원을 한 가지 들어준다는 전설이 있다. 관봉석조여래좌상 앞에서 정성스레 기도를 올리는 사람들로 사시사철 붐빈다!</span><br>';
										}
										if (e.includes("폐철교")) {
											output += '<br><span class="course";>폐철교의 아름다운 변신, 마을과 마을을 잇는 인도교  기차가 다니던 철교가 마을과 사람을 잇는 인도교로 재탄생  낮과 밤 모두 금호강에 반사되는 인도교가 멋진 풍경을 만든다!</span><br>';
										}

										if (e.includes("유기문화실")) {
											output += '<br><span class="course";>방짜유기에 쉽고 재미나게 다가가는 방법  전국 유일의 방짜유기 박물관! 유기문화실, 기증실, 재현실, 체험장 등을 통해 우리 고유 문화유산인 방짜유기와 제작 기술을 전승, 보존, 계승한다!</span><br>';
										}
										if (e.includes("안전의식")) {
											output += '<br><span class="course";>시민 안전의식 함양의 장 각종 안전체험장을 한 자리에서 경험. 심폐소생술(CPR) 등 체험을 통한 학습이 가능하며 다채로운 구성과 소방관의 열정적인 시범으로 활기를 띤다!</span><br>';
										}
										if (e.includes("명상")) {
											output += '<br><span class="course";>고즈넉한 산사에 머무르며 예불, 다도, 명상 등을 통해 자신의 참 모습을 되돌아보는 나를 찾아 떠나는 여행 체험!</span><br>';
										}
										if (e.includes("시설지구에서")) {
											output += '<br><span class="course";>수확의 계절 가을, 곱게물든 팔공산의 단풍과 정성껏 빌면 한가지 소원은 꼭 이루어 준다는 갓바위와 함께 다양한 행사 프로그램으로 팔공산 갓바위 시설지구, 팔공산 동화 시설지구에서 한해씩 교대 개최!</span><br>';
										}
										if (e.includes("코스낙동강자전거")) {
											output += '<br><span class="course";>낙동강을 따라 시원하게 뻗은 자전거길을 달리며 일상에 지친심신을 달래보자!</span><br>';
										}
										output += '</div><br>';
									}
									$('.box').html(output);
								}).fail(function() {
							alert('실패');
						});
			}

		$(document).on('click','.output-item',function() {
					 $(this).css('display', 'block');
					 
					var tour = $(this).find('.touristSite').text();
					var address = $(this).find('.address').text(); // 클릭한 요소의 주소 값을 추출
					var tel = $(this).find('.tel').text();
					var d = $(this).find('.course').text();
					var home = $(this).find('.homepage').text();
									
					if (marker) {
						marker.setMap(null); // 이전 마커 제거
						}

						//console.log(home);
						if (address === "대구광역시 중구 일대") {
								address = '대구광역시 중구 서성로 10';
								}
						if (selectedElement !== null) {
							// 이전에 선택한 요소가 있을 경우 초기화
							selectedElement.removeClass('selected');
									}
						$(this).addClass('selected');
							selectedElement = $(this);

							geocoder.addressSearch(address,function(result,status) {
								if (status === kakao.maps.services.Status.OK) {
									var coords = new kakao.maps.LatLng(result[0].y,result[0].x);
											if (marker) {marker.setMap(''); // 이전 마커 제거
															}
															marker = new kakao.maps.Marker(
																{
																		map : map,
																		position : coords,
																		clickable : true
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
									               '        <div class="body" >' + 
									               '            <div class="img">' +
									               '                <img src="' + tourImage + '" width="73" height="70">' +
									               '           </div>' + 
									               '            <div class="desc">' + 
									                address + 
									               '                <div class="jibun ellipsis">' + tel + '</div>' + 
									               '<a href="https://map.kakao.com/?q=' + address + '" target="_blank" class="link">길찾기  </a>'+ linkHtml +'<br>'+ 
									               
									               '            </div>' + 
									               '        </div>' + 
									               '    </div>' +    
									               '</div>';
															var overlay = new kakao.maps.CustomOverlay(
																	{
																		content : infowindowContent,
																		map : map,
																		position : marker.getPosition(),
																	});

															kakao.maps.event.addListener(marker,'click',function() {
																			overlay.setMap(map);
																			});
															map.setCenter(coords);
														}
													});
								});
	function getCurrentPosition() {
  if ("geolocation" in navigator) {
    navigator.geolocation.getCurrentPosition(function(position) {
      var latitude = position.coords.latitude; // 현재 위치의 위도
      var longitude = position.coords.longitude; // 현재 위치의 경도

      if (marker) {
        marker.setMap(null); // 이전 마커 제거
      }
      initializeMap(latitude, longitude);
    });
  }
}

document.getElementById('current_me_position').addEventListener('click', function() {
  getCurrentPosition();
});

$(document).on('click', '.close', function() {
  $('.wrap').css('display', 'none');
});

getCurrentPosition();




