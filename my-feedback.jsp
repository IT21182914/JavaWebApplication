<jsp:include page="includes/header.jsp" />
<jsp:include page="includes/navbar.jsp" />

<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>

<%
String driver = "com.mysql.cj.jdbc.Driver";
String connectionUrl = "jdbc:mysql://localhost:3306/oop";
String username = "root";
String password = "";
try {
	Class.forName(driver);
} catch (ClassNotFoundException e) {
	e.printStackTrace();
}
Connection connection = null;
PreparedStatement statement = null;
ResultSet resultSet = null;
%>


<style>
.bd-placeholder-img {
	font-size: 1.125rem;
	text-anchor: middle;
	-webkit-user-select: none;
	-moz-user-select: none;
	user-select: none;
}

@media ( min-width : 768px) {
	.bd-placeholder-img-lg {
		font-size: 3.5rem;
	}
}

.b-example-divider {
	height: 3rem;
	background-color: rgba(0, 0, 0, .1);
	border: solid rgba(0, 0, 0, .15);
	border-width: 1px 0;
	box-shadow: inset 0 .5em 1.5em rgba(0, 0, 0, .1), inset 0 .125em .5em
		rgba(0, 0, 0, .15);
}

.b-example-vr {
	flex-shrink: 0;
	width: 1.5rem;
	height: 100vh;
}

.bi {
	vertical-align: -.125em;
	fill: currentColor;
}

.nav-scroller {
	position: relative;
	z-index: 2;
	height: 2.75rem;
	overflow-y: hidden;
}

.nav-scroller .nav {
	display: flex;
	flex-wrap: nowrap;
	padding-bottom: 1rem;
	margin-top: -1px;
	overflow-x: auto;
	text-align: center;
	white-space: nowrap;
	-webkit-overflow-scrolling: touch;
}
</style>



<div class="container mt-2">

	<%
	if (session.getAttribute("userId") == null) {
	%>
	<%
	response.sendRedirect("login.jsp");
	%>
	<%
	}
	%>


	<div class="container">

		<h1 class="h2">My Feedbacks</h1>

	</div>
	<%
	if (request.getAttribute("msgSuccess") != null) {

		out.print("<div class='alert alert-success' role='alert'>Item added successful</div>");
	}

	if (request.getAttribute("deleteSuccess") != null) {

		out.print("<div class='alert alert-success' role='alert'>Item deleted successful</div>");
	}

	if (request.getAttribute("updateSuccess") != null) {

		out.print("<div class='alert alert-success' role='alert'>Item updated successful</div>");
	}

	if (request.getAttribute("updateError") != null) {

		out.print("<div class='alert alert-success' role='alert'>Item updated error</div>");
	}
	%>

		<%
			if (request.getAttribute("feedbackValidError") != null) {
			%>
			<%
			out.print("<div class='alert alert-danger' role='alert'>Invalid Feedback</div>");
			}
			%>

	<table class="table" id="feedbacks">
		<tr>
			<th>Feedback Id</th>
			<th>Feedback</th>
			<th>Update</th>
			<th>Delete</th>
		</tr>

		<%
		try {
			connection = DriverManager.getConnection(connectionUrl, username, password);
			statement = connection.prepareStatement(
			"select f.id ,  f.feedback  from feedback f , user u where f.user = (select id from user where email = ?);");
			
			statement.setString(1, (String) session.getAttribute("userId"));
			
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
		%>
		<tr>
			<td><%=resultSet.getInt("id")%></td>
			<td><%=resultSet.getString("feedback")%></td>
			<td><a id="updateBtn" class="btn btn-warning"
				href='update-feedback.jsp?id=<%=resultSet.getInt("id")%>&f=<%=resultSet.getString("feedback") %>'>Update
					Feedback</a></td>
			<td>
				<form action="UserController" method="post">
					<input type="hidden" name="id" value="<%=resultSet.getInt("id")%>" />
					<input id="deleteBtn" type="submit" class="btn btn-danger"
						name="action" value="Delete Feedback" />
				</form>
			</td>
		</tr>
		<%
		}
		connection.close();
		} catch (Exception e) {
		e.printStackTrace();
		}
		%>

	</table>
</div>


<jsp:include page="includes/footer.jsp" />
