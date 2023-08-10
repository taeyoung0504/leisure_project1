
/*
document.addEventListener("DOMContentLoaded", function() {
	// Get the select element
	const selectElement = document.getElementById("accSelect");

	// Add an event listener to the select element
	selectElement.addEventListener("change", function() {
		// Get the selected value
		const selectedValue = selectElement.value;

		// Get all the table rows
		const tableRows = document.querySelectorAll("#booking-table tbody tr");

		// Loop through each row and check if the accommodation title matches the selected value
		tableRows.forEach(function(row) {
			const accomTitleCell = row.querySelector("td:nth-child(2)"); // Assuming the accommodation title is in the second column

			if (selectedValue === "모두" || accomTitleCell.textContent === selectedValue) {
				// If the selected value is "모두" or the accommodation title matches the selected value, show the row
				row.style.display = "table-row";
			} else {
				// Otherwise, hide the row
				row.style.display = "none";
			}
		});
	});
});
*/
/*
document.addEventListener("DOMContentLoaded", function() {
	// Get the select element
	const selectElement = document.getElementById("accSelect");

	// Get the no match message element
	const noMatchMessage = document.getElementById("noMatchMessage");

	// Add an event listener to the select element
	selectElement.addEventListener("change", function() {
		// Get the selected value
		const selectedValue = selectElement.value;

		// Get all the table rows
		const tableRows = document.querySelectorAll("#booking-table tbody tr");

		// Initialize a variable to keep track of whether there are matching rows or not
		let hasMatchingRows = false;

		// Loop through each row and check if the accommodation title matches the selected value
		tableRows.forEach(function(row) {
			const accomTitleCell = row.querySelector("td:nth-child(2)"); // Assuming the accommodation title is in the second column

			if (selectedValue === "모두" || accomTitleCell.textContent === selectedValue) {
				// If the selected value is "모두" or the accommodation title matches the selected value, show the row
				row.style.display = "table-row";
				hasMatchingRows = true;
			} else {
				// Otherwise, hide the row
				row.style.display = "none";
			}
		});

		// Show/hide the no match message based on whether there are matching rows
		if (hasMatchingRows) {
			noMatchMessage.style.display = "none";
		} else {
			noMatchMessage.style.display = "block";
		}
	});
});
*/






/*모달 */

function openModal(button) {
	const row = button.parentNode.parentNode;
	const bookNumValue = row.querySelector('td:first-child').textContent;
	const authenticatedUserName = document.getElementById('username').textContent;

	console.log(bookNumValue);
	console.log(authenticatedUserName);

	const modal = document.getElementById('myModal');
	modal.style.display = 'block';

	document.getElementById('reasonCancle').value = '';
	
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

            // Create a data object to send in the AJAX request
            const data = {
                reasonCancel: reasonCancelInput,
                bookNumValue: bookNumValue,
                authenticatedUserName: authenticatedUserName,
                result: null // Set the initial value of the "result" field to null (optional)
            };

            const backendEndpointURL = 'http://192.168.10.67:8080/cancel-requests/submit';

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

// Call the hideCancelButton function when the page loads
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
    
    


/* 거절 시 숨기기 





 function hideCancelButton() {
    // Get all the span elements within the table body
    const spanElements = document.querySelectorAll("tbody span");

    // Loop through each span element
    spanElements.forEach((spanElement) => {
      // Get the text value of the span element
      const spanText = spanElement.textContent.trim();

      // Check if the span text value is "거절"
      if (spanText === "거절") {
        // If the text is "거절", find the closest button with the class "cancelButtondd" and hide it
        const cancelButton = spanElement.closest("tr").querySelector(".cancelButtondd");
        cancelButton.style.display = "none";
      }
    });
  }

  // Call the hideCancelButton function when the page loads
  document.addEventListener("DOMContentLoaded", hideCancelButton);
*/



/*
function openModal() {
	var modal = document.getElementById('myModal');
	modal.style.display = 'block';
}

function closeModal() {
	var modal = document.getElementById('myModal');
	modal.style.display = 'none';
}




	// 버튼 클릭 이벤트에 함수 연결
	var button = document.getElementById("cancelButtonSubmit");
	button.addEventListener("click", showAlert);
    
	*/


/*
 document.addEventListener("DOMContentLoaded", function() {
	const phoneNumberElements = document.getElementsByClassName("tel-number");
	for (const element of phoneNumberElements) {
	  const phoneNumber = element.textContent.trim();
	  element.textContent = formatPhoneNumber(phoneNumber);
	}
  });

  function formatPhoneNumber(phoneNumber) {
	const cleaned = phoneNumber.replace(/\D/g, ""); // Remove non-digit characters
	const match = cleaned.match(/^(\d{3})(\d)(\d{3})(\d{4})$/); // Match groups of 3, 1, 3, and 4 digits
	if (match) {
	  const maskedMiddleThree = "*".repeat(3); // Create a string of 3 asterisks for the middle digits
	  const maskedLastFour = "*".repeat(4); // Create a string of 4 asterisks for the last digits
	  return `${match[1]}-${match[2]}${maskedMiddleThree}-${maskedLastFour}`;
	}
	// If the format doesn't match, return the original number
	return phoneNumber;
  }
  
  
  */