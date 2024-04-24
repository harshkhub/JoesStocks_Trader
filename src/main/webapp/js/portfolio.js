/**
 * 
 */

 document.addEventListener('DOMContentLoaded', function(){
	
	fetch('/assignment_4/getPortfolio')
		.then(response => response.json())
		.then(data => console.log(data))
		.catch(error => console.error('Error:', error));
		
 });
 
 document.addEventListener('DOMContentLoaded', checkLogin);
 
 function logOut(){
    fetch('/assignment_4/logoutUser', {
        method: 'GET',
        credentials: 'include'
    });
}