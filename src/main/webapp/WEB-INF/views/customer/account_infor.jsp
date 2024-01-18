<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form"%>

<%@ include file="../include/header/header_custom.jspf"%>

<style>
.btn-opt {
	border: 0.5px solid rgb(128, 128, 128);
}

.btn:hover {
	background-color: rgb(179, 199, 180);
}

.report-exp {
	margin-bottom: 80px;
}
</style>

<h2 class="page-title text-center fw-bold mb-3">
	<i class="bi bi-journal-bookmark-fill"></i>帳戶查詢
</h2>
<div class="container mt-5">

	<h4 class="text-center">帳戶篩選</h4>
	<form method="GET" action="./searchaccount" onsubmit="return validateForm()">
            <div class="row justify-content-center align-items-center">
                <div class="col-md-7 mb-3 mb-md-0">
                    <select class="form-select" aria-label="Default select example" name="currencyId">
                        <option value="" disabled selected>請選擇帳戶</option>
                        <c:forEach items="${useraccount}" var="useraccount">
                            <option value="${useraccount.currencyId}">${useraccount.currency_currencyName}帳戶</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4">
                    <button type="submit" class="btn btn-secondary btn-block ">帳戶查詢</button>
                    <button type="button" class="btn btn-secondary btn-block" onclick="window.location.href='/MyWebBank/mvc/mybank/customer/myaccount_infor'">查看全部記錄</button>
                </div>
            </div>
        </form>

	<div class="mx-2 mx-md-5 mt-3 table-responsive-lg">

		<table class="table table-striped table-hover ">
			<thead>
				<tr>
					<th scope="col">交易時間</th>

					<th scope="col">帳號</th>
					<th scope="col">幣別</th>
					<th scope="col">交易金額</th>
					<th scope="col">帳戶餘額</th>

					<th scope="col">備註</th>


				</tr>
			</thead>

			<tbody>
				<c:forEach items="${ useraccountinfor }" var="accountinfor">
					<tr>
						<td>${accountinfor.transaction_time}</td>

						<td>${accountinfor.accountId}</td>
						<td>${accountinfor.currency_currencyName}</td>
						<td>${accountinfor.transationmoney}</td>
						<td>${accountinfor.currentAmount}</td>

						<td>換匯交易</td>
					</tr>
				</c:forEach>


			</tbody>

		</table>
	</div>
	<div class="text-center my-5" style="color: red">${ noDataMessage }</div>


</div>

<script>
	function validateForm() {
		var selectedValue = document.querySelector('select[name="currencyId"]').value;
		if (!selectedValue) {
			alert('請選擇帳戶！');
			return false;
		}
		return true;
	}
</script>



<%@ include file="../include/header/footer.jspf"%>
