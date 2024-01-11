<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>



<%@ include file="../include/header/header_manager.jspf"%>


<html>
<head>
<style>
.container {
	display: flex;
	flex-direction: row;
	
}
.title{
	margin-top: 100px;
	
}


#piechart, #top_x_div {
	flex: 1;
}

#line_top_x {
	margin: 50px auto;
}

</style>
</head>

<body>
	<div class="text-center title">
		<h2 class="text-center fw-bold"><i class="bi bi-journal-check "></i> 會員資料分析報告</h2>
	</div>
	<div class="container col-12 mt-2">



		<div class="border col-4 ">
			<!-- 男女比7 -->
			<h4 class="text-center mt-2">【會員性別分布】</h4>
			<div id="piechart" style="height: 400px;"></div>

		</div>
		<div class=" border col-8">
			<!-- 年齡分配 -->
			<input type="hidden" value="${age1}"  id="age1">
			<input type="hidden" value="${age2}"  id="age2">
			<input type="hidden" value="${age3}"  id="age3">
			<input type="hidden" value="${age4}"  id="age4">
			<input type="hidden" value="${age5}"  id="age5">
			<h4 class="text-center mt-2">【會員年齡分布】</h4>
			<div id="top_x_div" style="height: 400px;"></div>
		</div>
	</div>
	<div class="col-12 border mb-5">
		<!--會員人數成長趨勢-->
		<h4 class="text-center mt-5">【會員成長趨勢】</h4>
		<div id="line_top_x" class="text-center"
			style="width: 800px; height: 400px;"></div>
	</div>



<script type="text/javascript"
	src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">
	google.charts.load('current', {
		'packages' : [ 'corechart', 'bar', 'line' ]
	});
	google.charts.setOnLoadCallback(drawChart);
	<!--男女比例-->
	google.charts.setOnLoadCallback(drawStuff);
	<!--年齡分布-->
	google.charts.setOnLoadCallback(drawChart2);
	<!--會員人數成長趨勢-->
	<!--男女比例-->		

	
	function drawChart() {
		var data = google.visualization.arrayToDataTable([ [ '性別', '人數' ],
				[ '女性', ${womenAmount}], [ '男性',${menAmount}],
		]);
		
		var options = {
			title : ''
		};
		var chart = new google.visualization.PieChart(document
				.getElementById('piechart'));
		chart.draw(data, options);
	}

	
	var age1 = parseInt(document.getElementById("age1").value, 10);
	var age2 = parseInt(document.getElementById("age2").value, 10);
	var age3 = parseInt(document.getElementById("age3").value, 10);
	var age4 = parseInt(document.getElementById("age4").value, 10);
	var age5 = parseInt(document.getElementById("age5").value, 10);
	
	<!--年齡分布圖-->
	function drawStuff() {
		var data = new google.visualization.arrayToDataTable([
				[ 'Move', 'Percentage' ], [ "18-29 歲", ${age1} ], [ "30-39 歲", ${age2} ],
				[ "40-49 歲",${age3}], [ "50-59 歲", ${age4} ], [ "60 歲以上", ${age5} ] ]);

		var options = {
			width : 800,
			legend : {
				position : 'none'
			},

			bar : {
				groupWidth : "85%"
			}
		};

		var chart = new google.charts.Bar(document.getElementById('top_x_div'));
		// Convert the Classic options to Material options.
		chart.draw(data, google.charts.Bar.convertOptions(options));
	};

	<!--會員人數成長趨勢-->
	function drawChart2() {
		var data = new google.visualization.DataTable();
		data.addColumn('number', 'mounth');
		data.addColumn('number', '每月註冊成功會員人數');
	

		data.addRows([ [ 1, 30 ], [ 2, 50 ],
				[ 3, 75 ], [ 4, 54 ],
				[ 5,89 ], [ 6,108 ],
				[ 7,230], [ 8, 245 ],
				[ 9, 298 ], [ 10,265 ],
				[ 11,323 ], [ 12,309 ] ]);

		var options = {
			chart : {
				title : '',

			},
			width : 900,
			height : 400,
			axes : {
				x : {
					0 : {
						side : 'top'
					}
				}
			}
		};

		var chart = new google.charts.Line(document
				.getElementById('line_top_x'));

		chart.draw(data, google.charts.Line.convertOptions(options));
	}
</script>
</body>
</html>
