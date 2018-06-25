<%@page import="org.apache.commons.io.FileUtils"%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
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
    if(null!=request.getParameter("e"))
    {
        out.print("<span style=\" color:red \" >Please upload a readable text file</span>");
        request.removeAttribute("e");
    }
%>
<%
if(request.getParameter("a").equals("1"))
{
%>
<p>
    <form action ="upload_file" method="POST"><p>
        <textarea rows=15 cols=75 name='text_1'>
            Enter text here...
        </textarea>
        <textarea rows=15 cols=75 name='text_2'>
            Enter text here...
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
    </form>  <br><br>
                <p> <a href="index.jsp?x=1">Back</a></p>     
</p>
<%
    }
else{

%>
<form action="upload_file" method="post" enctype="multipart/form-data">
<p>
    <p>
        <label for='f1'>File 1</label>
        <input type="file" name="file_1" id="f1">
    </p>
    <p>
        <label for='f2'>File 2</label>
        <input type="file" name="file_2" id="f2">
    </p>
    <p>
        <label for='1'>Include Whitespaces</label>
        <input type="radio" value="1" name=radio id='1'>
        <label for='2'>Exclude Whitespaces</label>
        <input type="radio" value="0" name=radio id='2' checked>
    </p>
    <p>
        <label for='3'>Include Newline</label>
        <input type="radio" value="1" name="checked" id='3'>
        <label for='4'>Exclude Newline</label>
        <input type="radio" value="0" name="checked" id='4' checked>
    </p>
        <input type="submit" name="submit">
</p>
</form><br><br>
                <p> <a href="index.jsp?x=1">Back</a></p>

<%

}
%>
</div>      
</body>
</html>
