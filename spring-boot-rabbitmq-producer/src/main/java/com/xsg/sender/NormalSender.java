package com.xsg.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.xsg.config.RabbitMQExchangeConfig.*;

@Component
public class NormalSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void sendMsg(String msg){
        logger.info("发送消息{}" ,msg);
        Message message = MessageBuilder.withBody(msg.getBytes()).build();
        rabbitTemplate.convertAndSend(NORMAL_EXCHANGE, QUEUE_KEY, message);

    }
}
