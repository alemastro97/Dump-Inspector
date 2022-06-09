var updateDataForm = document.getElementById("updateDataForm");
var updatePwdForm = document.getElementById("updatePwdForm");
var errorChange = document.getElementById("errorChange").value;
var errorLog = document.getElementById("errorLog").value;
window.addEventListener("load",
		function(e) {
	/**--------------Controlli a caricamento della pagina--------------------------**/
	updatePwdForm.style.display = "none";
	if(errorLog == 1 || errorChange.charAt(2)==1 || errorChange.charAt(3)==1) showPwdChangeForm();
	if(errorChange.charAt(0)==1){document.getElementById("Error_Username").style.display = "block";} else {document.getElementById("Error_Username").style.display = "none";}
	if(errorChange.charAt(1)==1){document.getElementById("Error_Mail").style.display = "block";} else {document.getElementById("Error_Mail").style.display = "none";}
	if(errorChange.charAt(2)==1){document.getElementById("strength").style.display = "block";} else {document.getElementById("strength").style.display = "none";}
	if(errorChange.charAt(3)==1){document.getElementById("DifferentPwd").style.display = "block";} else {document.getElementById("DifferentPwd").style.display = "none";}
	/**---------------------------------------------------------------------------------------------------------**/
	/**-------------------------------------Selezione tipo di utente--------------------------------------------**/
	/**---------------------------------------------------------------------------------------------------------**/	
})


function showPwdChangeForm(){

	updateDataForm.style.display = "none";
	updatePwdForm.style.display = "block";
	if(errorChange.charAt(2)==1){document.getElementById("strength").style.display = "block";} else {document.getElementById("strength").style.display = "none";}
	if(errorChange.charAt(3)==1){document.getElementById("DifferentPwd").style.display = "block";} else {document.getElementById("DifferentPwd").style.display = "none";}

}


updatePwdForm.newpwd.addEventListener("keyup", (e) => { 
	var strength = document.getElementById("strength");
	strength.style.display = "block"; // rende visibile la parola che indica la sicurezza della password immessa
	var strongRegex = new RegExp("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g");
	var mediumRegex = new RegExp("^(?=.{7,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
	var enoughRegex = new RegExp("(?=.{8,}).*", "g");
	var pwd = updatePwdForm.newpwd;
	if (pwd.value.length == 0) {
		strength.innerHTML = "Type Password";
	} else if (false == enoughRegex.test(pwd.value)) {
		strength.innerHTML = "More Characters";
	} else if (strongRegex.test(pwd.value)) {
		strength.innerHTML = "<span style='color:green'>Strong!</span>";
	} else if (mediumRegex.test(pwd.value)) {
		strength.innerHTML = "<span style='color:orange'>Medium!</span>";
	} else {
		strength.innerHTML = "<span style='color:red'>Weak!</span>";
	}
}, false)

	/**Evento inerente al cambio pagina da Login a registrazione**/
	updateDataForm.changePwd.addEventListener("click", showPwdChangeForm, false)
	
	updatePwdForm.changeData.addEventListener("click",
			(e) => {
				document.getElementById("updateDataForm").style.display = "block";
				document.getElementById("updatePwdForm").style.display = "none";
				if(errorChange.charAt(0)==1){document.getElementById("Error_Username").style.display = "block";} else {document.getElementById("Error_Username").style.display = "none";}
				if(errorChange.charAt(1)==1){document.getElementById("Error_Mail").style.display = "block";} else {document.getElementById("Error_Mail").style.display = "none";}
			}, false)
	updatePwdForm.addEventListener("submit",
		(e) => {
			if (updatePwdForm.newpwd.value != updatePwdForm.newpwdc.value) {
				e.preventDefault();
				document.getElementById("DifferentPwd").style.display = "block";
				updatePwdForm.newpwd.focus();
				return false;
			}
			else if (updatePwdForm.newpwd.value.length<8) {
				e.preventDefault();
				document.getElementById("stength").style.display = "block";
				updatePwdForm.newpwd.focus();
				return false;
			}
		}, false)
	