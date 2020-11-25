package com.pruebas;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final String authUsr = "admin";
	private final String authPwd = "password";
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// get request parameters for userID and password
		System.out.println("doPost - LoginServlet ");
		String loginUsername = request.getParameter("username");
		String loginPassword = request.getParameter("password");
		
		if(authUsr.equals(loginUsername) && authPwd.equals(loginPassword)){
			System.out.println("login and password are correct");
			HttpSession session = request.getSession(true); // creating new session object
			session.setAttribute("username", loginUsername);
			
			//setting session to expiry in 30 mins
			session.setMaxInactiveInterval(30*60);
			
			// store username value in a cookie
			Cookie userNameCk = new Cookie("username", loginUsername);
			userNameCk.setMaxAge(30*60); // set cookie max age to 30 min
			response.addCookie(userNameCk);
			
			// redirect to the main page of the web application (login ok)
			RequestDispatcher reqDispatcher = getServletContext().getRequestDispatcher("/main/inputForm.jsp");
			reqDispatcher.include(request, response);
						
		} else {			
			// print an error message to the web page (login error)
			PrintWriter pWriter= response.getWriter(); 
			pWriter.println("<font color=red>Either user name or password is wrong.</font>");
		}
		
	}

}
