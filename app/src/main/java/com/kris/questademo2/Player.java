package com.kris.questademo2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Player implements Serializable{
    private String name;
    private int health;
    private int exp;
    private int money;
    private int level;
    private boolean ifdead;
    private ArrayList<HashMap<String, String>> mainParentItems;
    private ArrayList<ArrayList<HashMap<String, String>>> mainChildItems;
    private ArrayList<HashMap<String, String>> prevParentItems;
    private ArrayList<ArrayList<HashMap<String, String>>> prevChildItems;
    private ArrayList<HashMap<String, String>> lockedParentItems;
    private ArrayList<ArrayList<HashMap<String, String>>> lockedChildItems;
    private HashMap<Integer, Quest> NOTIFICATION_QUEUE;

    public Player(String name){
        this.name = name;
        initialize();
    }

    public void initialize(){
        health = 100;
        exp = 0;
        money = 100;
        level = 1;
        ifdead = false;
        mainParentItems = new ArrayList<>();
        mainChildItems =new ArrayList<>();
        prevParentItems = new ArrayList<>();
        prevChildItems = new ArrayList<>();
        lockedParentItems = new ArrayList<>();
        lockedChildItems = new ArrayList<>();
        NOTIFICATION_QUEUE = new HashMap<>();
    }

    public void saveNewNotification(int ID, Quest quest){NOTIFICATION_QUEUE.put(ID,quest); }


    public void healthDec(){
        health -= 20;

        if(health <= 0){
            setIfdead(true);
        }

    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void healthInc(boolean ifBig) {
        if(ifBig){
            health += 100;
        }else{
            health += 20;
        }
    }

    public void moneyDec(int amount){
        money -= amount;
    }

    public void expInc(boolean isLTQ){
        if(isLTQ){
            exp += 50;
        }else{
            exp += 5;
        }
    }

    public Calendar getUlTime(int ID){
        Calendar ulTime =  NOTIFICATION_QUEUE.get(ID).getUlTime();
        return ulTime;
    }

    public Calendar getDuTime(int ID){
        Calendar duTime =  NOTIFICATION_QUEUE.get(ID).getDuTime();
        return duTime;
    }

    public String getDetail(int ID){
        String detail = NOTIFICATION_QUEUE.get(ID).getDetail();
        return detail;
    }

    public boolean getDue(int ID){
        boolean ifDue = NOTIFICATION_QUEUE.get(ID).isDue();
        return ifDue;
    }
    public boolean getUnlock(int ID){
        boolean ifLock = NOTIFICATION_QUEUE.get(ID).isLock();
        return ifLock;
    }

    public Quest getQuest(int ID){
        return NOTIFICATION_QUEUE.get(ID);
    }

    public void moneyInc(boolean isLTQ){
        if(isLTQ){
            money += 100;
        }else{
            money += 20;
        }
    }

    public boolean isIfdead() {
        return ifdead;
    }

    public void setIfdead(boolean ifdead) {
        this.ifdead = ifdead;
    }

    public int getMoney() {
        return money;
    }

    public int getHealth() {
        return health;
    }

    public int getExp() {
        return exp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public int levelInc(){
        level ++;
        return level;
    }

    public void reviveMoney() {
        this.money -= 5000;
    }

    public int getLevel() {
        return level;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public HashMap<Integer, Quest> getNOTIFICATION_QUEUE() {
        return NOTIFICATION_QUEUE;
    }

    public ArrayList<ArrayList<HashMap<String, String>>> getMainChildItems() {
        return mainChildItems;
    }

    public ArrayList<HashMap<String, String>> getMainParentItems() {
        return mainParentItems;
    }

    public void setMainChildItems(ArrayList<ArrayList<HashMap<String, String>>> mainChildItems) {
        this.mainChildItems = mainChildItems;
    }

    public void setMainParentItems(ArrayList<HashMap<String, String>> mainParentItems) {
        this.mainParentItems = mainParentItems;
    }

    public ArrayList<ArrayList<HashMap<String, String>>> getPrevChildItems() {
        return prevChildItems;
    }

    public ArrayList<HashMap<String, String>> getPrevParentItems() {
        return prevParentItems;
    }

    public void setPrevChildItems(ArrayList<ArrayList<HashMap<String, String>>> prevChildItems) {
        this.prevChildItems = prevChildItems;
    }

    public void addPrevChildItem(ArrayList<HashMap<String, String>> prevChildItem){
        prevChildItems.add(prevChildItem);
    }

    public void setPrevParentItems(ArrayList<HashMap<String, String>> prevParentItems) {
        this.prevParentItems = prevParentItems;
    }

    public void addPrevParentItem(HashMap<String, String> prevParentItem){
        prevParentItems.add(prevParentItem);
    }

    public void removeLockedParentItem(int ID){
        lockedParentItems.remove(ID);
    }

    public void removeLockedChildItem(int ID){
        lockedChildItems.remove(ID);
    }

    public void setLockedParentItems(ArrayList<HashMap<String, String>> lockedParentItems){
        this.lockedParentItems = lockedParentItems;
    }

    public void setLockedChildItems(ArrayList<ArrayList<HashMap<String, String>>> lockedChildItems) {
        this.lockedChildItems = lockedChildItems;
    }

    public void addLockedParentItems(HashMap<String, String> lockedParentItem){
        lockedParentItems.add(lockedParentItem);
    }

    public void addLockedChildItems(ArrayList<HashMap<String, String>> lockedChildItem){
        lockedChildItems.add(lockedChildItem);
    }

    public ArrayList<ArrayList<HashMap<String, String>>> getLockedChildItems() {
        return lockedChildItems;
    }

    public ArrayList<HashMap<String, String>> getLockedParentItems() {
        return lockedParentItems;
    }
}
