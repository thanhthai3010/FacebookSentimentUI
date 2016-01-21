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

function divClick(div) {
	var pageID = $('#pageID').val();
	var index = div.getAttribute('data-value');
	var url = "./pieChart?id=" + index +"&pageID=" + pageID;
	window.location = url;
}


function divHover(div) {
	var topicIndex = div.getAttribute('data-value');
	var topicId = "#id" + topicIndex;
	
	var tableToolTip = "#tableId" + topicIndex;

	var delay = 1000, setTimeoutConst;
	
	$(topicId).hover(function(event) {
		var offsetX = event.clientX + 20;
		var offsetY = event.clientY + 50;
		setTimeoutConst = setTimeout(function() {
			// do something
			$(tableToolTip).css({
				left : offsetX,
				top : offsetY
			});
			$(tableToolTip).stop(true, true).fadeIn();
		}, delay);

	}, function(ev) {
		clearTimeout(setTimeoutConst);
		$(tableToolTip).stop(true, true).fadeOut();
	}).mousemove(function(ev) {
		// $(tableToolTip).css({left:ev.pageX - 300,top:ev.pageY - 50});
	});
}


function generateTopic(obj, col) {
	var str = "";
	str += String.format(
					'<div class="{0} topic" id="{1}" onclick="divClick(this)" data-value="{2}" onmouseover="divHover(this)"></div>',
					col, "id" + obj.idTopic, obj.idTopic);
	return str;
}

$(function() {

	$.get("./getListTopic", function(data) {

		var listTopic = JSON.parse(data);
		switch (listTopic.length) {
		case 2:
			for (var i = 0; i < listTopic.length; i++) {
				$('#display').append(generateTopic(listTopic[i], "col-md-6"));
			}
			break;
		case 3:
			for (var i = 0; i < listTopic.length; i++) {
				$('#display').append(generateTopic(listTopic[i], "col-md-4"));
			}
			break;
		case 4: // hien thi 2 dong 2-2
			for (var i = 0; i < listTopic.length; i++) {
				$('#display').append(generateTopic(listTopic[i], "col-md-6"));
				if (i == 1)
					$('#display').append("<div class='breakLine'></div>");
			}
			break;
		case 5: // hien thi 3-2
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
			for (var i = 0; i < listTopic.length; i++) {
				$('#display').append(generateTopic(listTopic[i], "col-md-4"));
				if (i == 2)
					$('#display').append("<div class='breakLine'></div>");
			}
			break;
		default:
			// hien thi 1 dong
			for (var i = 0; i < listTopic.length; i++) {
				$('#display').append(generateTopic(listTopic[i], "col-md-4"));
			}
			break;
		}

		// call function draw
		for (var j = 0; j < listTopic.length; j++) {
			drawWordCloud(listTopic[j].textValues, "#id" + listTopic[j].idTopic);
		}

		// new 
		drawHiddenTable(listTopic);
		
	});

	
	/* draw hidden table for show tooltip */
	function generateTableDiv(topicID) {
		var str = "";
		str += String.format(
						'<table id="{0}" style="display: none; position:absolute; background:#FFDEAD	 !important; ">\
			            <tr><th>Word</th> <th>Probability</th></tr>\
						</table>', topicID);
		return str;
	}
	function drawHiddenTable(dataTopic) {
		for (var i = 0; i < dataTopic.length; i++) {
			$('#table').append(generateTableDiv(("tableId" + dataTopic[i].idTopic)));
		}
		
		for (var i = 0; i < dataTopic.length; i++) {
			createHiddenTableData(dataTopic[i].textValues, ("#tableId" + dataTopic[i].idTopic))
		}
		
		
		// show data
	}
	
	function createHiddenTableData(tableData, id) {
		var trHTML = '';
		$.each(tableData, function (i, item) {
            trHTML += '<tr><td>' + item.text + '</td><td>' + item.value + '</td></tr>';
        });
        $(id).append(trHTML);
	}
	
	// function draw word cloud
	function drawWordCloud(data, id) {

		var fill = d3.scale.category20();

		d3.layout.cloud().size([ 300, 300 ]).words(data.map(function(d) {
			return {
				text : d.text,
				size : d.value*550 + 10
			};
		})).rotate(function() {
			return ~~Math.floor(Math.random() * 30) - 20;
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