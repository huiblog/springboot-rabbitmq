package com.xsg.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

import static com.xsg.config.RabbitMQExchangeConfig.EXCHANGE_C;

/**
 * 用于发送A消息的sender
 */
@Component
public class ASender implements RabbitTemplate.ReturnCallback,RabbitTemplate.ConfirmCallback {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 回调
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info(" 回调id:" + correlationData);
        if (ack) {
            logger.info("消息成功消费");
        } else {
            logger.info("消息消费失败:" + cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode,
                                String replyText, String exchange, String routingKey) {
        logger.info("消息内容:{}", new String(message.getBody()));
        logger.info("回复文本:{},回复代码：{}", replyText, replyCode);
        logger.info("交换器名称:{},路由键：{}", exchange, routingKey);
    }

    @PostConstruct
    public void init(){
        rabbitTemplate.setReturnCallback(this);
        rabbitTemplate.setConfirmCallback(this);
    }

    public void sendMsg(String content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(EXCHANGE_C, "aa.apple.big", content, correlationId);

    }
}