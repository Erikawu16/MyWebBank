<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ include file="../include/header/header_custom.jspf"%>



<style>
.tab-title {
	coloe: rgb(128, 128, 128);
}

.btn {
	border: 1px solid rgb(128, 128, 128);
}

.btn:hover {
	background-color: rgb(179, 199, 180);
}

.card {
	width: 80vw;
}

.tab-content {
	margin-bottom: 100px;
}
</style>

<h2 class="page-title text-center fw-bold mb-3">
	<i class="bi bi-person-badge"></i>${user.username} 帳戶總覽
</h2>




<div
	class="tab-content text-center d-flex flex-column align-items-center  mx-auto "
	id="myTabContent">



		<form method="GET" action="./myaccount_infor">
			<div class="btn-group" role="group"
				aria-label="Basic outlined example">
				<button type="submit" class="btn">查看帳戶資訊</button>
				<button type="button" class="btn"
					onclick="window.location.href='/MyWebBank/mvc/mybank/customer/myexcahang'">我要換匯</button>
			</div>

			
			<div class="card  mt-5 ">
				<c:forEach items="${account}" var="account">

					<h5 class="card-header">${account.currency_currencyName}存款</h5>


					<div class="card-body">
						<h5 class="card-title">
							<c:set var="formattedId"
								value="${fn:substring(account.accountId, 0, 3)}-${fn:substring(account.accountId, 3, 6)}-${fn:substring(account.accountId, 6, 8)}-${fn:substring(account.accountId, 8, 10)}" />
							${formattedId}
						</h5>
						<p class="card-text">帳戶餘額: ${account.amount}元</p>

					</div>
				</c:forEach>
			</div>
		</form>

	</div>

	<%@ include file="../include/header/footer.jspf"%>