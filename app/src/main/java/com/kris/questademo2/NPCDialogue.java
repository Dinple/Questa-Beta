package com.kris.questademo2;

import java.util.ArrayList;

public class NPCDialogue {
    private static final String MythSeller = "MS";

    public static String getMythSellerDialogue(int i, final String name) {
        String dialogue ="...";
        ArrayList<String> dialogueList = new ArrayList<String>(){{
            add("Seems like you discovered my store,"+name);
            add("Well,don't stare at me then. Go pick up something...");
            add("I am just a random seller,you can call me Strenger.");
            add("Hope you find everything you want today...");
            add("Maybe not everything, since you come early...");
            add("...");
            add("...");
            add("...");
            add("My Crystal ball is not for sale...");
            add("...");
            add("...");
            add("...");
        }};

        dialogue = dialogueList.get(i);
        return dialogue;
    }
}
