package com.pruebas;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DownloadServlet
 */
//@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ServletContext servletContext;
	private String downloadedFilePrefix;
	/**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		servletContext = getServletContext();
		
		downloadedFilePrefix = config.getInitParameter("downloadedFilePrefix");
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String docFolder = servletContext.getInitParameter("rutaBajada");
		System.out.println("docFolder: " + docFolder);
		System.out.println("downloadedFilePrefix: " + downloadedFilePrefix);
		
		String downloadFileName = request.getParameter("downloadFileName");
		System.out.println("downloadFileName: " + downloadFileName);

		if (downloadFileName!=null && docFolder!=null){
			String downloadFileFullPath = URLDecoder.decode(docFolder + downloadFileName, "ISO_8859_1");

	        File downloadFile = new File(downloadFileFullPath); // get the full path on the server of the file to download
	         	         
	         if (downloadFile.exists()){
	 	        FileInputStream inStream = new FileInputStream(downloadFile); // open inputstream of the file to download
	 	        
		        // gets MIME type of the file
		        String mimeType = servletContext.getMimeType(downloadFileFullPath);
		        if (mimeType == null) {        
		            // set to binary type if MIME mapping not found
		            mimeType = "application/octet-stream";
		        }
		        System.out.println("MIME type: " + mimeType);
		         
		        // modifies response
		        response.setContentType(mimeType);
		        response.setContentLength((int) downloadFile.length());
		         
		        // sets response header
		        String headerKey = "Content-Disposition";
		        String headerValue = String.format("attachment; filename=\"%s\"", downloadedFilePrefix + downloadFile.getName());
		        response.setHeader(headerKey, headerValue);
		         
		        // gets response's output stream
		        OutputStream outStream = response.getOutputStream();
		         
		        byte[] buffer = new byte[4096];
		        int bytesRead = -1;
		         
		        // reads inputstream of file to download and write it on response outputstream
		        while ((bytesRead = inStream.read(buffer)) != -1) {
		            outStream.write(buffer, 0, bytesRead);
		        }
		         
		        // closes output and input stream
		        inStream.close();
		        outStream.close();     
	        	 
	         } else {
	        	 String errorMessage = "Error: file '" + 
	        			 			   request.getParameter("downloadFileName") + 
	        			 			   "' does not exist in '" + 
	        			 			   docFolder + 
	        			 			   "' folder!";
	        	 // ATTENTION: it's not possible to open both PrintWriter and outputStrim for the same response,
	        	 // therefore they are opened in alternative  block code (if and else)
	        	 PrintWriter writer = response.getWriter(); 
	        	 writer.println(errorMessage);	        	 
	         }
	         
		}
 		
	}

}
