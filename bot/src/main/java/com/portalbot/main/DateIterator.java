package com.portalbot.main;


public class DateIterator {
    private String year;
    private int month;
    private int day;

    public DateIterator(String yyyy, String mm, String dd) {
        year = yyyy;
        month = Integer.parseInt(mm);
        day = Integer.parseInt(dd);
    }

    public String next() {
       int mult;
        String currentDate = convertToString(year, month, day);
        switch (month) {
            case 1:
            case 7:
            case 3:
            case 5:
            case 8:
            case 10:
            case 12:
                mult = 31;
               break;
            case 2: mult = 28;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                mult = 30;
                break;
            default : mult = 0;
                break;
       }
       if (((day + 1) % mult) != 1) {
           day += 1;
       } else {
           if (month != 12) {
               day = 1;
               month += 1;
           }
       }
       return currentDate;
    }

    public String convertToString(String year, int month, int day) {
        String dayStr = day < 10 ? String.format("0%s", day) : String.valueOf(day);
        String monthStr = month < 10 ? String.format("0%s", month) : String.valueOf(month);
        return String.format("%s-%s-%s", year, monthStr, dayStr);
    }
}
