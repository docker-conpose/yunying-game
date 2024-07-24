package com.joolun;

import com.joolun.web.netty.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.InetSocketAddress;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class JooLunWxApplication
{
    public static void main(String[] args)
    {
        ConfigurableApplicationContext context = SpringApplication.run(JooLunWxApplication.class, args);
        NettyServer server = context.getBean(NettyServer.class);
        server.start(new InetSocketAddress("0.0.0.0", 3307));
    }
}
