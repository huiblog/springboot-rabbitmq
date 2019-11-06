package com.xsg.Recever;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.xsg.config.RabbitMQExchangeConfig.QUEUE_C;

/***
 * @ClassName: MsgReceiver
 * @Description: TODO
 * @Author: 渣渣辉
 * @Date: 2019/7/10  16:59
 * @version : V1.0
 */
@Component
@RabbitListener(queues = QUEUE_C)
public class MsgReceiver {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @RabbitHandler
//    public void process(String content) {
//        logger.info("接收处理队列C当中的消息： " + content);
//    }

    @RabbitHandler
    public void processMessage2(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            channel.basicAck(tag, false);            // 确认消息
            logger.info("消费者成功确认" + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//
//    @RabbitHandler
//    public void processMessage2(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
//        System.out.println(message);
//        try {
//            channel.basicAck(tag,false);            // 确认消息
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    @RabbitHandler
//    public void processMessage22(String message, Channel channel,@Headers Map<String,Object> map) {
//        System.out.println("手动确认" + message);
//        try {
//            int i = 5/0;
//        } catch (Exception e) {
//          logger.error("错误" + e);
//        } finally {
//            try {
//               // channel.basicNack((Long)map.get(AmqpHeaders.DELIVERY_TAG),false,true);      //否认消息,一直重新入队列然后一直重新消费
//                channel.basicReject((Long)map.get(AmqpHeaders.DELIVERY_TAG),false); //拒绝该消息，消息会被丢弃，不会重回队列
//            } catch (IOException e) {
//                logger.error("拒绝消息出错" + e);
//            }
//        }

//        if (map.get("error")!= null){
//            System.out.println("错误的消息");
//            try {
//                channel.basicNack((Long)map.get(AmqpHeaders.DELIVERY_TAG),false,true);      //否认消息
//                return;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            channel.basicAck((Long)map.get(AmqpHeaders.DELIVERY_TAG),false);            //确认消息
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
