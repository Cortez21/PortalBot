package com.portalbot.main;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Serializable {
    private String taskNumber;
    private String requestNumber;
    private String account;
    private String date;
    private String address;
    private String time;
    private String name;
    private String phoneNumber;
    private String taskType;
    private String entryTime;
    private String installation;
    private String status;
    private String statusMessage;

    public Task(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public String getAccount() {
        return account;
    }

    public String getDate() {
        return date;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getInstallation() {
        return installation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public void setInstallation(String installation) {
        this.installation = installation;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toMessage() {
        String ls = System.lineSeparator();
        return new StringBuilder()
                .append(statusMessage).append(ls)
                .append("Дата: ").append(date).append(whenWillItBe(date)).append(" ").append(time).append(ls)
                .append(taskType).append(ls)
                .append(address).append(ls)
                .append("Абонент: ").append(name).append(ls)
                .append("тел.: ").append(phoneNumber).append(ls)
                .append("Установка: ").append(installation).append(ls)
                .toString();
    }

    public String whenWillItBe(String date) {
        String result = "";
        String year = new SimpleDateFormat("yyyy").format(new Date());
        String month = new SimpleDateFormat("MM").format(new Date());
        String day = new SimpleDateFormat("dd").format(new Date());
        DateIterator dateIterator = new DateIterator(year, month, day);

        String today = dateIterator.next();
        String yesterday = dateIterator.next();

        if (date.equals(today)) {
            result = " (На сегодня)";
        } else if (date.equals(yesterday)) {
            result = " (На завтра)";
        }
        return result;
    }
}
