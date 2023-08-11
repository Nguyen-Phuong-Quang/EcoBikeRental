package org.hust.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hoang.lh194766
 * <p>
 * this class is create for the purpose to decrease the similarity
 * of validate fuction in the controllers
 */
public class Validator {

    /**
     * Check if a string contain any special characters.
     *
     * @param string the string to be checked
     * @return true - if the string does not contain any special characters <p>
     *     false - if the string contain any special characters
     */
    public static boolean validateNoSpecialCharacterString(String string) {
        if (StringUtils.isBlank(string)) {
            return false;
        }

        for (char c : string.toCharArray()) {
            if (!(Character.isLetterOrDigit(c))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a string contains any special characters with a few exception.
     *
     * @param string the string to be checked
     * @param allowedSpecialCharacters list of special characters that is allowed
     * @return true - if the string does not contain any special characters except for ones in allowed list <p>
     *     false - otherwise
     */
    public static boolean validateSomeSpecialCharacterString(String string, char... allowedSpecialCharacters) {
        if (StringUtils.isBlank(string)) {
            return false;
        }

        List<Character> specialCharacters = new ArrayList<>();
        for (char c : allowedSpecialCharacters) {
            specialCharacters.add(c);
        }
        for (char c : string.toCharArray()) {
            if (!(Character.isLetterOrDigit(c) || (specialCharacters.contains(c)))) {
                return false;
            }
        }
        return true;
    }
}
