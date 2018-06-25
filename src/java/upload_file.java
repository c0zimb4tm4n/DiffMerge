import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;


@WebServlet(urlPatterns = {"/upload_file"})
@MultipartConfig
public class upload_file extends HttpServlet {
    String s1,s2,i,j;
    
        
    public List<DiffRow> diffmerge(String string1,String string2,String i,String j) throws DiffException
    {
        if(j.equals("1"))
        {
            String temp1= string1.replace("\n", " <newline> ");
            String temp2= string2.replace("\n", " <newline> ");
            string1=temp1+"\n";
            string2=temp2+"\n";
        }
      
        String[] a= string1.split("\\r?\\n");
        String[] b= string2.split("\\r?\\n");
       
        List<String> list1 = Arrays.asList(a);
        List<String> list2 = Arrays.asList(b);
    
        
        if(!i.equals("0"))
        {
            DiffRowGenerator generator = DiffRowGenerator.create()
                    .showInlineDiffs(true)
                    .inlineDiffByWord(true)
                    .oldTag(f -> "~")
                    .newTag(f -> "`")
                    .build();
            List<DiffRow> rows = generator.generateDiffRows(list1,list2);
            return rows;
        }
        else
        {
            DiffRowGenerator generator = DiffRowGenerator.create()
                    .showInlineDiffs(true)
                    .inlineDiffByWord(true)
                    .oldTag(f -> "~")
                    .newTag(f -> "`")
                    .ignoreWhiteSpaces(true)
                    .build();
            List<DiffRow> rows = generator.generateDiffRows(list1,list2);
            return rows;
        }
    }
    
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException
    {
        HttpSession session= request.getSession(); 
        PrintWriter out = response.getWriter();
        if(request.getParameter("text_1")!=null)
        {
            //input from text are
            s1=request.getParameter("text_1");
            s2=request.getParameter("text_2");
            i=request.getParameter("radio");
            j=request.getParameter("checked");
            
        }
        else
        {
            //input from text file upload
            Part file_1= request.getPart("file_1");
            Part file_2= request.getPart("file_2");
            if(file_1.getContentType().indexOf("text")<0)
            {
                session.setAttribute("error", "Please Select a text file");
                response.sendRedirect("file.jsp?a=2&e=1");
                return;
            }
            if(file_2.getContentType().indexOf("text")<0)
            {
                session.setAttribute("error", "Please Select a text file");
                response.sendRedirect("file.jsp?a=2&e=1");
                return;
            }
            InputStream file1= file_1.getInputStream();
            InputStream file2= file_2.getInputStream();
            Scanner scanner= new Scanner(file1);
            s1=scanner.useDelimiter("\\A").next();
            Scanner scanner2= new Scanner(file2);
            s2=scanner2.useDelimiter("\\A").next();
            i=request.getParameter("radio");
            j=request.getParameter("checked");
            
        }
        
        try{
            out.println("<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">\n"+
                        "<link rel=\"stylesheet\" href=\"file_css.css\">" +
                        "<title>DiffMerge</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    \n" +
                        "  <div class='container'>"+
                        "<h1><span style=\" color:black \">File Diff | DiffMerge</span></h1>");
       
            //
            List<DiffRow> row=diffmerge(s1,s2,i,j);
            int flag1=0,flag2=0,count=0;
            out.println("<br><br><table>"+
                        "<tr>\n" +
                        "<th id=\'a\'>L.</th>\n"+
                        "<th>File 1</th>\n" +
                    "<th id=\'a\'>L.</th>\n"+
                        "<th>File 2</th> \n" +
                        "</tr>");
            
            for (DiffRow r : row) 
            {
                if(r.getOldLine().equals(r.getNewLine()))
                {
                   
                }
                StringBuilder sb1 = new StringBuilder(r.getOldLine());
                StringBuilder sb2 = new StringBuilder(r.getNewLine());
                
                Pattern p5 = Pattern.compile(" <newline> ");
                Matcher m5 = p5.matcher(sb1); 
                while(m5.find())
                {
                    out.print("hello");
                    sb1.replace(m5.start(),m5.end(),"\n");
                }
               
                Pattern p1 = Pattern.compile("~");
                Matcher m1 = p1.matcher(sb1); 
                System.out.print("loop");
                while(m1.find())
                {
                    System.out.print("loop");
                    if(flag1==0)
                    {
                        sb1.replace(m1.start(),m1.end(),"<span id='l'>");
                        flag1=1;
                        m1 = p1.matcher(sb1); 
                    }
                    else
                    {
                        sb1.replace(m1.start(),m1.end(),"</span>");
                        flag1=0;
                        m1 = p1.matcher(sb1); 
                    }
                }
                Pattern p2 = Pattern.compile("`");
                Matcher m2 = p2.matcher(sb2); 
                
                while(m2.find())
                {
                    if(flag2==0)
                    {
                        sb2.replace(m2.start(),m2.end(),"<span id='r'>");
                        flag2=1;
                        m2 = p2.matcher(sb2); 
                    }
                    else
                    {
                        sb2.replace(m2.start(),m2.end(),"</span>");
                        flag2=0;
                        m2 = p2.matcher(sb2); 
                    }
                }
                Pattern p3 = Pattern.compile("span");
                Matcher m3 = p3.matcher(sb1);
                out.print("<tr>");
                if(m3.find()||(r.getOldLine().isEmpty()&&!r.getNewLine().isEmpty()))
                {
                    out.print("<td id='num' class='l'>"+count+"</td><td class='l'>"+sb1+"</td>");   
                }
                else
                {
                    out.print("<td id='num'>"+count+"</td><td>"+sb1+"</td>"); 
                }
                m3 = p3.matcher(sb2);
                if(m3.find()||(!r.getOldLine().isEmpty()&&r.getNewLine().isEmpty()))
                {
                    out.print("<td id='num' class='r'>"+count+"</td><td class='r'>"+sb2+"</td>");   
                }
                else
                {
                    out.print("<td id='num' >"+count+"</td><td>"+sb2+"</td>"); 
                }
                out.print("</tr>");
                count++;
                
            }
            out.println("</table>"+
                        "</div>\n" +
                        "</body>\n" +
                        "</html>");
            
            
            
            
        } catch (DiffException ex) {
            Logger.getLogger(upload_file.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        response.setContentType("text/html");
       
    }

   


}