package com.portalbot.main;

import org.apache.commons.codec.digest.DigestUtils;
import org.telegram.telegrambots.api.objects.Message;

public class Authorizer {
    private Connection connection = new Connection();
    private Serializer serializer = new Serializer();
    private DataStream data = new DataStream();

    public boolean registration(Message message, String login, String password) {
        boolean result = false;
        password =  DigestUtils.md5Hex(password);
        logging(login, password);
        if (data.getBody().contains("Сменить пароль")) {
            serializer.userRegistration(new User(message.getChatId().toString(), login, password));
            serializer.addToUserList(message.getChatId().toString());
            serializer.saveMessage(message);
            result = true;
        }
        return result;
    }


    public void logging(String login, String password) {
        data.setQuery("https://portal.alpm.com.ua/index.php?action=login");
        data.setParams(String.format("login=%s&password=%s", login, password));
        data = connection.start(data);
    }
    public boolean checkRegistration(Message message) {
        boolean result = false;
        if (serializer.loadUser(message.getChatId().toString()).getChatID() != null) {
            result = true;
        }
        return result;
    }


}
