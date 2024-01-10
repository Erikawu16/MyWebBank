<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../include/header/header_index.jspf"%>


<style>
.bi {
	color: rgb(141, 171, 217)
}

.btn-send {
	color: rgb(0, 0, 0);
	background: rgb(152, 162, 177);
}

.btn-send:hover {
	background: rgb(141, 171, 217);
}

.btn-home{
  position: fixed;
  bottom:20px;
  right: 5px;
  }
  
</style>
<div class="border mx-5 mt-5 p-3">
	<h4 class="text-center">貼心提醒!!</h4>
<div class="text-center w-50 mx-auto">
	<ul style="list-style-type: none" class="text-start">
		<li><i class="bi bi-1-square-fill"></i>&nbsp;資料審核需要1~2週時間,若審核通過將有專人與您聯繫</li>
		<li><i class="bi bi-2-square-fill"></i>&nbsp;等待審核期間有任何問題歡迎電洽詢</li>
		<li><i class="bi bi-3-square-fill"></i>&nbsp;若有任何疑慮或接獲偽冒本行名義之情況，請參考以下做法：
                                                    <br> ☎️ 請撥打警政署【165反詐騙專線】諮詢，或洽詢本行客服中心。

</li>
	</ul>
</div>
</div>
<div class="w-75 mx-auto mt-5 ">
<div class="progress">
  <div class="progress-bar" role="progressbar" style="width: 100%;" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100">100%</div>
</div>
<h2 class="text-center mt-3">恭喜完成!!!</h2>
<a href="./login">>>返回首頁</a>
</div>