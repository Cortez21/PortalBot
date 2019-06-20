package com.portalbot.main;

public class RouterChecker {
   public String tryingToCheck(String account, String chatID) {
       String result = null;
       try {
           PortalRequester requester = new PortalRequester(chatID);
           for (int i = 0; i < 10; i++) {
               String data = requester.checkRouter(account);
               String[] splitted = data.split(":");
               if (!splitted[1].contains("0,")) {
                   result = String.format("Роутер активирован: %s", splitted[1].substring(1, 11));
                   break;
               } else if (!splitted[2].contains("0}")) {
                   result = String.format("Роутер ПРО активирован: %s", splitted[2].substring(1, 11));
                   break;
               } else {
                   result = "По данному ЛС нет активированных роутеров";
               }
           }
       } catch (BadLoggingException e) {
           result = null;
       }


       return result;
   }

}
