<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<jsp:include page="includes/header.jsp" />
<jsp:include page="includes/navbar.jsp" />

<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>

<%
String id = request.getParameter("id");
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

String userEmail = (String) session.getAttribute("userId");
String itemName = "";
String price = "";
String description = "";
String image = "";
String itemId = "";
String itemIdTemp = "";
%>

<%
try {
	connection = DriverManager.getConnection(connectionUrl, username, password);
	statement = connection.prepareStatement("SELECT * from item where id = ?");
	statement.setInt(1, Integer.parseInt(id));
	resultSet = statement.executeQuery();
	while (resultSet.next()) {
		itemId = String.valueOf(resultSet.getInt("id"));
		itemName = resultSet.getString("item_name");
		price = String.valueOf(resultSet.getFloat("price"));
		description = resultSet.getString("description");
		image = resultSet.getString("image");
%>
<%
}
connection.close();
} catch (Exception e) {
e.printStackTrace();
}
%>

<%
try {
	connection = DriverManager.getConnection(connectionUrl, username, password);
	statement = connection.prepareStatement(
	"select i.id from feedback f , item i where f.user in (SELECT u.id from user u where u.email = ? ) and i.feedback_id = f.feedback_id");
	statement.setString(1, userEmail);
	resultSet = statement.executeQuery();
	while (resultSet.next()) {
		itemIdTemp = String.valueOf(resultSet.getInt("id"));
%>
<%
}
connection.close();
} catch (Exception e) {
e.printStackTrace();
}
%>



<section class="py-5">
	<div class="container px-4 px-lg-5 my-5">
		<div class="row gx-4 gx-lg-5 align-items-center">
			<div class="col-md-6">
				<img class="card-img-top mb-5 mb-md-0" src="<%out.print(image);%>"
					alt="..." />
			</div>
			<div class="col-md-6">
				<h1 class="display-5 fw-bolder">
					<%
					out.print(itemName);
					%>
				</h1>
				<div class="fs-5 mb-5">
					<span class="">Rs<%
					out.print(price);
					%>0
					</span>
				</div>
				<p class="lead">
					<%
					out.print(description);
					%>
				</p>
				<div class="d-flex">

					<input class="form-control text-center me-3" id="inputQuantity"
						type="hidden" value=<%out.print(id);%> style="max-width: 3rem"
						name="item_id" /> <input class="form-control text-center me-3"
						id="inputQuantity" type="num" value="1" style="max-width: 3rem"
						name="qty" />
					<button class="btn btn-outline-dark flex-shrink-0" name="action"
						type="submit" value="Add to cart">
						<i class="bi-cart-fill me-1"></i> Add to cart
					</button>

				</div>


			</div>





			<%
			if (session.getAttribute("userId") != null) {
			%>

			<form class="mt-2" action="UserController" method="post">
				<input type="hidden" name="itemId"
					value="<%out.print(request.getParameter("id"));%>" />
				<textarea class="form-control" name="feedback"></textarea>
				<%
				if (request.getAttribute("feedbackSuccess") != null) {
				%>
				<%
				out.print("<div class='alert alert-success mt-2' role='alert'>Feedback added!!</div>");
				}
				%>
				<%
				if (request.getAttribute("feedbackValidError") != null) {
				%>
				<%
				out.print("<div class='alert alert-danger mt-2' role='alert'>Invalid Feedback</div>");
				}
				%>
				<input type="submit" name="action" value="Submit Feedback"
					class="btn btn-secondary mt-2" />
			</form>
			<%
			}
			%>
		</div>
	</div>
</section>

<jsp:include page="includes/footer.jsp" />