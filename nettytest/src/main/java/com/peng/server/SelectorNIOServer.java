package com.peng.server;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SelectorNIOServer {
    //server端
    private ServerSocketChannel server;
    //selector多路复用
    private Selector selector;
    //端口
    private int port;
    //readBuffer
    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    //writeBuffer
    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

    private SelectorNIOServer() {
        try {
            //打开server
            server = ServerSocketChannel.open();
            //非阻塞模式，默认为true，阻塞模式
            server.configureBlocking(false);
            //打开多路复用,可开启多个多路复用
            selector = Selector.open();
            //感兴趣状态SelectionKey.OP_ACCEPT;SelectionKey.OP_READ;SelectionKey.OP_WRITE;SelectionKey.OP_CONNECT
            server.register(selector, SelectionKey.OP_ACCEPT);
            //绑定端口
            server.socket().bind(new InetSocketAddress(8888));
            System.out.println("----------服务器启动-----------");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //单例模式
    private static class SingleClass {
        private static final SelectorNIOServer INSTANCE = new SelectorNIOServer();

    }

    public static SelectorNIOServer open() {
        return SingleClass.INSTANCE;
    }

    //测试方法
    public static void main(String[] args) {
        SelectorNIOServer instance = SelectorNIOServer.open();
        try {
            instance.processSelector();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void processSelector() throws IOException {
        //多路复用轮询事件
        while (selector.select() > 0) {
            try {
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    process(next);
                    //移除SelectionKey
                    iterator.remove();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void process(SelectionKey next) throws Exception {

        if (next.isValid() && next.isReadable()) {
            System.out.println("可读");
            SocketChannel accept = (SocketChannel) next.channel();
            readData(accept);
            accept.register(selector,SelectionKey.OP_WRITE);
        }
        if (next.isValid() && next.isAcceptable()) {
            System.out.println("可接受");
            SocketChannel accept = server.accept();
            accept.configureBlocking(false);
            accept.register(selector, SelectionKey.OP_READ);
        }  if (next.isValid() && next.isWritable()) {
            System.out.println("可写");
            SocketChannel accept = (SocketChannel) next.channel();
             writeData(accept);
             //关闭通道，否则会不停的轮询报错
             accept.socket().close();
        }
    }

    private void writeData(SocketChannel accept) {
        writeBuffer.put("服务器已接收到信息，稍后处理".getBytes());
        writeBuffer.flip();
        try {
            accept.write(writeBuffer);
            writeBuffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readData(SocketChannel accept) {

        try {
            //处理数据
            if (accept != null) {
                int read = accept.read(readBuffer);
                readBuffer.flip();
                System.out.println(new String(readBuffer.array(), 0, read));
                readBuffer.clear();
                //accept.register(selector, SelectionKey.OP_WRITE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                accept.socket().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
}