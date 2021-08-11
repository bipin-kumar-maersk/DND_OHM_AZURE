package net.apmoller.azure.dnd.utility;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import emp.maersk.com.DnDOnlineCalculatorV1;
import org.apache.tomcat.util.json.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public  class JsonFormattor {

    public static void readFromFile(String filename){

        try {
            int count=0;
            String text;
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while((text = reader.readLine())!=null){
             //  System.out.println(text);
                count++;
            }
            System.out.println("Total Count " +count);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<DnDOnlineCalculatorV1> readFromFileAndReturn(){
        long startTime = System.currentTimeMillis();
        List<DnDOnlineCalculatorV1> result=new ArrayList<>();
        File dir = new File("src/main/localsample/");
        File[] files = dir.listFiles();
        String content;
        for (File file : files) {
            readFromFileAndSetInResult(file.getAbsolutePath(),result);
        }
        long stopTime = System.currentTimeMillis();
        System.out.println("Total execution time is "+(stopTime - startTime) +"millis");
        return result;
    }
    public static List<DnDOnlineCalculatorV1> readFromFileAndReturnList(){
        long startTime = System.currentTimeMillis();
        List<DnDOnlineCalculatorV1> result=new ArrayList<>();
        File dir = new File("src/main/sample/");
        File[] files = dir.listFiles();
        String content;
        for (File file : files) {
            readFromFileAndSetInResultList(file.getAbsolutePath(),result);
        }
        long stopTime = System.currentTimeMillis();
        System.out.println("Total execution time is "+(stopTime - startTime) +"millis");
        return result;
    }
    public static void readFromFileAndSetInResult(String filename,List<DnDOnlineCalculatorV1> result){

        try {
            int count=0;
            String text;
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while((text = reader.readLine())!=null){
                //  System.out.println(text);
                Gson g = new Gson();
                DnDOnlineCalculatorV1 s = g.fromJson(text, DnDOnlineCalculatorV1.class);
                result.add(s);
                count++;
            }
            System.out.println("Total Count " +result.size());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void readFromFileAndSetInResultList(String filename,List<DnDOnlineCalculatorV1> result){

        try {
            int count=0;
            String text;
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            //JsonSerializer serializer = new JsonSerializer();
            System.out.println("Total Count " +result.size());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        File dir = new File("src/main/sample/");
        File[] files = dir.listFiles();
        String content;
        for (File file : files) {
            readFromFile(file.getAbsolutePath());
            long endTime1 = System.currentTimeMillis();
        }
        long stopTime = System.currentTimeMillis();

        System.out.println("Total execution time is "+(stopTime - startTime) +"millis");
    }
}
