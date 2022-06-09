var x = new XMLHttpRequest();
var conflict_index = 0;
window.addEventListener("load",(e)=>{
	generatePages();
	if(conflict.length>0){
		imageClick(conflict[conflict_index].idImmagine);
	}
},false)
	
function imageClick(a)
{
		x.open("GET","//localhost:8080/dump-inspectors/GetImageConflict?idImmagine=" + a, true);
		x.onreadystatechange = responseChecking;
		x.send(null);	
}

function responseChecking() 
{		document.getElementById("images").setAttribute("src","data:image/jpeg;base64," + conflict[conflict_index].image);
		var title = document.getElementById("title_conflict");
		var text = document.getElementById("text_conflict");
		text.innerHTML = "";
		if (x.readyState == 4 && x.status == 200)
		{
			var txt = x.responseText;
			var objs = JSON.parse(txt);
			var ann = JSON.parse(objs.annotazioni);
			var nomi = JSON.parse(objs.utenti);

			if(ann.length>0)
			{
				for(var k = 0; k < ann.length; k++)
				{
					let comment = document.createElement("span");
					comment.innerHTML = nomi[k] + " il " + ann[k]["datacreazione"] + " ha scritto: " + ann[k]["note"];
					text.appendChild(comment);
					text.appendChild(document.createElement("br"));
				}
			}else{alert("Nente  per questa immagine")}
		  }
}


function generatePages(){
	var pagination = document.getElementById("pages");
	pagination.innerHTML = "";
	var i = 0;
	for(i = 0; i < conflict.length;i++){
		var page = document.createElement("li");
		page.addEventListener("click",(e)=>{
			conflict_index = e.target.value;
			imageClick(conflict[conflict_index].idImmagine);
		},false);
		var link = document.createElement("a");
		link.value = i;
		link.className = "page-link";
		page.className = "page-item";
		link.innerHTML = (i+1);
		page.appendChild(link);
		pagination.appendChild(page);
	}
}