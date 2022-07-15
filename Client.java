package Terminal;

import java.util.Scanner;

public class Client {
    private final String Full_name;
    private final String ID;
    private final String phone_number;

    Client(String full_name, String id, String phone){
        this.Full_name = full_name;
        this.ID = id;
        this.phone_number = phone;
    }

    public String getFull_name() {
        return Full_name;
    }

    public static Client register_new_client(Scanner sc) throws IncorrectDataException{

        System.out.print("Full name: ");
        String name = Client.client_Name(sc);

        System.out.print("ID: ");
        String id = sc.nextLine();

        System.out.print("Phone: ");
        String phone = null;
        try {
            phone = sc.nextLine();
            if(!(phone.trim().matches("^\\+?\\d*$"))){
                throw new IncorrectDataException("Incorrect data! Incorrect phone number format!");
            }
        }catch (IncorrectDataException ex){
            System.err.println(ex.getMessage());
        }

        Client pers = new Client(name, id, phone);

        return  pers;

    }


    public static String client_Name(Scanner sc) throws IncorrectDataException{

        String name = null;
        try{
            name = sc.nextLine();
            if(!(name.matches("([A-Za-z]+)\\s*([A-Za-z]+)\\s*([A-Za-z]?)")))
                throw new IncorrectDataException("Incorrect format of Full name!");
        }catch (IncorrectDataException ex1){
            System.err.println(ex1.getMessage());
            System.exit(0);
        }

        return name;

    }     //for client name
}
