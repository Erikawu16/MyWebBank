<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form"%>
<%@ include file="./include/header/header_index.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyBankLogin</title>
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
.custom-btn-width {
	width: 300px;
}
</style>
<link rel="stylesheet" href="/MyWebBank/css/login.css">
</head>
<body style="padding-top: 80 px">

	<div class="banner banner-container">
		<img class="banner-img d-block w-100 " src="../../img/banner02.jpg"
			alt="...">

		<div class="banner-title">
			<h1 class="fw-bold banner-title1 ">
				MyBank<br>提供您便捷安全服務
			</h1>
			<p class="fw-bold banner-title2 fs-5">自動化服務 | 智慧現金管理 | 便捷融資服務
				|收款服務 | 轉帳服務</p>
		</div>

		<div class="banner-btn mt-5 ">
			<a href="./regist" type="button"
				class="btn btn-outline-light btn-secondary mx-2 btn-lg">馬上註冊</a> <a
				href="#login" type="button"
				class="btn btn-outline-light btn-secondary mx-2 btn-lg">立即登入</a>
		</div>

	</div>


	<div class="container1">

		<div class="container mt-5 w-75 ">
			<div class="row col-12 ">
				<!-- 匯率專區 -->
				<div class="col col-6  border border-2 rounded p-3 ">
					<h4 class="text-center fw-bold mb-4">匯率查詢</h4>
					<div class="row row-cols-1 row-cols-xl-3 g-4">
						<!-- 美元匯率 -->
						<div class="col">
							<div class="card">
								<img src="../../img/flag-square/usa.jpg "
									class="card-img-top flag" alt="...">
								<div class="card-body">
									<h5 class="card-title">美元</h5>
									<h3 class="card-title">USD</h3>
									<p class="card-text">

										買入&nbsp;&nbsp;&nbsp;&nbsp;<span><fmt:formatNumber
												value="${currencyUSD.bankin}" pattern="#,##0.00" /></span>

									</p>
									<p class="card-text">
										賣出&nbsp;&nbsp;&nbsp;&nbsp;<span><fmt:formatNumber
												value="${currencyUSD.bankout}" pattern="#,##0.00" /></span>

									</p>
								</div>
							</div>
						</div>
						<!-- 日圓匯率 -->
						<div class="col">
							<div class="card">
								<img src="../../img/flag-square/jp.jpg"
									class="card-img-top flag" alt="...">
								<div class="card-body">
									<h5 class="card-title">日圓</h5>
									<h3 class="card-title">JPY</h3>
									<p class="card-text">
										買入&nbsp;&nbsp;&nbsp;&nbsp;<span><fmt:formatNumber
												value="${currencyJPY.bankin}" pattern="#,##0.00" /></span>

									</p>
									<p class="card-text">

										賣出&nbsp;&nbsp;&nbsp;&nbsp;<span><fmt:formatNumber
												value="${currencyJPY.bankout}" pattern="#,##0.00" /></span>
									</p>
								</div>
							</div>
						</div>
						<!-- 人民幣匯率 -->
						<div class="col">
							<div class="card">
								<img src="../../img/flag-square/cn.png"
									class="card-img-top flag" alt="...">
								<div class="card-body">
									<h5 class="card-title">人民幣</h5>
									<h3 class="card-title">CNY</h3>
									<p class="card-text">

										買入&nbsp;&nbsp;&nbsp;&nbsp;<span><fmt:formatNumber
												value="${currencyCNY.bankin}" pattern="#,##0.00" /></span>

									</p>
									<p class="card-text">
										賣出&nbsp;&nbsp;&nbsp;&nbsp;<span><fmt:formatNumber
												value="${currencyCNY.bankout}" pattern="#,##0.00" /></span>

									</p>
								</div>
							</div>
						</div>


					</div>
					<div class=" mt-2 text-center">
						<a type="button" data-bs-toggle="modal"
							data-bs-target="#exampleModal">匯率查詢 </a> <span>|</span> <a
							type="button">匯率更新</a>


					</div>
				</div>
				<!-- 匯率專區end -->

				<!-- 登入專區 -->

				<div class="col  col-5 border-2   rounded p-4 ">
					<form method="post" action="./login">

						<h4 class="fw-bolder text-center mb-4 title" id="login">網路銀行登入</h4>



						<div class="mb-0">
							<input name="userId" class="form-control" id="id"
								value="a123456789" placeholder="身分證字號">
						</div>



						<div class="mb-0">
							<label for="password" class="form-label">
			
							</label> <input
								type="password" name="password" class="form-control"
								id="password" value="123" placeholder="使用者密碼">
						</div>

						<div class="mb-4 text-center">
							<label for="code" class="form-label"></label> 
							<input type="text"
								id="code" name="code" class="form-control"
								placeholder="請輸入下方驗證碼">
								
							<div class="my-4">
								<img src="./getcode" alt="驗證碼">
							</div>
						</div>


						<div class="d-flex justify-content-center ">
							<button type="submit"
								class="btn btn-primary  mb-3 btn-block custom-btn-width">會員登入</button>

						</div>




						<div class="d-flex justify-content-center ">
							<a href="./regist"
								class="btn btn-primary btn-block custom-btn-width">會員註冊</a>
						</div>

						<div class="d-flex justify-content-center my-3 ">
							<a href="./forgot_password" class="text-decoration-none "><i
								class="bi bi-question-square-fill"></i> 忘記密碼</a>
						</div>
						<div style="color: red">${ loginMessage }</div>

					</form>
				</div>
			</div>
			<!-- 登入專區 end-->

		</div>

	</div>




</body>
<script>
console.log(${sessionCode});
</script>
</html>