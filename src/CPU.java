import java.util.*;
import java.lang.*;
import java.io.*;

public class CPU {

    public static void main(String[] args){
        System.out.println("Starting emulator...");

        try{
                // Creating memory process
            Runtime rt = Runtime.getRuntime();

                //  Process code to run program in cmd
            Process memProc = rt.exec("java Memory input " + args[0]);

                //  Process code to run program in IDE
            //Process memProc = rt.exec("java -cp out/production/\"Project 1\" Memory input src/\"sample4.txt\"");

                //  Initializing streams for interprocess communication
            InputStream is = memProc.getInputStream();
            OutputStream os = memProc.getOutputStream();
            PrintWriter pw = new PrintWriter(os);

                //  Defining CPU variables
            int pgrmCounter = 0;
            int sPointer = 1000;
            int instrReg;
            int accumulator = 0;
            int X = 0;
            int Y = 0;
            /*int timerRate = Integer.parseInt(args[1]);*/
            boolean sendRequest = true;
            boolean isKernelMode = false;
            boolean timerInt = false;

                //  Sending requests from the CPU to the Memory till end instruction is received
            while(sendRequest){
                    //  Verifying whether the user has access to the address
                verifyMemoryAccess(pgrmCounter, isKernelMode, pw);

                //  Sending instructions from CPU to Memory
                pw.printf("r " + pgrmCounter + "\n");
                pw.flush();

                //  Getting the instruction sent as a response form memory into the instruction register
                instrReg = getResponseFromMemory(is);

                //  Switch statement to execute the necessary commands for an instruction
                switch(instrReg){
                    //  Load value in to the accumulator
                    case 1:
                        pgrmCounter++;
                        pw.printf("r " + pgrmCounter + "\n");
                        pw.flush();
                        accumulator = getResponseFromMemory(is);
                        pgrmCounter++;
                        break;

                    //  Load value at the given address into the accumulator
                    case 2:
                        pgrmCounter++;

                            //  Verifying whether the user has access to the address
                        verifyMemoryAccess(pgrmCounter, isKernelMode, pw);
                        pw.printf("r " + pgrmCounter + "\n");
                        pw.flush();
                        instrReg = getResponseFromMemory(is);

                            //  Verifying whether the user has access to the address
                        verifyMemoryAccess(instrReg, isKernelMode, pw);
                        pw.printf("r " + instrReg + "\n");
                        pw.flush();
                        accumulator = getResponseFromMemory(is);
                        pgrmCounter++;
                        break;

                    //  Load value from the address found in the given address into the accumulator
                    case 3:
                        pgrmCounter++;

                            //  Verifying whether the user has access to the address
                        verifyMemoryAccess(pgrmCounter, isKernelMode, pw);
                        pw.printf("r " + pgrmCounter + "\n");
                        pw.flush();
                        instrReg = getResponseFromMemory(is);

                            //  Verifying whether the user has access to the address
                        verifyMemoryAccess(instrReg, isKernelMode, pw);
                        pw.printf("r " + instrReg + "\n");
                        pw.flush();
                        instrReg = getResponseFromMemory(is);

                            //  Verifying whether the user has access to the address
                        verifyMemoryAccess(instrReg, isKernelMode, pw);
                        pw.printf("r " + instrReg + "\n");
                        pw.flush();
                        accumulator = getResponseFromMemory(is);
                        pgrmCounter++;
                        break;

                    //  Load value at (address+X) into the accumulator
                    case 4:
                        pgrmCounter++;

                            //  Verifying whether the user has access to the address
                        verifyMemoryAccess(pgrmCounter, isKernelMode, pw);
                        pw.printf("r " + pgrmCounter + "\n");
                        pw.flush();
                        instrReg = (getResponseFromMemory(is) + X);

                            //  Verifying whether the user has access to the address
                        verifyMemoryAccess(instrReg, isKernelMode, pw);
                        pw.printf("r " + instrReg + "\n");
                        pw.flush();
                        accumulator = getResponseFromMemory(is);
                        pgrmCounter++;
                        break;

                    //  Load value at (address+Y) into the accumulator
                    case 5:
                        pgrmCounter++;

                            //  Verifying whether the user has access to the address
                        verifyMemoryAccess(pgrmCounter, isKernelMode, pw);
                        pw.printf("r " + pgrmCounter + "\n");
                        pw.flush();
                        instrReg = getResponseFromMemory(is) + Y;

                            //  Verifying whether the user has access to the address
                        verifyMemoryAccess(instrReg, isKernelMode, pw);
                        pw.printf("r " + instrReg + "\n");
                        pw.flush();
                        accumulator = getResponseFromMemory(is);
                        pgrmCounter++;
                        break;

                    //  Load from (sPointer+X) into the accumulator
                    case 6:
                        instrReg = (sPointer + X);

                            //  Verifying whether the user has access to the address
                        verifyMemoryAccess(instrReg, isKernelMode, pw);
                        pw.printf("r " + instrReg + "\n");
                        pw.flush();
                        accumulator = getResponseFromMemory(is);
                        pgrmCounter++;
                        break;

                    //  Store value in the accumulator to the address
                    case 7:
                        pgrmCounter++;

                            //  Verifying whether the user has access to the address
                        verifyMemoryAccess(pgrmCounter, isKernelMode, pw);
                        pw.printf("r " + pgrmCounter + "\n");
                        pw.flush();
                        instrReg = getResponseFromMemory(is);

                            //  Verifying whether the user has access to the address
                        verifyMemoryAccess(instrReg, isKernelMode, pw);
                        pw.printf("w " + instrReg + " " + accumulator + "\n");
                        pw.flush();
                        pgrmCounter++;
                        break;

                    //  Generating a random integer and storing in the accumulator
                    case 8:
                        Random r = new Random();
                        accumulator = r.nextInt( 100 - 1) + 1;
                        System.out.println("Random number generated: " + accumulator);
                        pgrmCounter++;
                        break;

                    //  If port == 1, then print accumulator as an int, if port == 2, print accumulator as a char
                    case 9:
                        pgrmCounter++;
                        pw.printf("r " + pgrmCounter + "\n");
                        pw.flush();

                        int port = getResponseFromMemory(is);

                        if(port == 1){
                            System.out.print(accumulator);
                        }
                        else {
                            System.out.print((char)accumulator);
                        }
                        pgrmCounter++;
                        break;

                    //  Add X to the accumulator
                    case 10:
                        accumulator += X;
                        pgrmCounter++;
                        break;

                    //  Add Y to the accumulator
                    case 11:
                        accumulator += Y;
                        pgrmCounter++;
                        break;

                    //  Subtract X from the accumulator
                    case 12:
                        accumulator -= X;
                        pgrmCounter++;
                        break;

                    //  Subtract Y from the accumulator
                    case 13:
                        accumulator -= Y;
                        pgrmCounter++;
                        break;

                    // Copy value from accumulator to X
                    case 14:
                        X = accumulator;
                        pgrmCounter++;
                        break;

                    //  Copy value in X to accumulator
                    case 15:
                        accumulator = X;
                        pgrmCounter++;
                        break;

                    //  Copy value in accumulator to Y
                    case 16:
                        Y = accumulator;
                        pgrmCounter++;
                        break;

                    // Copy value in Y to accumulator
                    case 17:
                        accumulator = Y;
                        pgrmCounter++;
                        break;

                    // Copy value in accumulator to sPointer
                    case 18:
                        sPointer = accumulator;
                        pgrmCounter++;
                        break;

                    // Copy value in sPointer to accumulator
                    case 19:
                        accumulator = sPointer;
                        pgrmCounter++;
                        break;

                    //  Jump to the given address
                    case 20:
                        pgrmCounter++;

                            //  Verifying whether the user has access to the address
                        verifyMemoryAccess(pgrmCounter, isKernelMode, pw);
                        pw.printf("r " + pgrmCounter + "\n");
                        pw.flush();
                        instrReg = getResponseFromMemory(is);
                        pgrmCounter = instrReg;
                        break;

                    //  Jump to given address if value of accumulator == 0
                    case 21:
                        if(accumulator == 0){
                            pgrmCounter++;

                                //  Verifying whether the user has access to the address
                            verifyMemoryAccess(pgrmCounter, isKernelMode, pw);
                            pw.printf("r " + pgrmCounter + "\n");
                            pw.flush();
                            instrReg = getResponseFromMemory(is);
                            pgrmCounter = instrReg;
                        }
                        else{
                            pgrmCounter += 2;
                        }
                        break;

                    // Jump to given address if value of accumulator != 0
                    case 22:
                        if(accumulator != 0){
                            pgrmCounter++;

                                //  Verifying whether the user has access to the address
                            verifyMemoryAccess(pgrmCounter, isKernelMode, pw);
                            pw.printf("r " + pgrmCounter + "\n");
                            pw.flush();
                            instrReg = getResponseFromMemory(is);
                            pgrmCounter = instrReg;
                        }
                        else{
                            pgrmCounter += 2;
                        }
                        break;

                    //  Push next instruction to sPointer and jump to the given address
                    case 23:
                        pgrmCounter++;

                            //  Verifying whether the user has access to the address
                        verifyMemoryAccess(pgrmCounter, isKernelMode, pw);
                        pw.printf("r " + pgrmCounter + "\n");
                        pw.flush();
                        instrReg = getResponseFromMemory(is);
                        sPointer--;
                        pgrmCounter++;

                            //  Verifying whether the user has access to the address
                        verifyMemoryAccess(pgrmCounter, isKernelMode, pw);
                        pw.printf("w " + sPointer + " " + pgrmCounter + "\n");
                        pw.flush();
                        pgrmCounter = instrReg;
                        break;

                    //  Pop return address from stack and jump to the address
                    case 24:
                        pw.printf("r " + sPointer + "\n");
                        pw.flush();
                        instrReg = getResponseFromMemory(is);
                        sPointer++;
                        pgrmCounter = instrReg;
                        break;

                    //  Increment value of X
                    case 25:
                        X++;
                        pgrmCounter++;
                        break;

                    // Decrement value in X
                    case 26:
                        X--;
                        pgrmCounter++;
                        break;

                    //  Push accumulator to the stack
                    case 27:
                        sPointer--;
                        pw.printf("w " + sPointer + " " + accumulator + "\n");
                        pw.flush();
                        pgrmCounter++;
                        break;

                    // Pop from stack into the accumulator
                    case 28:
                        pw.printf("r " + sPointer + "\n");
                        pw.flush();
                        sPointer++;
                        accumulator = getResponseFromMemory(is);
                        pgrmCounter++;
                        break;

                    //  Perform system call
                    case 29:
                        // Change mode to kernel and move current data in user stack to system stack
                        isKernelMode = true;
                        pgrmCounter++;
                        int sysStack = 1999;
                        pw.printf("w " + sysStack + " " + pgrmCounter + "\n");
                        pw.flush();
                        sysStack--;
                        pw.printf("w " + sysStack + " " + sPointer + "\n");
                        pw.flush();
                        pgrmCounter = 1500;
                        sPointer = sysStack;
                        break;

                    //  Return from system call
                    case 30:
                        // Switch back to user mode from kernel mode and load data back into user stack from system stack
                        pw.printf("r " + sPointer + "\n");
                        pw.flush();
                        int tempStackPointer = getResponseFromMemory(is);
                        pw.printf("w " + sPointer + " " + -1 + "\n");
                        pw.flush();
                        sPointer++;
                        pw.printf("r " + sPointer + "\n");
                        pw.flush();
                        pgrmCounter = getResponseFromMemory(is);
                        pw.printf("w " + sPointer + " " + -1 + "\n");
                        pw.flush();
                        sPointer = tempStackPointer;
                        isKernelMode = false;
                        break;

                    //  End Execution
                    default:
                        //  Ending execution when the End instruction is received
                        sendRequest = false;
                        System.out.println("End instruction received...");
                        System.out.println("Terminating Memory process...");

                        //  Exiting Memory.java process
                        pw.printf("e\n");
                        pw.flush();
                        break;
                }
            }

                //  Elegantly killing child process
            memProc.waitFor();

            System.out.println("Process exited: " + memProc.exitValue());
            System.out.print("Exiting emulator...");
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

        //  Function to check if user is accessing system memory space
    private static void verifyMemoryAccess(int pgrmCounter, boolean isKernelMode, PrintWriter pw){
            //  Checking if the current mode is user and address to access is greater than 999
        if(pgrmCounter > 999 && !isKernelMode){
            System.out.println("Memory Violation: Accessing system address [1000 <-> 1999] in user mode");
            System.out.println("Terminating Memory process...");
            pw.printf("e\n");
            pw.flush();
            System.out.print("Exiting emulator...");
            System.exit(0);
        }
    }
}
