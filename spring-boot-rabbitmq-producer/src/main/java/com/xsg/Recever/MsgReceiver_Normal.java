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

import static com.xsg.config.RabbitMQExchangeConfig.NORAL_QUEUE_NAME;

@Component
public class MsgReceiver_Normal {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitListener(queues = NORAL_QUEUE_NAME)
    public void processMessage2(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        String msg = new String(message.getBody());
        logger.info("收到业务消息A：{}" , msg);
        boolean ack = true;
        Exception exception = null;
        try {
            if (msg.contains("deadletter")){
                throw new RuntimeException("dead letter exception");
            }
        } catch (Exception e){
            ack = false;
            exception = e;
        }
        if (!ack){
            System.out.println("消息消费发生异常，error msg:{}" + exception.getStackTrace());
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        } else {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
