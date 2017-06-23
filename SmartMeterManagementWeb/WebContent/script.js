$(document).ready(function() {
	/*
	Initiliasiert die beiden Smart Meter als JavaScript Objekte.
	Properties: Name, Kennziffer, erlaubte Stromstärke, anliegende Stromstärke, angliegende
	Spannung, Ablesungen und zugehöriges Bild.
	
	Werden später vom Server überschrieben. aber 
	
	const smOne = {
		name: "Smart Meter One",
		id: "SM00000001",
		allowedCurrent : 50,
		current : getCurrent(50),
		voltage : getVoltage(),
		readings : [],
		imgSrc : "https://upload.wikimedia.org/wikipedia/commons/9/9a/Intelligenter_zaehler-_Smart_meter.jpg"
	}

	const smTwo = {
		name : "Smart Meter Two",
		id : "SM00000002",
		allowedCurrent : 100,
		current : getCurrent(100),
		voltage : getVoltage(),
		readings : [],
		imgSrc : "http://www.nikkei.com/content/pic/20141028/96958A9F889DE5EAE6E5E0E6E4E2E3E4E3E2E0E2E3E6E2E2E2E2E2E2-DSXZZO7847265016102014000000-PN1-13.jpg"
	}
	
	let smartMeters=[smOne,smTwo];
	*/
	
	/*Legt Array für die Smart Meter an */
	let smartMeters = [];
	
	/*Hilfsfunktion zum Runden*/
	function round(value, precision) {
    var multiplier = Math.pow(10, precision || 0);
    return Math.round(value * multiplier) / multiplier;
	}
	/*Hilfsfunktion für Id*/
	function pad(num, size) {
	    var s = num+"";
	    while (s.length < size) s = "0" + s;
	    return s;
	}
	/*
	Berechnet die zulässige Spannung für ein Smart Meter
	*/
	function getVoltage(){
		return round(220 + 20*Math.random(),1);
	}
	/*
	Berechnet die anliegende Stromstärke in Abhängigkeit der erlaubten Stromstärke
	*/
	function getCurrent(allowedCurrent){
		return round(allowedCurrent + 5,1);
	}
	/*
	Gibt HTML für ein Smart Meter zurück
	*/
	function renderMeterHTML(meter){
		/* HTML für die Ablesungen des Smart Meters */
		const readings = $.map(meter.readings ,function(reading,i){
			return `<li><span>${reading.userName}, </span><span>${reading.date} </span><span>,<strong>${reading.kiloWattHours} kwH</strong></span></li>`;
		});
		const readingsHTML = readings.join("");
		/*Smart Meter HTML */
		return(`
		 <div class="specs">
			<div>
				<h2>Smart Meter ${meter.name}</h2>
				<p><strong>Gerätekennung: </strong>${meter.id}</p>
				<p><strong>Maximale Belastung: ${meter.allowedCurrent}A</strong></p>
				<p><strong>Anliegende Stromstärke: ${meter.current}V</strong></p>
				<p><strong>Anliegende Spannung: ${meter.voltage}V</strong></p>
			</div>
			<img src="${meter.imgSrc}"
			height="250px">
		</div>
		<h2>Ablesungen</h2>
		<ul class="readings">
			${readingsHTML}
		</ul>`
		);
	}
	
	/*
	Gibt HTML für die Übersicht zurück
	*/
	function renderOverviewHTML(){
		const meterInputs = $.map(smartMeters ,function(meter,i){
			return `<div><input type="radio" name=${meter.name} value=${i} id={meter.id}>${meter.name}<label for=${meter.name}</label></div>`;
		});
		const meterInputHTML = meterInputs.join("");
		$("#content").html(`<form id="submit-form">
			<fieldset>
				<legend>Verbrauchswert eintragen und ablesen lassen:</legend>
				<div>
					<label for="Value">Verbrauchswert</label>
					<input type="number" placeholder="Anzahl der Kilowattstunden" min="0" name="Value">
				</div>
				<div>
					${meterInputHTML}
				</div>
				<button id="submit">Eintragen</button>
			</fieldset>
		</form>`);
		
		/*
		Event Listener für das Formular. Sobald es abgeschickt wird, wird in das zugehörige
		Smart Meter Objekt ein neuer Eintrag eingefügt mit Datum, Nutzerkennung und kwH-Zahl.
		*/
		$("#submit").on("click", function(e){
			e.preventDefault();
			const formData = $('#submit-form').serializeArray();
			
			const data = {};
			
			data.kiloWattHours = formData[0].value;
			data.id = parseInt(formData[1].value)+1;

			
			$.ajax({
				method : "POST",
				url: "reading",
				data : data,
				success : (response) => {
					smartMeters = response;
					
					for(let meter of smartMeters){
						meter.id = "SM"+pad(meter.Id,8);
						meter.current = getCurrent(meter.allowedCurrent);
						meter.voltage = getVoltage();
					}
					
					renderNavigation();
					
					console.log("Ablesung erfolgreich");
				},
			});

			return false
		});
	}
	
	/*
	 * Zeigt Formelement für Smartmetererstellung an
	 */
	$("#new-meter").on("click",function(){
		$("#content").html(`<form id="new-meter-form">
		<fieldset>
				<legend>Daten für neues Smart Meter eingeben:</legend>
				<div>
					<label for="Name">Name</label>
					<input type="text" placeholder="Name des Smart Meters" name="Name">
				</div>
				<div>
					<label for="allowedCurrent">Erlaubte Stromstärke</label>
					<input type="number" placeholder="Erlaubte Stromstärke für das Gerät " min="0" name="allowedCurrent">
				</div>
				<div>
					<label for="img">Bild</label>
					<input type="text" placeholder="Optional: URL eines Bildes des Geräts" name="img"> 
				</div>
				<button id="submit-meter">Eintragen</button>
				<div id="sucess-message"><div>
		</fieldset>
		</form>`);		
		
		/* Erstellt neues Smart Meter und führt POST-Request aus
		 * 
		 */
		$("#submit-meter").on("click",function(e){
			e.preventDefault();
			
			const formData = $("form").serializeArray();
			let name, allowedCurrent, img;
			
			let data = {};
			if(!formData[0].value){
				$("#sucess-message").textContent = "Please enter a name";
				return false;
			}
			else data.name = formData[0].value;
			if(!formData[1].value){
				$("#sucess-message").textContent = "Please enter a valid maximum current";
				return false;
			}
			else data.allowedCurrent = formData[1].value;
			if(formData[2].value===""){
				data.imgSrc = "https://upload.wikimedia.org/wikipedia/commons/9/9a/Intelligenter_zaehler-_Smart_meter.jpg";		
			}
			else data.imgSrc = formData[2].value;
			
			/* Post Request für neues SmartMeter */
			$.ajax({
				method : "POST",
				url: "meter",
				data : data,
				success : (response) => {
					smartMeters = response;
					
					for(let meter of smartMeters){
						meter.id = "SM"+pad(meter.Id,8);
						meter.current = getCurrent(meter.allowedCurrent);
						meter.voltage = getVoltage();
					}
					
					renderNavigation();
					
					$("#success-message").textContent = "SmartMeter added";
				},
			});
		})
	})
	
	/* Fügt Smart Meter der Navigation hinzu */
	function renderNavigation(){
		
		$('ul li:gt(1)').remove(); 
		
		smartMeters.forEach(meter => {
			$("ul").append(`<li class="meter">${meter.name}</li>`);
		});
		
	}
	/*
	Berechnet zufällige Spannung für ein Smart Meter und zeigt dessen Daten an
	*/
	$("ul").delegate("li","click", function(event){
		const index = $(this).index();
		if(index>1){
			meter = smartMeters[index-2];
			meter.Voltage = getVoltage();
			$("#content").html(renderMeterHTML(meter));
		}
	});
	

	/* Initialisiert die Stromzähler, indem ein GET-Request ausgeführt wird. Anschließend werden Navigation und Übersicht angezeigt. */
	$.get({
		url: "meter",
		success : (response) => {
			smartMeters = response;
			
			for(let meter of smartMeters){
				meter.id = "SM"+pad(meter.Id,8);
				meter.current = getCurrent(meter.allowedCurrent);
				meter.voltage = getVoltage();
			}
			
			renderNavigation();
			renderOverviewHTML();
			
			$("#success-message").textContent = "SmartMeter added";
		},
	});
	
	/* Führt den Login eines Users aus. Dieser wird serverseitig im HTTPSession.Objekt gespeichert */
	$("#login").on("click", function(){
		
		const data = {};
		data.userName = $("#username").val();
		
		$.post({
			url: "index",
			data: data,
			success : (response) => {
				console.log("logged in!");
			}
		})
	})
	/* zeigt standardmäßig die Übersicht an*/
	
	$("#home").on("click", function(e){
		renderOverviewHTML();
	})
});
