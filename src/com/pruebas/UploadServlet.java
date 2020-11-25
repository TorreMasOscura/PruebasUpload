package com.pruebas;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

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

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		servletContext = getServletContext();
	}

//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//	    final Part filePart = request.getPart("uploadFile"); // get the file to upload part
//	    final String fileFullPathName = getFileName(filePart);	// get the full path of the file to upload
//
//	    String url = servletContext.getInitParameter("docFolder");
//	    // extract the file name from its full path
//	    Path p = Paths.get(fileFullPathName);
//	    String fileName = p.getFileName().toString();	
//	    
//	    // get the full path on the server to assign to the file to upload
//	    String outputFileFullPathName = url +  fileName;
//	    
//	    OutputStream out = null;
//	    InputStream filecontent = null;
//	    final PrintWriter writer = response.getWriter();
//	    
//	    URLConnection connection = new URL(url).openConnection();
//	    connection.setDoOutput(true);
//	    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//
//	    
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
	
	    HttpURLConnection conn;
	    DataOutputStream dos;
	    String lineEnd = "\r\n";
	    String twoHyphens = "--";
	    String boundary = "*****";
	    int bytesRead, bytesAvailable, bufferSize;
	    byte[] buffer;
	    int maxBufferSize = 1024 * 1024;
	    File sourceFile = new File("192.168.0.130:2049/mnt/shared");

	    
	    final Part filePart = request.getPart("uploadFile"); // get the file to upload part
	    final String fileFullPathName = getFileName(filePart);	// get the full path of the file to upload

	    String ruta = servletContext.getInitParameter("docFolder");
	    // extract the file name from its full path
	    Path p = Paths.get(fileFullPathName);
	    String nombreArchivo = p.getFileName().toString();	
	    
	    // get the full path on the server to assign to the file to upload
	    String outputFileFullPathName = ruta +  nombreArchivo;
	    
	        try {

	            FileInputStream fileInputStream = new FileInputStream(nombreArchivo);
//	            dos = new DataInputStream(fileInputStream);
	            URL url = new URL("192.168.0.130:2049/mnt/shared");  

	            conn = (HttpURLConnection) url.openConnection();
	            conn.setDoInput(true); // Allow Inputs
	            conn.setDoOutput(true); // Allow Outputs
	            conn.setUseCaches(false); // Don't use a Cached Copy
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Connection", "Keep-Alive");
	            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	            conn.setRequestProperty("uploaded_file", "192.168.0.130:2049/mnt/shared");

	            dos = new DataOutputStream(conn.getOutputStream());

	            dos.writeBytes(twoHyphens + boundary + lineEnd);
	            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
	                    + nombreArchivo + "\"" + lineEnd);

	            dos.writeBytes(lineEnd);

	            bytesAvailable = fileInputStream.available();

	            bufferSize = Math.min(bytesAvailable, maxBufferSize);
	            buffer = new byte[bufferSize];

	            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

	            while (bytesRead > 0) {

	                dos.write(buffer, 0, bufferSize);
	                bytesAvailable = fileInputStream.available();
	                bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

	            }

	            dos.writeBytes(lineEnd);
	            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

	            int serverResponseCode = conn.getResponseCode();

	            System.out.println("Codigo de respuesta : " + serverResponseCode);
	        

	            fileInputStream.close();
	            dos.flush();
	            dos.close();

	        } catch (MalformedURLException ex) {

	        	System.out.println("Error en la formacion de la URL");

	        } catch (Exception e) {

	            e.printStackTrace();

	        }	    
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
