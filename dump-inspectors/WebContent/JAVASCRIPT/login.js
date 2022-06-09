var Registration = document.getElementById("registrationForm");
var Login = document.getElementById("loginForm");
var radios = document.forms["registrationForm"].elements["UserType"];
var errorReg = document.getElementById("errorRegistrazione").value;
var x = new XMLHttpRequest();
/**------------------------------------Evento di caricamento della pagina------------------------------------**/
Registration.Username.addEventListener("keyup",openConnection,false)
Registration.Email.addEventListener("keyup",openConnection,false)
function openConnection(){
	x.open("GET","//localhost:8080/dump-inspectors/CheckCredenzialiRegistrazione?username=" + Registration.Username.value + "&email=" + Registration.Email.value, true);
	x.onreadystatechange = responseChecking;
	x.send(null);
}
Registration.ConfermaPassword.addEventListener("keyup", (e) => { 
	if(Registration.ConfermaPassword.value !== Registration.Password.value ){
		document.getElementById("error_pwdConfirm").style.display = "block";}
	else{document.getElementById("error_pwdConfirm").style.display = "none";}
}, false)
function responseChecking() {
	  if (x.readyState == 4 && x.status == 200) {
		  var txt = x.responseText;
		  var obj = JSON.parse(txt);
		  if(obj.username === true)document.getElementById("error_username").style.display = "block";
		  else document.getElementById("error_username").style.display = "none";

		  if(obj.email === true)document.getElementById("error_email").style.display = "block";
		  else document.getElementById("error_email").style.display = "none";
	  }
	}



window.addEventListener("load",
		function(e) {
	/**--------------Controlli a caricamento della pagina--------------------------**/
	Registration.style.display = "none";
	if(errorReg !=="0000"){changePage();}
	/**---------------------------------------------------------------------------------------------------------**/
	/**-------------------------------------Selezione tipo di utente--------------------------------------------**/
	for(var i = 0, max = radios.length; i < max; i++) {
		radios[i].onchange = function() {
			if(this.value == "Manager"){
				document.getElementById("photo").style.display = "none";
			}else{
				document.getElementById("photo").style.display = "block";
			}
		}
	}
	/**---------------------------------------------------------------------------------------------------------**/	
})
/**---------------------------------------------------------------------------------------------------------**/
/**--------------------Funzione per il cambio di pagina Login->Registrazione--------------------------------**/
function changePage(){
	radios[0].checked = true;
	document.getElementById("photo").style.display = "none";
	document.getElementById("loginForm").style.display = "none";
	document.getElementById("registrationForm").style.display = "block";
	if(errorReg.charAt(0)==1){document.getElementById("error_username").style.display = "block";} else {document.getElementById("error_username").style.display = "none";}
	if(errorReg.charAt(1)==1){document.getElementById("error_email").style.display = "block";} else {document.getElementById("error_email").style.display = "none";}
	if(errorReg.charAt(3)==1){document.getElementById("error_pwdConfirm").style.display = "block";} else {document.getElementById("error_pwdConfirm").style.display = "none";}
}
/**---------------------------------------------------------------------------------------------------------**/
/**Controllo sulla password in termini di sicurezza, eseguito ogni volta che una lettera è rilasciata**/
Registration.Password.addEventListener("keyup", (e) => { 
	var p_bar = document.getElementById("p_bar");
	var text = document.getElementById("progess_text");
	var strongRegex = new RegExp("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g");
	var mediumRegex = new RegExp("^(?=.{7,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
	var enoughRegex = new RegExp("(?=.{8,}).*", "g");
	var pwd = Registration.Password;
	if (pwd.value.length == 0) {
		document.getElementById("25").setAttribute("aria-valuenow",0);
		document.getElementById("50").setAttribute("aria-valuenow",0);
		document.getElementById("75").setAttribute("aria-valuenow",0);
		document.getElementById("100").setAttribute("aria-valuenow",0);
		
		text.classList.remove("valid-feedback");
		text.classList.remove("invalid-feedback");
		text.className = "invalid-feedback";
		text.innerHTML = "Inserisci Password";
		document.getElementById("25").style.width = "0%";
		document.getElementById("50").style.width = "0%";
		document.getElementById("75").style.width = "0%";
		document.getElementById("100").style.width = "0%";
	} else if (false == enoughRegex.test(pwd.value)) {
		document.getElementById("25").setAttribute("aria-valuenow",25);
		document.getElementById("50").setAttribute("aria-valuenow",0);
		document.getElementById("75").setAttribute("aria-valuenow",0);
		document.getElementById("100").setAttribute("aria-valuenow",0);
		
		text.classList.remove("valid-feedback");
		text.classList.remove("invalid-feedback");
		text.className = "invalid-feedback";
		text.innerHTML = "Password troppo corta";
		document.getElementById("25").style.width = "25%";
		document.getElementById("50").style.width = "0%";
		document.getElementById("75").style.width = "0%";
		document.getElementById("100").style.width = "0%";
	} else if (strongRegex.test(pwd.value)) {

		document.getElementById("25").setAttribute("aria-valuenow",25);
		document.getElementById("50").setAttribute("aria-valuenow",50);
		document.getElementById("75").setAttribute("aria-valuenow",75);
		document.getElementById("100").setAttribute("aria-valuenow",100);
		document.getElementById("25").style.width = "25%";
		document.getElementById("50").style.width = "50%";
		document.getElementById("75").style.width = "75%";
		document.getElementById("100").style.width = "100%";
		
		text.classList.remove("valid-feedback");
		text.classList.remove("invalid-feedback");
		text.className = "valid-feedback";
		text.innerHTML = "Forte";
	} else if (mediumRegex.test(pwd.value)) {
		document.getElementById("25").setAttribute("aria-valuenow",25);
		document.getElementById("50").setAttribute("aria-valuenow",50);
		document.getElementById("75").setAttribute("aria-valuenow",75);
		document.getElementById("100").setAttribute("aria-valuenow",0);
		document.getElementById("25").style.width = "25%";
		document.getElementById("50").style.width = "50%";
		document.getElementById("75").style.width = "75%";
		document.getElementById("100").style.width = "0%";
		
		text.classList.remove("valid-feedback");
		text.classList.remove("invalid-feedback");
		text.className = "valid-feedback";
		text.innerHTML = "Media";
	} else {
		document.getElementById("25").setAttribute("aria-valuenow",25);
		document.getElementById("50").setAttribute("aria-valuenow",50);
		document.getElementById("75").setAttribute("aria-valuenow",0);
		document.getElementById("100").setAttribute("aria-valuenow",0);
		document.getElementById("25").style.width = "25%";
		document.getElementById("50").style.width = "50%";
		document.getElementById("75").style.width = "0%";
		document.getElementById("100").style.width = "0%";
	
		text.classList.remove("valid-feedback");
		text.classList.remove("invalid-feedback");
		text.className = "valid-feedback";
		text.innerHTML = "Debole";
	}
	text.style.display = "block";
}, false)
/**---------------------------------------------------------------------------------------------------------**/
/**Controllo password e conferma password, nel caso in cui siano differenti blocca la chiamata alla servlet
 * e mette il focus sulla textbox della password segnalando il problema delle password differenti**/
Registration.addEventListener("submit",
		(e)=>{
						var check = checkForm();
						if(check === 1) {e.preventDefault();}
				}
				,false)
function checkForm(){
	var check = 0;
	if(Registration.Username.classList.contains("form-control is-valid"))Registration.Username.classList.remove("form-control is-valid");
	if(Registration.Username.classList.contains("form-control is-invalid"))Registration.Username.classList.remove("form-control is-invalid");
	if(Registration.Email.classList.contains("form-control is-valid"))Registration.Email.classList.remove("form-control is-valid");
	if(Registration.Email.classList.contains("form-control is-invalid"))Registration.Email.classList.remove("form-control is-invalid");
	if(Registration.Password.classList.contains("form-control is-valid"))Registration.Password.classList.remove("form-control is-valid");
	if(Registration.Password.classList.contains("form-control is-invalid"))Registration.Password.classList.remove("form-control is-invalid");
	if(Registration.ConfermaPassword.classList.contains("form-control is-valid"))Registration.ConfermaPassword.classList.remove("form-control is-valid");
	if(Registration.ConfermaPassword.classList.contains("form-control is-invalid"))Registration.ConfermaPassword.classList.remove("form-control is-invalid");

	if(document.getElementById("error_username").style.display === "block")
	{check = 1;
	Registration.Username.className = "form-control is-invalid";
	}else{Registration.Username.className = "form-control is-valid";}
	if(document.getElementById("error_email").style.display === "block"){check = 1;
	Registration.Email.className = "form-control is-invalid";
	}else{Registration.Email.className = "form-control is-valid";}
	
	if(Registration.Password.value.length < 8){check = 1;
	Registration.Password.className = "form-control is-invalid";
	}else{Registration.Password.className = "form-control is-valid";}
	if(Registration.Password.value != Registration.ConfermaPassword.value){check = 1;
	Registration.ConfermaPassword.className = "form-control is-invalid";
	}else{Registration.ConfermaPassword.className = "form-control is-valid";}
	
	return check;
}
/**----------------------------------------------------------------------------------------------------------**/
/**Evento inerente al cambio pagina da Login a registrazione**/
document.getElementById("linkRegistrazione").addEventListener("click",changePage, false)
/**---------------------------------------------------------------------------------------------------------**/
/**------------------Evento inerente al cambio pagina da Registrazione a Login------------------------------**/
document.getElementById("linkLogin").addEventListener("click",
			(e) => {
				document.getElementById("loginForm").style.display = "block";
				document.getElementById("registrationForm").style.display = "none";
			}, false)
/**---------------------------------------------------------------------------------------------------------**/