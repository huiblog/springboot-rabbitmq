package com.xsg.Recever;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.xsg.config.RabbitMQExchangeConfig.QUEUE_A;

/***
 * @ClassName: MsgReceiverC_one
 * @Description: TODO
 * @Author: 渣渣辉
 * @Date: 2019/7/10  17:00
 * @version : V1.0
 */

@Component
@RabbitListener(queues = QUEUE_A)
public class MsgReceiverC_one {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitHandler
    public void process(String content) {
        logger.info("处理器one接收处理队列A当中的消息： " + content);
    }

}
