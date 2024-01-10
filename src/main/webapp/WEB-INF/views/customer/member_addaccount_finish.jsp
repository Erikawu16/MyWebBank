<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ include file="../include/header/header_custom.jspf"%>





<head>
<style>
.container {
	margin-top: 100px;
}

table {
	border-collapse: collapse;
	width: 50%;
	margin: 20px auto;
}

th, td {
	border: 1px solid #ddd;
	padding: 8px;
	text-align: left;
}

th {
	background-color: #f2f2f2;
}
</style>
</head>
<body>
	<div class="container">
		<h1 class="text-center">新外幣帳戶創建成功</h1>

		<table>
			<tr>
				<th>項目</th>
				<th>詳細資訊</th>
			</tr>
			<tr>
				<td>使用者名稱</td>
				<td>${user.username}</td>
			</tr>
			<tr>
				<td>新建帳戶號</td>

				<td>${account.accountId}</td>

			</tr>
			<tr>
				<td>新建帳戶幣別</td>
				<td>${Foreignaccount.currencyname}</td>
			</tr>
			<tr>
				<td>創立時間</td>
				<td><fmt:formatDate value="${account.create_time}"
						pattern="yyyy-MM-dd HH:mm:ss" /></td>

			</tr>
		</table>
		<div class="text-center mt-5">
			<div class="mx-auto" style="width: 200px;">
				<!-- 使用 mx-auto 讓容器水平置中，設定容器寬度 -->
				<button type="button" class="btn btn-primary"
					onclick="window.location.href='/MyWebBank/mvc/mybank/customer/mydata'">返回會員專區</button>
			</div>
		</div>
	</div>
</body>

</html>


<%@ include file="../include/header/footer.jspf"%>

