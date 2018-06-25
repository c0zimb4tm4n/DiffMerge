import net.lingala.zip4j.core.ZipFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.codec.binary.Base64;

@WebServlet(urlPatterns = {"/directory"})
@MultipartConfig
public class directory extends HttpServlet {
    
    File dir_1;
    File dir_2;
    static List<String> names=new ArrayList();
    HashMap<String, String> mapA1 = new HashMap<>();
    HashMap<String, String> mapB1 = new HashMap<>();
    HashMap<String, File> mapA2 = new HashMap<>();
    HashMap<String, File> mapB2 = new HashMap<>();

    
    public String checksum(File file) throws FileNotFoundException, IOException{
        FileInputStream fis = new FileInputStream(file);
        CRC32 crcMaker = new CRC32();
        byte[] buffer = new byte[65536];
        int bytesRead;
        while((bytesRead = fis.read(buffer)) != -1) {
            crcMaker.update(buffer, 0, bytesRead);
        }
        return Long.toString(crcMaker.getValue()); 
    }
    
    
    public void addFiles(File a, HashMap<String, String> map1, HashMap<String, File> map2) throws IOException{
        File[] list=a.listFiles();
        for(File fil : list)
        {
            if(fil.isDirectory())
            {
                addFiles(fil,map1,map2);
            }
            else
            {
                String c =checksum(fil);
                map1.put(fil.getName(),c);
                map2.put(c,fil);
            }
        }
    }
    
   
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try {
            PrintWriter out = response.getWriter();
            ServletFileUpload sf= new ServletFileUpload(new DiskFileItemFactory());
            List<FileItem> multi= sf.parseRequest(request);
            for(FileItem item : multi )
            {
                item.write(new File("C:\\A\\"+item.getName()));
                names.add(item.getName());
            }
            ZipFile zipFile = new ZipFile("C:/A/"+ names.get(0));
            zipFile.extractAll("C:/source");
            ZipFile zipFile2 = new ZipFile("C:/A/"+ names.get(1));
            zipFile2.extractAll("C:/target");
            //unzipping
            dir_1=new File("C:\\source");
            dir_2=new File("C:\\target");
            addFiles(dir_1, mapA1,mapA2);
            addFiles(dir_2, mapB1,mapB2);
            
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
                        "<h1><span style=\" color:black \">Directory Diff | DiffMerge</span></h1>");
            out.println("<br><br><table>"+
                        "<tr>\n" +
                        "<th>Directory 1</th>\n"+
                        "<th>Directory 2</th>\n" +
                        "<th>Action</th>\n"+
                        "</tr>");
            
            
            for (Map.Entry<String, String> entry : mapA1.entrySet()) 
            {
                out.print("<tr>");
                
                if(mapB1.containsKey(entry.getKey()))
                {
                    
                    if(mapB1.get(entry.getKey()).equals(entry.getValue()))
                    {
                        out.println("<td>"+entry.getKey()+"</td><td>"+entry.getKey() +"</td><td>unchanged</td>");
                        mapB2.remove(mapB1.get(entry.getKey()));
                        mapB1.remove(entry.getKey());
                    }
                    else
                    {
                        File f1=mapA2.get(entry.getValue());
                        File f2=mapB2.get(mapB1.get(entry.getKey()));
                        String temp1=f1.getPath();
                        String temp2=f2.getPath();
                        byte[] bytesEncoded = Base64.encodeBase64(temp1.getBytes());
                        byte[] bytesEncoded1 = Base64.encodeBase64(temp2.getBytes());
                        String a1 =new String(bytesEncoded);
                        String a2 =new String(bytesEncoded1);
                        out.println("<td>"+entry.getKey()+"</td><td>"+entry.getKey() +"</td><td><a href=\"trail.jsp?path_1="+a1+"&path_2="+a2+"\">modified</a></td>");
                        mapB2.remove(mapB1.get(entry.getKey()));
                        mapB1.remove(entry.getKey());
                    }
                }
                else if(mapB2.containsKey(entry.getValue()))
                {
                    out.println("<td>"+entry.getKey()+"</td><td>"+mapB2.get(entry.getValue()).getName()+"</td><td>rename</td>");
                    File f=mapB2.get(entry.getValue());
                    mapB2.remove(entry.getValue());
                    mapB1.remove(f.getName());
                }
                else
                {
                    out.println("<td>"+entry.getKey()+"</td>\t-\t<td>" +"</td><td>removed</td>");
                }
                out.print("</tr>");
            }
            
            for (Map.Entry<String, String> entry : mapB1.entrySet()) 
            {
                out.println("<tr><td></td><td>"+entry.getKey()+"</td><td>added</td></tr>");
            }
            out.println("</div>"
                    + "</body>"
                    + "</html>");
              
            

            
        } catch (FileUploadException ex) {
            Logger.getLogger(directory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(directory.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        
    }
    

}
