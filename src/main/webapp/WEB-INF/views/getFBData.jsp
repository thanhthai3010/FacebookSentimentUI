<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Social Data Page</title>

<!-- JavaScript file -->
<script src="<c:url value="/resources/js/jquery.js" />"></script>
<script
	src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap-dialog.min.js" />"></script>

<!-- Bootstrap Core CSS -->
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="<c:url value="/resources/css/getFBData.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/screenLoading.css" />">
<link rel="stylesheet"
	href="<c:url value="/resources/css/navigation.css" />">
<link rel="stylesheet"
	href="<c:url value="/resources/css/bootstrap-dialog.min.css" />">
	
<script type="text/javascript">


	$(document).ready(function() {

		$('#getFBData').click(function() {
			
			var userAccessToken = $('#userAccessToken').val();
			var pageID = $('#pageID').val();
			
			var date = $('#inputDate').val();
			var d=new Date(date.split("/").reverse().join("-"));
			var dd=d.getDate();
			var mm=d.getMonth()+1;
			var yy=d.getFullYear();
			var newDate = "0";
			var newMonth = "0";
			
			if (Math.floor(dd / 10) == 0)
				newDate += dd.toString();
			else
				newDate = dd.toString();

			if (Math.floor(mm / 10) == 0)
				newMonth += mm.toString();
			else
				newMonth = mm.toString();
			var inputDate = newMonth + "/" + newDate + "/" + yy;

			
			if (userAccessToken != '' && pageID != '' && inputDate != '') {
				$('#loader-wrapper').show();
				// paramter 
				var fbParameters = {
					"userAccessToken" : userAccessToken,
					"pageID" : pageID,
					"inputDate": inputDate
				};

				var data = {};
				data[0] = JSON.stringify(fbParameters);
				
				var success = function(response) {
					$('#loader-wrapper').hide();
					 BootstrapDialog.show({
						 	type: BootstrapDialog.TYPE_SUCCESS,
				            title: 'Successful Message',
				            message: response
				        });
				};
				var error = function() {
					$('#loader-wrapper').hide();
					 BootstrapDialog.show({
						 	type: BootstrapDialog.TYPE_DANGER,
				            title: 'Danger Message',
				            message: 'Can not get data from Facebook'
				        });
				};
				var ajaxObject = {
						url : 'saveFBData',
						type : 'POST',
						dataType : 'json',
						data : data,
						success : success,
						error : error
					};
				// calling
				$.ajax(ajaxObject);
			} else {
				 BootstrapDialog.show({
					 	type: BootstrapDialog.TYPE_DANGER,
			            title: 'Danger Message',
			            message: 'Please enter missing information!'
			        });
			}
			
		});

	});
</script>

</head>

<body>
<jsp:include page="navigation.jsp" ></jsp:include>
<!-- Loading screen -->
	<div id="loader-wrapper">
		<div id="box-light"></div>
		<div id="loader"></div>
	</div>

	<div class="getData">
		<div class="heading">
			<h2>
				Welcome to <br> Social Crawling Data
			</h2>
			<form  role="form" >

				<div class="input-group input-group-lg">
					<span class="input-group-addon"><i
						class="glyphicon glyphicon-user"></i></span> 
						<input id="userAccessToken" class="form-control"  placeholder="User Access token" type="text" required>
				</div>

				<div class="input-group input-group-lg">
					<span class="input-group-addon"><i
						class="glyphicon glyphicon-th-list"></i></span> 
						<input id="pageID" type="text" class="form-control"  placeholder="Facebook Page ID" required>
				</div>
				
				<div class="input-group input-group-lg">
					<span class="input-group-addon">
					<i class="glyphicon glyphicon-calendar"></i>
					</span>
					<input type="date" class="form-control"  id="inputDate"  required>
				</div>
				<button type="button" id="getFBData">Start Crawling Data</button>
			</form>
		</div>
	</div>
</body>
</html>