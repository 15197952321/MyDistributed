package com.search.test;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * Created by Administrator on 2017/11/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-activemq.xml"})
public class Test {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource(name = "queueDestination")
    private Destination destination;

    @org.junit.Test
    public void testP2Pfs() throws Exception {
        // 第一步：创建ConnectionFactory对象，需要指定服务端ip及端口号。
        //brokerURL服务器的ip及端口号
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.131:61616");
        // 第二步：使用ConnectionFactory对象创建一个Connection对象。
        Connection connection = connectionFactory.createConnection();
        // 第三步：开启连接，调用Connection对象的start方法。
        connection.start();
        // 第四步：使用Connection对象创建一个Session对象。
        //第一个参数：是否开启事务。 true：开启事务，第二个参数忽略。
        //第二个参数：当第一个参数为false时，才有意义。消息的应答模式。1、自动应答2、手动应答。一般是自动应答。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 第五步：使用Session对象创建一个Destination对象（topic、queue），此处创建一个Queue对象。
        //参数：队列的名称。
        Queue queue = session.createQueue("test1-queue");
        // 第六步：使用Session对象创建一个Producer对象。
        MessageProducer producer = session.createProducer(queue);
        // 第七步：创建一个Message对象，创建一个TextMessage对象。
        /*TextMessage message = new ActiveMQTextMessage();
        message.setText("hello activeMq,this is my first test.");*/
        TextMessage textMessage = session.createTextMessage("hello activeMq,this is my second test.");
        // 第八步：使用Producer对象发送消息。
        producer.send(textMessage);
        // 第九步：关闭资源。
        producer.close();
        session.close();
        connection.close();

    }

    @org.junit.Test
    public void testTopic() throws Exception{
        // 第一步：创建ConnectionFactory对象，需要指定服务端ip及端口号。
        // brokerURL服务器的ip及端口号
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.131:61616");
        // 第二步：使用ConnectionFactory对象创建一个Connection对象。
        Connection connection = connectionFactory.createConnection();
        // 第三步：开启连接，调用Connection对象的start方法。
        connection.start();
        // 第四步：使用Connection对象创建一个Session对象。
        // 第一个参数：是否开启事务。true：开启事务，第二个参数忽略。
        // 第二个参数：当第一个参数为false时，才有意义。消息的应答模式。1、自动应答2、手动应答。一般是自动应答。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 第五步：使用Session对象创建一个Destination对象（topic、queue），此处创建一个topic对象。
        // 参数：话题的名称。
        Topic topic = session.createTopic("test1-topic");
        // 第六步：使用Session对象创建一个Producer对象。
        MessageProducer producer = session.createProducer(topic);
        // 第七步：创建一个Message对象，创建一个TextMessage对象。
        TextMessage textMessage = session.createTextMessage("hello activeMq,this is my topic test");
        // 第八步：使用Producer对象发送消息。
        producer.send(textMessage);
        // 第九步：关闭资源。
        producer.close();
        session.close();
        connection.close();

    }

    @org.junit.Test
    public void testSpringZh() throws Exception {
        jmsTemplate.send(destination, new MessageCreator(){
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage("spring activemq test");
                return textMessage;
            }
        });
    }


}
