package org.example.valid;

import javax.swing.*;

public class AuthValid {

    public static boolean checkLengthTextField(String textField) {
        if (textField.length() < 6) return false;
        return true;
    }

    public static boolean isPasswordDifferentFromAccount(String account, String password) {
        if (account.equals(password)) return false;
        return true;
    }

    public static boolean checkNullTextField(String textField) {
        if (textField == null && textField.isEmpty()) return false;
        return true;
    }
}
