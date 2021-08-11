package net.apmoller.azure.dnd.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
@Data
@ToString
public class Response implements Serializable {


    private Long TimeTakenToReadDataFromFiles;
    private Integer TimeMessageCount;
    private Long TimeTakenToSortObjectBasedOnBOL;
    private Long TimeTakenToPostMessageInKafka;
    private Long TotalTimeTaken;

}
