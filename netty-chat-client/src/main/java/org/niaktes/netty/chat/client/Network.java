package org.niaktes.netty.chat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Network {

    private static final String HOST = "localhost";
    private static final int PORT = 8189;

    private SocketChannel channel;

    public Network(Callback onMessageReceivedCallback) {
        Thread thread = new Thread(() -> {
            NioEventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        channel = socketChannel;
                        socketChannel.pipeline().addLast(new StringDecoder(), new StringEncoder(),
                                new ClientHandler(onMessageReceivedCallback));
                    }
                });
                ChannelFuture future = bootstrap.connect(HOST, PORT).sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } finally {
                workerGroup.shutdownGracefully();
            }
        });
        thread.start();
    }

    public void sendMessage(String message) {
        channel.writeAndFlush(message);
    }

    public void close() {
        channel.close();
    }

}