package com.akira.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author akira
 * @date 2021-08-07 16:16:40
 * @description
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);

        // 配置远程服务器 IP && PORT
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 9999);

        // 未连接成功
        if (!sc.connect(address)) {
            while (!sc.finishConnect()) {
                // 非阻塞进行其他任务
                System.out.println("do something else!");
            }
        }
        // 连接成功
//        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String welcome = "hello server! -- from client!";
        ByteBuffer buffer = ByteBuffer.wrap(welcome.getBytes(StandardCharsets.UTF_8));
        sc.write(buffer);
        System.out.println("client send : " + welcome);
        System.in.read();
    }
}
