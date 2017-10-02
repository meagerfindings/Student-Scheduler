package com.meagerfindings.matgreten.student_scheduler;

/**
 * Created by matgreten on 9/18/17.
 */

class InputValidation {

    /*Regular Expressions: Predefined Character Classes. (2015). Retrieved September 18, 2017, from https://docs.oracle.com/javase/tutorial/essential/regex/pre_char_classes.html
    Provided clarity on which characters would be excluded and included by performing regex matching on input strings, ultimately allowing sanitization of user input.*/


    public static String validateString(String inputString) {
        StringBuilder sanitizedString = new StringBuilder();

        for (int i = 0; i < inputString.length(); i++) {

            String charToEval = String.valueOf(inputString.charAt(i));

            if (charToEval.matches("^[0-9a-zA-Z-+_\\s]+")) sanitizedString.append(charToEval);
        }

        return sanitizedString.toString();
    }

    public static String validateEmail(String inputString) {
        StringBuilder sanitizedString = new StringBuilder();

        for (int i = 0; i < inputString.length(); i++) {

            String charToEval = String.valueOf(inputString.charAt(i));

            if (charToEval.matches("^[0-9a-zA-Z@+_.]+")) sanitizedString.append(charToEval);
        }
        return sanitizedString.toString();
    }

    public static String validatePhone(String inputString) {
        StringBuilder sanitizedString = new StringBuilder();

        for (int i = 0; i < inputString.length(); i++) {

            String charToEval = String.valueOf(inputString.charAt(i));

            if (charToEval.matches("^[0-9\\s.-]+")) sanitizedString.append(charToEval);
        }
        return sanitizedString.toString();
    }
}