<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../include/header/header_index.jspf"%>
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
	background-color: rgb(225, 135, 127);
	z-index: 1000;
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
			<a class="navbar-brand text-light" href="#">MyBank忘記密碼</a>
		</div>
		<div class="backend " id="">
			<a class="text-white" href="../index.jsp">返回會員登入</a>

		</div>

	</nav>


	<div class="d-flex justify-content-center align-items-center vh-100">
		<form class="needs-validation" novalidate method="post" action="./otpsend">

			<h4 class="text-center">忘記密碼</h4>


			<div>
		<input type="" class="form-control" name="userId" value="${userId}">
		<input type="" class="form-control" name="OTPcode" value="${OTPcode}">
				<p>信箱驗證正確!!驗證碼已發送到您的信箱</p>
				<p>登入後請盡快更改密碼</p>
				

				
				<label class="form-label">請輸入驗證碼</label> 
				<input type="text"
					class="form-control" id="validcode" name="validcode" value=""
					required placeholder="請輸入驗證碼">
			</div>
			<div class="text-center my-3">
				<button class="btn btn-secondary " type="submit">送出驗證碼</button>
			</div>

		</form>
	</div>
</body>
</html>





<%@ include file="../include/header/footer.jspf"%>