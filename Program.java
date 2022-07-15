package Terminal;

import java.io.IOException;
import java.util.*;

public class Program {
    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        Map <String, Bill> Bills = new HashMap<>();  // db for bills
        Set<Payment> payments = new HashSet<>();


        try {
            create_bills_db(Bills, sc);
        } catch (NumberFormatException ex){} catch (IncorrectDataException e) {}


        try {
            Transaction.Operation(sc, Bills, payments);
        } catch (IncorrectDataException e) {} catch (InterruptedException e) {} catch (NotEnoughLedgerException e) {}
        catch (IOException e) {}


    }

    public static void create_bills_db(Map Bills, Scanner sc) throws NumberFormatException, IncorrectDataException{

        System.out.print("How many clients will be registered: ");

        int count_of_clients = -1;

        try {
            count_of_clients = Integer.parseInt(sc.nextLine());
            if(count_of_clients < 0){
                throw new NumberFormatException();
            }
        }catch (NumberFormatException ex){
            System.err.println("Incorrect count of clients!");
        }

        for (int i = 0; i < count_of_clients; i++) {

            try {
                Bill.add_bills(Bills, sc);
            } catch (IncorrectDataException e) {
                System.out.println(e.getMessage());
            }
        }


    }
}
