/**
 * Created by Caleb Lohrmann and Terence Ho on 3/1/2019.
 */
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class Wallet {
    int balance = 0;
    int counter = 0;
    String id = "";
    String kWallet = "";
    String kbank= "F25D58A0E3E4436EC646B58B1C194C6B505AB1CB6B9DE66C894599222F07B893";
    ArrayList<String> internalTable = new ArrayList<String>();

    //wallet is created takes either a 7 digit student id and takes the last three digits to create the ID or takes 3 id
    // not need full 7 digit id to get anything from the bank 3 digit just for sending synching or receiving
    public Wallet(String id)
    {
        this.id = id;
        try {
            // byte[] t = {1,5,3};
            //  int help = 49053051;
            //  String ok = String.valueOf(153);
            //byte [] bytes = ByteBuffer.allocate(4).putInt(help).array();

            // Static getInstance method is called with hashing SHA
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // digest() method called
            // to calculate message digest of an input
            // and return array of byte
            byte[] messageDigest = md.digest(id.getBytes());
            //System.out.println(messageDigest.toString());
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            kWallet = hashtext;
        } catch (NoSuchAlgorithmException e)
        {
            System.out.println("Error from algothim");
        }

        if (id.length() != 3) {
            this.id = id.substring(4, 7);
            System.out.println("id: " + this.id);
        }

    }

    public void getkWallet()
    {
        System.out.println(kWallet);
    }

    private byte[] toByte (String target)
    {

        byte[] val = new byte[target.length() / 2];

        for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(target.substring(index, index + 2), 16);
            val[i] = (byte) j;
        }
        return  val;
    }

    private String toStringFromByte(byte[] target)
    {
        StringBuilder sb = new StringBuilder();
        for (byte b : target) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }


    public void gen()
    {
        String t = generateToken("444","333",21,1);
        AES256Decrypt(kbank,t);
    }


    // Returns String from AES encryption
    public String AES256Encrypt(String key, String plainText)
    {
        try {
            Cipher c = Cipher.getInstance("AES/ECB/NoPadding");
            byte[] k = toByte(key);
            byte[] pt = toByte(plainText);
            SecretKeySpec skeySpec = new SecretKeySpec(k, "AES");
            c.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = c.doFinal(pt);
            String finalCrypt = toStringFromByte(encrypted);
            System.out.println("encrypted token: "+ finalCrypt);
            finalCrypt = finalCrypt.replace(" ","");
            return finalCrypt;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Returns plaintext from AES decryption
    public String AES256Decrypt(String key, String encrypted)
    {
        try {
            Cipher c = Cipher.getInstance("AES/ECB/NoPadding");
            byte[] k = toByte(key);
            byte[] ec = toByte(encrypted);
            SecretKeySpec skeySpec = new SecretKeySpec(k, "AES");
            c.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decValue = c.doFinal(ec);

            String decrypt = toStringFromByte(decValue);
            // final decrypted string
            System.out.println(decrypt);
            decrypt = decrypt.replace(" ", "");
            return decrypt;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return  null;
    }

    private String generateToken(String walletIDA, String walletIDB, int amount, int counter) {
        if(walletIDA.getBytes().length > 4) {
            System.err.println("Invalid bit length for wallet ID A");
        }
        if(walletIDB.getBytes().length > 4) {
            System.err.println("Invalid bit length for wallet ID B");
        }
        StringBuilder sb1 = new StringBuilder();

        String walletIDAStr = String.format("%08d", Integer.parseInt(walletIDA));
        String walletIDBStr = String.format("%08d", Integer.parseInt(walletIDB));
        String amountStr = String.format("%08d", amount);
        String counterStr = String.format("%08d", counter);

        sb1.append(walletIDAStr);
        sb1.append(walletIDBStr);
        sb1.append(amountStr);
        sb1.append(counterStr);
        System.out.println("created string: " + sb1.toString());

        return  AES256Encrypt(kbank,sb1.toString());
    }


    // UI 1
    public void reciveFundsFromBank(String emd)
    {
        try {
            //this is the kwallet id in the user example.
            // kWallet = "CF959C7BFC4FB5792AA25457578EF9E8B78E3558A8B7BF6A92338397B5F4639D";
            byte[] cipherText = toByte(emd);
            //System.out.println(cipherText.toString());
            byte[] key = toByte(kWallet);

            Cipher c = Cipher.getInstance("AES/ECB/NoPadding");
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            c.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decValue = c.doFinal(cipherText);

            String decrypt = toStringFromByte(decValue);
            // final decrypted string
            System.out.println(decrypt);
            decrypt = decrypt.replace(" ", "");
            boolean check = true;
            for(int i = 0; i < decrypt.length() / 2 - 1; i++)
            {
                if (decrypt.charAt(i) != '0')
                {
                    check = false;
                }
            }
            if (check == true)
            {
                int decimal=Integer.parseInt(decrypt);
                balance = balance + decimal;
                System.out.println(balance);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // UI 2
    public String syncWallets(Wallet walletIDB, int amount, int counter) {
        String tokenAB = generateToken(this.id, walletIDB.id, amount, counter);
        System.out.println(tokenAB);
        return tokenAB;
    }
    public boolean syncToken(String token)
    {
        String decryptedToken = AES256Decrypt(kbank,token);

        System.out.println(decryptedToken);

        String walletIDAStr = decryptedToken.substring(0,8).replaceFirst("^0*", "");
        String walletIDBStr = decryptedToken.substring(9,16).replaceFirst("^0*", "");
        String amountStr = decryptedToken.substring(17,24).replaceFirst("^0*", "");
        if (amountStr.isEmpty()) amountStr = "0";
        String counterStr = decryptedToken.substring(25,32).replaceFirst("^0*", "");
        if (counterStr.isEmpty()) counterStr = "0";

        if (!this.id.equals(walletIDBStr)) { // check walletID if Token was created for wallet decrypting token
            // invalid
            System.out.println("Incorrect token, not for your wallet");
            return false;
        } else {
            System.out.println("Token is for your wallet");
        }
        this.counter += Integer.valueOf(counterStr);
        Wallet walletB = new Wallet(walletIDBStr);
        this.internalTable.add("walletID:" + walletIDBStr + " amount:" + amountStr + " counter: " + walletB.counter++);
    }

    // UI 3
    public boolean sendFunds(Wallet walletIDB, int amount)
    {
        if(this.balance < amount) { // check if wallet has enough balance to send the funds
            System.out.println("Insufficient funds in wallet " + this.id);
            return false;
        }
        String token = generateToken(this.id, walletIDB.id, amount, walletIDB.counter);
        this.internalTable.add("to:" + walletIDB.id + " amount:" + amount + " counter: " + walletIDB.counter++);
        System.out.println("to:" + walletIDB.id + " amount:" + amount + " counter: " + walletIDB.counter);

        // updated our balance
        this.balance = this.balance - amount;
        return true;
    }

    // UI 4
    public void receivingFunds(String token)
    {
        String decryptedToken = AES256Decrypt(kbank,token);

        String walletSender = decryptedToken.substring(0,8).replaceFirst("^0*", "");
        String walletReceiver = decryptedToken.substring(9,16).replaceFirst("^0*", "");
        String amountStr = decryptedToken.substring(17,24).replaceFirst("^0*", "");
        if (amountStr.isEmpty()) amountStr = "0";
        String counterStr = decryptedToken.substring(25,32).replaceFirst("^0*", "");
        if (counterStr.isEmpty()) counterStr = "0";

        System.out.println(this.id);
        System.out.println(walletReceiver);

        if (!this.id.equals(walletReceiver)) { // check walletID if Token was created for wallet decrypting token
            // invalid
            System.out.println("Incorrect token, not for your wallet");
            return;
        } else {
            System.out.println("Token is for your wallet");
        }

        this.counter += Integer.valueOf(counterStr);
        Wallet walletSendr = new Wallet(walletSender);

        // check if counter is not equal to the counter in the token
        if (this.counter != Integer.valueOf(counterStr)) {
            return;
        }
        this.balance += Integer.valueOf(amountStr); // add funds to balance
        this.internalTable.add("walletID:" + walletSender + " amount:" + amountStr + " counter: " + walletSendr.counter++);

        System.out.println("walletID:" + walletSender + " amount:" + amountStr + " counter: " + walletSendr.counter);
        System.out.println("Funds received");
        return;
    }
}
