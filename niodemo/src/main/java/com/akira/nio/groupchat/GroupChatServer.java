package com.akira.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 *
 * @author akira
 * @date 2021-08-07 19:44:53
 * @description
 */
public class GroupChatServer {

    public static final Integer PORT = 8888;

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    public GroupChatServer() throws IOException {
        this.selector = Selector.open();
        this.serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(PORT);
        this.serverSocketChannel.configureBlocking(false);
        this.serverSocketChannel.socket().bind(address);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public static void main(String[] args) throws IOException {
        // 创建服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }

    public void listen() {
        while (true) {
            try {
                int count = selector.select();
                if (count > 0) {
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        if (key.isAcceptable()) {
                            SocketChannel clientChannel = serverSocketChannel.accept();
                            clientChannel.configureBlocking(false);
                            clientChannel.register(selector, SelectionKey.OP_READ);
                            // online tips
                            System.out.println("client: " + clientChannel.getRemoteAddress() + " is online! ");
                        }

                        if (key.isReadable()) {
                            // read channel data
                            readClientMsg(key);
                        }
                        // 当前的 key 删除，防止重复处理
                        iter.remove();
                    }
                } else {
                    System.out.println("waiting the person with a destiny...");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readClientMsg(SelectionKey key) {
        SocketChannel clientChannel = null;
        try {
            clientChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // read msg from channel to buffer
            int length = clientChannel.read(buffer);
            if (length > 0) {
                String msg = new String(buffer.array());
                System.out.format("server get msg from client(%s) : %s\n", clientChannel.getRemoteAddress(), msg);

                // send msg to other clients
                sendMsgToOtherClients(msg, clientChannel);
            }
        } catch (IOException e) {
            try {
                System.out.println(clientChannel.getRemoteAddress() + "is offline...");
                //取消注册
                key.cancel();
                //关闭通道
                clientChannel.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void sendMsgToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("server send msg to others...");
        // 遍历所有注册到 selector 上的 SocketChannel,并排除 self
        for (SelectionKey key : selector.keys()) {
            // get channel
            Channel targetChannel = key.channel();
            // exclude self
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                SocketChannel dest = (SocketChannel) targetChannel;
                // send msg to buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                // writer msg from buffer to channel
                dest.write(buffer);
            }
        }
    }
}
