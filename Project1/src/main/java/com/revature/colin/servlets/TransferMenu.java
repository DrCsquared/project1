package com.revature.colin.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TransferMenu
 */
public class TransferMenu extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TransferMenu() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		
		out.println(headingSetUp());
		out.println(" <body>\r\n"
				+ "\r\n"
				+ "        <!-- button grouping-->\r\n"
				+ "    <br>\r\n"
				+ "    <div class=\"container bg-dark\" style=\"width: 25%\">\r\n"
				+ "        <br>\r\n"
				+ "            <h1 class=\"display-6\" align=\"center\" style=\"text-shadow: -1px 0 white, 0 1px white, 1px 0 white, 0 -1px white;\">Transfer Menu</h1>\r\n"
				+ "            <br>\r\n"
				+ "        <div class=\"btn-group-vertical btn-block\" align=\"center\">\r\n"
				+ "            \r\n"
				+ "            <br>\r\n"
				+ "            <form action=\"internalTransfer\" method=\"post\">\r\n"
				+ "            <div align=\"center\">\r\n"
				+ "            <input type=\"submit\" class=\"btn btn-outline-success\" value=\"Internal Transfer\"/>\r\n"
				+ "            </div>\r\n"
				+ "            </form>\r\n"
				+ "            \r\n"
				+ "            <form action=\"externalTransfer\" method=\"post\">\r\n"
				+ "            <div align=\"center\">\r\n"
				+ "            <input type=\"submit\" class=\"btn btn-outline-success\" value=\"External Transfer\"/>\r\n"
				+ "            </div>\r\n"
				+ "            </form>	\r\n"
				+ "        </div>\r\n"
				+ "    </div>\r\n");
		out.println(returnButton());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	public String headingSetUp() 
	{
		String headOfWebPage;
		
		headOfWebPage = "<html><head><title> User Pending List</title>"
				+ "<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css\" integrity=\"sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l\" crossorigin=\"anonymous\">"
				+ "<script src=\"https://code.jquery.com/jquery-3.5.1.slim.min.js\" integrity=\"sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj\" crossorigin=\"anonymous\"></script> "
				+ "<script src=\"https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js\" integrity=\"sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN\" crossorigin=\"anonymous\"></script> "
		        + "<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/js/bootstrap.min.js\" integrity=\"sha384-+YQ4JLhjyBLPDQt//I+STsc9iw4uQqACwlvpslubQzn4u2UU2UFM80nGisd026JF\" crossorigin=\"anonymous\"></script>"
				+ "<div class=\"jumbotron\">\r\n"
				+ "            <h1 class=\"display-3\" align=\"center\" style=\"background: url(BankHomeScreen.jpg) no-repeat center bottom fixed;\r\n"
				+ "            -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover;\"><p style=\"text-shadow: -1px 0 white, 0 1px white, 1px 0 white, 0 -1px white;\">Welcome to Cumberland Bank</p></h1>\r\n"
				+ "            \r\n"
				+ "            <!-- pagination-->\r\n"
				+ "            <nav aria-label=\"Page Navigation Example\">\r\n"
				+ "                <ul class=\"pagination\">\r\n"
				+ "                    <l1 class=\"page-item\">\r\n"
				+ "                        <a href=\"CumberlandBankHomePage.html\" class=\"btn btn-outline-success\">Homepage</a>\r\n"
				+ "                    </l1>\r\n"
				+ "                    <l1 class=\"page-item\">\r\n"
				+ "                        <a href=\"CreateUser.html\" class=\"btn btn-outline-success\">Create User</a>\r\n"
				+ "                    </l1>\r\n"
				+ "                    <l1 class=\"page-item\">\r\n"
				+ "                        <a href=\"Login.html\" class=\"btn btn-outline-success\">Login</a>\r\n"
				+ "                    </l1>\r\n"
				+ "\r\n"
				+ "                </ul>\r\n"
				+ "            </nav>\r\n"
				+ "\r\n"
				+ "        </div>" +"</head><body>";
		
		return headOfWebPage;
	}
	
	public String returnButton() 
	{
		String backToCustomerMenu = "<br><br><br><div class=\"container\" style=\"width: 25%\">\r\n"
				+ "\r\n"
				+ "            <a href=\"CustomerMenu.html\" class=\"btn btn-outline-success btn-block\">Return</a>\r\n"
				+ "        </div> </body>\r\n"
				+ "</html>";
		
		return backToCustomerMenu;
	}

}
