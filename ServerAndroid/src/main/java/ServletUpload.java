import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;


public class ServletUpload extends HttpServlet{

    private int bytesRead, bytesAvailable, bufferSize;
    private byte[] buffer;
    private int maxBufferSize = 1*1024*1024;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement((String) null);
            req.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, multipartConfigElement);
            System.out.println("Olla");
            String file=req.getPart("file1").getSubmittedFileName().split("/")[req.getPart("file1").getSubmittedFileName().split("/").length-1];
            System.out.println(file);



            byte [] b= IOUtils.toByteArray(new InputStreamReader(req.getPart("description").getInputStream()),"UTF-8");
            String filesource=new String(b,"UTF-8");
            String name=filesource.split(";")[0].split("=")[1].replace(" ","1");

            File dirsound=new File("recorder/"+name+"/sounds");
            File dirsmeta=new File("recorder/"+name+"/metas");
            try{
                dirsound.mkdir();
                dirsmeta.mkdir();

            }
            catch (Exception e){
                System.out.println("dir exists");
            }
            FileUtils.writeByteArrayToFile(new File("recorder/"+name+"/sounds/"+file),IOUtils.toByteArray(req.getPart("file1").getInputStream()));
            System.out.println(new String(b,"UTF-8"));
            String num=file.split("g")[1];

            FileUtils.writeByteArrayToFile(new File("recorder/"+name+"/metas/meta"+num.substring(0,num.length()-4)+"txt"),b);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e){
resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
e.printStackTrace();
        }

    }
}