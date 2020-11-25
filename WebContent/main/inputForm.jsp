<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>NFS</title>
</head>
<body>
<h1>Pruebas para NFS</h1>

<%-- <strong>Welcome <%=session.getAttribute("username") %> </strong> --%>

<h2>Descarga</h2>
<!-- nombre del proyecto con el nombre del patron en el web -->
<form action="/PruebasUpload/download" method="get">
Nombre Fichero:	<input type="text" name="downloadFileName">
	<input type="submit" value="Bajar fichero">
</form>

<h2>Subida</h2>
<form action="/PruebasUpload/upload" method="post" enctype="multipart/form-data">
Escoge fichero para subida:	<input type="file" name="uploadFile">
	<input type="submit" value="Subir fichero">
</form>

</body>
</html>