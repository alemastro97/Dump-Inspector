//WIZARD ---------------------------------------------------------------------

//campagna è variabile locale definita inline
var x,
phase,
serverPath = "http://localhost:8080/dump-inspectors",
listaLocalita = document.getElementById("sreg"),
newFlag = document.getElementsByName("newLocalita")[0],
form = document.getElementById("form"),
lat = document.getElementById("lat"),
lon = document.getElementById("lon"),
prec = document.getElementsByName("prec")[0],
succ = document.getElementsByName("succ")[0],
canc = document.getElementsByName("canc")[0];
send = document.getElementsByName("send")[0];
firstPage = document.getElementById("firstPage"),
secondPage = document.getElementById("secondPage"),
thirdPage = document.getElementById("thirdPage");

phase = 1;

window.addEventListener("load", caricaLocalita, false);
listaLocalita.addEventListener("change", visualizzaNuova, false);
prec.addEventListener("click", precPress, false);
succ.addEventListener("click", succPress, false);
canc.addEventListener("click", cancPress, false);



function caricaLocalita() {
	x = new XMLHttpRequest();
	x.onreadystatechange = aggiornaLocalita;
	x.open("GET", serverPath + "/GetLocalitaLista?campagnaId=" + campagna.idCampagna);
	x.send();
}

function aggiornaLocalita() {
	if (x.readyState == 4 && x.status == 200) {
		var arrayLocalita = JSON.parse(x.response);
		var def = document.createElement("option");
		def.textContent = "Nuova Localita...";
		def.value = 0;
		listaLocalita.appendChild(def);
		for (var i = 0; i < arrayLocalita.length; i++) {
			var opt = document.createElement("option");
			opt.textContent = arrayLocalita[i].nome;
			opt.value = arrayLocalita[i].idLocalita;
			listaLocalita.appendChild(opt);
		}

		listaLocalita.selectedIndex = 0;
		generateDefaultList();
		var e = document.createEvent("HTMLEvents");
		e.initEvent("change", false, true);
		listaLocalita.dispatchEvent(e);
	}
}

function visualizzaNuova() {
	var toHide = document.getElementsByClassName("newHidden");
	var len = toHide.length;
	if (listaLocalita.selectedIndex == 0) {
		newFlag.value = "true"
			for (var i = 0; i < len; i++) {
				toHide[i].style.display = "block";
			}
	} else if (listaLocalita.selectedIndex != 0) {
		newFlag.value = "false";
		for (var i = 0; i < len; i++) {
			toHide[i].style.display = "none";
		}
	}
}

function checkCoordinates() {
	var lati = lat.value;
	var long = lon.value;

	if (!check_lat_lon(lati, long)) {
		var alert = document.getElementById("latlonAlert");
		alert.style.display = "block";
		return false;
	} else {
		return true;
	}
}

function check_lat_lon(lat, lon){
	var clat = /^(-?[1-8]?\d(?:\.\d{1,18})?|90(?:\.0{1,18})?)$/;
	var clon = /^(-?(?:1[0-7]|[1-9])?\d(?:\.\d{1,18})?|180(?:\.0{1,18})?)$/;
	var validLat =  clat.test(lat);
	var validLon = clon.test(lon);
	if(validLat && validLon) {
		return true;
	} else {
		return false;
	}
}

function succPress(e) {
	if (phase == 1) {
		if (newFlag.value === "true") { //Usiamo trim per rimuovere gli spazi bianchi in caso di un errore da parte dell'utente nell'inserimento dei nomi nella form
			if (checkCoordinates() && form.nome.value.trim() !== "" && form.comune.value.trim() !== "" && form.regione.value.trim() !== "") {//Controllo di non nullità sulla form
				document.getElementById("default_box").style.display = "none";
				var alert = document.getElementById("detailsAlert");
				alert.style.display = "none";
				secondP(); // nel caso in cui tutti i valori inseriti nella prima parte della form siano valide si passa alla pagina successiva e scompare la tabella delle località di default
			} else {
				var alert = document.getElementById("detailsAlert");
				alert.style.display = "block";//in caso di un inserimento errato nella prima pagina verrà visualizzato un messaggio di errore
			}
		} else {
			document.getElementById("default_box").style.display = "none";
			secondP();
		}
	} else if (phase == 2) {
		if( form.image.files.length == 0 ){//controllo sull'immagine inserita, in caso di nullità compare un errore che esorta a inserire un'immagine
			var alert = document.getElementById("imageAlert");
			alert.style.display = "block";
		} else {
			thirdP(); //nel caso in cui si sia inserita un'immagine si passa alla pagina successiva
		}
	}
}

function precPress(e) {
	if (phase == 2) {
		document.getElementById("default_box").style.display = "block";
		firstP();
	} else if (phase == 3) {
		document.getElementById("default_box").style.display = "none";
		secondP();
	}
}

function firstP() {
	document.getElementById("default_box").style.display = "block";
	firstPage.style.display = "block";
	secondPage.style.display = "none";
	thirdPage.style.display = "none";
	prec.style.display = "none";
	succ.style.display = "block";
	send.style.display = "none";
	phase = 1;
}

function secondP() {
	document.getElementById("default_box").style.display = "none";
	firstPage.style.display = "none";
	secondPage.style.display = "block";
	thirdPage.style.display = "none";
	prec.style.display = "block";
	succ.style.display = "block";
	send.style.display = "none";
	phase = 2;
}

function thirdP() {
	document.getElementById("default_box").style.display = "none";
	firstPage.style.display = "none";
	secondPage.style.display = "none";
	thirdPage.style.display = "block";
	prec.style.display = "block";
	succ.style.display = "none";
	send.style.display = "block";
	phase = 3;
}

function cancPress() {
	document.getElementById("default_box").style.display = "block";
	firstP();
}
document.getElementById("form").addEventListener("submit",(e)=>{
	var inputDate = new Date(document.getElementById("form").data_acquisizione.value);
	var todaysDate = new Date();
	if(inputDate.setHours(0,0,0,0) > todaysDate.setHours(0,0,0,0)) {
	    e.preventDefault();
	    document.getElementById("DateController").style.display = "block";
	}else{document.getElementById("DateController").style.display = "none";}
	
},false);
//GESTIONE IMMAGINI ----------------------------------------------------------

var elencoLoc = document.getElementById("elenco-localita");
var elencoFoto = document.getElementById("imgLocalita");
var manLoc = document.getElementById("manager-localita");
var imgX = new XMLHttpRequest();
var nomeLocalita;

window.addEventListener("load", inizializzaImmagini, false);

function inizializzaImmagini() {
	var lista = elencoLoc.children;
	if (lista.length > 0) {
		for (var i = 0; i < lista.length; i++) {
			lista[i].firstElementChild.addEventListener("click", caricaImmagini, false);
		}
		var ev = document.createEvent("HTMLEvents");
		ev.initEvent("click", false, true);
		lista[0].firstElementChild.dispatchEvent(ev);
	}
}

function caricaImmagini(e) {
	elencoFoto.innerHTML = "";
	nomeLocalita = e.target.textContent;
	var idLoc = e.target.nextElementSibling.value;
	imgX.onreadystatechange = mostraImmagini;
	imgX.open("GET", "//localhost:8080/dump-inspectors/GetLocationImage?idLocalita=" + idLoc, true);
	imgX.send();
}

function mostraImmagini() {
	if (imgX.readyState == 4 && imgX.status == 200) {
		var txt = imgX.responseText;
		var obj = JSON.parse(txt);
		console.log(obj);
		if (obj.length > 0) {
			var daEliminare = document.getElementById("imgSpan");
			if (daEliminare !== null) {
				manLoc.removeChild(daEliminare);
			}
			var titolo = document.createElement("span");
			titolo.textContent = "Immagini della localita': " + nomeLocalita;
			titolo.id = "imgSpan";
			manLoc.insertBefore(titolo, elencoFoto);
			for (var i = 0; i < obj.length; i++) {
				var immagine = document.createElement("img");
				immagine.src = "data:image/jpeg;base64," + obj[i].image;
				elencoFoto.appendChild(immagine);
			}
		}
	}
}
//GESTIONE RIEMPIMENTO TABELA LOCALITA' DI DEFAULT
var default_location = null; 
var defaultX = new XMLHttpRequest();
function generateDefaultList(){
	defaultX.open("GET","//localhost:8080/dump-inspectors/GetDefaultLocation", true);
	defaultX.onreadystatechange = createList;
	defaultX.send(null);
}

function createList(){
	if(defaultX.readyState == 4 && defaultX.status == 200){
		var txt = defaultX.responseText;
		default_location = JSON.parse(txt);
		var list = document.getElementById("default_location");
		for(var i = 0; i < default_location.length; i++){
			var li = document.createElement("li");
			li.className = "list-group-item";
			li.innerHTML = default_location[i].nome + " " + default_location[i].comune + " " + default_location[i].regione
			li.value = i;
			li.addEventListener("click",fillWizard,false);
			list.appendChild(li);
		}
	}
}


function fillWizard(){
	var i = this.value;
	document.getElementById("lat").value = default_location[i].latitudine;
	document.getElementById("lon").value = default_location[i].longitudine;
	document.getElementById("form").nome.value = default_location[i].nome;
	document.getElementById("form").comune.value = default_location[i].comune;
	document.getElementById("form").regione.value = default_location[i].regione;
}













