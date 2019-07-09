package com.portalbot.main.queue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueueParser {
    public List<String> parseQueue(String source) {
        List<String> result = new ArrayList<>();
        String[] splitted = source.split("\"id\":\"");
        for (int i = 1; i < splitted.length; i++) {
            result.add(splitted[i].substring(0, 6));
        }
        return result;
    }

    public QueueTask parseTask(String source) {
        QueueTask resultTask = new QueueTask();
        resultTask.setPortalNumber(source.split("Заявка №")[1].substring(0, 6));
        resultTask.setTaskNumber(source.split("Наряд:</th><td>")[1].substring(0, 7));
        resultTask.setType(source.split("Тип:</th><td>")[1].split("</td></tr>")[0].replace(" ", "").replace("\n", ""));
        resultTask.setSubType(source.split("Подтип:</th><td classContext=\"subtype\" contextItem=")[1].split(">")[1].split("</td")[0]);
        if (source.contains("Лицевой счет:</th><td classContext=\"abon\" contextItem=")) {
            resultTask.setAccount(source.split("Лицевой счет:</th><td classContext=\"abon\" contextItem=")[1].split(">")[1].substring(0, 7));
        }
        resultTask.setAddress(source.split("classContext=\"address\"")[1].split(">")[1].split("</td", 2)[0].replace(",", " "));
        resultTask.setFlat(source.split("Квартира:</th><td classContext=\"conFlat&id=")[1].split("\">")[0]);
        resultTask.setName(source.split("Клиент:</th><td classContext=\"conClient\">")[1].split("</td></tr>", 2)[0].replace("*", "").replace("_", ""));
        if (source.contains("Пакет:</th><td classContext=\"package\" contextItem=\"")) {
            resultTask.setTariff(source.split("Пакет:</th><td classContext=\"package\" contextItem=\"")[1].split("\">")[1].split("</td></tr>")[0]);
        }
        resultTask.setPhoneNumber(source.split("Телефон:</th><td>")[1].split("</td></tr>")[0]);
        if (source.contains("Телефон 2:</th><td>")) {
            resultTask.setPhoneNumber2(source.split("Телефон 2:</th><td>")[1].split("</td></tr>")[1]);
        }
        if (source.contains("Плановая дата включения:</th><td>")) {
            resultTask.setPlanedDate(source.split("Плановая дата включения:</th><td>")[1].split(" 00:00:00", 2)[0]);
        }
        resultTask.setUrgent(source.split("Срочная:</th><td>")[1].split("</td></tr>", 2)[0].equals("Нет") ? false : true);
        StringBuilder sb = new StringBuilder();
        String routers = getInstallationValue("Роутер</th><td>", source);
        String routersPro = getInstallationValue("Роутер ПРО</th><td>", source);
        String tuners =  getInstallationValue("ТВ-тюнер</th><td>", source);
        String withoutTuners =  getInstallationValue("ТВ без тюнера</th><td>", source);
        String sim =  getInstallationValue("SIM карта</th><td>", source);

        if (routers != null && !routers.equals("0")) {
            sb.append("Роутер-").append(routers).append(" ");
        }
        if (routersPro != null && !routersPro.equals("0")) {
            sb.append("Роутер ПРО-").append(routersPro).append(" ");
        }
        if (tuners != null && !tuners.equals("0")) {
            sb.append("Тюнер-").append(tuners).append(" ");
        }
        if (withoutTuners != null && !withoutTuners.equals("0")) {
            sb.append("Без тюнера-").append(withoutTuners).append(" ");
        }
        if (sim != null && !sim.equals("0")) {
            sb.append("SIM карта-").append(sim).append(" ");
        }
        resultTask.setInstallation(sb.toString().equals("") ? null : sb.toString());
        resultTask.setStatusMessage(null);
        resultTask.setAddressId(source.split("classContext=\"address\" contextItem=\"")[1].split("\" onmouseover")[0]);
        resultTask.setOwnersChatId(null);
        resultTask.setQueueMessage(false);
        resultTask.setInWorkMessage(false);
        resultTask.setEntryDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        return resultTask;
    }

    public String getInstallationValue(String installationSource, String source) {
        String result = null;
        if (source.contains(installationSource)) {
            result = source.split(installationSource)[1].split("</td><td>", 2)[0];
        }
        return result;
    }


}
