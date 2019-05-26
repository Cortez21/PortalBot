package com.portalbot.main;

import org.telegram.telegrambots.api.objects.Message;

public class Dialog {
    public String ask(Message message) {
        SessionsHolder holder = new SessionsHolder();
        Authorizer authorizer = new Authorizer();
        String text = message.getText();
        String result;
        if (text.contains("/registration")) {
            String[] splitted = text.split(" ");
            if (splitted.length < 3) {
                result = "Ошибка при вводе даннных. Используйте следующий формат: /registration(пробел)ЛОГИН(пробел)ПАРОЛЬ";
            } else if (authorizer.registration(message, splitted[1], splitted[2])) {
                result = "Регистрация прошла успешно!";
            } else {
                result = "Неверные данные. Проверьте пожалуйста логин/пароль и повторите запрос. ";
            }
        } else if (text.contains("/listen")) {
            if (!authorizer.checkRegistration(message)) {
                result = "Сперва нужно зарегистрироваться!";
            } else if (holder.checkSession(message.getChatId().toString())) {
                result = "Прослушивание заявок уже было активировано";
            } else if (holder.openSession(message.getChatId().toString())) {
                result = "Прослушивание заявок активировано";
            } else {
                result = "Ошибка! Обратитесь к администратору https://t.me/CortezZz13th";
            }

        } else if (text.contains("/notlisten")) {
            if (!authorizer.checkRegistration(message)) {
                result = "Сперва нужно зарегистрироваться!";
            } else if (!holder.checkSession(message.getChatId().toString())) {
                result = "Прослушивание заявок уже было отключено";
            } else if (holder.closeSession(message.getChatId().toString())) {
                result = "Прослушивание заявок отключено";
            } else {
                result = "Ошибка! Обратитесь к администратору https://t.me/CortezZz13th";
            }
        } else {
            result = "Привет монтажник! \n Список доступных команд: \n /registration ЛОГИН ПАРОЛЬ - Регистрация с использованием логина и пароля для входа в чистему https://portal.alpm.com.ua \n /listen - Прослушивание входящих заявок. Доступно после регистрации \n /notlisten - Отключение прослушивания входящих заявок";
        }
        LogWriter.addDialog(message);
        LogWriter.saveDialog();
        return result;
    }
}
