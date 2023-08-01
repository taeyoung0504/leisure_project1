async function fetchData() {
   try {
      // Replace '/api/roles' with your actual API endpoint to fetch data
      const response = await fetch('/api/roles');
      if (!response.ok) {
         throw new Error('Network response was not ok');
      }
      const responseData = await response.json();
      return responseData;
   } catch (error) {
      console.error('Error fetching data:', error);
   }
}

// Function to update the chart with new data
async function updateChart() {
   const responseData = await fetchData();

   // 파이 차트 그리기
   const roleChartCanvas = document.getElementById('roleChart').getContext('2d');
   new Chart(roleChartCanvas, {
      type: 'pie',
      data: {
         labels: responseData.map(entry => entry.role),
         datasets: [{
            data: responseData.map(entry => entry.count),
            backgroundColor: ['rgba(75, 192, 192, 0.2)', 'rgba(153, 102, 255, 0.2)', 'rgba(255, 159, 64, 0.2)','rgba(255, 189, 64, 0.2)'],
            borderColor: ['rgba(75, 192, 192, 1)', 'rgba(153, 102, 255, 1)', 'rgba(255, 159, 64, 1)'],
            borderWidth: 1
         }]
      },
      options: {
         responsive: true,
         maintainAspectRatio: false,
         plugins: {
            datalabels: {
               display: 'auto', // Show data labels on hover
               color: '#000',
               formatter: (value, ctx) => {
                  let sum = 0;
                  let dataArr = ctx.chart.data.datasets[0].data;
                  dataArr.map(data => {
                     sum += data;
                  });
                  let percentage = (value * 100 / sum).toFixed(2) + "%";
                  return percentage;
               },
               font: {
                  weight: 'bold'
               }
            }
         }
      }
   });
}

// Call the updateChart function initially to load the chart
updateChart();










/* 막대그래프 (sectors) */
document.addEventListener("DOMContentLoaded", function() {
   fetch('/api/sector') // Assuming this URL maps to your API endpoint
      .then(response => response.json())
      .then(data => {
         const sectors = data.map(item => item.acc_sectors);
         const counts = data.map(item => item.sector_count);

         createBarChart(sectors, counts);
      })
      .catch(error => console.error('Error fetching data:', error));
});

function createBarChart(sectors, counts) {
   const ctx = document.getElementById('sectorChart').getContext('2d');

   // Define an array of colors to be used for different sectors
   const colors = [
      'rgba(75, 192, 192, 0.6)', 
      'rgba(255, 99, 132, 0.6)', 
      'rgba(54, 162, 235, 0.6)', 
      
   ];

   const myChart = new Chart(ctx, {
      type: 'bar',
      data: {
         labels: sectors, // Set x-axis labels to acc_sectors
         datasets: [{
            label: '숙소 개수',
            data: counts, // Set y-axis data to sector_count
            backgroundColor: colors.slice(0, sectors.length), // Use the defined colors
            borderColor: 'rgba(75, 192, 192, 1)',
         
         }]
      },
      options: {
         responsive: true,
         maintainAspectRatio: false,
         plugins: {
            datalabels: {
               anchor: 'end',
               align: 'end',
               color: 'black',
               font: {
                  weight: 'bold'
               },
               formatter: function(value) {
                  return value; // Show the value on top of the bars
               }
            }
         },
         scales: {
            x: {
               grid: {
                  display: false
               }
            },
            y: {
               beginAtZero: true,
               stepSize: 10, // Set the step size to 10
               title: {
                  display: true,
                  text: 'Sector Count' // Add y-axis title
               }
            }
         }
      }
   });
}


/* 금일 예약 통계 */
  function updateBookingCount() {
            fetch('/api/today_booking')
                .then(response => response.text())
                .then(data => {
                    document.getElementById('todayBookingCount').innerText = data;
                })
                .catch(error => console.error('Error:', error));
        }

        // Call the function to update the booking count on page load
        updateBookingCount();

/*예약 총 매출 */
function updateBookingCount2() {
    fetch('/api/today_benefit_total')
        .then(response => response.text())
        .then(data => {
            // 값이 없을 경우 기본값 0으로 설정
            const todayTotalmoneyElement = document.getElementById('todayTotalmoney');
            todayTotalmoneyElement.innerText = data || 0;
        })
        .catch(error => console.error('Error:', error));
}

// Call the function to update the booking count on page load
updateBookingCount2();
        
        
        
        
        
   /* 취소 총 통계 */

function updateCancleCount2() {
            fetch('/api/today_cancle_total')
                .then(response => response.text())
                .then(data => {
                    document.getElementById('todayTotaCancle').innerText = data;
                })
                .catch(error => console.error('Error:', error));
        }

        // Call the function to update the booking count on page load
        updateCancleCount2();


/* 취소 금액 통계 */
function updateCancleCount33() {
    fetch('/api/today_benefit_total3')
        .then(response => response.text())
        .then(data => {
            // 값이 없을 경우 기본값 0으로 설정
            const todayTotalcal2Element = document.getElementById('todayTotalcal2');
            todayTotalcal2Element.innerText = data || 0;
        })
        .catch(error => console.error('Error:', error));
}

// Call the function to update the cancel count on page load
updateCancleCount33();









/* 방문자 통계 */

async function fetchVisitData() {
   try {
      const response = await fetch('/api/today_visit_count');
      if (!response.ok) {
         throw new Error('Network response was not ok');
      }
      const responseData = await response.json();
      return responseData;
   } catch (error) {
      console.error('Error fetching visit data:', error);
   }
}

async function updateLineChart() {
   const visitData = await fetchVisitData();

   const visitDates = Object.keys(visitData);
   const visitCounts = Object.values(visitData);

   const lineChartCanvas = document.getElementById('lineChart').getContext('2d');
   new Chart(lineChartCanvas, {
      type: 'line',
      data: {
         labels: visitDates,
         datasets: [{
            label: '일 별 방문자 수',
            data: visitCounts,
            borderColor: 'rgba(255, 99, 132, 1)',
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            borderWidth: 2,
            pointRadius: 5,
            pointHoverRadius: 7,
         }]
      },
      options: {
         responsive: true,
         maintainAspectRatio: false,
         plugins: {
            datalabels: {
               display: false,
            }
         },
         scales: {
            x: {
               grid: {
                  display: false,
               }
            },
            y: {
               beginAtZero: true,
               stepSize: 10, // You can adjust the step size based on your data
               title: {
                  display: true,
                  text: '방문자 수',
               }
            }
         }
      }
   });
}

// Call the updateLineChart function initially to load the chart
updateLineChart();
