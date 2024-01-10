<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%

	String error = (String)request.getAttribute("error");

%>    
<html>
<head>
<meta charset="UTF-8">
<title>MyBank</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>


<style>
.navbar {
	background-color: rgb(139, 154, 139);
}

.backend a {
	position: absolute;
	top: 10px;
	right: 20px;
}

body {
	font-family: Arial, sans-serif;
	background-color: #f4f4f4;
	margin: 0;
	padding: 0;
}

form {
	background-color: #fff;
	padding: 20px;
	padding-top: 40px;
	padding-bottom: 10px;
	border-radius: 8px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	width: 500px;
	height: auto;
}
</style>


</head>


<body>
	<nav class="navbar navbar-expand-lg navbar-light position-stiky top-0">
		<div class="ms-1">
			<a class="navbar-brand text-light" href="#">MyBank後台系統</a>
		</div>
		<div class="backend " id="">
			<a class="text-white" href="./login">前台登入</a>

		</div>

	</nav>
	




	<div class="d-flex justify-content-center align-items-center vh-100">
		<form class="needs-validation" novalidate method="post"
			action="./login_manager">
			<h4 class="text-center">Login</h4>
			<%
	    	  	if(error != null) {
	    	  		out.print("<p class='text-center text-danger fw-bold'>"+error+"</p>");
	    	  	}
	    	  %>

			<div>
				<label for="username" class="form-label">ManagerId</label> 
				<input
					type="text" class="form-control" id="managername" name="managername"
					value="" required placeholder="請輸入工號">
				<div class="invalid-feedback">請輸入工號</div>
			</div>
			<div>
				<label for="password" class="form-label">Password</label> <input
					type="password" class="form-control" id="password" name="password"
					value="" required placeholder="請輸入密碼">
				<div class="invalid-feedback">請輸入密碼</div>
			</div>
			<div class="d-flex justify-content-center mt-3">
				<button class="btn btn-primary" type="submit">登入</button>
			</div>
					<div style="color: red">${ loginMessage }</div>
		</form>
	</div>
</body>
</html>

<script type="text/javascript">
	//Example starter JavaScript for disabling form submissions if there are invalid fields
	(function() {
		'use strict'

		// Fetch all the forms we want to apply custom Bootstrap validation styles to
		var forms = document.querySelectorAll('.needs-validation')

		// Loop over them and prevent submission
		Array.prototype.slice.call(forms).forEach(function(form) {
			form.addEventListener('submit', function(event) {
				if (!form.checkValidity()) {
					event.preventDefault()
					event.stopPropagation()
				}

				form.classList.add('was-validated')
			}, false)
		})
	})()
	
	
</script>



<%@ include file="../include/header/footer.jspf"%>