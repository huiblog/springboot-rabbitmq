package com.xsg.Recever;


import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.xsg.config.RabbitMQExchangeConfig.DL_QUEUE_NAME;
import static com.xsg.config.RabbitMQExchangeConfig.QUEUE_C;

@Component
public class MsgDLReceiver {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitListener(queues = DL_QUEUE_NAME)
    public void receiveA(Message message, Channel channel) throws IOException {
        logger.info("收到死信消息{}" , new String(message.getBody()));
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
    }
}
