package com.portalbot.main;

import java.util.*;

public class TaskParser {
    public List<Task> execute(String body) {
        LogWriter.add("Parsing data...");
        List<String> sourceTasks = Arrays.asList(body.split("tr rowid="));
        List<String> newSourceTasks = new ArrayList<>();
        newSourceTasks.addAll(sourceTasks);
        List<Task> result = new ArrayList<>();
        newSourceTasks.remove(0);
        for (String value : newSourceTasks) {
            result.add(taskParser(value));
        }
        LogWriter.add("Parsing successful");
        return result;
    }

    public Task taskParser(String source) {
        String[] data = source.split("<td");
        Task result = new Task(getTaskNumber(data[2]));
        result.setTime(getTime(data[0]));
        result.setAccount(getAccount(data[3]));
        result.setAddress(getAddress(data[4]));
        result.setAddressId(getAddressId(data[4]));
        result.setName(getName(data[5]));
        result.setTaskType(getType(data[6]));
        result.setPortalTaskNumber(getPortalTaskNumber(data[1]));
        if (result.getTaskNumber().equals("none") && result.getAccount().equals("none")) {
            result.setTaskNumber(result.getName());
        } else if (result.getTaskNumber().equals("none")) {
            result.setTaskNumber(result.getAccount());
        }

        if ((data[8].contains("ТВ-тюнер:")
        || data[8].contains("ТВ без тюнера:")
        || data[8].contains("Замена ТВ тюнера:")
        || data[8].contains("Роутер:")
                || data[8].contains("SIM карта:")
                || data[8].contains("Роутер ПРО:"))
        && (data[10].contains("ТВ-тюнер:")
                || data[10].contains("ТВ без тюнера:")
                || data[10].contains("Замена ТВ тюнера:")
                || data[10].contains("Роутер:")
                || data[10].contains("SIM карта:")
                || data[10].contains("Роутер ПРО:"))
        && (data[12].contains("ТВ-тюнер:")
                || data[12].contains("ТВ без тюнера:")
                || data[12].contains("Замена ТВ тюнера:")
                || data[12].contains("Роутер:")
                || data[12].contains("SIM карта:")
                || data[12].contains("Роутер ПРО:"))) {
            String installation = getInstall(data[8], data[9]);
            installation = getInstall(data[10], data[11], installation);
            installation = getInstall(data[12], data[13], installation);
            result.setInstallation(installation);

            result.setStatus(getStatus(data[15]));
            result.setPhoneNumber(getPhoneNumber(data[16]));
        } else if ((data[8].contains("ТВ-тюнер:")
                || data[8].contains("ТВ без тюнера:")
                || data[8].contains("Замена ТВ тюнера:")
                || data[8].contains("Роутер:")
                || data[8].contains("SIM карта:")
                || data[8].contains("Роутер ПРО:"))
                && (data[10].contains("ТВ-тюнер:")
                || data[10].contains("ТВ без тюнера:")
                || data[10].contains("Замена ТВ тюнера:")
                || data[10].contains("Роутер:")
                || data[10].contains("SIM карта:")
                || data[10].contains("Роутер ПРО:"))) {
            String installation = getInstall(data[8], data[9]);
            installation = getInstall(data[10], data[11], installation);
            result.setInstallation(installation);

            result.setStatus(getStatus(data[13]));
            result.setPhoneNumber(getPhoneNumber(data[14]));
        } else if (data[8].contains("ТВ-тюнер:")
                || data[8].contains("ТВ без тюнера:")
                || data[8].contains("Замена ТВ тюнера:")
                || data[8].contains("Роутер:")
                || data[8].contains("SIM карта:")
                || data[8].contains("Роутер ПРО:")) {
            String installation = getInstall(data[8], data[9]);
            result.setInstallation(installation);

            result.setStatus(getStatus(data[11]));
            result.setPhoneNumber(getPhoneNumber(data[12]));
        } else {
            result.setStatus(getStatus(data[9]));
            result.setPhoneNumber(getPhoneNumber(data[10]));
        }

        return result;
    }

    public String getTaskNumber(String source) {
        String result = "none";
        String[] splitted = source.split("№ наряда:<br/>");
        if (splitted.length > 1) {
            result = splitted[1].substring(0, 7);
        }
        return result;
    }

    public String getAccount(String source) {
        String result = "none";
        String[] splitted = source.split("ЛС:<br/><b>");
        if (splitted.length > 1 && !splitted[1].substring(0, 4).equals("</b>")) {
            result = splitted[1].substring(0, 7);
        }
        return result;
    }

    public String getAddress(String source) {
        String result = "None";
        String[] splitted = source.split("\"viewAddrLink\">")[1].split("</a>");
        if (splitted.length > 1) {
            result = splitted[0];
        }
        return result;
    }

    public String getName(String source) {
        String[] splitted = source.split("</td>");
        return splitted[0].substring(1).replace("*", "").replace("_", "");
    }

    public String getType(String source) {
        String result;
        String[] data = source.split("<br/>");
        if (data.length > 1) {
            result = data[0].substring(1);
        } else {
            result = source.split("</td>")[0].substring(1);
        }
        return result;
    }

    public String getStatus(String source) {
        String result = null;
        if (source.contains("background-color:")) {
            String[] splitted = source.split("background-color:")[1].split("</td>")[0].split("\">");
            if (splitted.length > 1) {
                result = splitted[1];
            }
        }
        return result;
    }

    public String getPhoneNumber(String source) {
        String result = "None";
        String[] splitted = source.split("<br/>");
        if (splitted.length > 1) {
            result = splitted[0].substring(13);
        }
        return result;
    }

    public String getInstall(String name, String count) {
        String result = name.split("</td>")[0].substring(1);
        result = String.format("%s %s", result, count.split("</td>")[0].substring(1));
        return result;
    }

    public String getInstall(String name, String count, String alreadyExist) {
        String result = String.format("%s, %s", alreadyExist, name.split("</td>")[0].substring(1));
        result = String.format("%s %s", result, count.split("</td>")[0].substring(1));
        return result;
    }

    public String getTime(String source) {
        String result = "None";
        String[] splitted = source.split("</th>")[1].split("\">");
        if (splitted.length > 1) {
            result = splitted[1];
        }
        return result;
    }

    public String getPortalTaskNumber(String source) {
        String result = "None";
        String[] splitted = source.split("editRequest")[1].split("</a><br/>");
        if (splitted.length > 1) {
            result = splitted[0].substring(10);
        }
        return result;
    }

    public String getAddressId(String source) {
        return source.split("addrid=\"")[1].split("\"")[0];
    }
}
