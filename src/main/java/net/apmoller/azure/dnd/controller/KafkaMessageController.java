package net.apmoller.azure.dnd.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import emp.maersk.com.DnDOnlineCalculatorV1;
import lombok.extern.slf4j.Slf4j;
import net.apmoller.azure.dnd.model.Response;
import net.apmoller.azure.dnd.service.KafkaMessageProducer;
import net.apmoller.azure.dnd.utility.JsonFormattor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class KafkaMessageController {


    @Autowired
    KafkaMessageProducer kafkaMessageProducer;
    //https://dndtrial.blob.core.windows.net/test/CXED_210219O0000F01.json
    @Value("azure-blob://test/TEST_30000")
    private Resource blobFile;

    @GetMapping("/readBlobFile")
    public String readBlobFile() throws IOException {
        Long startTime=System.currentTimeMillis();
        String s= StreamUtils.copyToString(
                this.blobFile.getInputStream(),
                Charset.defaultCharset());
        Long endTime1 =System.currentTimeMillis();
        System.out.println("Time taken to read from azure blob "+(endTime1-startTime));
        List<DnDOnlineCalculatorV1> result=new ArrayList<>();
        Long startTime1=System.currentTimeMillis();
        List<String> j= Arrays.asList(s.split("\\r?\\n"));
        Long endTime2 =System.currentTimeMillis();
        System.out.println("Time taken to split by new line "+(endTime2-startTime1));
        j.parallelStream().forEach(a->{
                   Gson g = new Gson();
                   result.add(((DnDOnlineCalculatorV1)g.fromJson(a, DnDOnlineCalculatorV1.class)));
                });
        Long endTime3=System.currentTimeMillis();
        System.out.println("Time taken to covert into  DnDOnlineCalculatorV1 "+(endTime3-endTime2));
        result.stream().forEachOrdered(a->kafkaMessageProducer.sendMessage(a));
        Long endTime4=System.currentTimeMillis();
        System.out.println("Time taken to push to  kafka "+(endTime4-endTime3));
        Long endTime=System.currentTimeMillis();
        return "success SENT "+ result.size() +" message to kafka in " + (endTime-startTime) + " millis";
    }


    @PostMapping(value = "/send/avro/dnd/info" )
    public String kafkaMessage(@RequestBody DnDOnlineCalculatorV1 message) {
        //log.info("message recieved from payload" +message);
        log.debug("Debug enabled");
        kafkaMessageProducer.sendMessage(message);
        return "Success";
    }

    @GetMapping(value = "/send/avro/file/topic" )
    public @ResponseBody
    Response pushFileDataKafkaMessage() {
        Response response=new Response();
        //log.info("message recieved from payload" +message);
        Long starttime=System.currentTimeMillis();
        log.debug("Debug enabled");
        long startTime1 = System.currentTimeMillis();
        List<DnDOnlineCalculatorV1> listOfMessage= JsonFormattor.readFromFileAndReturn();
        long endTime1 = System.currentTimeMillis();
        long startTime2 = System.currentTimeMillis();
        listOfMessage.sort(Comparator.comparing(DnDOnlineCalculatorV1::getBillOfLadingNumber));
        long endTime2 = System.currentTimeMillis();
        long startTime3 = System.currentTimeMillis();
        listOfMessage.stream().forEachOrdered(a->kafkaMessageProducer.sendMessage(a));
        long endTime3 = System.currentTimeMillis();
        //kafkaMessageProducer.sendMessage(message);
        Long endTime=System.currentTimeMillis();
        response.setTimeTakenToReadDataFromFiles(endTime1-startTime1);
        response.setTimeMessageCount(listOfMessage.size());
        response.setTimeTakenToSortObjectBasedOnBOL(endTime2-startTime2);
        response.setTimeTakenToPostMessageInKafka(endTime3-startTime3);
        response.setTotalTimeTaken(endTime-starttime);
        return response;
    }
}
