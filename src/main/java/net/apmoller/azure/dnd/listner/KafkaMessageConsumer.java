package net.apmoller.azure.dnd.listner;


import emp.maersk.com.DnDOnlineCalculatorV1;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class KafkaMessageConsumer {



    @KafkaListener(topics = "dnd_topic", groupId = "group_id")
    public void consumeCustomer(ConsumerRecord<String, DnDOnlineCalculatorV1> record) {



        //log.info("Received Messasge in group consumer1 : {}");
        var data = record.value();
        //log.info("dnd Data -->{}",record.value());

    }

    @KafkaListener(topics = "dnd_topic", groupId = "group_id")
    public void consumeCustomer2(ConsumerRecord<String, DnDOnlineCalculatorV1> record) {



        //log.info("Received Messasge in group consumer2 : {}");
        var data = record.value();
        //log.info("dnd Data -->{}",record.value());

    }


}
