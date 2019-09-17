package com.portalbot.main.queue;

public class QueueTask {
    private String portalNumber;
    private String taskNumber;
    private String type;
    private String subType;
    private String account;
    private String address;
    private String flat;
    private String name;
    private String tariff;
    private String phoneNumber;
    private String phoneNumber2;
    private String planedDate;
    private boolean urgent;
    private String installation;
    private String statusMessage;
    private String addressId;
    private String ownersChatId;
    private boolean queueMessage;
    private boolean inWorkMessage;
    private String entryDate;
    private String ls = System.lineSeparator();
    private String port;
    private String pairA;
    private String pairB;

    public String getPairB() {
        return pairB;
    }

    public void setPairB(String pairB) {
        this.pairB = pairB;
    }

    public String getPairA() {
        return pairA;
    }

    public void setPairA(String pairA) {
        this.pairA = pairA;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }


    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        phoneNumber2 = phoneNumber2;
    }

    public String getPlanedDate() {
        return planedDate;
    }

    public void setPlanedDate(String planedDate) {
        this.planedDate = planedDate;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public String getInstallation() {
        return installation;
    }

    public void setInstallation(String installation) {
        this.installation = installation;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getOwnersChatId() {
        return ownersChatId;
    }

    public void setOwnersChatId(String ownersChatId) {
        this.ownersChatId = ownersChatId;
    }

    public boolean isQueueMessage() {
        return queueMessage;
    }

    public void setQueueMessage(boolean queueMessage) {
        this.queueMessage = queueMessage;
    }

    public boolean isInWorkMessage() {
        return inWorkMessage;
    }

    public void setInWorkMessage(boolean inWorkMessage) {
        this.inWorkMessage = inWorkMessage;
    }

    public String getPortalNumber() {
        return portalNumber;
    }

    public void setPortalNumber(String portalNumber) {
        this.portalNumber = portalNumber;
    }

    public String toQueueMessage() {

        return new StringBuilder()
                .append("НОВАЯ ЗАЯВКА В ОЧЕРЕДИ").append(ls)
                .append(type).append(" ").append(subType).append(ls)
                .append(tariff != null ? String.format("Пакет: %s \n", tariff) : "")
                .append(String.format("[%s, кв.%s](https://portal.alpm.com.ua/headless.php?action=addrBrief&id=%s)", address, flat, addressId)).append(ls)
                .append(planedDate != null ? String.format("Желаемая дата подключения: %s \n", planedDate) : "")
                .append(name).append(ls)
                .append("Телефон: ").append(phoneNumber).append(" ").append(phoneNumber2 != null ? phoneNumber : "").append(ls)
                .append("Срочная: ").append(urgent ? "Да" : "Нет").append(ls)
                .append(account != null ? String.format("Лицевой счет: %s \n", account) : "")
                .append(taskNumber != null ? String.format("Номер наряда: %s \n", taskNumber) : "")
                .append(port != null ? String.format("Порт абонента: %s \n", port) : "")
                .append(installation != null ? String.format("Установка: %s \n", installation) : "")
                .append(pairA != null && pairB != null ? String.format("Тест кабеля: PairA: %sm, PairB: %sm \n", pairA, pairB) : "")
                .toString();
    }
}
