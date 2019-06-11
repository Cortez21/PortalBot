package com.portalbot.main;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import java.util.TimerTask;

public class Bot extends TelegramLongPollingBot {

    private static String token;
    private static String name;

    public static void main(String[] args) {
        getRequisites();
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
            setButtons(sendMessage);
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
                                            sendMsg(message, task.toMessage());
                                            LogWriter.add(String.format("Sending message to %s", chatID));
                                        }
                                    }
                                }
                                if (alarms.size() > 0) {
                                    for (String chatID : alarms) {
                                        Message message = serializer.loadMessage(chatID);
                                        sendMsg(message, "*Есть не закрытые заявки*");
                                        LogWriter.add(String.format("Sending alarm to %s about non-closed tasks", chatID));
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

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/listen"));
        keyboardFirstRow.add(new KeyboardButton("/notlisten"));

        keyboardRows.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }

    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public static void getRequisites() {
        try {
            FileReader fr = new FileReader("files/botRequisites.txt");
            Scanner scanner = new Scanner(fr);
            name = scanner.nextLine().substring(5);
            token = scanner.nextLine().substring(6);
        } catch (IOException e) {
            LogWriter.add("botRequisites.txt not found!");
            e.printStackTrace();
        }
    }
}