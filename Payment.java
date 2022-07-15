package Terminal;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

public class Payment implements Serializable {

    private double payment;
    private int ID;
    private String operation_type;
    private String participants_info;
    private String currency;

    Payment(double summa, String participants_info, String type, String currency) {
        this.payment = summa;
        this.ID =  + (int)(Math.random()*((1000000000 - 1000000) + 1)) + 1000000;
        this.operation_type = type;
        this.participants_info = participants_info;
        this.currency = currency;
    }

    public static void Payment_info(Scanner sc){

        System.out.print("Transaction# ");
        String a = sc.nextLine();
        File dir = new File("Receipts");
        File fl = new File(dir + "\\" + a);

        if(fl.exists())
            System.out.println("Receipt is found." );
        else
            System.out.println("Transaction not found!");

        try(FileReader read = new FileReader(fl)){
            if(fl.canRead()){
                System.out.println("Getting the info about transaction #" + fl.getName());

                char[] buff = new char[256];   // buffer  to read the info about transaction

                int c;  // our symbol we have read

                while((c = read.read(buff)) > 0 ){

                    if(c < 256)
                        buff = Arrays.copyOf(buff,c);

                    System.out.print(buff);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Transaction not found!");
        } catch (IOException e) {
            System.err.println("IO issue!");
        }

        System.out.println("Press any key to continue...");
        new java.util.Scanner(System.in).nextLine();   // to make a pause between actions

    }


    public static void all_payments(Scanner sc) throws IOException {

        File dir = new File("Receipts");

        try {
            if (dir.isDirectory()) {
                System.out.println("Content: ");
                for (File fl : dir.listFiles()) {
                    if (fl.isDirectory())
                        System.out.println("Folder: " + fl.getName());
                    else System.out.println("File: " + fl.getName());
                }
                System.out.println("Open folder with all payments?");
                String answer = sc.nextLine();
                if(answer.equalsIgnoreCase("yes")){
                    Desktop top = null;

                    if(Desktop.isDesktopSupported()){
                        top = Desktop.getDesktop();
                    }

                    try{
                        top.open(dir);
                    }catch (IOException ex2){
                        System.out.println("Can't open folder! Check the path to folder!");
                        System.exit(0);
                    }
                }

            } else throw new FileNotFoundException("Folder is not found!");
        }catch (FileNotFoundException ex1){
            System.err.println(ex1.getMessage());
            System.exit(0);
        }

        System.out.println("Press any key to continue...");
        new java.util.Scanner(System.in).nextLine();   // to make a pause between actions

    }

    public static void record_payment(Set payments, double summa, String participants, String oper_type, Scanner sc, String currency) throws IncorrectDataException {
        Payment temp = new Payment(summa, participants, oper_type, currency);
        payments.add(temp);

        String info = "Payment# " + temp.ID + "\nOperation type: " + temp.operation_type + temp.participants_info + "\nSumma of operation: " + temp.payment + " " +  temp.currency + "\n";

        System.out.println("\n\n "+ info + "\n");

        System.out.println("Print receipt?");

        String ans = sc.nextLine();
        if(ans.equalsIgnoreCase("yes")){

            File dir = new File("Receipts");
            File fl = new File(dir + "\\" + Integer.toString(temp.ID));

            try {
                dir.mkdir();// create a folder

                if(dir.exists())
                    System.out.println("Information recording into folder 'Receipts' in the root catalog");

                boolean created  = fl.createNewFile();

                if(created)
                    System.out.println("Typing receipt...");
                else
                    System.out.println("File is not created!");

            } catch (IOException e) {
                System.err.println("IO error! Check file: " + temp.ID);
            }

            try(FileWriter write = new FileWriter(fl, false)){

                //ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fl));

                //oos.writeObject(temp);  // with serializable write an object info in file

                write.write(info);  // write info in file by string with Filewriter

               // oos.flush();
               // oos.close();
                if(fl.length()!=0){
                    System.out.println("Successful!");
                }
            } catch (IOException e) {
                System.out.println("Can't print the receipt!");
            }

        } else if (ans.equalsIgnoreCase("no")) {
           return;
        }
        else throw new IncorrectDataException("Incorect answer!");

    }


}
