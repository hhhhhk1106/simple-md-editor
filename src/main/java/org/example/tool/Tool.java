package org.example.tool;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class Tool {
    public static String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    public static String getTimePeriod(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        LocalDateTime dateTime1 = LocalDateTime.parse(startTime, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(endTime, formatter);

        Duration duration = Duration.between(dateTime1, dateTime2);

        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        StringBuilder result = new StringBuilder();

        if (days > 0) {
            result.append(days).append("day");
        }
        if (hours > 0) {
            result.append(hours).append("hour");
        }
        result.append(minutes).append("min");
        result.append(seconds).append("s");

        return result.toString();
    }
    public static boolean getUserAnswerYes() {
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        while (true) {
            if(userInput.equals("Y")||userInput.equals("y")) {
                return true;
            } else if(userInput.equals("N")||userInput.equals("n")){
                return false;
            } else {
                System.out.println("请输入[Y\\N]。");
                userInput = scanner.nextLine();
            }
        }
    }
}
