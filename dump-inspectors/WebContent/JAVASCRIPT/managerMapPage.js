var x = new XMLHttpRequest();

var obj;
var index = 0;
var index_annotation = 0;
var annotation_number = 2;
var images = null;
var annotations = null;
var target;
var arr ;
var greenIcon = L.icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-green.png'//////////////////////////////////////////////////////////////////////
});

var redIcon = L.icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-red.png'//////////////////////////////////////////////////////////////////////
});


var yellowIcon = L.icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-yellow.png'//////////////////////////////////////////////////////////////////////
});

window.addEventListener("load",(e)=>{
	document.getElementById("container_image").style.display = "none";
	document.getElementById("userCard").style.display ="none";
	document.getElementById("precedente").style.display = "none";
	document.getElementById("successivo").style.display = "none";	
	var map = L.map('mapid');
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
		marker.idLocalita = element.idLocalita;
		marker.bindPopup(element.nome+"<br>" + element.comune+"<br>" + element.regione +"<br>" + lat+"<br>" + lon+"<br>");
		if(codes[i] == 0) marker.setIcon(greenIcon);
		else if(codes[i] == 1) marker.setIcon(yellowIcon);
		else if(codes[i] == 2) marker.setIcon(redIcon);
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
		i++;
		
	})
	
	 map.fitBounds([				 
		[minLat,minLon],
    	[maxLat, maxLon]
    ]);
    map.zoomOut(1);   

},false)

function generateImageList(event)
{
	document.getElementById("container_image").style.display = "block";
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
	    checkButton();
		if(images.length>0){
		document.getElementById("image_box").setAttribute("src","data:image/jpeg;base64," + images[index].image);
		getAnnotation();
		}else{alert("Non ci sono immagini riferite a questa localita'");}
	  }
	}

function getAnnotation(){
	x.open("GET","//localhost:8080/dump-inspectors/GetAllUserAnnotation?idImmagine=" + images[index].idImmagine, true);
	x.onreadystatechange = generateAnnotationList;
	x.send(null);
}

function generateAnnotationList(){
	if (x.readyState == 4 && x.status == 200) {
		var txt = x.responseText;
		annotations = JSON.parse(txt);
		index_annotation = 0;
		changePage();
		generatePages();
		
	}
}

function generatePages(){
	var pagination = document.getElementById("pages");
	pagination.innerHTML = "";
	var q = Math.floor(annotations.length/annotation_number);
	var r = annotations.length%annotation_number;
	var i = 0;
	for(i = 0; i < q;i++){
		var page = document.createElement("li");
		page.addEventListener("click",(e)=>{
			index_annotation = e.target.value;
			changePage();
		},false);
		var link = document.createElement("a");
		link.value = i;
		link.className = "page-link";
		page.className = "page-item";
		link.innerHTML = (i+1);
		page.appendChild(link);
		pagination.appendChild(page);
	}
	if(r !== 0){var page = document.createElement("li");
	page.addEventListener("click",(e)=>{
		index_annotation = e.target.value;
		changePage();
	},false);
	var link = document.createElement("a");
	link.className = "page-link";
	page.className = "page-item";
	link.innerHTML = (i+1);
	link.value = i;
	page.appendChild(link);
	pagination.appendChild(page);
	}
}
function changePage(){
	document.getElementById("comment_page").innerHTML = "";
	
	for(var i = (annotation_number*index_annotation); i < (annotation_number*(index_annotation+1));i++){
		if(annotations.length == i){break;}
		var li_annotation = document.createElement("li");
		li_annotation.className = "list-item";
		li_annotation.className = "list-group-item";
		if(annotations[i].validita)
		li_annotation.innerHTML = annotations[i].note + " <span class='badge badge-primary'>Valida</span>";
		else
		li_annotation.innerHTML = annotations[i].note + " <span class='badge badge-danger'>Non valida</span>";
		li_annotation.value = annotations[i].idAnnotazione;
		li_annotation.addEventListener("click",(e)=>{getDetails(e.target.value);},false);
		document.getElementById("comment_page").appendChild(li_annotation);
	}
	document.getElementById("userCard").style.display = "none";
}

document.getElementById("successivo").addEventListener("click",(e)=>{
	if(index != (images.length)){
		index += 1
		document.getElementById("image_box").setAttribute("src", "data:image/jpeg;base64," + images[index].image);
		checkButton();
		getAnnotation();}
},false)
document.getElementById("precedente").addEventListener("click",(e)=>{
	if(index != 0){
		index -= 1
		document.getElementById("image_box").setAttribute("src", "data:image/jpeg;base64," + images[index].image);
		checkButton();
		getAnnotation();}
},false)
function checkButton(){
	if(index===0){document.getElementById("precedente").style.display="none";}else{document.getElementById("precedente").style.display="block";}
	if(index===(images.length - 1)){document.getElementById("successivo").style.display="none";}else{document.getElementById("successivo").style.display="block";}
}
function getDetails(idAnnotation){
	x.open("GET","//localhost:8080/dump-inspectors/GetUserNameByAnnotation?idAnnotazione=" + idAnnotation, true);
	x.onreadystatechange = generateDetails;
	x.send(null);
}

function generateDetails(){
	if(x.readyState == 4 && x.status == 200){
		var txt = x.responseText;
		var user = JSON.parse(txt);	
		document.getElementById("userCard").style.display ="block";
		document.getElementById("imgUser").setAttribute("src", "data:image/jpeg;base64," + user[3]);
		document.getElementById("userData").innerHTML = "Username: " + user[0] + "<br>Email: " + user[1] + "<br>Esperienza: " + user[2];
		
	}
}