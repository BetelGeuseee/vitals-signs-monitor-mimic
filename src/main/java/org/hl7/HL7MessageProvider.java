package org.hl7;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.net.URL;

/**
 * @author - Shirshak Upadhayay 
 * @Date - 21/09/2024
 */
public class HL7MessageProvider {

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private OutputStreamWriter outputStreamWriter;
    private String serverIP;
    private int serverPort;
    private boolean isConnected=false;
    private int interval;

    public HL7MessageProvider(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public void init() {
        try {
            socket = new Socket(serverIP, serverPort);
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            isConnected=true;
        } catch (IOException exception) {
            System.out.println("Exception Occurred: " + exception.getMessage());
        }
    }

    public JsonNode getHL7MessageData() {
        ClassLoader classLoader = HL7MessageProvider.class.getClassLoader();
        URL hl7MessageUrl = classLoader.getResource("jsonData/hl7Message.json");
        if (hl7MessageUrl == null) {
            System.out.println("Resource not found");
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = hl7MessageUrl.openStream()) {
            return objectMapper.readTree(inputStream);
        } catch (IOException ioException) {
            System.out.println("Exception occurred while serializing HL7Message: " + ioException.getMessage());
        }
        return null;
    }

    public void closeConnection() throws Exception{
            bufferedWriter.close();
            outputStreamWriter.close();
            socket.close();
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }
    public boolean isConnected() {
        return isConnected;
    }
    public int getInterval() {
        return interval;
    }
    public void setInterval(int interval) {
        this.interval = interval;
    }

}



