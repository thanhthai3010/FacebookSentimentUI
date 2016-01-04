<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>View all of topics</title>
  <link rel="shortcut icon"  href="<c:url value="/resources/image/favicon.ico" />"  type='image/vnd.microsoft.icon'/>
<script src="<c:url value="/resources/js/jquery.js" />"></script>
<script src="<c:url value="/resources/js/d3.js" />"></script>
<script src="<c:url value="/resources/js/d3.layout.cloud.js" />"></script>
<!-- Latest compiled JavaScript -->
<script
	src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
<script
	src="<c:url value="/resources/js/viewTopics.js" />"></script>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="<c:url value="/resources/css/bootstrap.min.css" />">

<link href='https://fonts.googleapis.com/css?family=Roboto+Slab:400,700&subset=latin,vietnamese,latin-ext' rel='stylesheet' type='text/css'>

<style type="text/css">
#display {
	margin: 0 auto;
	text-align: center;
}

.onLine {
	float: left;
}

.breakLine {
	clear: both;
}

svg {
	transition:0.5s;
}

svg:hover {
	background-color: wheat;
	cursor: pointer;
	transition:0.5s;
}
</style>

</head>
<body>
	<div class="container">
		<h2>Hottest topics of ${pageName} from ${dateFrom} to ${dateTo}</h2>
		<div id=display></div>
	</div>
</body>
</html>