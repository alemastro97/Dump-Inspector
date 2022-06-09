var update = document.getElementById("updateForm");
var obj = null;
var x = new XMLHttpRequest();
window.addEventListener("load",(e)=>{
	update.username.value = user_account.username;
	update.email.value = user_account.email;
	document.getElementById("25").setAttribute("aria-valuenow",0);
	document.getElementById("50").setAttribute("aria-valuenow",0);
	document.getElementById("75").setAttribute("aria-valuenow",0);
	document.getElementById("100").setAttribute("aria-valuenow",0);
	document.getElementById("25").style.width = "0%";
	document.getElementById("50").style.width = "0%";
	document.getElementById("75").style.width = "0%";
	document.getElementById("100").style.width = "0%";
	document.getElementById("old_img").setAttribute("src","data:image/jpeg;base64," + user_account.foto);
	
},false)

update.username.addEventListener("keyup",openConnection,false)
update.email.addEventListener("keyup",openConnection,false)
update.newpwd.addEventListener("keyup", (e) => { 

	var p_bar = document.getElementById("p_bar");
	var text = document.getElementById("progess_text");
	var strongRegex = new RegExp("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g");
	var mediumRegex = new RegExp("^(?=.{7,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
	var enoughRegex = new RegExp("(?=.{8,}).*", "g");
	var pwd = update.newpwd;
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


update.oldpwd.addEventListener("keyup", (e) => { 
		if(update.oldpwd.value == user_account.password){
			update.newpwd.readOnly = false;
			update.newpwdconfirm.readOnly = false;
		}else{
			update.newpwd.readOnly = true;
			update.newpwdconfirm.readOnly = true;
		}
	}, false)
update.newpwdconfirm.addEventListener("keyup", (e) => { 
	if(update.newpwdconfirm.value !==  update.newpwd.value ){
		document.getElementById("error_pwdConfirm").style.display = "block";}
	else{document.getElementById("error_pwdConfirm").style.display = "none";}
}, false)
document.getElementById("submit").addEventListener("click",
		(e)=>{
				var check = checkForm();
				if(check === 1) {e.preventDefault();}
		}
		,false)

function responseChecking() {
			  if (x.readyState == 4 && x.status == 200) {
				  var txt = x.responseText;
				  obj = JSON.parse(txt);
				  if(obj.username === true)document.getElementById("error_username").style.display = "block";
				  else document.getElementById("error_username").style.display = "none";

				  if(obj.email === true)document.getElementById("error_email").style.display = "block";
				  else document.getElementById("error_email").style.display = "none";
			  }
			}
function openConnection(){
	x.open("GET","//localhost:8080/dump-inspectors/CheckCredenziali?username=" + update.username.value + "&email=" + update.email.value, true);
	x.onreadystatechange = responseChecking;
	x.send(null);
}


function checkForm(){
	var check = 0;
	if(update.username.classList.contains("form-control is-valid"))update.username.classList.remove("form-control is-valid");
	if(update.username.classList.contains("form-control is-invalid"))update.username.classList.remove("form-control is-invalid");
	if(update.email.classList.contains("form-control is-valid"))update.email.classList.remove("form-control is-valid");
	if(update.email.classList.contains("form-control is-invalid"))update.email.classList.remove("form-control is-invalid");
	if(update.newpwd.classList.contains("form-control is-valid"))update.newpwd.classList.remove("form-control is-valid");
	if(update.newpwd.classList.contains("form-control is-invalid"))update.newpwd.classList.remove("form-control is-invalid");
	if(update.newpwdconfirm.classList.contains("form-control is-valid"))update.newpwdconfirm.classList.remove("form-control is-valid");
	if(update.newpwdconfirm.classList.contains("form-control is-invalid"))update.newpwdconfirm.classList.remove("form-control is-invalid");

	if(document.getElementById("error_username").style.display === "block")
	{check = 1;
	update.username.className = "form-control is-invalid";
	}else{update.username.className = "form-control is-valid";}
	if(document.getElementById("error_email").style.display === "block"){check = 1;
	update.email.className = "form-control is-invalid";
	}else{update.email.className = "form-control is-valid";}
	if(update.oldpwd.value === user_account.password){
	if(update.newpwd.value.length < 8){check = 1;
	update.newpwd.className = "form-control is-invalid";
	}else{update.newpwd.className = "form-control is-valid";}
	if(update.newpwd.value != update.newpwdconfirm.value){check = 1;
	update.newpwdconfirm.className = "form-control is-invalid";
	}else{update.newpwdconfirm.className = "form-control is-valid";}
	}
	return check;
}
