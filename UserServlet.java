package com.shop.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shop.model.Feedback;
import com.shop.model.User;
import com.shop.service.IUserService;
import com.shop.service.UserServiceImpl;

/**
 * Servlet implementation class UserController
 */
@WebServlet("/UserController")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @throws IOException
	 * @throws ServletException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	
	//User Controller
	//register user
	protected void registerUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//get form values using parameters
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		String password = request.getParameter("password");
		String cpassword = request.getParameter("cpassword");

		//errors list
		ArrayList<String> errors = new ArrayList<String>();

		//email validation regex pattern
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);

		if (!pat.matcher(email).matches()) {
			errors.add("Email is invalid");
			request.setAttribute("emailError", "Email is invalid");
		}

		if (!password.equals(cpassword)) {
			errors.add("Both passwords must be same");
			request.setAttribute("pwError", "Both Passwords must be the same");
		}

		if (password.length() < 8) {
			errors.add("Both passwords must be same");
			request.setAttribute("pwErrorLength", "Password must be greater than 8 charactors");
		}

		if (errors.size() > 0) {
			request.setAttribute("msg", "error");
			RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
			dispatcher.forward(request, response);
			return;
		}

		//OOP
		//calling User class constructor to make user object
		User user = new User(name, phone, email, address, password);
		IUserService userService = new UserServiceImpl();
		
		boolean res = userService.registerUser(user);
		
		if (res) {
			request.setAttribute("msgSuccess", "success");
			RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
			dispatcher.forward(request, response);
		} else {
			request.setAttribute("msgError", "error");
			RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
			dispatcher.forward(request, response);
		}

	}

	//login user
	protected void loginUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		UserServiceImpl userService = new UserServiceImpl();
		boolean res = userService.loginUser(email, password);
		if (res) {
			HttpSession session = request.getSession();
			session.setAttribute("userId", email);
			RequestDispatcher dispatcher = request.getRequestDispatcher("profile.jsp");
			dispatcher.forward(request, response);
		} else {
			request.setAttribute("msgError", "error");
			RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
			dispatcher.forward(request, response);
		}

	}

	//logout user
	//same as the logout admin
	protected void logoutUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.invalidate();
		RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
		dispatcher.forward(request, response);

	}

	protected void addItemToCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		int cartItems = 0;
		int qty = Integer.parseInt(request.getParameter("qty"));
		int item_id = Integer.parseInt(request.getParameter("item_id"));

		if (session.getAttribute("cart") != null) {
			System.out.println("thynawaaaaaaaaa");
			int temp = (int) session.getAttribute("cart");
			System.out.print(temp);
			cartItems += temp;
			cartItems += qty;
		} else {
			System.out.println("Naaa");
			cartItems += qty;
			session.setAttribute("cart", cartItems);

		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("item-details.jsp?id=" + item_id);
		dispatcher.forward(request, response);

	}

	//update user profile
	protected void updateProfile(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		IUserService userService = new UserServiceImpl();
		
		//get request parameters
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");

		ArrayList<String> errors = new ArrayList<String>();

		//validations
		if (phone.strip().length() > 10 || phone.strip().length() < 10) {
			request.setAttribute("mobileNoInvalid", "error");
			errors.add("mobile no is invalid");

		}

		if (name == null || name.strip().length() == 0 || phone == null || phone.strip().length() == 0
				|| address == null || address.strip().length() == 0) {
			request.setAttribute("validateError", "error");
			errors.add("all fields are required");
		}
		
		if(errors.size()>0) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("profile.jsp");
			dispatcher.forward(request, response);
			return;	
		}

		//calling user class constructor to make user obejct
		User user = new User(id, name, phone, address);

		boolean res = userService.updateProfile(user);

		if (res) {
			request.setAttribute("updateProfileSuccess", "success");
			RequestDispatcher dispatcher = request.getRequestDispatcher("profile.jsp");
			dispatcher.forward(request, response);
		} else {
			request.setAttribute("updateProfileError", "error");
			RequestDispatcher dispatcher = request.getRequestDispatcher("profile.jsp");
			dispatcher.forward(request, response);
		}

	}

	//make feedback
	protected void makeFeedback(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		int itemId = Integer.parseInt(request.getParameter("itemId"));
		String feedback = request.getParameter("feedback");
		String userEmail = (String) session.getAttribute("userId");
		
		if(feedback.strip().length()==0 || feedback=="") {
			request.setAttribute("feedbackValidError", "error");
			RequestDispatcher dispatcher = request.getRequestDispatcher("item-details.jsp?id=" + itemId);
			dispatcher.forward(request, response);
			return;
		}

		
		//OOP
		//calling feedback class constructor to make feedback object
		Feedback userFeedback = new Feedback(feedback);

		IUserService userService = new UserServiceImpl();
		boolean res = userService.makeFeedback(userFeedback, itemId, userEmail);

		if (res) {
			request.setAttribute("feedbackSuccess", "success");
			RequestDispatcher dispatcher = request.getRequestDispatcher("item-details.jsp?id=" + itemId);
			dispatcher.forward(request, response);
		} else {
			request.setAttribute("feedbackError", "error");
			RequestDispatcher dispatcher = request.getRequestDispatcher("item-details.jsp?id=" + itemId);
			dispatcher.forward(request, response);
		}

	}
	
	//update feedback
	protected void updateFeedback(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String feedback = request.getParameter("feedback");
		
		//feedback validation
		if(feedback.strip().length()==0 || feedback=="") {
			request.setAttribute("feedbackValidError", "error");
			RequestDispatcher dispatcher = request.getRequestDispatcher("my-feedbacks.jsp");
			dispatcher.forward(request, response);
			return;
		}

		IUserService userService = new UserServiceImpl();
		boolean res = userService.updateFeedback(feedback, id);

		response.sendRedirect("my-feedbacks.jsp");

	}
	
	//delete specific feedback
	protected void deleteFeedback(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));

		UserServiceImpl userService = new UserServiceImpl();
		boolean res = userService.deleteFeedback(id);

		response.sendRedirect("my-feedbacks.jsp");

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		if (action.equals("Sign Up")) {
			this.registerUser(request, response);
		} else if (action.equals("Login")) {
			this.loginUser(request, response);
		} else if (action.equals("Logout")) {
			this.logoutUser(request, response);
		} else if (action.equals("Add to cart")) {
			this.addItemToCart(request, response);
		} else if (action.equals("Update Profile")) {
			this.updateProfile(request, response);
		} else if (action.equals("Submit Feedback")) {
			this.makeFeedback(request, response);
		}else if(action.equals("Update Feedback")) {
			this.updateFeedback(request , response);
		}else if(action.equals("Delete Feedback")) {
			this.deleteFeedback(request, response);
		}
	}

}
