<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Choose the Report and the Format of the report</title>

<script type="text/javascript"  src="/reports/js/reports.js">
</script>

<style type="text/css">
	body{
		line-height: 1.5em;
		font-family: Arial, Helvetica, "Helvetica Neue", Geneva, sans-serif;
		margin: 0;
		padding: 10px;
	}
	table, th, td {
			border: 1px solid black;
			line-height: 1.5em;
			font-family: Arial, Helvetica, "Helvetica Neue", Geneva, sans-serif;
			margin: 0;
			padding: 5px;
	}
	th	{
			background-color:green;
			color:white;
	}
	.padded {
			padding: 15x;
	}
		
</style>
</head>
<body>
	<h2 style="color:red;"> Japser Reports Demo </h2>
	<br><br>
	<form name="reportForm" action="/reports/reportOutput" method="post">
		<table border=1 width="90%">
			<tr>
				<th> Operation </th>
				<th> Choose </th>
			</tr>
			
			<tr>
				<td>
					Please select the type of report demo you want to view..
					<br>
				</td>
				<td align="center">
					<select class="padded" name="selectReportSection" onChange="javascript:insertOptions(this.value);">
						<option value="-1">Select 1</option>
						<option value="DSExample">Data Source Examples</option>
						<option value="FormatExample">Formatting(background, applying styles etc.) Examples</option>
						<option value="PositionExample">Positioning Examples</option>
						<option value="ChartExample">JFreeChart Chart Examples</option>
						<option value="ScriptExample">Jasper Scriptlet Examples</option>
						<option value="VariableExample">Variables And Expressions Examples</option>
						<option value="SubReportExample">Grouping and Sub-reports Examples</option>
					</select>
				</td>
			</tr>
			
			<tr>
				<td>
					Please select the report you want to view..
					<br>
				</td>
				<td align="center">
					<select class="padded" name="selectReport" id="selectReport">
						<option value="-1">Select 1</option>
					</select>
				</td>
			</tr>
			</tr>
			<tr>
				<td>
					Please select the output format.
				</td>
				<td>
					<input type="radio" name="format" value="viewer" checked="checked">Print Preview To Screen</input><br>
					<input type="radio" name="format" value="pdf">PDF File(Acrobat)</input><br>
					<input type="radio" name="format" value="txt">Plain Text File</input><br>
					<input type="radio" name="format" value="rtf">Rich Text Format(Word)</input><br>
					<input type="radio" name="format" value="csv">Comma Separated File(CSV)</input><br>
					<input type="radio" name="format" value="xls">Excel File(XLS)</input><br>
					<input type="radio" name="format" value="xml">XML File</input><br>
				</td>
			</tr>
			
			<tr>
				<td colspan="2" align="center">
					<input type=submit value="Submit"/>
				</td>
			</tr>
			
			<tr>
				<td style="font:bold" colspan=2">
					<p>
						1. To test the Hibernate reports use the following URL if you are running the application on localhost on port 8080.<br><br>
						
							<a href="networkRequestServlet">
								http://localhost:8080/reports/networkRequestServlet
							</a><br><br>
							
						2. To test the grouped reports, use the following URL<br><br>
							
							<a href="networkRequestGroupedServlet">
								http://localhost:8080/reports/networkRequestGroupedServlet
							</a><br><br>
					</p>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>