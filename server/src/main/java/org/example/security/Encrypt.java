package org.example.security;

import org.example.model.Message;
import org.example.model.RoomChat;
import org.example.model.SendMessage;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;

public class Encrypt {
    private static String AESKEY = "G9fC2vY8mH4xL1Qz";

    public static String AESEncrypt(String text) {
        byte[] encryptedBytes = null;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(AESKEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            encryptedBytes = cipher.doFinal(text.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String AESDecrypt(String text) {
        String decryptText = null;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(AESKEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] decodeBytes = Base64.getDecoder().decode(text);
            byte[] decryptedBytes = cipher.doFinal(decodeBytes);
            decryptText = new String(decryptedBytes);
        }catch (Exception e){
            e.printStackTrace();
        }

        return decryptText;
    }

    public static String hashPassword(String password) {
        String hashedPassword = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            hashedPassword = Base64.getEncoder().encodeToString(hash);
        }catch (Exception e){
            e.printStackTrace();
        }

        return hashedPassword;
    }

    public static ArrayList<String> AESEncryptArrayList(ArrayList<String> arraylist) {
        ArrayList<String> arrayListEncrypted = new ArrayList<>();
        try {
            for (int i = 0; i < arraylist.size(); i++) {
                arrayListEncrypted.add(AESEncrypt(arraylist.get(i)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return arrayListEncrypted;
    }

    public static ArrayList<String> AESDecryptArrayList(ArrayList<String> arraylist) {
        ArrayList<String> arrayListDecrypted = new ArrayList<>();
        try {
            for (int i = 0; i < arraylist.size(); i++) {
                arrayListDecrypted.add(AESDecrypt(arraylist.get(i)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return arrayListDecrypted;
    }

    public static ArrayList<RoomChat> AESEncryptRoomChatArrayList(ArrayList<RoomChat> list) {
        ArrayList<RoomChat> roomChatArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < list.size(); i++) {
                roomChatArrayList.add(new RoomChat(
                        AESEncrypt(list.get(i).getNameRoom()),
                        AESEncrypt(list.get(i).getCreater())
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return roomChatArrayList;
    }

    public static ArrayList<RoomChat> AESDecryptRoomChatArrayList(ArrayList<RoomChat> list) {
        ArrayList<RoomChat> roomChatArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < list.size(); i++) {
                roomChatArrayList.add(new RoomChat(
                        AESDecrypt(list.get(i).getNameRoom()),
                        AESDecrypt(list.get(i).getCreater())
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return roomChatArrayList;
    }

    public static ArrayList<Message> AESEncryptMessageArrayList(ArrayList<Message> list) {
        ArrayList<Message> messagesArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < list.size(); i++) {
                messagesArrayList.add(new Message(
                        AESEncrypt(list.get(i).getSender()),
                        AESEncrypt(list.get(i).getMessage()),
                        AESEncrypt(list.get(i).getTimestamp())
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return messagesArrayList;
    }

    public static ArrayList<Message> AESDecryptMessageArrayList(ArrayList<Message> list) {
        ArrayList<Message> messagesArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < list.size(); i++) {
                messagesArrayList.add(new Message(
                        AESDecrypt(list.get(i).getSender()),
                        AESDecrypt(list.get(i).getMessage()),
                        AESDecrypt(list.get(i).getTimestamp())
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return messagesArrayList;
    }

    public static SendMessage AESEncryptSendMessage(SendMessage sendMessage) {
        return new SendMessage(
                Encrypt.AESEncrypt(sendMessage.getSender()),
                Encrypt.AESEncrypt(sendMessage.getToRoom()),
                Encrypt.AESEncrypt(sendMessage.getMessage())
        );
    }

    public static SendMessage AESDecryptSendMessage(SendMessage sendMessage) {
        return new SendMessage(
                Encrypt.AESDecrypt(sendMessage.getSender()),
                Encrypt.AESDecrypt(sendMessage.getToRoom()),
                Encrypt.AESDecrypt(sendMessage.getMessage())
        );
    }
}
