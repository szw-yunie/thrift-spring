package com.willing.thrift.spring.core.hello;

import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class HelloHandler implements Hello.Iface {
    @Override
    public String sayHello(String name) throws TException {
        return "Hello, " + name;
    }

    public static void main(String[] args) throws TException {
        Hello.AsyncIface client = new Hello.AsyncClient(null, null, null);
        client.sayHello("", null);

    }

    private static void service() {
        try {

            Hello.Processor<HelloHandler> processor = new Hello.Processor<>(new HelloHandler());
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

            // Use this for a multithreaded server
            // TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

            System.out.println("Starting the simple server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
