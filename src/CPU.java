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

                //  Communicating between processes
            PrintWriter pw = new PrintWriter(os);

                //  Defining CPU variables
            int pgrmCounter = 1;
            int sPointer = 999;
            int instrReg = 0;
            int accumulator = 0;
            int X = 0;
            int Y = 0;

            /********************************************************/
                //  Sending and printing dummy commands to the Memory process
            pw.printf("read\n");
            pw.flush();

            pw.printf("write 72 100\n");
            pw.flush();

            pw.printf("exit\n");
            pw.flush();

            //printResponse(is);
            /********************************************************/

            memProc.waitFor();
            int exitVal = memProc.exitValue();

            System.out.println("Process exited: " + exitVal);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void printResponse(InputStream is){
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
}
