var x = new XMLHttpRequest();
var marker_list = new Array(localita.length);
var index = 0;
var target;
var images = null;


window.addEventListener("load",(e)=>{
	document.getElementById("dynamic_list").style.display = "none";
	document.getElementById("welcomeuser").innerHTML = "Campagna " + campagna.nome ;
	document.getElementById("precedente").style.display = "none";
	document.getElementById("successivo").style.display = "none";	
	var map = L.map('map');//.setView([45.8119642,9.0854556], 13);
	L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
	  attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
	}).addTo(map);
	var myFeatureGroup = L.featureGroup().addTo(map).on("click", generateImageList);
	var maxLat = 100;
    var minLat = -100;
    var maxLon = 190;
    var minLon = -190;
    var first = false;
	var marker;
	var i = 0;
	localita.forEach(function(element){
		var lat = element.latitudine;
    	var lon = element.longitudine;
    	var id = element.idLocalita;
    	
		marker = L.marker([lat,lon]).addTo(map);
		marker.addTo(myFeatureGroup);
		marker.idLocalita = element.idLocalita
		marker.bindPopup(element.nome+"<br>" + element.comune+"<br>" + element.regione +"<br>" + lat+"<br>" + lon+"<br>");
		if(!first)
		{
			maxLat>90   ? maxLat = lat : "";
			minLat<-90  ? minLat = lat : "";
			maxLon>180  ? maxLon = lon : "";
			minLon<-180 ? minLon = lon : "";
			first = true;
		}
		
		lat>maxLat ? maxLat = lat : lat<minLat ? minLat = lat : "";
		lon>maxLon ? maxLon = lon : lon<minLon ? minLon = lon : "";
		
	})
	
	 map.fitBounds([				 
		[minLat,minLon],
    	[maxLat, maxLon]
    ]);
    map.zoomOut(1);   
},false)

function generateImageList(event)
{
	document.getElementById("dynamic_list").style.display = "none";
	document.getElementById("precedente").style.display = "block";
	document.getElementById("successivo").style.display = "block";	
	openConnectionGetImages(event.layer.idLocalita);
}

function openConnectionGetImages(id_localita){
	x.open("GET","//localhost:8080/dump-inspectors/GetLocationImage?idLocalita=" + id_localita, true);
	x.onreadystatechange = responseChecking;
	x.send(null);
	}


function responseChecking() {
	  if (x.readyState == 4 && x.status == 200) {
		index = 0; 
		var txt = x.responseText;
		images = JSON.parse(txt);
		if(images.length>0){
			checkButton();
			document.getElementById("dynamic_list").style.display = "block";
			document.getElementById("imageTag").setAttribute("src", "data:image/jpeg;base64," + images[index].image);
			generateAnnotation();			
		}else{document.getElementById("dynamic_list").style.display = "none";alert("Non ci sono immagini per la seguente localita'.");}
	  }
	}

function generateAnnotation(){
	x.open("GET","//localhost:8080/dump-inspectors/GetAnnotation?idImmagine=" + images[index].idImmagine, true);
	x.onreadystatechange = generateFormAnnotation;
	x.send(null);
} 

function generateFormAnnotation(){
	if (x.readyState == 4 && x.status == 200) {
		var txt = x.responseText;
		var obj_annotation = JSON.parse(txt);
		if(obj_annotation == null)
		{
			document.getElementById("Insert_note_show").style.display = "none";
			var form = document.getElementById("formAnnotation");
			form.style.display = "block";
			form.Nota.value = "";
			if(form.Nota.classList.contains("is-invalid"))form.Nota.classList.remove("is-invalid");
			form.hidden_image.setAttribute('value',images[index].idImmagine);
			document.getElementById("r1").setAttribute('checked', "true");
		}
		else{
			var textArea = document.getElementById("Insert_note");
			document.getElementById("formAnnotation").style.display = "none";
			
			document.getElementById("Insert_note_show").style.display = "block";
			textArea.value = obj_annotation.note;
			textArea.disabled = true;
		}
		
	}
}

document.getElementById("precedente").addEventListener("click",(e)=>{
	if(index != 0){
	index -= 1;
	document.getElementById("imageTag").setAttribute("src", "data:image/jpeg;base64," + images[index].image);
	checkButton();
	generateAnnotation();
	}
},false)

document.getElementById("successivo").addEventListener("click",(e)=>{
	if(index != (images.length)){
	index += 1
	document.getElementById("imageTag").setAttribute("src", "data:image/jpeg;base64," + images[index].image);
	checkButton();
	generateAnnotation();
	}
	
},false)

document.getElementById("formAnnotation").addEventListener("submit",(e)=>{
	e.preventDefault();
	if(e.target.Nota.value !== ""){
	x.open("POST","//localhost:8080/dump-inspectors/CreateAnnotation?hidden_image=" + images[index].idImmagine + "&Nota=" + e.target.Nota.value + "&radio=" + e.target.radio.value, true);
	x.onreadystatechange = updateAnnotation;
	x.send(null);
	}else{document.getElementById("Nota").className ="form-control is-invalid";}
},false)

function updateAnnotation(){
	if(x.readyState == 4 && x.status == 200){
		document.getElementById("Nota").value = "";
		generateAnnotation();}
}

function checkButton(){
	if(index===0){document.getElementById("precedente").style.display="none";}else{document.getElementById("precedente").style.display="block";}
	if(index===(images.length - 1)){document.getElementById("successivo").style.display="none";}else{document.getElementById("successivo").style.display="block";}
}