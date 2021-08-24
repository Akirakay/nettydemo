package com.akira.codec.server;

import com.akira.codec.pojo.MyDataInfo;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 *
 * @author akira
 * @date 2021-08-16 22:32:46
 * @description
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        if (msg.getDataType().equals(MyDataInfo.MyMessage.DataType.StudentType)) {
            System.out.println("receive a student:" + msg.getStudent().getName());
        } else {
            System.out.println("receive a worker:" + msg.getWorker().getName());
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client ^_^", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
