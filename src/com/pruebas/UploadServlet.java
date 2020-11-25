package com.pruebas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


import sun.net.www.http.HttpClient;


/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/upload")
@MultipartConfig(maxFileSize=1024*1024*5)
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ServletContext servletContext;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		servletContext = getServletContext();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//	    final Part filePart = request.getPart("uploadFile"); // get the file to upload part
//	    final String fileFullPathName = getFileName(filePart);	// get the full path of the file to upload
//	    
//
//	    String docFolder = servletContext.getInitParameter("rutaSubida");
//	    // extract the file name from its full path
//	    Path p = Paths.get(fileFullPathName);
//	    String fileName = p.getFileName().toString();	
//	    
//	    // get the full path on the server to assign to the file to upload
//	    String outputFileFullPathName = docFolder +  fileName;
//	    
//	    OutputStream out = null;
//	    InputStream filecontent = null;
//	    final PrintWriter writer = response.getWriter();
//
//	    try {
//	    	// open the outputstream where to write the file on the server
//	        out = new FileOutputStream(new File(outputFileFullPathName));
//	        
//	        // open the input stream of the file to upload
//	        filecontent = filePart.getInputStream();
//
//	        int read = 0;
//	        final byte[] bytes = new byte[1024];
//
//	        // write the file content from input stream to output stream
//	        while ((read = filecontent.read(bytes)) != -1) {
//	            out.write(bytes, 0, read);
//	        }
//	        
//	        writer.println("New file '" + fileName + "' uploaded!" );
//	        
//	        
//	    } catch (FileNotFoundException fne) {
//	        writer.println("<br/> ERROR: " + fne.getMessage());
//
//	        
//	    } finally {
//	        if (out != null) {
//	            out.close();
//	        }
//	        if (filecontent != null) {
//	            filecontent.close();
//	        }
//	        if (writer != null) {
//	            writer.close();
//	        }
//	    }	  
//	}
//	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//get the file chosen by the user
		Part filePart = request.getPart("uploadFile");
		
	    final String fileFullPathName = getFileName(filePart);	// get the full path of the file to upload
	    

	    String docFolder = servletContext.getInitParameter("rutaSubida");
	    // extract the file name from its full path
	    Path p = Paths.get(fileFullPathName);
	    String fileName = p.getFileName().toString();
		
		
//		if(getSubmittedFileName(filePart).endsWith(".jpg") || getSubmittedFileName(filePart).endsWith(".png")){

		    InputStream fileInputStream = filePart.getInputStream();
		    File fileToSave = new File("C:\\Users\\MORENORALEXT\\Desktop\\Fujitsu\\" + fileName);
			Files.copy(fileInputStream, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			String fileUrl = "http://localhost:8080/uploaded-files/" + getSubmittedFileName(filePart);			
			String name = request.getParameter("name");
			
			response.getOutputStream().println("<p>Thanks " + name + "! Here's the image you uploaded:</p>");
			response.getOutputStream().println("<img src=\"" + fileUrl + "\" />");
			response.getOutputStream().println("<p>Upload another image <a href=\"http://localhost:8080/index.html\">here</a>.</p>");	
//		}
//		else{
//			//the file was not a JPG or PNG
//			response.getOutputStream().println("<p>Please only upload JPG or PNG files.</p>");
//			response.getOutputStream().println("<p>Upload another file <a href=\"http://localhost:8080/index.html\">here</a>.</p>");	
//		}
	}
	
	private static String getSubmittedFileName(Part part) {
	    for (String cd : part.getHeader("content-disposition").split(";")) {
	        if (cd.trim().startsWith("filename")) {
	            String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	            return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
	        }
	    }
	    return null;
	}
	
	private String getFileName(final Part part) {
	    for (String content : part.getHeader("content-disposition").split(";")) {
	        if (content.trim().startsWith("filename")) {
	            return content.substring(
	                    content.indexOf('=') + 1).trim().replace("\"", "");
	        }
	    }
	    return null;
	}

}
