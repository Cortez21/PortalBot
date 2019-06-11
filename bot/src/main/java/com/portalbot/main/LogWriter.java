package com.portalbot.main;

import org.telegram.telegrambots.api.objects.Message;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogWriter {
    private static String tempLogData = "";
    private static String tempDialogData = "";
    private static String lastDate;


    public static void add(String value) {
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        tempLogData = new StringBuilder()
                .append(tempLogData)
                .append(currentTime)
                .append(" - ")
                .append(value)
                .append(System.lineSeparator())
                .toString();
    }
    public static void addDialog(Message message) {
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        tempDialogData = new StringBuilder()
                .append(tempDialogData)
                .append(currentTime)
                .append(" - ")
                .append("[").append(message.getChatId()).append("]")
                .append(message.getText())
                .append(System.lineSeparator())
                .toString();
    }

    public static void save() {
        try {
            FileWriter writer = new FileWriter(String.format("files/logs/%s.log", new SimpleDateFormat("yyyy-MM-dd").format(new Date())), true);
            FileWriter lastWriter = new FileWriter("files/logs/last.log", checkOldDate());
            writer.write(tempLogData);
            lastWriter.write(tempLogData);
            lastWriter.close();
            writer.close();
            tempLogData = "";
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void saveDialog() {
        try {
            FileWriter writer = new FileWriter(String.format("files/logs/%sd.log", new SimpleDateFormat("yyyy-MM-dd").format(new Date())), true);
            FileWriter lastWriter = new FileWriter("files/logs/lastDialog.log", checkOldDate());
            writer.write(tempDialogData);
            lastWriter.write(tempDialogData);
            writer.close();
            lastWriter.close();
            tempDialogData = "";
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static boolean checkOldDate() {
        boolean result = true;
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (!currentDate.equals(lastDate)) {
            result = false;
            lastDate = currentDate;
        }
        return result;
    }
}
