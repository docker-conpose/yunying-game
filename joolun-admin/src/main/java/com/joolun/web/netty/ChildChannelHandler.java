package com.joolun.web.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ChildChannelHandler extends ChannelInitializer<NioSocketChannel> {
    @Override
    public void initChannel(NioSocketChannel ch){
        //心跳机制
        ch.pipeline().addLast(new IdleStateHandler(0, 0, 3, TimeUnit.MINUTES));

        ch.pipeline().addLast("decoder", new MyDecoder());
        ch.pipeline().addLast("encoder", new MyEncoder());
        ch.pipeline().addLast(new ChannelInboundHandler());
    }
}
