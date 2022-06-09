
var x = new XMLHttpRequest();
window.addEventListener("load",
		(e)=>{
			getUser();
		}
		,false);
function getUser(){
	x.open("GET","//localhost:8080/dump-inspectors/GetUserSession");
	x.onreadystatechange = usersession;
	x.send(null);
}

function usersession(){
	if(x.readyState == 4 && x.status == 200){
		var txt = x.responseText;
		var obj = JSON.parse(txt);
		document.getElementById("welcomeuser").innerHTML = "Benvenuto " + obj.username;
	}
}