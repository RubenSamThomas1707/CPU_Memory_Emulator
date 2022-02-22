import java.util.*;
import java.lang.*;
import java.io.*;

public class Memory {
    public static void main(String[] args){
        int[] instrList = new int[2000];
        Arrays.fill(instrList, -1);
        String filename = args[1];
        boolean processActive = true;

            //  Checking if the Memory file needs to take in input from the file
        if(args[0].equals("input")){
                //  Populating instructions into the instruction array
            populateInstructions(instrList, filename);
        }
            //  Initializing scanner to get input form the CPU process
        Scanner scan = new Scanner(System.in);

            //  Checking if any data has been sent by the CPU
        while(processActive){
                //  Storing different information sent by the CPU in an array
            String[] cpuCmd = scan.nextLine().split(" ");

                //  Switch statement to read or write information to and from the instrList array
            switch (cpuCmd[0]){
                    //  Case to read information from instrList and send over to CPU
                case "r":
                    int index = Integer.parseInt(cpuCmd[1]);
                    System.out.println(instrList[index]);
                    break;

                    // Case to write information at the provided at the index specified by the CPU
                case "w":
                    int indexToWrite = Integer.parseInt(cpuCmd[1]);
                    int numToWrite = Integer.parseInt(cpuCmd[2]);
                    instrList[indexToWrite] = numToWrite;
                    break;

                default:
                    processActive = false;
                    break;
            }
        }
        System.exit(0);
    }

        //  Function to insert instructions into the array
    private static void populateInstructions(int[] instrList, String filename){
        try{
            File inFile = new File(filename);
            Scanner inFileScanner = new Scanner(inFile);
            int index = 0;

                //  Iterating through input file to place information into the array
            while(inFileScanner.hasNext()){
                    //   Getting next line from the input file
                String fileInput = inFileScanner.nextLine();

                    //   Checking if the line is empty
                if(fileInput.length() != 0){
                    String[] line = fileInput.split(" ");

                        //   Checking if the line is only made up of comments or unwanted information
                    if(!line[0].equals("")){

                            //  Jumping to the mentioned address if there is a number beginning with a '.'
                        if(line[0].charAt(0) == '.'){
                                //Getting the new address to store the instructions at
                            index = Integer.parseInt(line[0].substring(1, line[0].length()));
                        }
                        else{
                            instrList[index] = Integer.parseInt(line[0]);
                            index++;
                        }
                    }
                }
            }
        }       //  End of try statement

        catch (Exception e){
            System.out.println("ERROR While Loading File....");
            System.out.println("Exiting...");
            e.printStackTrace();
        }       //  End of catch statement
    }

    //  Function to print the instruction list array for debugging
    private static void printInstrList(int[] instrList){
        int currIndex = 0;
        for(int num: instrList){
            System.out.println("Index: " + currIndex + "; Value: " + num);
            currIndex++;
        }
        System.out.println();
    }
}
