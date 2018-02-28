package com.peng.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 处理服务端 channel
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf bf;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        bf=(ByteBuf)msg;
        while (bf.isReadable()){
            byte b = bf.readByte();
            System.out.println((char)b);
        }
        bf.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
