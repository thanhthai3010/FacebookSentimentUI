<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Social Data Page</title>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script src="<c:url value="/resources/js/jquery.js" />"></script>
<script
	src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
<link rel="stylesheet" href="<c:url value="/resources/css/getFBData.css" />">

<script type="text/javascript">


	$(document).ready(function() {

		$('#getFBData').click(function() {

			var userAccessToken = $('#userAccessToken').val();
			var pageID = $('#pageID').val();

			// paramter 
			var fbParameters = {
				"userAccessToken" : userAccessToken,
				"pageID" : pageID
			};

			var data = {};
			data[0] = JSON.stringify(fbParameters);

			$.ajax({
				url : "./saveFBData",
				type : "POST",
				dataType : 'json',
				data : data,
				succes : function(response) {
					var rs = JSON.parse(response);
					if (rs.ID == 1) {
						alert('Hello world!');
					} else {
						alert('Error!');
					}
				},
				error : function(response) {
					alert('Hellos!');
				}
			});
		});

	});
</script>

</head>

<body>

	<div class="getData">
		<div class="heading">
			<h2>
				Welcome to <br> Social Crawling Data
			</h2>
			<form  role="form" >

				<div class="input-group input-group-lg">
					<span class="input-group-addon"><i
						class="glyphicon glyphicon-user"></i></span> 
						<input id="userAccessToken" class="form-control"  placeholder="User Access token" type="text">
				</div>

				<div class="input-group input-group-lg">
					<span class="input-group-addon"><i
						class="glyphicon glyphicon-th-list"></i></span> 
						<input id="pageID" type="text" class="form-control"  placeholder="Facebook Page ID">
				</div>
				
				<div class="input-group input-group-lg">
					<span class="input-group-addon">
					<i class="glyphicon glyphicon-calendar"></i>
					</span>
					<input type="date" class="form-control"  id="inputDate" >
				</div>
				<button type="button" id="getFBData">Start Crawling Data</button>
			</form>
		</div>
	</div>
</body>
</html>