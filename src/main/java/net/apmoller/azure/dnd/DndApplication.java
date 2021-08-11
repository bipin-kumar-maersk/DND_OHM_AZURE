package net.apmoller.azure.dnd;

import emp.maersk.com.DnDOnlineCalculatorV1;
import net.apmoller.azure.dnd.service.KafkaPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class DndApplication {

	public static void main(String[] args) {
		SpringApplication.run(DndApplication.class, args);
	}

	@Autowired
	private KafkaPublisher kafkaPublisher;

	@Bean
	public Function<DnDOnlineCalculatorV1, String> blobFunctionTest() {
		return custmessage -> kafkaPublisher.apply(custmessage);
	}

}
