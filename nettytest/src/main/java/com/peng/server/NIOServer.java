package com.peng.server;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NIOServer {
    //使用nio api创建socketserverchannel
    private static final int port = 8888;

    public static void main(String[] args) {
        try {
            serverposs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void serverposs() throws IOException {
        //创建server对象
        ServerSocketChannel serverSocketChannel = null;
        SocketChannel accept = null;
        serverSocketChannel = ServerSocketChannel.open();
        //配置为非阻塞模式，默认为阻塞模式
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(port));


        System.out.println("--------服务端启动--------");
        while (true) {
            try {
                //server端接受数据
                accept = serverSocketChannel.accept();

                ByteBuffer allocate = ByteBuffer.allocate(1024);
                if (accept != null) {
                    int read = accept.read(allocate);
                    allocate.flip();
                    System.out.println(new String(allocate.array(),0,read));
                    accept.socket().close();
                }
            } catch (IOException e) {
//出现错误关闭套接字
                if (accept != null) {
                    accept.socket().close();
                }
                e.printStackTrace();

            }

        }
    }
}