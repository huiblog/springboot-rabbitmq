package com.xsg.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ的Exchange交换机配置
 */
@Configuration
public class RabbitMQExchangeConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;


    public static final String EXCHANGE_A = "my-mq-exchange_A";
    public static final String EXCHANGE_B = "my-mq-exchange_B";
    public static final String EXCHANGE_C = "my-mq-exchange_C";


    public static final String QUEUE_A = "QUEUE_A";
    public static final String QUEUE_B = "QUEUE_B";
    public static final String QUEUE_C = "QUEUE_C";

    public static final String ROUTINGKEY_A = "spring-boot-routingKey_A";
    public static final String ROUTINGKEY_B = "spring-boot-routingKey_B";
    public static final String ROUTINGKEY_C = "spring-boot-routingKey_C";

    public static final String TOPIS_A = "*.apple.big";
    public static final String TOPIC_B = "#.little";
    public static final String TOPIC_C = "red.*.*";

    /**
     * 死信交换机名称
     */
    public static final String DL_EXCHANGE = "DL_EXCHANGE";
    /**
     * 普通交换机名称
     */
    public static final String NORMAL_EXCHANGE = "NORMAL_EXCHANGE";
    /**
     * 死信队列名称
     */
    public static final String DL_QUEUE_NAME = "DL_QUEUE";

    /**
     * 普通队列名称
     */
    public static final String NORAL_QUEUE_NAME = "NORMAL_QUEUE";

    /**
     * 绑定死信队列的路由键
     */
    public static final String DL_QUEUE_KEY = "KEY_DXL";


    /**
     * 绑定正常队列的路由键
     */
    public static final String QUEUE_KEY = "KEY_QUEUE";


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        //开启强制委托模式
        template.setMandatory(true);
        return template;
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     */
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(EXCHANGE_A);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_B);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_C);
    }


    /**
     * 创建死信交换机.
     */
    @Bean
    public DirectExchange  dlxExchange() {
        return new DirectExchange("DL_EXCHANGE");
    }

    /**
     * 创建普通交换机.
     */
    @Bean
    public DirectExchange  normalExchange() {
        return new DirectExchange("NORMAL_EXCHANGE");
    }

    /**
     * 普通队列.设置死信交换机
     * x-dead-letter-exchange   对应  死信交换机
     * x-dead-letter-routing-key  对应 死信队列
     *
     * @return the queue
     */
    @Bean
    public Queue normalQueue() {
        Map<String, Object> args = new HashMap<>(3);
        //x-dead-letter-exchange    声明  死信交换机，就是上面定义的交换机
        args.put("x-dead-letter-exchange", DL_EXCHANGE);
        //x-dead-letter-routing-key    声明 死信路由
        args.put("x-dead-letter-routing-key", DL_QUEUE_KEY);
        args.put("x-message-ttl",2000);
        Queue queue = new Queue(NORAL_QUEUE_NAME, true, false, false, args);
        return QueueBuilder.durable(NORAL_QUEUE_NAME).withArguments(args).build();
    }

    /**
     * 死信队列
     *
     * @return
     */
    @Bean
    public Queue dxlQueue() {
        Queue queue = new Queue(DL_QUEUE_NAME, true);
        return queue;
    }

    /**
     * 绑定死信队列
     *
     * @return
     */
    @Bean
    public Binding dxlBing() {
       return BindingBuilder.bind(dxlQueue()).to(dlxExchange()).with(DL_QUEUE_KEY);

    }

    /**
     * 绑定普通队列
     */
    @Bean
    public Binding bindDeadBuilders() {
        return BindingBuilder.bind(normalQueue()).to(normalExchange()).with(QUEUE_KEY);
    }


    /**
     * 获取队列A
     *
     * @return
     */
    @Bean
    public Queue queueA() {
        return new Queue(QUEUE_A, true); //队列持久
    }


    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queueA()).to(defaultExchange()).with(ROUTINGKEY_A);
    }

    @Bean
    public Queue queueB() {
        return new Queue(QUEUE_B, true); //队列持久
    }


    @Bean
    public Binding bindingB() {
        return BindingBuilder.bind(queueB()).to(fanoutExchange());
    }

    @Bean
    public Queue queueC() {
        return new Queue(QUEUE_C, false); //队列持久
    }


    @Bean
    public Binding bindingC() {
        return BindingBuilder.bind(queueC()).to(topicExchange()).with(TOPIS_A);
    }


}
