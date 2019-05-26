package com.portalbot.main;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import java.util.*;

import java.util.TimerTask;

public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        new Bot().startTimer(300000);
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException tae) {
            tae.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Dialog dialog = new Dialog();
        if (message != null & message.hasText()) {
            sendMsg(message, dialog.ask(message));
        }

    }

    public void startTimer(int millis) {
            new java.util.Timer().schedule(
                    new TimerTask() {
                        public void run() {
                            try {
                                SessionsHolder holder = new SessionsHolder();
                                ResponseData response = holder.ask();
                                Serializer serializer = new Serializer();
                                Map<String, List<Task>> tasksMap = response.getTasks();
                                List<String> alarms = response.getAlarmsForClosingTasks();
                                for (String chatID : tasksMap.keySet()) {
                                    Message message = serializer.loadMessage(chatID);
                                    List<Task> tasksList = tasksMap.get(chatID);
                                    if (tasksList.size() != 0) {
                                        for (Task task : tasksList) {
                                            LogWriter.add(String.format("Sending message to %s", chatID));
                                            sendMsg(message, task.toMessage());
                                        }
                                    }
                                }
                                if (alarms.size() > 0) {
                                    for (String chatID : alarms) {
                                        LogWriter.add(String.format("Sending alarm to %s about non-closed tasks", chatID));
                                        Message message = serializer.loadMessage(chatID);
                                        sendMsg(message, "*Есть не закрытые заявки*");
                                    }
                                }
                            } catch (Exception e) {
                                LogWriter.add(e.toString());
                                e.printStackTrace();
                            }
                            LogWriter.save();
                        }
                    },
                    0, millis);
    }

    public String getBotUsername() {
        return "AlpMontazhPortalBot";
    }

    @Override
    public String getBotToken() {
        return "754079123:AAEEOe_pHNBSPUJkjUKP_9EcuHxIMKx_D68";
    }
}