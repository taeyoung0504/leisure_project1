

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
			let url = "/tour/daegu_food";
			url += "?kw2=" + encodeURIComponent(selectedRegion);
			url += "&kw3=" + encodeURIComponent(selectedFoodTypes.join(','));
			window.location.href = url;
		} else if (selectedRegion && selectedFoodTypes2.length > 0) {
			let url = "/tour/daegu_food";
			url += "?kw2=" + encodeURIComponent(selectedRegion);
			url += "&kw4=" + encodeURIComponent(selectedFoodTypes2.join(','));
			window.location.href = url;
		}

		else if (selectedRegion) {
			let url = "/tour/daegu_food";
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
			let url = "/tour/daegu_food";
			url += "?kw2=" + encodeURIComponent(selectedRegion);
			url += "&kw3=" + encodeURIComponent(selectedFoodTypes.join(','));
			url += "&kw4=" + encodeURIComponent(selectedFoodTypes2.join(','));
			window.location.href = url;
		}
	});







	// Extract opening hours and compare with current time
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
		// Get the total number of images in the folder
		const numImages = 100; // Replace with the actual number of images in the folder

		// Generate a random number between 1 and numImages
		const randomNumber = Math.floor(Math.random() * numImages) + 1;

		// Set the background image URL
		const imageURL = `/img/tour/food/shop/${randomNumber}.jpg`;

		// Set the background image for the current element
		element.style.backgroundImage = `url('${imageURL}')`;
	});




/*  아래 수정 필요*/

function getAddressFromLatLng(lat, lng, callback) {
  var geocoder = new kakao.maps.services.Geocoder();

  geocoder.coord2Address(lng, lat, function(result, status) {
    if (status === kakao.maps.services.Status.OK) {
      var address = result[0].address.address_name;
      callback(address);
    } else {
      console.log("주소 변환 중 오류가 발생했습니다.");
    }
  });
}


function compareDistance(address1, address2, callback) {
  // Implement your distance comparison logic here
  // This function should calculate the distance between two addresses and invoke the callback with the result
  // For simplicity, let's assume the distance is a random number
  var distance = Math.random() * 10; // Replace this with your actual distance calculation

  callback(distance);
}


function showPosition(position) {
  var latitude = position.coords.latitude;
  var longitude = position.coords.longitude;

  getAddressFromLatLng(latitude, longitude, function(currentAddress) {
    var foodEntries = document.getElementsByClassName("food-entry");

    // Iterate over each food entry
    for (var i = 0; i < foodEntries.length; i++) {
      var foodEntry = foodEntries[i];
      var foodAddressElement = foodEntry.querySelector(".shop-address");
      var foodAddress = foodAddressElement.innerText;

      // Compare the distance between the current address and the food address
      compareDistance(currentAddress, foodAddress, function(distance) {
        // Update the distance result in the corresponding element
        var sddElement = foodEntry.querySelector("#sdd");
        var formattedDistance = formatDistance(distance);
        sddElement.innerText = formattedDistance;
      });
    }
  });
}



function formatDistance(distance) {
  if (distance >= 1) {
    // Convert to kilometers and format with two decimal places
    var formattedDistance = distance.toFixed(2) + " km";
    return formattedDistance;
  } else {
    // Convert to meters and format without decimal places
    var formattedDistance = (distance * 1000).toFixed(0) + " m";
    return formattedDistance;
  }
}

function showError(error) {
  switch (error.code) {
    case error.PERMISSION_DENIED:
      console.log("사용자가 위치 공유 권한을 거부했습니다.");
      break;
    case error.POSITION_UNAVAILABLE:
      console.log("위치 정보를 사용할 수 없습니다.");
      break;
    case error.TIMEOUT:
      console.log("위치 정보 요청이 시간 초과되었습니다.");
      break;
    case error.UNKNOWN_ERROR:
      console.log("알 수 없는 오류가 발생했습니다.");
      break;
  }
}

function getLocation() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(showPosition, showError);
  } else {
    console.log("Geolocation이 지원되지 않는 브라우저입니다.");
  }
}

window.onload = getLocation;
		
		
		
		function showPosition(position) {
  var latitude = position.coords.latitude;
  var longitude = position.coords.longitude;

  getAddressFromLatLng(latitude, longitude, function(currentAddress) {
    var currentMyDongnae = document.getElementById("current_my_dongnae");
    currentMyDongnae.innerHTML = currentAddress;

    var foodEntries = document.getElementsByClassName("food-entry");

    // Iterate over each food entry
    for (var i = 0; i < foodEntries.length; i++) {
      var foodEntry = foodEntries[i];
      var foodAddressElement = foodEntry.querySelector(".shop-address");
      var foodAddress = foodAddressElement.innerText;

      // Compare the distance between the current address and the food address
      compareDistance(currentAddress, foodAddress, function(distance) {
        // Update the distance result in the corresponding element
        var sddElement = foodEntry.querySelector("#sdd");
        var formattedDistance = formatDistance(distance);
        sddElement.innerText = formattedDistance;
      });
    }
  });
}
		
	
	
	
		
});





