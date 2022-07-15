package Terminal;

import java.io.IOException;
import java.util.*;

public class Transaction {


    private static void Cash_transfer(Map Bills, Scanner sc, Set recipients) throws InvalidCardNumber, NotEnoughLedgerException, IncorrectDataException, InterruptedException {

        System.out.println("\n#Cash Transfer#");
        String type = "Cash transfer";

        System.out.print("Sender: ");
        String sender = sc.nextLine();
        Bill from = Bill.bill_search(Bills, sc);  // from

        System.out.println("Ledger: " +  from.printSumma() +  " " + from.getCurrency());

        System.out.print("Recipient: ");
        String recipient = sc.nextLine();
        Bill to = Bill.bill_search(Bills, sc);    // to

        boolean approve = true;
        double send_sum = 0;


        try{
            System.out.print("Summa to transfer: ");
            send_sum = Double.parseDouble(sc.nextLine());

            if(send_sum < 0){
                approve = false;
                throw new IncorrectDataException("Incorrect data! Summa can't be negative!");
            }

        }catch (NumberFormatException e){
            System.err.println(e.getMessage());
        }catch (IncorrectDataException ex){
            System.err.println(ex.getMessage());
        }

        try {
            if (send_sum > from.getSumm()) {
                approve = false;
                throw new NotEnoughLedgerException("Not enough ledger!", from.getSumm(), from.getCurrency());
            }

            from.setSumm(from.getSumm() - send_sum);  // send money
            to.setSumm(to.getSumm() + send_sum);    // obtain money

        }catch (NotEnoughLedgerException ep){
            System.err.println(ep.getMessage());
        } finally {
            System.out.println("Verification...");
            Thread.sleep(1000);
            if (approve){
                System.out.println("Successful!");
            }else{
                System.out.println("Deal is not accomplished!");
            }
        }

        String participants = "\nSender: " + from.person.getFull_name() + "\tcard# " + from.getCard() + "\nRecipient: " + to.person.getFull_name() + "\tcard# " + to.getCard();

        Payment.record_payment(recipients, send_sum, participants, type, sc,from.getCurrency());

        System.out.println("Press any key to continue...");
        new java.util.Scanner(System.in).nextLine();   // to make a pause between actions

    }


    private static void Convert(Scanner sc, Map Bills, Set payments) throws InvalidCardNumber, IncorrectDataException, NotEnoughLedgerException {

        String type = "Currency convert";
        System.out.println("#" + type +"#");
        Bill current_bill = Bill.bill_search(Bills, sc);
        System.out.println("Currency of your bill: " + current_bill.getCurrency());
        System.out.println("Ledger: " +  current_bill.printSumma() +  " " + current_bill.getCurrency());


        System.out.println("Which currency to convert: ");
        for (Currency curr:Currency.values()) {
            System.out.print(curr + " ");
        }

        String currncy_to = sc.nextLine().toUpperCase();
        Currency currency = null;

        try {
            currency = Currency.valueOf(currncy_to);
        }catch (IllegalArgumentException ex1){
            System.err.println("No such currency!");
        }

        String convert = current_bill.getCurrency() + "_to_" + currency.toString();

        Currency_rate trade = null;

        try {
            trade = Currency_rate.valueOf(convert);
        }catch (IllegalArgumentException ex2){
            System.err.println("No such convert!");
            System.exit(0);
        }

        System.out.println("Convert: " + trade.toString() );

        boolean approve = true;
        double send_sum = 0;

        try{
            System.out.print("Summa to transfer: ");
            send_sum = Double.parseDouble(sc.nextLine());

            if(send_sum < 0){
                approve = false;
                throw new IncorrectDataException("Incorrect data! Summa can't be negative!");
            }

        }catch (NumberFormatException e){
            System.err.println(e.getMessage());
        }catch (IncorrectDataException ex){
            System.err.println(ex.getMessage());
        }
        try {
            if (send_sum > current_bill.getSumm()) {
                approve = false;
                throw new NotEnoughLedgerException("Not enough ledger!", current_bill.getSumm(), current_bill.getCurrency());
            }
        }catch (NotEnoughLedgerException ex3){
            System.err.println(ex3.getMessage());
        }


        System.out.print("Password: ");

        try {
            int password = Bill.input_password(sc, current_bill);
            if(send_sum == current_bill.getSumm()){
                System.out.println("Convert full ledger!\nConvert the currency of your bill?");
                String ans = sc.nextLine();
                if(ans.equalsIgnoreCase("yes")){
                    current_bill.setCurrency(currency);  // change the currency of bill
                }
            }
        } catch (InvalidPassword e) {
            System.out.println(e.getMessage());
        }
        finally {

            System.out.println("Verification...");
            if(approve)
                System.out.println("Successful!");
            else
                System.out.println("Deal is not accomplished!");

        }

        current_bill.setSumm(current_bill.getSumm() - send_sum);   // convert money from ur bill to cash in the other currency
        if(current_bill.getCash().containsKey(currency)){  // if u have cash of this currency
            current_bill.setCash(current_bill.getCash(currency) + (send_sum * trade.rate), currency);
        }else
        current_bill.setCash(send_sum * trade.rate, currency);


        System.out.println("Current ledger: " + current_bill.printSumma() + " " + current_bill.getCurrency());
        System.out.println("Converted money: " + current_bill.printSumma(currency) + " " + currency );

        String INFO_TRANSACTION = "\nType of convert: " +trade.toString() + "\nConsumer: " + current_bill.person.getFull_name() + "\nCard# " + current_bill.getCard();
        Payment.record_payment(payments,send_sum, INFO_TRANSACTION, type,sc, current_bill.getCurrency());

        System.out.println("Press any key to continue...");
        new java.util.Scanner(System.in).nextLine();   // to make a pause between actions
    }

    public static void Operation(Scanner sc, Map Bills, Set receipts) throws IncorrectDataException, InterruptedException, NotEnoughLedgerException, IOException {

        while (true) {

            System.out.println("Choose the operation: ");
            System.out.println("1>>Cash Transfer ");
            System.out.println("2>>Convert ");
            System.out.println("3>>Ledger ");
            System.out.println("4>>Transactions ");
            System.out.println("5>>Exit ");

            int operation = 0;

            try {
                operation = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println(ex.getMessage() + "Incorrect format of operation code! Must be a digit!");
            }

            switch (operation) {
                case 1:
                    Transaction.Cash_transfer(Bills, sc, receipts);
                    break;
                case 2:
                    Transaction.Convert(sc, Bills, receipts);
                    break;
                case 3:
                    Bill.bill_info(Bills, sc);
                    break;
                case 4:
                    Payment.all_payments(sc);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Operation not found!");
                    break;
            }

        }
    }
}
