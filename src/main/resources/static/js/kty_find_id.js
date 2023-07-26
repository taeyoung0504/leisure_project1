function checkEmail() {
			var email = document.getElementById("find_id_email").value;
			var xhr = new XMLHttpRequest();
			xhr.open("POST", "/user/find_id", true);
			xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			xhr.onreadystatechange = function () {
				if (xhr.readyState === 4 && xhr.status === 200) {
					var response = xhr.responseText;
					document.getElementById("email-error").innerHTML = response; // 결과를 <div> 요소에 표시

				}
			};
			xhr.send("email=" + encodeURIComponent(email));
		}