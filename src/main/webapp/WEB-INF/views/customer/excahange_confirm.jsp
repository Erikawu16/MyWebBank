<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form"%>


<%@ include file="../include/header/header_custom.jspf"%>
<style>
.btn {
	border: 1px solid rgb(128, 128, 128);
}

.btn:hover {
	background-color: rgb(179, 199, 180);
}

.container {
	margin: 80px auto;
	width: 300px;
	font-family: Arial, sans-serif;
}

table {
	width: 100%;
}

th, td {
	padding: 8px;
	text-align: left;
	border-bottom: 1px solid #ddd;
}

th {
	background-color: #f2f2f2;
}
</style>


<div class="container">
	<h2 class="my-3">【明細確認】</h2>
	<form id="exchangeForm" method="post" action="./excahange_finish">
		<input name="_method" type="hidden" value="${ _method }" />
		<table>
			<tr>
				<th>項目</th>
				<th>明細</th>
			</tr>
			<tr>
				<td>轉出帳號：</td>
				<td><input type="hidden" name="accountoutId"
					value="${accountout.accountId}" readonly>
					${accountout.accountId}</td>
			</tr>
			<tr>
				<td>轉入帳號：</td>
				<td><input type="hidden"  name="accountinId"
					value="${accountin.accountId}" readonly>
					${accountin.accountId}</td>
			</tr>
			<tr>
				<td>轉出貨幣：</td>
				<td><input type="hidden"  name="moneyOutCurrencyId"
					value="${moneyOutSelect.currencyId}" readonly>${moneyOutSelect.currencyname}</td>
			</tr>
			<tr>
				<td>轉出金額：</td>
				<td><input type="hidden"  name="moneyOutAmount"
					value="${moneyinput}" readonly>${moneyinput}</td>
			</tr>
			<tr>
				<td>轉入貨幣：</td>
				<td><input type="hidden"  name="moneyInCurrencyId"
					value="${moneyInSelect.currencyId}" readonly>${moneyInSelect.currencyname}</td>
			</tr>
			<tr>
				<td>轉入金額：</td>
				<td><input type="hidden"  name="moneyInAmount"
					value="${moneyoutput}" readonly>${moneyoutput}</td>
			</tr>
			<tr>
				<td>匯率：</td>
				<td><input type="hidden"  name="exchangeRate"
					value="${moneyOutSelect.bankin}" readonly>${moneyInSelect.bankin}</td>
			</tr>
		</table>
		<button class="mt-5 btn btn-secondary" type="submit">確認兌換</button>
	</form>
</div>


<%@ include file="../include/header/footer.jspf"%>