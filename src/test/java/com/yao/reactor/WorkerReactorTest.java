package com.yao.reactor;

import com.yao.serialize.PkgClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class WorkerReactorTest {

    public final String HOST = "localhost";
    public final int PORT = 9999;


    @Test
    void server() throws IOException {
        new WorkerReactor().bind(PORT);
    }


    @Test
    public void client() throws InterruptedException {
        new PkgClient(new SingleReactorTest.SingleReactorHandler()).connect(HOST, PORT);
    }
}