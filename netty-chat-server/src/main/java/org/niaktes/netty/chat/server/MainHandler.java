package org.niaktes.netty.chat.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainHandler extends SimpleChannelInboundHandler<String> {

    private static final List<Channel> CHANNELS = new ArrayList<>();
    private static AtomicInteger newClientIndex = new AtomicInteger(1);
    private String clientName;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Клиент успешно подключен: " + ctx);
        CHANNELS.add(ctx.channel());
        clientName = "Клиент #" + newClientIndex.getAndIncrement();
        broadcastMessage("SERVER", clientName + " зашёл в чат.");
    }

    /*
     У Netty есть правило - снаружи всегда приходит ByteBuf из пакета io.netty.buffer.
     После прочтения всего буффера, его надо обязательно освободить во избежание утечек памяти.
     Но в данном случае мы используем StringEncoder, который пришедшие байты форматирует в Стрингу.
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        log.info("Получено сообщение от " + clientName + ": " + s);
        if(s.startsWith("/")) {
            if(s.startsWith("/changename")) {
                String newName = s.split("\\s", 2)[1];
                broadcastMessage("SERVER", clientName + " сменил имя на " + newName);
                clientName = newName;
            }
            return;
        }
        broadcastMessage(clientName, s);
    }

    public void broadcastMessage(String clientName, String message) {
        String out = String.format("[%s]: %s%n", clientName, message);
        for (Channel c : CHANNELS) {
            c.writeAndFlush(out);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Клиент " + clientName + " покинул сервер.");
        CHANNELS.remove(ctx.channel());
        broadcastMessage("SERVER", clientName + " вышел из чата.");
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("Клиент " + clientName + " вышел с ошибкой.");
        CHANNELS.remove(ctx.channel());
        broadcastMessage("SERVER", clientName + " вышел из чата.");
        ctx.close();
    }

}