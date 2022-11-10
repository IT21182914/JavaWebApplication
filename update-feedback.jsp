<jsp:include page="includes/header.jsp" />
<jsp:include page="includes/navbar.jsp" />


<div class="container col-sm-6 mt-5">
	<h1>Update feedback</h1>
		
	<form action="UserController" method="post">
		<div class="mb-3">
		<input type="hidden" name="id" value="<%= request.getParameter("id") %>" />
			<label for="name" class="form-label">Feedback </label> 
			<textarea type="text" rows=5
				class="form-control"  name="feedback" id="nameInput"><%= request.getParameter("f") %></textarea>

		</div>
		<button type="submit" name="action" value="Update Feedback" class="btn btn-primary">Update</button>
	
	</form>

</div>

<jsp:include page="includes/footer.jsp" />
