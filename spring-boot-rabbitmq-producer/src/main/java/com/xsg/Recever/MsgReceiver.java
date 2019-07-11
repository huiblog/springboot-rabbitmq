package com.xsg.Recever;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

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

    @RabbitHandler
    public void process(String content) {
        logger.info("接收处理队列C当中的消息： " + content);
    }

}
