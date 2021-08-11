package net.apmoller.azure.dnd.service;


import com.microsoft.azure.functions.ExecutionContext;
import emp.maersk.com.DnDOnlineCalculatorV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
public class KafkaMessageProducer {


    @Value("${topic.name.customers}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, DnDOnlineCalculatorV1> customerkafkaTemplate;


    public void sendMessage(DnDOnlineCalculatorV1 custmessage) {

        ListenableFuture<SendResult<String, DnDOnlineCalculatorV1>> future = customerkafkaTemplate.send(topicName, custmessage.getBillOfLadingNumber(),custmessage);
        future.addCallback(
                new ListenableFutureCallback<SendResult<String, DnDOnlineCalculatorV1>>() {
                    @Override
                    public void onSuccess(SendResult<String, DnDOnlineCalculatorV1> result) {
//                        log.info(
//                                "Sent message=[{}] with offset=[{}]", custmessage, result.getRecordMetadata().offset());
                    }

                    @Override
                    public void onFailure(Throwable ex) {
                        log.info("Unable to send message=[{}] due to : {}", custmessage, ex.getMessage());
                    }
                });
    }

    public void sendMessageTest(DnDOnlineCalculatorV1 custmessage, ExecutionContext context) {

        ListenableFuture<SendResult<String, DnDOnlineCalculatorV1>> future = customerkafkaTemplate.send(topicName, custmessage.getBillOfLadingNumber(),custmessage);
        future.addCallback(
                new ListenableFutureCallback<SendResult<String, DnDOnlineCalculatorV1>>() {
                    @Override
                    public void onSuccess(SendResult<String, DnDOnlineCalculatorV1> result) {
                       context.getLogger().info(
                               "Sent message"+ custmessage);
                    }

                    @Override
                    public void onFailure(Throwable ex) {
                        context.getLogger().info("Unable to send message=[{}] due to : {}"+  ex.getMessage());
                    }
                });
    }

}
