<%@ page language="java" contentType="text/html; charset=UTF-8"
	isErrorPage="true" pageEncoding="UTF-8"%>
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
<link rel="stylesheet" href="/MyWebBank/css/datatable.css">

<style>
.nav-pills .nav-link.active, .nav-pills .show>.nav-link {
	background-color: #97a9c3;
}

.nav-link {
	color: #97a9c3
}

.btn-detail {
	background-color: rgb(139, 154, 139);
}

.cotainer {
	margin-top: 80px;
}
</style>

</head>
<body>


	<div class="cotainer ">


		<div class="col w-75 mx-auto">
			<h2 class="text-center fw-bold">
				<i class="bi bi-person-fill-add"></i>待審核資料
			</h2>


			<table class="table text-center">
				<thead>
					<tr>
						<th scope="col">編號</th>
						<th scope="col">申請姓名</th>
						<th scope="col">申請時間</th>
						<th scope="col">身分證字號</th>
						<th scope="col">資料明細</th>

						<th scope="col">審核結果</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="user" items="${ users }" varStatus="stat">
						<tr>
							<th scope="row">${ user.id}</th>
							<td>${ user.username }</td>
							<td><fmt:formatDate value="${ user.registDate}"
									pattern="yyyy-MM-dd E" /></td>

							<td>${ user.userId  }</td>
							
							<td>
								<button type="button" class="btn btn-outline-primary"
									data-bs-toggle="modal" data-bs-target="#picModal${stat.index}">明細</button>
							</td>

							<td class="text-center">
								<div class="row justify-content-center">
									<div class="col-md-auto">
										<form method="POST" action="./pass/${ user.id }">
											<input name="_method" type="hidden" value="${_method}" />
											<button type="submit" class="btn btn-secondary  btn-block">通過</button>
										</form>
									</div>
									<div class="col-md-auto">

										<button type="button" class="btn btn-outline-primary"
											data-bs-toggle="modal" data-bs-target="#falsereasonModal">未通過
										</button>


									</div>
								</div>
							</td>



						</tr>
						<!-- Modal照片 -->
						<div class="modal fade" id="picModal${stat.index}" tabindex="-1"
							aria-labelledby="exampleModalLabel" aria-hidden="true">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="modal-header">
										<h5 class="modal-title" id="exampleModalLabel">上傳證件明細</h5>
										<button type="button" class="btn-close"
											data-bs-dismiss="modal" aria-label="Close"></button>
									</div>
									<div class="modal-body">
										<!-- 在這裡用迴圈顯示每個 base64 字串對應的圖片 -->
										<div id="imagePreviews">
											<img src="data:image/jpeg;base64,${ user.imgContent }"
												class="card-img-top flag" alt="123"
												style="width: 200px; height: auto;">
										</div>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-secondary"
											data-bs-dismiss="modal">Close</button>
									</div>
								</div>
							</div>
						</div>

						<!-- Modal END -->
						<!-- Modal未通過理由輸入 -->
						<div class="modal fade" id="falsereasonModal" tabindex="-1"
							aria-labelledby="exampleModalLabel" aria-hidden="true">


							<div class="modal-dialog">
								<form method="POST" action="./false/${user.id}">
									<input name="_method" type="hidden" value="${_method}" />
									<div class="modal-content">
										<div class="modal-header">
											<h5 class="modal-title" id="exampleModalLabel">請說明未通過原因</h5>
											<button type="button" class="btn-close"
												data-bs-dismiss="modal" aria-label="Close"></button>
										</div>
										<div class="modal-body">
											<div class="row justify-content-center">
												<div class="col-md-8">
													<textarea class="form-control" id="falsereason"
														name="falsereason" rows="5" placeholder="請輸入未通過原因"
														required></textarea>
												</div>
											</div>
										</div>
										<div class="modal-footer">

											<button type="submit" class="btn btn-danger">送出</button>

										</div>
									</div>
								</form>
							</div>
						</div>


						<!-- Modal END -->

					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>

</html>

