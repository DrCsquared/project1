package com.revature.colin.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.revature.colin.project1.User;
import com.revature.colin.project1.main;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	
	private String username;
	private String password;
	private static User tempUser;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		username = request.getParameter("username");
		password = request.getParameter("password");
		try {
			tempUser = main.userLoginWeb(username, password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(checkIfEmployee(tempUser,username, password)) 
		{
			request.getRequestDispatcher("/EmployeeMenu.html").include(request, response);
		}
		else if(tempUser != null  && !checkIfEmployee(tempUser,username, password))
		{
			request.getRequestDispatcher("/CustomerMenu.html").include(request,response);
			
		}
		else 
		{
			//out.println("Your username and password don't match" );
			//request.setAttribute("errorMessage","Your username and password don't match");
			request.getRequestDispatcher("/Login.html").include(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	public static boolean checkIfEmployee(User tempUser,String userName, String password) 
	{
		boolean tempCheck = false;
		if(tempUser != null) 
		{
			tempCheck = main.isEmployee(tempUser);
			//tempCheck = true;
		}
		else {
			tempCheck = false;
		}
		
		
		return tempCheck;
		
	}

}
