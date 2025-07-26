import com.mysql.cj.MessageBuilder;
import com.richal.learnonline.MediaAPP;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = MediaAPP.class)
@RunWith(SpringRunner.class)
public class MQTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    //单向消息
    @Test
    public void test1(){
        rocketMQTemplate.sendOneWay("message:sms1", "我是短信消息");
    }

    //同步消息
    @Test
    public void test2(){
        Message<String> message = new GenericMessage<>("消息内容");
        SendResult result = rocketMQTemplate.syncSend("message:sms2", message);
    }

    //异步消息
    @Test
    public void test3() throws InterruptedException {
        rocketMQTemplate.asyncSend("message:sms3", "你好", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println(sendResult);
            }
            @Override
            public void onException(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        Thread.sleep(1000000000);
    }
}
