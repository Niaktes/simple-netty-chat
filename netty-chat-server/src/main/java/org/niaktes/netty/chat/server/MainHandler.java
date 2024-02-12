package org.niaktes.netty.chat.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Клиент успешно подключен.");
    }

    /*
     У Netty есть правило - снаружи всегда приходит ByteBuf из пакета io.netty.buffer.
     После прочтения всего буффера, его надо обязательно освободить во избежание утечек памяти.
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;
        while (buffer.readableBytes() > 0) {
            System.out.print((char) buffer.readByte());
        }
        buffer.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}