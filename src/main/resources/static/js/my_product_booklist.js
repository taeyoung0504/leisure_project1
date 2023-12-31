




/*모달 */

function openModal(button) {
	const row = button.parentNode.parentNode;
	const bookNumValue = row.querySelector('td:first-child').textContent;
	const authenticatedUserName = document.getElementById('username').textContent;

	console.log(bookNumValue);
	console.log(authenticatedUserName);

	const modal = document.getElementById('myModal');
	modal.style.display = 'block';

	// Set the values in the modal form
	document.getElementById('bookNumValue').value = bookNumValue;
	document.getElementById('authenticatedUserName').value = authenticatedUserName;
}

function closeModal() {
	const modal = document.getElementById('myModal');
	modal.style.display = 'none';
}



function submitCancellation() {
	// Get the input value from the textarea
	const reasonCancelInput = document.getElementById('reasonCancle').value;

	// Get the bookNumValue and authenticatedUserName from the modal
	const bookNumValue = document.getElementById('bookNumValue').value;
	const authenticatedUserName = document.getElementById('authenticatedUserName').value;

	
	const data = {
		reasonCancel: reasonCancelInput,
		bookNumValue: bookNumValue,
		authenticatedUserName: authenticatedUserName,
		result: null 
	};
	const backendEndpointURL = 'http://192.168.10.67:8080/cancel-requests/submit';
//	const backendEndpointURL = 'http://localhost:8080/cancel-requests/submit';

	fetch(backendEndpointURL, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(data)
	})
		.then(response => {
			if (response.ok) {
				Swal.fire({
					position: 'center',
					icon: 'success',
					title: '취소요청이 완료되었습니다.',
					showConfirmButton: false,
					timer: 2000
				});
				
				closeModal();
				
			} else {
				// If there was an error, you can handle it here (e.g., display an error message)
				console.error('Error submitting cancellation request.');
			}
		})
		.catch(error => {
			console.error('Error:', error);
		});
}





function hideCancelButton() {
  
  const spanElements = document.querySelectorAll("tbody span");

  spanElements.forEach((spanElement) => {
   
    const spanText = spanElement.textContent.trim();
    console.log("Span Text:", spanText);

  
    if (spanText === "거절") {

      const cancelButton = spanElement.closest("tr").querySelector(".cancelButtondd");
      if (cancelButton) {
        cancelButton.style.display = "none";
        console.log("CancelButton Hidden");
      } else {
        console.log("CancelButton not found");
      }
    }
  });
}


document.addEventListener("DOMContentLoaded", hideCancelButton);






/* 달력 */
function filterByDate() {
    var selectedDate = document.getElementById("datePicker").value;
    var rows = document.querySelectorAll("#booking-table tbody tr");

    for (var i = 0; i < rows.length; i++) {
        var checkinDate = rows[i].querySelector("td:nth-child(6)").innerText;
        if (checkinDate === selectedDate) {
            rows[i].style.display = "table-row";
        } else {
            rows[i].style.display = "none";
        }
    }
}

function filterByDateAndAcc() {
    var selectedDate = document.getElementById("datePicker").value;
    var selectedAcc = document.getElementById("accSelect").value;
    var rows = document.querySelectorAll("#booking-table tbody tr");

    for (var i = 0; i < rows.length; i++) {
        var checkinDate = rows[i].querySelector("td:nth-child(6)").innerText;
        var accTitle = rows[i].querySelector("td:nth-child(2)").innerText;
        var displayRow = true;

        if (selectedDate !== "" && checkinDate !== selectedDate) {
            displayRow = false;
        }

        if (selectedAcc !== "모두" && accTitle !== selectedAcc) {
            displayRow = false;
        }

        rows[i].style.display = displayRow ? "table-row" : "none";
    }
}



function setDefaultDate() {
        var today = new Date();
        var year = today.getFullYear();
        var month = String(today.getMonth() + 1).padStart(2, '0');
        var day = String(today.getDate()).padStart(2, '0');
        var formattedDate = year + '-' + month + '-' + day;
        document.getElementById('datePicker').value = formattedDate;
    }

    // Call the function when the page loads
    window.onload = function () {
        setDefaultDate();
        filterByDateAndAcc(); // Optionally, apply the initial filter based on the default date
    };
    
    

