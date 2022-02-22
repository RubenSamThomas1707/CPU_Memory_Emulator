import java.util.*;
import java.lang.*;
import java.io.*;

public class CPU {

    public static void main(String[] args){
        System.out.println("Starting the emulator...");

        try{
                // Creating memory process
            Runtime rt = Runtime.getRuntime();

                //  Process code to run program in cmd
            Process memProc = rt.exec("java Memory input " + args[0]);

                //  Process code to run program in IDE
            //Process memProc = rt.exec("java -cp out/production/\"Project 1\" Memory input src/\"sample0.txt\"");

                //  Initializing streams for interprocess communication
            InputStream is = memProc.getInputStream();
            OutputStream os = memProc.getOutputStream();
            PrintWriter pw = new PrintWriter(os);

                //  Defining CPU variables
            int pgrmCounter = 0;
            int sPointer = 1000;
            int instrReg = -1;
            int accumulator = 0;
            int X = 0;
            int Y = 0;
            boolean isKernelMode = false;
            boolean sendRequest = true;

            while(sendRequest){
                //  Sending instructions from CPU to Memory
                pw.printf("r " + pgrmCounter + "\n");
                pw.flush();

                    //  Getting the instruction sent as a response form memory into the instruction register
                instrReg = getResponseFromMemory(is);

                    //  Switch statement to execute the necessary commands for an instruction
                switch(instrReg){
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        Random r = new Random();
                        accumulator = r.nextInt( 100 - 1) + 1;
                        System.out.println("Random number generated: " + accumulator);
                        pgrmCounter++;
                        break;
                    case 9:
                        pgrmCounter++;
                        pw.printf("r " + pgrmCounter + "\n");
                        pw.flush();

                        int port = getResponseFromMemory(is);

                        if(port == 1){
                            System.out.println(accumulator);
                        }
                        else {
                            System.out.println((char)accumulator);
                        }
                        pgrmCounter++;
                        break;
                    case 10:
                        accumulator += X;
                        pgrmCounter++;
                        break;
                    case 11:
                        accumulator += Y;
                        pgrmCounter++;
                        break;
                    case 12:
                        break;
                    case 13:
                        break;
                    case 14:
                        X = accumulator;
                        pgrmCounter++;
                        break;
                    case 15:
                        break;
                    case 16:
                        Y = accumulator;
                        pgrmCounter++;
                        break;
                    case 17:
                        break;
                    case 18:
                        break;
                    case 19:
                        break;
                    case 20:
                        break;
                    case 21:
                        break;
                    case 22:
                        break;
                    case 23:
                        break;
                    case 24:
                        break;
                    case 25:
                        break;
                    case 26:
                        break;
                    case 27:
                        break;
                    case 28:
                        break;
                    case 29:
                        break;
                    case 30:
                        break;
                    default:
                            //  Ending execution when the End instruction is received
                        sendRequest = false;
                        System.out.println("Exiting instruction received...");
                            //  Exiting Memory.java process
                        pw.printf("e\n");
                        pw.flush();
                        break;
                }
            }

                //  Elegantly killing child process
            memProc.waitFor();

            System.out.println("Process exited: " + memProc.exitValue());
            System.out.println("Exiting the emulator...");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

        //  Converting response received from Memory to String
    private static int getResponseFromMemory(InputStream is){
        int response = -1;
        try{
                //  Getting the response received back from Memory
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String str = br.readLine();
            response = Integer.parseInt(str);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return response;
    }
}