<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ include file="../include/header/header_custom.jspf"%>





<head>
<style>
.container {
	max-width: 75vw;
	background-color: #fff;
	padding: 20px;
	border-radius: 8px;
}

.profile-details {
	display: flex;
	flex-direction: column;
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

.btn {
	padding: 10px 20px;
	background-color: #007bff;
	color: #fff;
	border: none;
	border-radius: 4px;
	text-decoration: none;
	cursor: pointer;
	text-align: center;
}

.btn:hover {
	background-color: #0056b3;
}
</style>


</head>

<body>
	<h2 class="  fw-bold mb-3 text-center page-title ">
		<i class="bi bi-person-circle mb-5"></i>會員資料
	</h2>
	<div class="container">


		<div class="profile-details">
			<form method="post" action="./data_change">
				<input name="_method" type="hidden" value="${ _method }" />
				<table>
					<tr>
						<th>項目</th>
						<th>資料</th>
					</tr>
					<tr>
						<td>姓名</td>
						<td>${ user.username }</td>
					</tr>
					<tr>
						<td>身分證字號</td>
						<td>${ user.userId }</td>
					</tr>
					<tr>
						<td>請輸入新電子郵件</td>
						<td><input type="email" name="newEmail"
							value="${ user.email }" /></td>
					</tr>
					<tr>

						<td>請輸入舊密碼</td>
						<td><input type="password" name="oldPassword"
							value="${ user.password }" /></td>
					</tr>
					<tr>
						<td>請輸入新密碼</td>
						<td><input type="password" name="newPasswords" value="" /></td>
					</tr>


					<tr>
						<td>請再輸入一次新密碼</td>
						<td><input type="password" name="newPasswords" value="" /></td>
					</tr>


					<tr>
						<td>生日</td>
						<td><fmt:formatDate value="${ user.birth }"
								pattern="yyyy-MM-dd" /></td>
					</tr>
					<tr>
						<td>性別</td>
						<td>${ user.sex.name}</td>
					</tr>
					<tr>
						<td>註冊日期</td>
						<td>${ user.registDate }</td>
					</tr>
					<tr>
						<td>目前持有帳戶</td>
						<td><c:forEach items="${ accountlist }" var="accountlist">
						${ accountlist.currency_currencyName }
						</c:forEach></td>
				
					</tr>
				</table>

				<button type="submit" class="btn button-primary">${ submitBtnName }</button>
				<div style="color: red">${ errorMessage }</div>
				<div style="color: red">${ sussesMessage }</div>
			</form>

		</div>
	</div>
	<%@ include file="../include/header/footer.jspf"%>
</body>

</html>




