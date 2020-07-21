package com.yao.scene.tomcat.servlet;


import com.yao.scene.tomcat.http.GPRequest;
import com.yao.scene.tomcat.http.GPResponse;
import com.yao.scene.tomcat.http.GPServlet;

public class SecondServlet extends GPServlet {

    @Override
    public void doGet(GPRequest request, GPResponse response) throws Exception {
        this.doPost(request, response);
    }

    @Override
    public void doPost(GPRequest request, GPResponse response) throws Exception {
        response.write("This is Second Serlvet");
    }

}
