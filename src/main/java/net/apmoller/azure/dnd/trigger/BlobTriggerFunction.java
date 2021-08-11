package net.apmoller.azure.dnd.trigger;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.BlobTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.StorageAccount;
import emp.maersk.com.DnDOnlineCalculatorV1;
import net.apmoller.azure.dnd.service.KafkaMessageProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Azure Functions with Azure Blob trigger.
 */
public class BlobTriggerFunction {
    /**
     * This function will be invoked when a new or updated blob is detected at the specified path. The blob contents are provided as input to this function.
     */

    @Autowired
    KafkaMessageProducer kafkaMessageProducer;

   // @FunctionName("BlobTrigger-Java")
   // @StorageAccount("dndstorageaccount")
    public void run(
        @BlobTrigger(name = "content", path = "test/{name}", dataType = "binary") byte[] content,
        @BindingName("name") String name,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java Blob trigger function processed a blob. Name: " + name + "\n  Size: " + content.length + " Bytes");
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
            context.getLogger().info("going to push message to kafka topic with template "+kafkaMessageProducer);
            result.stream().forEachOrdered(a->new KafkaMessageProducer().sendMessageTest(a,context));
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
