package com.meagerfindings.matgreten.c196_student_scheduler_mat_greten;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by matgreten on 9/18/17.
 */

class InputValidation {


    public static String validateString(String inputString) {
        StringBuilder sanitizedString = new StringBuilder();

        for (int i = 0; i < inputString.length(); i++) {

            String charToEval = String.valueOf(inputString.charAt(i));

            // TODO: CITE: https://docs.oracle.com/javase/tutorial/essential/regex/pre_char_classes.html
            if (charToEval.matches("^[0-9a-zA-Z\\s]+")) sanitizedString.append(charToEval);
        }

        return sanitizedString.toString();
    }

    public static String validateEmail(String inputString) {
        StringBuilder sanitizedString = new StringBuilder();

        for (int i = 0; i < inputString.length(); i++) {

            String charToEval = String.valueOf(inputString.charAt(i));

            if (charToEval.matches("^[0-9a-zA-Z@.]+")) sanitizedString.append(charToEval);
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