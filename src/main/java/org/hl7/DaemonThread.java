package org.hl7;

import com.fasterxml.jackson.databind.JsonNode;

import javax.swing.*;


/**
 * @author - Shirshak Upadhayay
 * @Date - 22/09/2024
 */
public class DaemonThread implements Runnable{

    private HL7MessageProvider hl7MessageProvider;
    private JTextArea textArea;

    public DaemonThread(HL7MessageProvider hl7MessageProvider, JTextArea jTextArea){
        this.textArea = jTextArea;
        this.hl7MessageProvider = hl7MessageProvider;
    }

    @Override
    public void run() {
        try {
            JsonNode rootNode = hl7MessageProvider.getHL7MessageData();
            JsonNode entriesNode = rootNode.get("entries");
            int count = 1;
            for (JsonNode entryNode : entriesNode) {
                char[] data = entryNode.get("data").asText().toCharArray();
                hl7MessageProvider.getBufferedWriter().write(data);
                hl7MessageProvider.getBufferedWriter().flush();
                textArea.append("Sent " + count + " EL7 Message Packet \n");
                textArea.append("Sleeping For "+ hl7MessageProvider.getInterval()+" seconds \n\n");
                count++;
                Thread.sleep(hl7MessageProvider.getInterval()*1000);
            }
        }catch (Exception e){
            textArea.append(e.getMessage()+"\n");
        }

    }
}
