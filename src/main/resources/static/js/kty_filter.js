
/* 페이징 처리 를 위한 js */
document.addEventListener("DOMContentLoaded", function() {
	const pageElements = document.getElementsByClassName("page-link");
	Array.from(pageElements).forEach(function(element) {
		element.addEventListener('click', function() {
			document.getElementById('page').value = this.dataset.page;
			document.getElementById('searchForm').submit();
		});
	});


	document.addEventListener("DOMContentLoaded", function() {
		const pageElements = document.getElementsByClassName("page-link2");
		Array.from(pageElements).forEach(function(element) {
			element.addEventListener('click', function() {
				document.getElementById('page').value = this.dataset.page;
				document.getElementById('kw2').value = document.getElementById('region_dropdown').value;
				const selectedFoodTypes = Array.from(document.querySelectorAll('input[name="foodType"]:checked')).map(el => el.value);
				document.getElementById('kw3').value = selectedFoodTypes.join(',');
				document.getElementById('searchForm2').submit();
			});
		});
		document.addEventListener("DOMContentLoaded", function() {
			const pageElements = document.getElementsByClassName("page-link3");
			Array.from(pageElements).forEach(function(element) {
				element.addEventListener('click', function() {
					document.getElementById('page').value = this.dataset.page;
					document.getElementById('kw2').value = document.getElementById('region_dropdown').value;

					document.getElementById('searchForm3').submit();

				});

			});
		});


		document.addEventListener("DOMContentLoaded", function() {
			const pageElements = document.getElementsByClassName("page-link4");
			Array.from(pageElements).forEach(function(element) {
				element.addEventListener('click', function() {
					document.getElementById('page').value = this.dataset.page;
					document.getElementById('kw2').value = document.getElementById('region_dropdown').value;
					const selectedFoodTypes = Array.from(document.querySelectorAll('input[name="ParkingType"]:checked')).map(el => el.value);
					document.getElementById('kw4').value = selectedFoodTypes.join(',');
					document.getElementById('searchForm4').submit();

				});

			});
		});


		document.addEventListener("DOMContentLoaded", function() {
			const pageElements = document.getElementsByClassName("page-link5");
			Array.from(pageElements).forEach(function(element) {
				element.addEventListener('click', function() {
					document.getElementById('page').value = this.dataset.page;
					document.getElementById('kw2').value = document.getElementById('region_dropdown').value;
					const selectedFoodTypes = Array.from(document.querySelectorAll('input[name="foodType"]:checked')).map(el => el.value);
					document.getElementById('kw3').value = selectedFoodTypes.join(',');
					const selectedFoodTypes2 = Array.from(document.querySelectorAll('input[name="ParkingType"]:checked')).map(el => el.value);
					document.getElementById('kw4').value = selectedFoodTypes.join(',');
					document.getElementById('searchForm5').submit();

				});

			});
		});

		const regionDropdown = document.getElementById('region_dropdown');
		const foodTypeCheckboxes = document.querySelectorAll('input[name="foodType"]');
		regionDropdown.addEventListener('change', function() {
			document.getElementById('kw2').value = this.value;
			document.getElementById('searchForm2').submit();
		});
		foodTypeCheckboxes.forEach(function(checkbox) {
			checkbox.addEventListener('change', function() {
				const selectedFoodTypes = Array.from(document.querySelectorAll('input[name="foodType"]:checked')).map(el => el.value);
				document.getElementById('kw3').value = selectedFoodTypes.join(',');
				document.getElementById('searchForm2').submit();
			});
		});
	});


	const btnSearch = document.getElementById("btn_search_food");
	btnSearch.addEventListener('click', function() {
		document.getElementById('kw').value = document.getElementById('search_food').value;
		document.getElementById('page').value = 0;
		document.getElementById('searchForm').submit();
	});



	const btnSearch2 = document.getElementById("searchBtn");
	btnSearch2.addEventListener('click', function() {
		const regionDropdown = document.getElementById("region_dropdown");
		const selectedRegion = regionDropdown.value;

		const checkboxes = document.querySelectorAll('input[name="foodType"]:checked');
		const selectedFoodTypes = Array.from(checkboxes).map(checkbox => checkbox.value);

		const checkboxes2 = document.querySelectorAll('input[name="ParkingType"]:checked');
		const selectedFoodTypes2 = Array.from(checkboxes2).map(checkbox => checkbox.value);

		if (selectedRegion && selectedFoodTypes.length > 0) {
			let url = "/tour/daegu_food2";
			url += "?kw2=" + encodeURIComponent(selectedRegion);
			url += "&kw3=" + encodeURIComponent(selectedFoodTypes.join(','));
			window.location.href = url;
		} else if (selectedRegion && selectedFoodTypes2.length > 0) {
			let url = "/tour/daegu_food4";
			url += "?kw2=" + encodeURIComponent(selectedRegion);
			url += "&kw4=" + encodeURIComponent(selectedFoodTypes2.join(','));
			window.location.href = url;
		}

		else if (selectedRegion) {
			let url = "/tour/daegu_food3";
			url += "?kw2=" + encodeURIComponent(selectedRegion);
			window.location.href = url;
		}
	});

	const btnSearch3 = document.getElementById("searchBtn");
	btnSearch2.addEventListener('click', function() {
		const regionDropdown = document.getElementById("region_dropdown");
		const selectedRegion = regionDropdown.value;

		const checkboxes = document.querySelectorAll('input[name="foodType"]:checked');
		const selectedFoodTypes = Array.from(checkboxes).map(checkbox => checkbox.value);

		const checkboxes2 = document.querySelectorAll('input[name="ParkingType"]:checked');
		const selectedFoodTypes2 = Array.from(checkboxes2).map(checkbox => checkbox.value);
		if (selectedRegion && selectedFoodTypes.length > 0 && selectedFoodTypes2.length > 0) {
			let url = "/tour/daegu_food5";
			url += "?kw2=" + encodeURIComponent(selectedRegion);
			url += "&kw3=" + encodeURIComponent(selectedFoodTypes.join(','));
			url += "&kw4=" + encodeURIComponent(selectedFoodTypes2.join(','));
			window.location.href = url;
		}
	});







	// 현재날짜를 기준으로 공공데이터로 불러온 가게가 영업중인지 영업 종료인지 판별하는 js 코드 */
	const foodEntries = document.getElementsByClassName("food-entry");
	Array.from(foodEntries).forEach(function(entry) {
		const openingHours = entry.getElementsByClassName("shop-opening-text")[0].textContent.trim().split(" ~ ");
		const currentTime = new Date();
		const currentHour = currentTime.getHours();

		const openingTime = parseInt(openingHours[0].split(":")[0]);
		const closingTime = parseInt(openingHours[1].split(":")[0]);

		const resultOpening = entry.getElementsByClassName("result_opening")[0];
		if ((currentHour >= openingTime && currentHour < closingTime) ||
			(closingTime < openingTime && (currentHour < closingTime || currentHour >= openingTime))) {
			resultOpening.textContent = "영업 중";
		} else {
			resultOpening.textContent = "영업 종료";
		}
	});

});


document.addEventListener("DOMContentLoaded", function() {
	const foodShopPhotos = document.getElementsByClassName("food_shop_photo");

	Array.from(foodShopPhotos).forEach(function(element) {
		
		const numImages = 100; 

		const randomNumber = Math.floor(Math.random() * numImages) + 1;

	
		const imageURL = `/img/tour/food/shop/${randomNumber}.jpg`;

		element.style.backgroundImage = `url('${imageURL}')`;
	});



	
	
	
		
});





