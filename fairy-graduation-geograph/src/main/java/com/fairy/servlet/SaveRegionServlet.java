package com.fairy.servlet;

import com.fairy.utils.FileUtil;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class SaveRegionServlet extends HttpServlet implements Servlet{
    private FileUtil fileUtil = FileUtil.getInstance();
    private String outPath = "fairy-graduation-geograph/data/regions.json";

//    public static void main(String[] args) throws ServletException, IOException {
//        new SaveRegionServlet().test();
//    }
//
//    public void test(){
//        System.out.println("hello world");
//    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);

        try{
            String nodesStr = req.getParameter("nodes");
            File file = new File(outPath);
            if(!file.exists()){
                file.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)));
            bw.append(nodesStr);
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        fileUtil.saveToFile(outPath, nodesStr, false);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        this.doPost(req, resp);
    }


}
