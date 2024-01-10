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

<!-- 引入 DataTables JS -->
<script type="text/javascript"
	src="https://cdn.datatables.net/1.10.21/js/jquery.dataTables.min.js"></script>
<script type="text/javascript"
	src="https://cdn.datatables.net/buttons/2.4.2/js/dataTables.buttons.min.js"></script>
<!-- 引入 DataTables 匯出列印功能 -->
<script type="text/javascript"
	src="https://code.jquery.com/jquery-3.7.0.js"></script>
<script type="text/javascript"
	src="https://cdn.datatables.net/1.13.7/js/jquery.dataTables.min.js"></script>
<script type="text/javascript"
	src="https://cdn.datatables.net/buttons/2.4.2/js/dataTables.buttons.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/pdfmake.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/vfs_fonts.js"></script>
<script type="text/javascript"
	src="https://cdn.datatables.net/buttons/2.4.2/js/buttons.html5.min.js"></script>
<script type="text/javascript"
	src="https://cdn.datatables.net/buttons/2.4.2/js/buttons.print.min.js"></script>
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
			<h2 class="text-center fw-bold"><i class="bi bi-person-fill-check"></i>會員資料清單</h2>
			<table class="table" id="myTable">

				<thead>
					<tr>
						<th scope="col">NO.</th>
						<th scope="col">用戶姓名</th>
						<th scope="col">申請時間</th>
						<th scope="col">身份證字號</th>
						<th scope="col">出生日期</th>
						<th scope="col">連絡電話</th>
						<th scope="col">用戶明細</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="account" items="${accounts}" varStatus="loop">
						<tr>
							<td>${loop.index + 1}</td>
							<!-- This will display the item number -->

							<th scope="row">${ account.username }</th>
							
							<th><fmt:formatDate value="${ account.registDate }"
									pattern="yyyy-MM-dd " /></th>
							
							
							<th scope="row">${ account.userId}</th>
							<th><fmt:formatDate value="${ account.birth }"
									pattern="yyyy-MM-dd " /></th>
							<th scope="row">${ account.email}</th>
							<th scope="row">
								<form method="post" action="./${ account.id} ">
									<button type="submit" class="btn btn-secondary">用戶明細</button>
								</form>
							</th>

						</tr>

					</c:forEach>
				</tbody>
			</table>



		</div>

	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#myTable').DataTable({
				language : {
					"lengthMenu" : "顯示 _MENU_ 筆資料",
					"sProcessing" : "處理中...",
					"sZeroRecords" : "没有匹配结果",
					"sInfo" : "目前有 _MAX_ 筆資料",
					"sInfoEmpty" : "目前共有 0 筆紀錄",
					"sSearch" : "關鍵字搜尋:",
					"sEmptyTable" : "尚未有資料紀錄存在",
					"sLoadingRecords" : "載入資料中...",
					"sInfoThousands" : ",",
					"oPaginate" : {
						"sFirst" : "首頁",
						"sPrevious" : "上一頁",
						"sNext" : "下一頁",
						"sLast" : "末頁"
					}

				},
				// 設定匯出功能
				dom : 'lBfrtip',
				buttons : [

				{
					extend : 'excel',
					text : 'EXCEL下載',
					className : 'btn btn-outline-secondary'
				}, {
					extend : 'print',
					text : 'PDF/列印',
					className : 'btn btn-outline-secondary'
				}

				]

			});
		});
	</script>
</body>

</html>


