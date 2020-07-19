package com.yao.sample.tomcat.servlet;


import com.yao.sample.tomcat.http.GPRequest;
import com.yao.sample.tomcat.http.GPResponse;
import com.yao.sample.tomcat.http.GPServlet;

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
