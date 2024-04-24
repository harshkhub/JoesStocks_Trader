/**
 * 
 */
function registerUser(event){
		
		event.preventDefault();
		
		var xhr = new XMLHttpRequest();
		xhr.open('POST', '/assignment_4/registerUser', true)
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		
		var email = document.getElementById("EmailReg").value;
		var username = document.getElementById("UsernameReg").value;
		var password = document.getElementById("PasswordReg").value;
		var confirmPassword = document.getElementById("confirmPassword").value;
		
		// Validate confirm password
		if(password !== confirmPassword) {
			alert("Passwords do not match.");
			return;
		}
		
		var formData = "email=" + encodeURIComponent(email) +
		   "&username=" + encodeURIComponent(username) +
		   "&password=" + encodeURIComponent(password);
		
		xhr.onload = function(){
			 
			if (xhr.status >= 200 && xhr.status < 300){
				
				try{
				const JsonResponse = JSON.parse(xhr.responseText)
				window.location.href = 'index.html';
				console.log(JsonResponse);
				}
				catch(e){
					console.error("Could not parse JSON response", e);
		            alert("Registration failed: could not parse server response.");
				}
			}
			else {
		        // Handle request error
		        alert("Registration failed: " + xhr.statusText);
		    }
			
		}
		
		xhr.send(formData);
		
		
	}
	
	function loginUser(event){
		
		event.preventDefault();
		
		var xhr = new XMLHttpRequest();
		xhr.open('POST', '/assignment_4/loginUser', true)
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		
		var username = document.getElementById("Username").value;
		var password = document.getElementById("Password").value;

		
		var formData = "username=" + encodeURIComponent(username) +
		   "&password=" + encodeURIComponent(password);
		
		xhr.onload = function(){
			 
			if (xhr.status >= 200 && xhr.status < 300){
				
				try{
				const JsonResponse = JSON.parse(xhr.responseText)
				window.location.href = 'index.html';
				console.log(JsonResponse);
				}
				catch(e){
					console.error("Could not parse JSON response", e);
		            alert("Login failed: could not parse server response.");
				}
			}
			else {
		        // Handle request error
		        alert("Registration failed: " + xhr.statusText);
		    }
	}
		xhr.send(formData);
	}
	
	//check login status

	function checkLogin(){
	fetch('/assignment_4/isLoggedIn')
		.then(response=>response.json())
		.then(data =>{
			if(data.userLoggedIn){
				displayLoggedInNavbar();
			}
			else{
				displayLoggedOutNavbar();
			}
		})
		.catch(error => console.error("Error: "), error)
	}
	
	function displayLoggedInNavbar(){
		document.getElementById('navbar-logged-in').style.display = 'block';
		document.getElementById('navbar-logged-out').style.display = 'none';
	}
	
	function displayLoggedOutNavbar(){
		document.getElementById('navbar-logged-out').style.display = 'block';
		document.getElementById('navbar-logged-in').style.display = 'none';
	}
	
	document.addEventListener('DOMContentLoaded', checkLogin());
	
	//logout user
		
	function logOut(){
		fetch('/assignment_4/logoutUser', {
			method: 'GET',
			credentials: 'include'
		})
		.catch(error => console.error("Error: "), error)
	}
	