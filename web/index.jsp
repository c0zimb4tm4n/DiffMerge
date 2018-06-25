<%-- 
    Document   : index
    Created on : 19 Jun, 2018, 10:45:09 AM
    Author     : Administrator
--%>

<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    public void dir_delete(File index)
    {
        String entries[]=index.list();
        System.out.println("called del");
        for(String s:entries)
        {
            File currentFile = new File(index.getPath(),s);
            if(currentFile.isDirectory())dir_delete(currentFile);
            currentFile.delete();
        }
    }
    public void flush()
    {
        System.out.println("called");
        File source= new File("c:\\source");
        File target= new File("c:\\target");
        File zip= new File("c:\\A");
        dir_delete(source);
        dir_delete(target);
        dir_delete(zip);

    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <title>DiffMerge</title>
    </head>
    <body>
        <div class="container">
            <h1><span style=" color:black ">Welcome | DiffMerge</span></h1>
        <%
            if(null==request.getParameter("x"))
            {
                flush();
                request.setAttribute("x", "0");
                %>
                <p>
                <p> <a href="index.jsp?x=1">Files v Files</a></p>
                <p> <a href="index.jsp?x=2">Directory v Directory</a></p>
                </p>
                <%
            }
            else if(request.getParameter("x").equals("1"))
            {
                %>
                <p>
                <p> <a href="index.jsp?x=3">Upload Files</a></p>
                <p> <a href="index.jsp?x=4">Enter text manually</a></p><br><br>
                <p> <a href="index.jsp">Back</a></p>
                </p>
                <% 
            }
            else if(request.getParameter("x").equals("2"))
            {
                flush();
                %>
                <form method="post" action="directory" enctype="multipart/form-data">
                    <p>
                        <label for='f1'>File 1</label>
                        <input type="file" name="file_1" id="f1">
                    </p>
                    <p>
                        <label for='f2'>File 2</label>
                        <input type="file" name="file_2" id="f2">
                    </p>
                    <p>
                        <input type="submit">
                    </p>
                
                </form><br><br>
                <p> <a href="index.jsp?">Back</a></p>
                
                
                <%
            }
            else if(request.getParameter("x").equals("3"))
            {
                response.sendRedirect("file.jsp?a=2");
            }
            else if(request.getParameter("x").equals("4"))
            {
                response.sendRedirect("file.jsp?a=1");
            }
            %>
    </div>
    </body>
</html>
