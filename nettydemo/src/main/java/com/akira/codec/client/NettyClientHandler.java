package com.akira.codec.client;

import com.akira.codec.pojo.MyDataInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

/**
 *
 * @author akira
 * @date 2021-08-16 22:45:08
 * @description
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
    // channel is ready
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int randomInt = new Random().nextInt(3);
        MyDataInfo.MyMessage msg = null;
        if (randomInt % 2 == 0) {
            msg = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.StudentType)
                    .setStudent(MyDataInfo.Student.newBuilder()
                            .setId(1)
                            .setName("akira")
                            .build())
                    .build();
        } else {
            msg = MyDataInfo.MyMessage.newBuilder()
                    .setDataType(MyDataInfo.MyMessage.DataType.WorkerType)
                    .setWorker(MyDataInfo.Worker.newBuilder()
                            .setAge(24)
                            .setName("worker")
                            .build())
                    .build();
        }
        ctx.writeAndFlush(msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        System.out.println("服务器的地址： " + ctx.channel().remoteAddress());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
