package net.apmoller.azure.dnd.service;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.BlobTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.StorageAccount;
import emp.maersk.com.DnDOnlineCalculatorV1;
import org.springframework.cloud.function.adapter.azure.AzureSpringBootRequestHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KafkaPublisherHandler extends
        AzureSpringBootRequestHandler<DnDOnlineCalculatorV1, String> {

    @FunctionName("blobFunctionTest")
    @StorageAccount("dndstorageaccount")
    public void run(
            @BlobTrigger(name = "content", path = "test/{name}", dataType = "binary") byte[] content,
            @BindingName("name") String name,
            final ExecutionContext context
    ) {
        context.getLogger().info("Java Springboot Blob trigger function processed a blob. Name: " + name + "\n  Size: " + content.length + " Bytes");
        Long startTime=System.currentTimeMillis();
        String s = new String(content);
        Long endTime1 =System.currentTimeMillis();
        context.getLogger().info("Time taken to read from azure blob "+(endTime1-startTime));
        List<DnDOnlineCalculatorV1> result=new ArrayList<>();
        Long startTime1=System.currentTimeMillis();
        List<String> j= Arrays.asList(s.split("\\r?\\n"));
        Long endTime2 =System.currentTimeMillis();
        context.getLogger().info("Time taken to split by new line "+(endTime2-startTime1));
        context.getLogger().info(String.valueOf(j.size()));
        context.getLogger().info("Size " + String.valueOf(j.size()));


        try
        {
            context.getLogger().info("going to push message to kafka topic ");
            result.stream().forEachOrdered(a->  handleRequest(a, context));
        }
        catch (Exception e)
        {
            context.getLogger().info(e.getMessage());
        }
        // Close Kafka producer
        //producer.flush();
        // producer.close();
        //System.getenv("myAppSetting"))
    }
}
