/**
 * Created by Caleb Lohrmann and Terence Ho on 3/1/2019.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class Main {

    public  static  void main(String[] args)  throws IOException
    {
        System.out.println("Please enter your 7 digit id: ");
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        Wallet wallet = new Wallet(reader.readLine().toString());
//            wallet.gen();
       /* wallet.getkWallet();
        //his example emd
        wallet.reciveFundsFromBank("D8D9752ED5CCCB9F460FB8D6EB86A984");
        //test emds
        wallet.reciveFundsFromBank("38FA8A457C688A3DCCA14EF70AF41200");
        wallet.reciveFundsFromBank("5A7F7121C2B6A33AC5CC0A8A8D164D85");
        wallet.reciveFundsFromBank("B7F6D20D26F158656ADA4B9B1F6AC76B");
        wallet.reciveFundsFromBank("656C4E35D9AC045786C52710D926CDF8");
        wallet.reciveFundsFromBank("7DDD4D75622AD4140EBE1FF5BC332319");
        wallet.reciveFundsFromBank("506DA7C817847036740DDAF5924DBFCB"); // this is the correct emd
        wallet.reciveFundsFromBank("F27FBB631394F6A1D47046B77C81C0FD"); // this one
        wallet.reciveFundsFromBank("8F59485FAA90FAD494B52646B3FA2BF2");
        wallet.reciveFundsFromBank("62E4FEF3AA705C6DE2BD91028AEFDC62");
        wallet.reciveFundsFromBank("EBBB632DA3240F6277C20830D6774213");
        wallet.reciveFundsFromBank("DFF663AFB11C4E8450033D1E90DC8F18");
        wallet.reciveFundsFromBank("9DC4B037A1772850022EBA2C45648F2F");
        wallet.reciveFundsFromBank("2712213D11C79BEE5688CC44E9BDEF2A");
        wallet.reciveFundsFromBank("EA3D8344E6D28F253B150A0C374AC7D0");
        wallet.reciveFundsFromBank("8487D3DFACD1DDCF5E8DCB915865C8C8");
        wallet.reciveFundsFromBank("1A8016A14B1FF899A8C929DE946976F7");
        wallet.reciveFundsFromBank("9F65661B3F0AE52B60286E70C10831AC"); */
        boolean menu  = true;
        while (menu) {
            System.out.println("Please select an option from the list:");
            System.out.println("A: Receiving funds from the bank");
            System.out.println("B: Synchronizing two wallets");
            System.out.println("C: Sending funds");
            System.out.println("D: Receiving Funds");
            System.out.println("F: Show Funds");
            System.out.println("E: Exit program");

            System.out.println("Select a letter: ");

            String answer = reader.readLine().toLowerCase();

            if (answer.equals("a")) {
                System.out.println("Please enter the EMD from the bank: ");
                String emdBankIssued = reader.readLine().toLowerCase();
                wallet.reciveFundsFromBank(emdBankIssued);

            } else if (answer.equals("b")) {
                System.out.println("Please enter the walletID to synchronize with: ");
                String walletBStr = reader.readLine();
                Wallet walletB = new Wallet(walletBStr);

                walletB.syncToken(wallet.syncWallets(walletB, 0, 0));

            } else if (answer.equals("c")) {
                System.out.println("Please enter the walletID of the receiver: ");
                String walletBStr = reader.readLine().toLowerCase();

                System.out.println("Please enter the amount being sent: ");
                String amount = reader.readLine().toLowerCase();

                Wallet walletB = new Wallet(walletBStr);

                // test
//                 walletB = new Wallet("120");
//                 amount = "50";
                if (wallet.sendFunds(walletB,Integer.parseInt(amount)))
                {
                    System.out.println( amount + " funds sent to " + walletB);
                } else {
                    System.out.println("Error sending funds");
                }

            } else if (answer.equals("d")) {
                //Test receiving funds
              //  Wallet testWallet = new Wallet("120");
                //String testToken = testWallet.syncWallets(wallet,43,1);
                //System.out.println("Test token: " + testToken);

                System.out.println("Please enter the receiving token: ");
                String tokenRecev = reader.readLine().toLowerCase();

                wallet.receivingFunds(tokenRecev);
                
            } else if (answer.equals("e")) {
                System.out.println("System exited");
                menu = false;
            }
            else if(answer.equals("f"))
            {
                System.out.println("Funds: " + wallet.balance);
            } else {
                System.out.println("Incorrect output please ");
            }
            System.out.println();
        }
    }
}