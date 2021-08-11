package net.apmoller.azure.dnd.service;

import emp.maersk.com.DnDOnlineCalculatorV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.function.Function;

@Component
@Configuration
public class KafkaPublisher  {

    @Value("${topic.name.customers}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, DnDOnlineCalculatorV1> customerkafkaTemplate;

    public String apply(DnDOnlineCalculatorV1 custmessage) {
        ListenableFuture<SendResult<String, DnDOnlineCalculatorV1>> future = customerkafkaTemplate.send(topicName, custmessage.getBillOfLadingNumber(),custmessage);
        future.addCallback(
                new ListenableFutureCallback<SendResult<String, DnDOnlineCalculatorV1>>() {
                    @Override
                    public void onSuccess(SendResult<String, DnDOnlineCalculatorV1> result) {
                        System.out.print(
                                "Sent message"+ custmessage);
                    }

                    @Override
                    public void onFailure(Throwable ex) {
                        System.out.print("Unable to send message=[{}] due to : {}"+  ex.getMessage());
                    }
                });
        return "success";
    }
}