import java.util.*;
import java.lang.*;
import java.io.*;

public class CPU {

    public static void main(String[] args){
        System.out.println("Printing from CPU");

        try{
                // Creating memory process
            Runtime rt = Runtime.getRuntime();

                //  Process code to run program in cmd
            Process memProc = rt.exec("java Memory input " + args[0]);

                //  Process code to run program in IDE
            //Process memProc = rt.exec("java -cp out/production/\"Project 1\" Memory input src/\"sample1.txt\"");

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
            /*boolean sendRequest = true;

            while(sendRequest){
                    //  Sending instructions from CPU to Memory
                pw.printf("r " + pgrmCounter + "\n");
                pw.flush();

                String response = getResponseFromMemory(is);
                System.out.println("Response from Memory: " + response);

                if(response.equals("50")){
                    sendRequest = false;
                }
            } */

                //  Sending instructions from CPU to Memory
            pw.printf("r " + pgrmCounter + "\n");
            pw.flush();

            pw.printf("w 72 100\n");
            pw.flush();

            pw.printf("e\n");
            pw.flush();

            printResponseFromMemory(is);

            memProc.waitFor();
            int exitVal = memProc.exitValue();
            System.out.println("Process exited: " + exitVal);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

        //  Function to print the response received back from Memory
    private static void printResponseFromMemory(InputStream is){
        try{
            int x;

                //  Printing data received from other process
            while ((x=is.read()) != -1)
                System.out.print((char)x);

            System.out.println();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

        //  Converting response received from Memory to String
    private static String getResponseFromMemory(InputStream is){
        StringBuilder sb = new StringBuilder();
        try{
            int x;

            //  Printing data received from other process
            while ((x=is.read()) != -1)
                sb.append((char)x);

            System.out.println(sb.toString());
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return sb.toString();
    }
}