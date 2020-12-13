package programmieraufgaben;

import java.util.Scanner;

/**
 * Dies ist die Start-Klasse.
 * Änderungen an dieser Klassen sind NICHT gestattet!
 *
 * @author RvS Tutorenteam
 */
public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Client client = new Client();

        client.connect();

        while(client.isConnected()){
            System.out.print("$ ");
            String userInput = input.nextLine();
            if(userInput.equals("EXIT")){
                client.disconnect();
            }else{
                String reply = client.request(userInput);

                String message = client.extract(reply);

                System.out.println(message);
            }
        }
        System.out.println("Der Client wurde beendet.");
    }
}
