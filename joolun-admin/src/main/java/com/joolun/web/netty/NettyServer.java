package com.joolun.web.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
@Slf4j
public class NettyServer {
    public void start(InetSocketAddress socketAddress){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boos = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            serverBootstrap.group(boos, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChildChannelHandler())
                    //设置队列大小
                    .option(ChannelOption.SO_BACKLOG, 2048);
            ChannelFuture future1 = serverBootstrap.bind(socketAddress).addListener(future -> {
                        if (future.isSuccess()) {
                            log.info("netty 端口[8188]绑定成功!");
                        } else {
                            log.info("netty 端口[8188]绑定失败!");
                        }
                    }).sync();
            future1.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            /**
             * 退出，释放线程池资源
             */
            boos.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
