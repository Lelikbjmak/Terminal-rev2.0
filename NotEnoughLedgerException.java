package Terminal;

public class NotEnoughLedgerException extends Exception{
        private double order;
        private String currency;

        public double getOrder() {
            return order;    // to get a summa of a person ho makes a transaction
        }

        public String getCurrency() {
            return currency;
        }


    public NotEnoughLedgerException(String message, double num, String a){   // constructor for a new exception
            super(message);
            order = num;
            currency = a;
        }
    }


