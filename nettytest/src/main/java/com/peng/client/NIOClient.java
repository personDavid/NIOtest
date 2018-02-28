package com.peng.client;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class NIOClient {
    private static int ii;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) {
        //       clientprocess();
        for (; ii < 10000; ii++) {
           /* try {
                Thread.sleep(new Random().nextInt(400));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            clientprocess();
        }

    }

    private static void clientprocess() {
        SocketChannel channel = null;
        try {
            channel = SocketChannel.open();
            //默认true，阻塞，会持续到建立连接或者报错
            // 配置异步阻塞，必须调用finishconnect方法才能真正的连接到服务器
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress("localhost", 8888));

            while (!channel.finishConnect()) {
                System.out.println(atomicInteger.incrementAndGet());
            }

            ByteBuffer allocate = ByteBuffer.allocate(320);
            allocate.put(("第" + ii + "用户进入聊天室").getBytes());
            allocate.flip();
            if (channel.isConnected()) {
                channel.write(allocate);
                allocate.clear();
                int read = channel.read(allocate);
                allocate.flip();
                System.out.println(new String(allocate.array(), 0, read));
            }

        } catch (IOException e) {
            try {
                if (channel != null)
                    channel.socket().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

    }
}
