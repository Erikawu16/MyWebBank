<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form"%>

<%@ include file="../include/header/header_custom.jspf"%>
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
	<i class="bi bi-journal-bookmark-fill"></i>交易查詢
</h2>
<div class="container mt-5">

	<%@ include file="../include/calander.jspf"%>




	<div class="mx-5 mt-3">
		<table class="table table-striped table-hover " id="myTable">
			<thead>
				<tr>
					<th scope="col">交易時間</th>

					<th scope="col">轉出帳號</th>
					<th scope="col">轉出幣別</th>
					<th scope="col">轉出金額</th>

					<th scope="col">轉入帳號</th>
					<th scope="col">轉入幣別</th>
					<th scope="col">轉入金額</th>
					<th scope="col">匯率</th>

					<th scope="col">備註</th>


				</tr>
			</thead>
			<tbody>
				<c:forEach items="${ accountdetail }" var="accountdetail">
					<tr>

						<td>${accountdetail.transaction_time}</td>
						<td>${accountdetail.from_accountId}</td>
						<td>${accountdetail.from_currencyName}</td>
						<td>${accountdetail.moneyout}</td>
						<td>${accountdetail.from_accountId}</td>
						<td>${accountdetail.to_currencyName}</td>
						<td>${accountdetail.moneyin}</td>
						<td>${accountdetail.rate}</td>
						<td>換匯交易</td>

					</tr>
				</c:forEach>


			</tbody>
		</table>
	</div>
	<div class=" text-center my-5" style="color: red">${ noDataMessage }</div>


</div>


<script type="text/javascript">
		$(document).ready(function() {
			$('#myTable').DataTable({
				
				 searching: false,
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


<%@ include file="../include/header/footer.jspf"%>
