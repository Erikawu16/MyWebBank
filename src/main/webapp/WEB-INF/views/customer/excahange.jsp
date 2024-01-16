<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form"%>
<%@ include file="../include/header/header_custom.jspf"%>
<script
	src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
<style>
.btn {
	border: 1px solid rgb(128, 128, 128);
}

.btn:hover {
	background-color: rgb(179, 199, 180);
}

.exange {
	margin: 20px auto 20px auto;
}

.lastsection {
	margin-bottom: 80px;
}
</style>

<h2 class="page-title text-center fw-bold mb-3">
	<i class="bi bi-currency-exchange"></i>我要換匯

</h2>

<div class="text-center" style="color: red">${ successMessage }</div>
<div class="text-center" style="color: red">${ errorMessage }</div>

<form method="post" action="./excahangecomfirm" id="exchangeForm">
	<div class="container mt-5">
		<div class="row col-12">
			<div class="col col-5">
				<h4 class="mb-3">選擇原始幣別</h4>
				<p class="text-danger">請先至會員專區申辦愈換匯的外幣帳戶</p>
				<select class="form-select mb-4" aria-label="Default select example"
					id="moneyOutSelect" name="moneyOutSelect"
					onchange="moneyOutOnchange(event)">
					<option value="" disabled selected>請選擇轉出帳戶</option>
					<c:forEach items="${ useraccount }" var="useraccount">
						<option value="${ useraccount.currencyId }"
							rate="${ useraccount.currency.bankout }">${ useraccount.currency_currencyname }</option>
					</c:forEach>
				</select>
				<div class="mb-0">
					<input name="moneyin" class="form-control" id="moneyin"
						placeholder="請輸入金額">

				</div>
			</div>


			<div class="col col-2 position-relative fs-1 ">
				<i
					class="bi bi-arrow-repeat  mb-0 position-absolute bottom-0 start-50 translate-middle-x"></i>
			</div>


			<div class="col col-5 ">

				<h4 class="mb-3">選擇轉換幣別</h4>
				<p class="text-danger">請勿選擇相同的幣別</p>
				<select class="form-select  mb-4" id="moneyInSelect"
					name="moneyInSelect" aria-label="Default select example"
					onchange="moneyInOnchange(event)">
					<option value="" disabled selected>請選擇轉入帳戶</option>
					<c:forEach items="${ useraccount }" var="useraccount">
						<option value="${ useraccount.currencyId }"
							rate="${ useraccount.currency.bankout }">${ useraccount.currency_currencyname }</option>
					</c:forEach>
				</select>
				<div class="mb-0">
					<input class="form-control" id="moneyout" name="moneyout" readonly>
				</div>
			</div>

		</div>
	</div>



	<div class="d-flex justify-content-center exange">

		<button type="submit" class="btn ">我要換匯</button>

	</div>


</form>



<%@ include file="../include/currency_table.jspf"%>
<form action="./getNewCurrency" method="get">
	<div class="d-flex justify-content-center mt-5 lastsection">
		<button type="submit" class="btn btn-secondary">取得最新匯率</button>
	</div>
</form>
<%@ include file="../include/header/footer.jspf"%>

<script>

var moneyOutRate = 1;
var moneyInRate = 1;
var moneyInInput = $('#moneyin');//輸入的換匯金額
var moneyOutInput = $('#moneyout'); 
var moneyInSelect = document.getElementById('moneyInSelect');
var moneyOutSelect = document.getElementById('moneyOutSelect');
//銀行買入匯率表
var currencyRateBankin = {
	<c:forEach items="${ currency }" var="currency">
		${ currency.currencyId } :${ currency.bankin },
	</c:forEach>
};
//銀行賣出匯率表
var currencyRateBankout = {
		<c:forEach items="${ currency }" var="currency">
			${ currency.currencyId } :${ currency.bankout },
		</c:forEach>
	};
//更新轉換後金額的函式
function updateExchangedAmount(inputAmount) {
    const exchangedAmountTWD = inputAmount * moneyOutRate;
    const exchangedAmount = (exchangedAmountTWD / moneyInRate).toFixed(2);
    console.log("moneyOutRate"+ moneyOutRate );
    console.log("moneyInRate"+ moneyInRate );
    console.log("轉台幣後"+ exchangedAmountTWD );
    console.log("轉換後"+ exchangedAmount);
    moneyOutInput.val(exchangedAmount); // 使用 val() 設置 input 元素的值
}
//貨幣轉入選擇事件處理函式
function moneyInOnchange(event) {
    let currencyId = event.target.value;
    let rate = currencyRateBankin[currencyId];
    moneyInRate = rate;
    
    for (var i = 0; i < moneyOutSelect.options.length; i++) {
        if (moneyOutSelect.options[i].value === currencyId) {
            moneyOutSelect.options[i].disabled = true;
        } else {
            moneyOutSelect.options[i].disabled = false;
        }
    }
    
    const inputAmount = parseFloat(moneyInInput.val());
    if (!isNaN(inputAmount)) {
        updateExchangedAmount(inputAmount);
    } else {
    	moneyOutInput.val('請輸入有效金額');
    }
    console.log("轉入currencyId={},rate={}", currencyId, rate);
}


//貨幣轉出選擇事件處理函式
function moneyOutOnchange(event) {
    let currencyId = event.target.value;
    let rate = currencyRateBankout[currencyId];
    moneyOutRate = rate;
    
    for (var i = 0; i < moneyInSelect.options.length; i++) {
        if (moneyInSelect.options[i].value === currencyId) {
            moneyInSelect.options[i].disabled = true;
        } else {
            moneyInSelect.options[i].disabled = false;
        }
        }
    
    const inputAmount = parseFloat(moneyInInput.val());
    if (!isNaN(inputAmount)) {
        updateExchangedAmount(inputAmount);
    } else {
    	moneyOutInput.val('請輸入有效金額');
    }
    
    console.log("轉出currencyId={},rate={}", currencyId, rate);
}
//輸入金額變化事件處理函式
$(document).ready(function() {
    moneyInInput.on('input', function() {
        const inputAmount = parseFloat(moneyInInput.val());
        if (!isNaN(inputAmount)) {
            updateExchangedAmount(inputAmount);
        } else {
        	moneyOutInput.val('請輸入有效金額');
        }
    });
});

//表單提交事件處理
$('#exchangeForm').submit(function(event) {
    event.preventDefault();

    const inputAmount = parseFloat(moneyInInput.val());
    const exchangedAmount = parseFloat(moneyOutInput.val());

    if (isNaN(inputAmount)  || inputAmount <= 0 ) {
        alert('請輸入有效金額且換匯金額不得為0');
    } else {
        this.submit();
    }
});


	</script>