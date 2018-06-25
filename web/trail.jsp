<%-- 
    Document   : trail
    Created on : 20 Jun, 2018, 1:40:26 PM
    Author     : Administrator
--%>

<%@page import="org.apache.commons.io.FileUtils"%>
<%@page import="java.io.File"%>
<%@page import="org.apache.tomcat.util.codec.binary.Base64"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<title>Files | DiffMerge</title>
</head>
<body>
<div class="container">        
    <h1><span style=" color:black ">File Diff | DiffMerge</span></h1>
        <%
            byte[] valueDecoded = Base64.decodeBase64(request.getParameter("path_1").getBytes());
            String s1= new String(valueDecoded);
            byte[] valueDecoded2 = Base64.decodeBase64(request.getParameter("path_2").getBytes());
            String s2= new String(valueDecoded2);
            
            File source= new File(s1);
            File target= new File(s2);
            String a = FileUtils.readFileToString(source);
            String b = FileUtils.readFileToString(target);
        %>
        
    <form action ="upload_file" method="POST"><p>
        <textarea rows=15 cols=75 name='text_1'>
        <% out.print(a); %>
        </textarea>
        <textarea rows=15 cols=75 name='text_2'>
        <% out.print(b); %>
        </textarea><br></p><p>
        <label for='1'>Include Whitespaces</label>
        <input type="radio" value="1" name=radio id='1'>
        <label for='2'>Exclude Whitespaces</label>
        <input type="radio" value="0" name=radio id='2' checked></p><p>
        <label for='3'>Include Newline</label>
        <input type="radio" value="1" name="checked" id='3'>
        <label for='4'>Exclude Newline</label>
        <input type="radio" value="0" name="checked" id='4' checked></p><p>
        <input type="submit" name="submit"></p>
    </form>       

</div>
</body>
</html>
