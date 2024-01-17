<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="../include/header/header_manager.jspf"%>
<!DOCTYPE html>
<html>
<head>
<script
	src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
<style>
.nav-pills .nav-link.active, .nav-pills .show>.nav-link {
	background-color: #97a9c3;
}

.nav-link {
	color: #97a9c3
}

.container {
	margin-top: 80px;
}

.profile-details {
	display: flex;
	flex-direction: column;
}

.page-title {
	margin-top: 100px;
}

h1 {
	text-align: center;
	margin-bottom: 30px;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-bottom: 30px;
}

th, td {
	padding: 10px;
	border-bottom: 1px solid #ddd;
	text-align: left;
}

th {
	background-color: #f2f2f2;
}
</style>

</head>
<body>

	<div class="cotainer ">
		<h2 class="  fw-bold mb-3 text-center page-title ">
			<i class="bi bi-person-circle mb-5"></i>${ user.username }會員資料
		</h2>
		<div class="col w-75 mx-auto">
			<div class="profile-details">

				<table>
					<tr>
						<th>項目</th>
						<th>資料</th>
					</tr>
					<tr>
						<td>用戶姓名</td>
						<td>${ user.username }</td>
					</tr>
					<tr>
						<td>身分證字號</td>
						<td>${ user.userId }</td>
					</tr>
					<tr>
						<td>電子郵件</td>
						<td>${ user.email }</td>
					</tr>

					<tr>
						<td>生日</td>
						<td><fmt:formatDate value="${ user.birth }"
								pattern="yyyy-MM-dd" /></td>
					</tr>
					<tr>
						<td>性別</td>
						<td>${ user.sex.name }</td>
					</tr>
					<tr>
						<td>註冊日期</td>
						<td><fmt:formatDate value="${ user.registDate }"
								pattern="yyyy-MM-dd" /></td>
					</tr>
					<tr>
					<td>證件</td>
						<td><img src="data:image/jpeg;base64,${ user.imgContent }"
							class="card-img-top flag" alt=""
							style="width: 200px; height: auto;"></td>
					</tr>
					<tr>
						<td>目前持有帳戶</td>
						<td><c:forEach items="${ accountlist }" var="accountlist">
						${ accountlist.currency_currencyname}
						</c:forEach></td>
					</tr>
					<!-- 其他會員資料項目... -->
				</table>
				<div class="text-center mb-5 mt-3">
					<button type="button" class="btn btn-outline-secondary  mx-2"
						onclick="window.top.location.href='./passuser'">返回總覽</button>
				</div>

			</div>

		</div>

	</div>

</body>

</html>


