String.format = function() {
	// The string containing the format items (e.g. "{0}")
	// will and always has to be the first argument.
	var theString = arguments[0];

	// start with the second argument (i = 1)
	for (var i = 1; i < arguments.length; i++) {
		// "gm" = RegEx options for Global search (more than one instance)
		// and for Multiline search
		var regEx = new RegExp("\\{" + (i - 1) + "\\}", "gm");
		theString = theString.replace(regEx, arguments[i]);
	}

	return theString;
}

/* define click function foreach topic, after that we will show detail of topic */
function divClick(div) {
	var pageID = $('#pageID').val();
	var index = div.getAttribute('data-value');
	var url = "./pieChart?id=" + index +"&pageID=" + pageID;
	window.location = url;
}

/*
 * When user hover topic
 * Display tooltip show information of each topic
 *  */
function divHover(div) {
	var topicIndex = div.getAttribute('data-value');
	var topicId = "#id" + topicIndex;
	
	var tableToolTip = "#tableId" + topicIndex;
	
	    $(topicId).qtip({
		overwrite : false,
		content : {
			text : $(tableToolTip)
		},
		style : {
			classes : 'qtip-blue qtip-bootstrap'
		},
		show : {
			delay : 1000,
			when : 'mouseover',
			ready : true
		},
		hide : {
			delay : 0,
			fixed : true
		},
		position : {
			target : 'mouse',
			adjust : {
				mouse : false
			}
		}
	});
}

/*
* Create html code foreach topic
 *  */
function generateTopic(obj, col) {
	var str = "";
	str += String.format(
					'<div class="{0} topic" id="{1}" onclick="divClick(this)" data-value="{2}" onmouseover="divHover(this)"></div>',
					col, "id" + obj.idTopic, obj.idTopic);
	return str;
}
$("p").css("padding", "1px");

/**
 * Main function
 */
$(function() {
	
	/**
	 * Call ajax to get data from service
	 */
	var amountRow = 0;
	$.get("./getListTopic", function(data) {

		listTopic = JSON.parse(data);
		switch (listTopic.length) {
		case 2:
			amountRow = 1;
			for (var i = 0; i < listTopic.length; i++) {
				$('#display').append(generateTopic(listTopic[i], "col-md-6"));
			}
			break;
		case 3:
			amountRow = 1;
			for (var i = 0; i < listTopic.length; i++) {
				$('#display').append(generateTopic(listTopic[i], "col-md-4"));
			}
			break;
		case 4: // hien thi 2 dong 2-2
			amountRow = 2;
			for (var i = 0; i < listTopic.length; i++) {
				$('#display').append(generateTopic(listTopic[i], "col-md-6"));
				if (i == 1)
					$('#display').append("<div class='breakLine'></div>");
			}
			break;
		case 5: // hien thi 3-2
			amountRow = 2;
			for (var i = 0; i < listTopic.length; i++) {
				if (i < 3) {
					$('#display').append(generateTopic(listTopic[i], "col-md-4"));
				} else {
					$('#display').append(generateTopic(listTopic[i], "col-md-6"));
				}
				if (i == 2)
					$('#display').append("<div class='breakLine'></div>");
			}
			break;
		case 6: // hien thi 3-3
			amountRow = 2;
			for (var i = 0; i < listTopic.length; i++) {
				$('#display').append(generateTopic(listTopic[i], "col-md-4"));
				if (i == 2)
					$('#display').append("<div class='breakLine'></div>");
			}
			break;
		default://7 8 9
			// hien thi 1 dong
			amountRow = 3;
			for (var i = 0; i < listTopic.length; i++) {
				$('#display').append(generateTopic(listTopic[i], "col-md-4"));
			}
			break;
		}

		// call function draw WordCloud of each topic
		for (var j = 0; j < listTopic.length; j++) {
			drawWordCloud(listTopic[j].textValues, "#id" + listTopic[j].idTopic);
		}

		// draw hidden table, for create tooltip
		drawHiddenTable(listTopic);
		var height = amountRow*300 + 15;
		$("#display").css("height", height + "px").css("transition","0.5s");
		
	});

	
	/* draw hidden table for show tooltip */
	function generateTableDiv(topicID) {
		var str = "";
		str += String.format(
				'<table id="{0}" style="display: none;"></table>',
				topicID);
		return str;
	}
	
	/**
	 * Add table of tooltip
	 */
	function drawHiddenTable(dataTopic) {
		for (var i = 0; i < dataTopic.length; i++) {
			$('#table').append(
					generateTableDiv(("tableId" + dataTopic[i].idTopic)));
		}

		for (var i = 0; i < dataTopic.length; i++) {
			createHiddenTableData(dataTopic[i].textValues,
					("#tableId" + dataTopic[i].idTopic))
		}
	}
	
	/**
	 * Create html code for hidden table
	 */
	function createHiddenTableData(tableData, id) {
		var trHTML = '';
		$.each(tableData, function (i, item) {
            trHTML += '<tr><td>' + item.text + '</td><td>' + item.value.toFixed(3) + '</td></tr>';
        });
        $(id).append(trHTML);
	}
	
	/**
	 * Draw WordCloud of topic
	 */
	function drawWordCloud(data, id) {

		var fill = d3.scale.category20();

		d3.layout.cloud().size([ 300, 300 ]).words(data.map(function(d) {
			return {
				text : d.text,
				size : d.value*550 + 10
			};
		})).rotate(function() {
			return ~~Math.floor(Math.random() * 30) - 26;
		}).font("Roboto Slab").fontSize(function(d) {
			return d.size;
		}).on("end", draw).start();

		function draw(words) {
			d3.select(id).append("svg").attr("width", 300).attr("height", 300)
					.append("g").attr("transform", "translate(150,150)")
					.selectAll("text").data(words).enter().append("text")
					.style("font-size", function(d) {
						return d.size + "px";
					}).style("font-family", "Roboto Slab").style("fill",
							function(d, i) {
								return fill(i);
							}).attr("text-anchor", "middle").attr(
							"transform",
							function(d) {
								return "translate(" + [ d.x, d.y ] + ")rotate("
										+ d.rotate + ")";
							}).text(function(d) {
						return d.text;
					});
		}
	}
});

//qtran
function filterTopic(){
	var strSearch = document.getElementById("searchBox").value;
	var flag = false;
	var topicId = 0;
	for (var i = 0; i < listTopic.length; i++) {
		flag = false;
		$.each(listTopic[i].textValues, function (i, item) {
			if((item.text.toLowerCase().indexOf(strSearch.trim().replace(new RegExp(" ", 'g'), "_").toLowerCase()) > -1) || strSearch === ''){
				flag = true;
				//$("#id" + (i + 1)).hide();
			}
            //trHTML += '<tr><td>' + item.text + '</td><td>' + item.value + '</td></tr>';
        });
		
		topicId = i + 1;
		if(!flag){
			$("#id" + topicId).fadeOut(resizeDisplayTopic);
		} else {
			$("#id" + topicId).fadeIn(resizeDisplayTopic);
		}
		//resizeDisplayTopic();
		//$('#display').append(generateTopic(listTopic[i], "col-md-6"));
	}
 
}

function resizeDisplayTopic()
{
	var counter = 0;
	 $("#display .topic").each(function(){
		 if($(this).is(":visible")){
			 counter++;
		 }
	 });
	 var amountR = Math.ceil(counter/3);
	 var height = amountR*300 + 15;
	 $("#display").css("height", height + "px");
}

