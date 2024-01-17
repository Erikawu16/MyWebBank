<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form"%>

<%@ include file="../include/header/header_index.jspf"%>


<style>
.checkbox-container {
	display: flex;
	flex-direction: column;
	margin-left: 50px;
}

.checkbox-container sp\:checkboxes, 
.checkbox-container sp\:errors {
	margin-bottom: 5px;
	/* Adjust spacing between checkboxes and error messages */
}

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

.btn-home {
	position: fixed;
	bottom: 20px;
	right: 5px;
}

.step {
	color: rgb(255, 255, 255);
	background: rgb(141, 171, 217);
}
</style>
<div class="border  mt-5 mb-5 p-3 w-75 mx-auto">
	<h4 class="text-center fw-bold">
		<i class="bi bi-question-circle-fill"></i>開戶提醒
	</h4>
	<div class="text-center w-75 mx-auto">
		<ul style="list-style-type: none" class="text-start">
			<li><i class="bi bi-1-square-fill"></i>&nbsp;請準備證件：身分證和第二證件（健保卡、駕照、護照擇一</li>
			<li><i class="bi bi-2-square-fill d-inline"></i>&nbsp;填寫個人基本資料</li>
			<li><i class="bi bi-3-square-fill"></i>&nbsp;審核同過後,收到專屬卡片</li>
		</ul>
	</div>
</div>

<div class="text-center btn-home">
	<a href="./login"><i
		class="bi bi-house-door-fill fs-2 text-withe"></i></a>

</div>



<!-- 第一部分 -->

<div class="w-75 mx-auto border border-3 rounded p-4  mb-5">
	<h3 class="text-center step  fw-bold">1.請填寫基本資料</h3>
	<div class="w-75 mx-auto mt-3 mb-5 "></div>


	<sp:form modelAttribute="user" method="post"
		action="./regist"
		class="row g-3 "  >
		<!-- 表單元素 -->
		<sp:input path="id" type="hidden" />

		<input name="_method" type="hidden" value="${_method}" />

		<div class="col-md-3 text-center">
			<label for="username" class="form-label  fs-5 fw-bold">姓名:</label> <br />
			<sp:input path="username" type="text" class="w-100" value="王小明"/>
			<sp:errors path="username" class="text-danger" />
			<p />
		</div>

		<div class="col-md-3 text-center">
			<label for="userId" class="form-label fs-5 fw-bold" >身份證字號:</label> <br />
			<sp:input path="userId" type="text" class="w-100" value="a123456789" />
			<sp:errors path="userId" class="text-danger" />
			<p />
		</div>

		<div class="col-md-3 text-center">
			<label for="birth" class="form-label  fs-5 fw-bold">出生年月日:</label> <br />
			<sp:input path="birth" type="date" class="w-100"/>
			<sp:errors path="birth" class="text-danger"/>
			<p />
		</div>
		<div class="col-md-3  text-center">
			<label for="birth" class="form-label  fs-5 fw-bold">性別:</label> <br />
			<sp:radiobuttons path="sexId" items="${ sexs }" itemLabel="name"
				itemValue="id" cssClass="element-margin"/>
			<sp:errors path="sexId" class="text-danger" />
			<p />
		</div>


		<div class="col-md-3 text-center">
			<label for="email" class="form-label fs-5 fw-bold">電子郵件：</label> <br />
			<sp:input path="email" type="email" class="w-100"  value="abc@gmail.com"/>
			<sp:errors path="email" class="text-danger" />
			<p />
		</div>


		<div class="col-md-3 text-center">
			<label for="password" class="form-label fs-5 fw-bold">密碼：</label> <br />
			<sp:input path="password" type="password" class="w-100" value="1234567890" />
			<sp:errors path="password" class="text-danger"/>
			<p />
		</div>
		
		<div class="col-md-3 text-center">
			<label for="confirmPassword" class="form-label fs-5 fw-bold">確認密碼：</label>
			<br />
			<input type="password" id="confirmPassword"  class="w-100" value="1234567890"></input>
		</div>



		<!-- 稅務身分 -->
		<div class="row">
			<div class="col-12 pb-1 ">
				<div class="checkbox-container mt-3">
					<h2 class="p2 fw-bold fs-5 ">【稅務身分】</h2>
					
						<sp:radiobuttons path="typeId" items="${ types }" itemLabel="name"
							itemValue="id" />
						<sp:errors path="typeId" class="text-danger" />
				
					<br />
					<p class="mb-5 text-black-50 fst-italic">為遵循以「美國外國帳戶稅收遵從法」(FATCA)為目的締結的跨政府協議(IGA) 和金融機構執行共同申報及盡職審查作業辦法
(CRS)，中國信託證券投資信託股份有限公司須蒐集和審查稅務用途金融帳戶資訊以確定受益人的稅務居民身分或多個稅務居民身
分。</p>
				</div>

			</div>
		</div>



		<!-- 第二部分 -->
		<div class="w-75 mx-auto mt-5 mb-3 d-block">
			<h3 class="text-center step  fw-bold mb-3">2.請上傳證件資料</h3>

		</div>

		<div>
			<textarea  rows="10" cols="30" id="imgBase64" name="imgContent" hidden></textarea>
			<%@ include file="../include/uploadpic.jspf"%>
			
		</div>




		<!-- 第三部分 -->
		<div class="w-75 mx-auto mt-5 mb-3 ">
			<h3 class="text-center step  fw-bold mb-3">3.資料確認送出</h3>

		</div>

		<div class="mb-5">


			<div class=" border border-3 rounded p-4">
				<div class="col-12 ">
					<div class="form-check">
						<input class="form-check-input ms-0" type="checkbox" value=""
							id="invalidCheck" required> <label
							class="form-check-label" for="invalidCheck">
							我已詳閱並同意銀行蒐集、處理及利用個人資料法定告知事項。 </label>
						<div class="invalid-feedback">請詳閱並確認後再送出</div>
					</div>
				</div>
				<div class="col-12 text-center my-3">
					<button class="btn btn-send" type="submit" >資料送出</button>
				</div>
			</div>

		</div>

		
		<!-- 顯示所有錯誤訊息 -->
		<sp:errors path="*" class="text-danger" />
	</sp:form>
</div>





