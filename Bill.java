package Terminal;



import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Bill {   // with generics

        Client person;
        String bill;   // bill of some Person
        final String card;
        private double summ;
        private Map<Currency, Double> cash;
        private Currency currency;   // currency pf ledger

        private final int password;   // password to accept transactions

        private Bill(String bill, int password, Currency currency, String te, Client person, double summa){

            this.person = person;
            this.bill = bill;
            this.card = te;
            this.summ = summa; //(Math.random()*(3020-10) + 10);   // [10;1000)
            this.password = password;
            this.currency = currency;
            this.cash = new HashMap<>();
        }

        public void gainSumm(double summ) {
            this.summ += summ;
        }

    public void setCash(double cash, Currency cash_cur) {

            this.cash.put(cash_cur,cash);  // if cash_curr alreday exist -> erase the previous num and append new one
    }

    public Map<Currency, Double> getCash() {
        return cash;
    }

    public double getCash(Currency need_curr) {

            return cash.get(need_curr);
    }

    public double getSumm() {
            return summ;
        }

        public void setSumm(double summ) {
            this.summ = summ;
        }

        public void setCurrency(Currency currency) {
            this.currency = currency;
        }

    public String printSumma(){
            String formattedDouble = new DecimalFormat("#0.00").format(summ);
            return formattedDouble;
        }

    public String printSumma(Currency key){
        String formattedDouble = new DecimalFormat("#0.00").format(cash.get(key));
        return formattedDouble;
    }
        public void sentSumm(double summ) {
            this.summ -= summ;
        }

        public String getBill() {
            return bill;
        }

        public String getCard() {
                return card;
        }

        public int getPassword() {
            return password;
        }

        public String getCurrency() {
            return currency.currency;
        }

    public void ConverLedger(Currency_rate rate) {
        this.summ = summ * rate.rate;
    }

    public static void add_bills (Map Bills, Scanner sc) throws IncorrectDataException {

            try {

               Client personage =  Client.register_new_client(sc);

                System.out.print("Bill: ");
                String bill = sc.nextLine();


                System.out.print("Currency ");
                for (Currency cr:Currency.values()) {
                    System.out.print(cr.currency + " ");
                }
                String curr = sc.nextLine().toUpperCase();
                Currency currency1 = null;
                try {
                    currency1 = Currency.valueOf(curr);
                }catch (IllegalArgumentException ex1){
                    System.err.println("No such argument in Currency!");
                    System.exit(0);
                }

                System.out.print("Create a password to confirm transactions: ");

                int password = 0;
                try {
                    password = Integer.parseInt(sc.nextLine());
                }catch (NumberFormatException ex){
                    System.err.println("Incorrect format of password! Must be 4 digits!");
                    System.exit(0);
                }

                // generating card number
                String curren = Long.toString(ThreadLocalRandom.current().nextLong(1_000_000_000_000_000L, 9_999_999_999_999_999L));
                curren = (curren.replaceAll("(.{4})", "$1 ")).trim();

                double summa = (Math.random()*(3020-10) + 10);

                Bill temp = new Bill(bill, password, currency1,curren, personage,summa);

                System.out.println("Card number: " + temp.getCard());

                Bills.put(temp.getCard(), temp);

            }
            catch (IncorrectDataException exxx){
                System.err.println(exxx.getMessage());
                System.exit(0);

            }
            System.out.println("----------------");

        }  // for add bills


        public static void bill_info(Map Bills, Scanner sc) {

            Bill temp = null;
            try {
                temp = Bill.bill_search(Bills, sc);
            } catch (InvalidCardNumber e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }

            System.out.print("Password: ");

            try {
                int password = Bill.input_password(sc, temp);
            } catch (InvalidPassword e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }

            System.out.println("\n#Bill: " + temp.getBill() + "\nOwner: " + temp.person.getFull_name() + "\nLedger: " + temp.printSumma() + " " +  temp.getCurrency());
            System.out.print("Cash: ");
            for (var ass: temp.getCash().entrySet()) {
                System.out.printf("%.2f %s ",ass.getValue(), ass.getKey().toString());
            }

            System.out.println("\nPress any key to continue...");
            new java.util.Scanner(System.in).nextLine();   // to make a pause between actions
        }   // for bill info


        public static Bill bill_search(Map Bills, Scanner sc) throws InvalidCardNumber{

            String card_num = null;
            System.out.print("Card number: ");

            try {
                card_num = Bill.input_card_number(sc);
            } catch(IncorrectDataException ex){
                System.exit(0);
            }

            try {
                if (!(Bills.containsKey(card_num))) {
                    throw new InvalidCardNumber("Incorrect Data! User not found!");
                }
            } catch (InvalidCardNumber exx){
                System.err.println(exx.getMessage());
                System.exit(0);
            }

            Bill temp = (Bill) Bills.get(card_num);

            return temp;
        } // for bill search


        public static String input_card_number(Scanner sc) throws IncorrectDataException{

            String str = null;

            try{
                str = sc.nextLine().trim();
                if (!(str.matches("(\\d{4}\\s?){4}"))){
                    throw new IncorrectDataException("Incorrect format of card number!");
                }
            }catch (IncorrectDataException ex){
                System.err.println(ex.getMessage());
                System.exit(0);
            }
            return str;
        }// for input card number



        public static int input_password(Scanner sc, Bill bill) throws InvalidPassword{

            int pass = 0;

            try{
                pass = Integer.parseInt(sc.nextLine());

                if (pass != bill.getPassword()){
                    throw new InvalidPassword("Invalid password!");
                }
            }catch (NumberFormatException ex1){
                System.err.println("Incorrect format of password!");
                System.exit(0);
            }catch (InvalidPassword ex2){
                System.out.println(ex2.getMessage());
                System.exit(0);
            }
            return pass;
        }  // for input_password



    } // for class



