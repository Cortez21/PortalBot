package com.portalbot.main;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Serializable {
    private PortalRequester requester;
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
    private String portalTaskNumber;
    private String ownersChatID;
    private String addressId;

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
        address = address.replace("Хмельницький, ", "").replace("вулиця ", "");
        this.address = address;
    }

    public void setPortalTaskNumber(String portalTaskNumber) {
        this.portalTaskNumber = portalTaskNumber;
    }

    public String getPortalTaskNumber() {
        return portalTaskNumber;
    }

    public String getOwnersChatID() {
        return ownersChatID;
    }

    public void setOwnersChatID(String ownersChatID) {
        this.ownersChatID = ownersChatID;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String toMessage() {
        String ls = System.lineSeparator();
        String tariff = getTariff();
        return new StringBuilder()
                .append(statusMessage).append(ls)
                .append(date).append(whenWillItBe(date)).append(ls)
                .append(time).append(ls)
                .append(String.format("_%s_", taskType)).append(ls)
                .append(tariff != null ? String.format("Пакет: %s \n", tariff) : "")
                .append(String.format("[%s](https://portal.alpm.com.ua/headless.php?action=addrBrief&id=%s)", address, addressId)).append(ls)
                .append("Абонент: ").append(name).append(ls)
                .append("тел.: ").append(phoneNumber).append(ls)
                .append(getInstallationString())
                .append(taskType.contains("Сервис-Жалобы на обслуживание") ? checkCableBreaking() : "")
                .toString();
    }

    public String whenWillItBe(String date) {
        String result = String.format(" (*Всего: %s*)", checkHowMuchTasks());
        String year = new SimpleDateFormat("yyyy").format(new Date());
        String month = new SimpleDateFormat("MM").format(new Date());
        String day = new SimpleDateFormat("dd").format(new Date());
        DateIterator dateIterator = new DateIterator(year, month, day);

        String today = dateIterator.next();
        String yesterday = dateIterator.next();

        if (date.equals(today)) {
            result = String.format(" (*На сегодня: %s*)", checkHowMuchTasks());
        } else if (date.equals(yesterday)) {
            result = String.format(" (*На завтра: %s*)", checkHowMuchTasks());
        }
        return result;
    }

    public String getInstallationString() {
        String result = "";
        if (installation != null) {
            result = new StringBuilder().append("Установка: ").append(installation).append(System.lineSeparator()).toString();
        }
        return result;
    }

    public long checkHowMuchTasks() {
        return new Serializer().loadTasks(ownersChatID).getTasks().values().stream().filter(task -> task.getDate().equals(date)).count();
    }

    public String checkCableBreaking() {
        String result = "";
        requester  = getRequester();
         if (requester == null) {
             return result;
         }
        String pairA = "";
        String pairB = "";
        LogWriter.add("Looking for cable breaking...");
        String[] splitted  = requester.switchingToTask(portalTaskNumber).split("Указать метр повреждения");
        if (splitted.length > 1) {
            if (splitted[1].substring(3, 4).equals("1")) {
                String[] cells = splitted[1].split("м");
                pairA = cells[0].substring(5);
                pairB = cells[1].substring(4);
            } else if (splitted[1].substring(3, 7).equals("Pair")) {
                String[] cells = splitted[1].split("meter");
                pairA = cells[0].substring(18);
                pairB = cells[1].substring(20);
            }
        }
        if (!pairA.equals("") && !pairB.equals("")) {
            LogWriter.add("Finded breaking of cable!");
            result = String.format("Тест кабеля: А - %sм, Б - %sм", pairA, pairB);
        }
        return result;
    }

    public String getTariff() {
        String result = null;
        String pageBody = getRequester().switchingToTask(portalTaskNumber);
        LogWriter.add("Trying to get tariff...");
        if (pageBody.contains("selected >")) {
            result = pageBody.split("selected >")[1].split("</option>")[0];
            result = result.replace("_", " ")
                    .replace("Киевстар ", "")
                    .replace("Київстар ", "");
        }
        return result;
    }

    public PortalRequester getRequester() {
        PortalRequester requester = null;
        User user = new Serializer().loadUser(ownersChatID);
        try {
            requester  = new PortalRequester(user.getPortalLogin(), user.getPortalPassword());
        } catch (BadLoggingException ble) {
            LogWriter.add(ble.getMessage());
        }
        return requester;
    }
}
