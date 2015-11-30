
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Index page</title>
	<script src="<c:url value="/resources/js/jquery.js" />"></script>
	
<script type="text/javascript">
$(document).ready(function(){
	
	$('#getFBData').click(function(){
		
		var userAccessToken =$ ('#userAccessToken').val();
		var pageID = $('#pageID').val();
		
		// paramter 
		var fbParameters = {
				"userAccessToken" : userAccessToken,
				"pageID": pageID
		};
		
		var data = {};
		data[0] = JSON.stringify(fbParameters);
		
		$.ajax({
            url: "./saveFBData",
            type: "POST",
            dataType: 'json',
            data: data,
            succes: function (response) {
            	var rs = JSON.parse(response);
            	if(rs.ID == 1){
            		alert('Hello world!');
            	} else {
            		alert('Error!');
            	}
            },
            error: function(response) {
            	alert('Hellos!');
             }
        });
    });
	
});

</script>	
	
</head>
<body>
<h1>
	Analysis page for LDA!
</h1>

<%-- <P>  The very first post: ${myFeed}. </P> --%>
<form>
	<table>
		<tr>
			<td><h3>Input your access token</h3></td>
		</tr>
			<tr>
				<td><label>User access token:</label></td>
				<td><input id="userAccessToken" type="text" maxlength="300"
					required="required" placeholder="User access token"
					value="CAACEdEose0cBANgpeCb1Nluu5JOTwSldYwWMOzIuZAILZCyA8GMx53dboaOZCyHrsnIH1t7K989xOg6Bb1rNnZALFMyl0stPEQjZCUXZBrZBJUG36V8hrJ6ck1YKzRJK5wZAjgS1bVXChDmlsoIRYwZAZB0ZCpRReQ4hwX96qWZB8xx5VpUlybqZBZCApVeB8xP7Px6o9MzCNLNNm5bAZDZD"></td>
				<td><label>Page ID:</label></td>
				<td><input id="pageID" type="text"
					placeholder="Page ID"
					value="500541623341892"></td>
			</tr>
			<tr>
			<td><button type="button" id="getFBData">Get data</button>
		</tr>
	</table>
</form>

</body>
</html>
