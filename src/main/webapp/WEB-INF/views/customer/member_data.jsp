<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form"%>
<%@ include file="../include/header/header_custom.jspf"%>





<head>
<style>
.container {
	max-width: 75vw;
	background-color: #fff;
	padding: 20px;
	border-radius: 8px;
	margin-bottom: 200px;
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

	<div class="container">
		<h2 class="  fw-bold mb-3 text-center page-title ">
			<i class="bi bi-person-circle mb-5"></i>會員資料
		</h2>
		<div class="profile-details">
			<form method="post" action="./data_change_page">
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
						<td>目前持有帳戶</td>
						<td><c:forEach items="${ accountlist }" var="accountlist">
						${ accountlist.currency_currencyname}
						</c:forEach></td>
					</tr>
					<!-- 其他會員資料項目... -->
				</table>
				<button type="submit" class="btn button-primary">${ submitBtnName }</button>
				<div style="color: red">${ sussesMessage }</div>
			</form>
		</div>

		<h2 class="  fw-bold mb-3 text-center page-title ">
			<i class="bi bi-person-circle mb-5"></i>外幣帳戶申請
		</h2>
		<div class="ForeignAccount">


			<form method="post" action="./addaccount" class="row g-3"
				onsubmit="return validateForm()">

				<div class="text-center w-75 mx-auto">
					<ul style="list-style-type: none" class="text-start">
						<li><i class="bi bi-1-square-fill"></i>&nbsp;全年24小時提供「買/賣外幣」、「幣別轉換」及「行內轉帳」服務。</li>
						<li><i class="bi bi-2-square-fill d-inline"></i>&nbsp;當日買/賣外幣交易(含臨櫃及自動化通路交易)累計達等值新臺幣50萬元(含)以上者，交易時間為營業日09:00-15:30。</li>
						<li><i class="bi bi-3-square-fill"></i>&nbsp;營業日09:00至23:00提供「轉定存」、「定存解約」服務。</li>
						<li><i class="bi bi-4-square-fill"></i>&nbsp;營業日09:00至17:00提供「外幣跨行匯款」、「外幣匯入解款」服務。</li>
					</ul>
				</div>

				<select class="form-select mb-4" aria-label="Default select example"
					id="Foreignaccount" name="Foreignaccount">
					<option value="" disabled selected>請選擇帳戶</option>
					<c:forEach items="${Unregisteredcurrency}" var="currency">
						<option value="${currency.currencyId}">
							${currency.currencyname}帳戶</option>
					</c:forEach>
				</select>

				<button type="submit" class="btn button-primary">新增外幣帳戶</button>
			</form>

			<script>
				function validateForm() {
					var selectedAccount = document
							.getElementById("Foreignaccount").value;
					if (selectedAccount === "") {
						alert("請選擇帳戶");
						return false; // 阻止表單提交
					}
					// 其他檢查邏輯或處理
					return true; // 允許表單提交
				}
			</script>

		</div>
	</div>
	<%@ include file="../include/header/footer.jspf"%>
</body>

</html>




