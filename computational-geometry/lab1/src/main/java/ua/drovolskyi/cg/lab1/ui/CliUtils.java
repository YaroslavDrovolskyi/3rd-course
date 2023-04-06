package ua.drovolskyi.cg.lab1.ui;

import ua.drovolskyi.cg.lab1.localizer.Chain;

import java.io.IOException;

public class CliUtils {
    // really wait for user's key or not
    private static boolean waitForKey = true;
    public static void printChains(Chain[] chains){
        for(Chain c : chains){
            System.out.println(c);
        }
    }

    public static void waitForKey(){
        System.out.println("Press any key to go to the next step");

        if(waitForKey){
            try {
                System.in.read();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public static void setWaitingForKey(boolean option){
        waitForKey = option;
    }
}
