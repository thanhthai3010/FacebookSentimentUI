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
	var index = div.getAttribute('data-value');
	var url = "./pieChart?id=" + index;
	window.location = url;
}

function generateCode(obj, col) {
	var str = "";
	str += String
			.format(
					'<div class="{0} topic" id="{1}" onclick="divClick(this)" data-value="{2}" ></div>',
					col, "id" + obj.idTopic, obj.idTopic);
	return str;
}

$(function() {

	$.get("./getListTopic", function(data) {

		var objs = JSON.parse(data);
		switch (objs.length) {
		case 2:
			for (var i = 0; i < objs.length; i++) {
				$('#display').append(generateCode(objs[i], "col-md-6"));
			}
			break;
		case 3:
			for (var i = 0; i < objs.length; i++) {
				$('#display').append(generateCode(objs[i], "col-md-4"));
			}
			break;
		case 4: // hien thi 2 dong 2-2
			for (var i = 0; i < objs.length; i++) {
				$('#display').append(generateCode(objs[i], "col-md-6"));
				if (i == 1)
					$('#display').append("<div class='breakLine'></div>");
			}
			break;
		case 5: // hien thi 3-2
			for (var i = 0; i < objs.length; i++) {
				if (i < 3) {
					$('#display').append(generateCode(objs[i], "col-md-4"));
				} else {
					$('#display').append(generateCode(objs[i], "col-md-6"));
				}
				if (i == 2)
					$('#display').append("<div class='breakLine'></div>");
			}
			break;
		case 6: // hien thi 3-3
			for (var i = 0; i < objs.length; i++) {
				$('#display').append(generateCode(objs[i], "col-md-4"));
				if (i == 2)
					$('#display').append("<div class='breakLine'></div>");
			}
			break;
		default:
			// hien thi 1 dong
			for (var i = 0; i < objs.length; i++) {
				$('#display').append(generateCode(objs[i], "col-md-4"));
			}
			break;
		}

		// call function draw
		for (var j = 0; j < objs.length; j++) {
			drawWordCloud(objs[j].textValues, "#id" + objs[j].idTopic);
		}

	});

	// function draw word cloud
	function drawWordCloud(data, id) {

		var fill = d3.scale.category20();

		d3.layout.cloud().size([ 300, 300 ]).words(data.map(function(d) {
			return {
				text : d.text,
				size : d.value + 20
			};
		})).rotate(function() {
			return ~~Math.floor(Math.random() * 20) - 20;
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