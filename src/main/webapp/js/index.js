

document.addEventListener('DOMContentLoaded', function() {
    //buy stock logic
    document.getElementById('buyStockForm').addEventListener('submit', function(event){
        event.preventDefault();
        buyStock();
    });

    // Function to add a submit event listener to a form
    function addSubmitListenerToForm(formId, tickerInputId, stockInfoId) {
        var form = document.getElementById(formId);
        if (!form) {
            console.error('Form with ID ' + formId + ' not found.');
            return;
        }

        form.addEventListener('submit', function(event) {
            event.preventDefault();
			
            console.log("Entered for form: " + formId);
            var ticker = document.getElementById(tickerInputId).value;
            var stockInfo = document.getElementById(stockInfoId);

            if (ticker) {
                var xhr = new XMLHttpRequest();

                xhr.open('GET', '/assignment_4/getTrade?ticker=' + encodeURIComponent(ticker), true);

                xhr.onload = function() {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        var response = JSON.parse(xhr.responseText);
						localStorage.setItem("price", response.c);
						localStorage.setItem("ticker", ticker);
                        console.log(response);
                     	console.log("Updated");
						if(!isLoggedIn){
                        stockInfo.innerHTML = `

                        <p class='ticker'>Stock Name: ${ticker}</p>
                        <p>Market status: ${response.name}</p>
                        `;
                        }
                        else{
							
						}
                    } else {
                        console.error('Request failed');
                    }
                };

                xhr.send();
            } else {
                console.log('Ticker symbol is required');
            }
        });
    }

    addSubmitListenerToForm('stockSearchForm', 'tickerInput', 'stockInfo');
    addSubmitListenerToForm('stockSearchForm1', 'tickerInput1', 'stockInfo1');
    
});

var isLoggedIn = false;

function checkLogin(){
    fetch('/assignment_4/isLoggedIn')
        .then(response => response.json())
        .then(data => {
            if(data.userLoggedIn){
                displayLoggedInNavbar();
                isLoggedIn = true;
            }
            else{
                displayLoggedOutNavbar();
                isLoggedIn = false;
            }
        })
        .catch(error => console.error("Error: ", error));
}

function displayLoggedInNavbar(){
    document.getElementById('logged-in').style.display = 'block';
    document.getElementById('logged-out').style.display = 'none';
}

function displayLoggedOutNavbar(){
    document.getElementById('logged-out').style.display = 'block';
    document.getElementById('logged-in').style.display = 'none';
}

document.addEventListener('DOMContentLoaded', checkLogin);

function logOut(){
    fetch('/assignment_4/logoutUser', {
        method: 'GET',
        credentials: 'include'
    });
}

function removeSearch(){
	document.getElementById('text-box').style.display = 'none';
}

function buyStock(){
    var numStocks = document.getElementById('numStocks').value;
    var price = localStorage.getItem("price");
    var ticker = localStorage.getItem("ticker");
    
    const url = "http://localhost:8080/assignment_4/buyTrade";
    const encodedTicker = encodeURIComponent(ticker);
	const encodedNumStock = encodeURIComponent(numStocks);
	const encodedPrice = encodeURIComponent(price);

// Construct the full URL with encoded parameters
	const fullUrl = `${url}?ticker=${encodedTicker}&numStock=${encodedNumStock}&price=${encodedPrice}`;

    
    fetch(fullUrl, {
		method: 'POST',
		credentials: 'include'
	})
    	.then(response =>response.json())
    	.then(data => {
			if(data.status){
				console.log("Success");
				alert(`Bought ${numStocks} shares of ${ticker} for $${price}`);
			}
			else{
				console.log("Failure")
				alert("FAILED: Purchase not possible");
			}
		})
    	.catch(error => console.error("Error: ", error));
    
    
}
