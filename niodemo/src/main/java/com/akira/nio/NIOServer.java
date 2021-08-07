package com.akira.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author akira
 * @date 2021-08-07 15:53:43
 * @description
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        // 创建 ServerSocketChannel
        ServerSocketChannel ssc = ServerSocketChannel.open();

        // 创建 selector
        Selector selector = Selector.open();

        // 绑定端口号
        ssc.socket().bind(new InetSocketAddress(9999));

        // 设置非阻塞模式
        ssc.configureBlocking(false);

        // 将 ssc 注册到 selector 上 监听连接事件
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // 无事件发生
            if (selector.select(1000) == 0) {
                System.out.println("no any event happened!");
                continue;
            }
            // 监听事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 待连接事件
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();

                    // 设置 sc 为非阻塞
                    sc.configureBlocking(false);
                    System.out.println("client connected successful!  ----> " + sc.hashCode());

                    // 注册 sc 到 selector
                    sc.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }
                // 待读取事件
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    buffer.flip();

                    StringBuffer receiveStrBuffer = new StringBuffer();
                    while (buffer.hasRemaining()) {
                        receiveStrBuffer.append((char) buffer.get());
                    }
                    System.out.println("watched client:" + receiveStrBuffer.toString());
                }
                //移除处理的key
                iterator.remove();
            }

        }
    }
}
