package com.kris.questademo2;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;

import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shawnlin.numberpicker.NumberPicker;

public class Main2Activity extends AppCompatActivity implements DateTimePickerDialog.TimePickerListener{

    private TextView weLog;

    private static Main2Activity CurrentActivity;

    public static Main2Activity getCurrentActivity(){ return CurrentActivity; }

    private LinearLayout addSubQuestView;

    private static CustomExpandableListView mainQuestList;

    private static CustomExpandableListView prevQuestList;

    private static CustomExpandableListView lockedQuestList;

    private ArrayList<DataItem> arCategory = new ArrayList<>();
    private ArrayList<SubCategoryItem> arSubCategory = new ArrayList<>();

    private ArrayList<DataItem> lockedCategory = new ArrayList<>();
    private ArrayList<SubCategoryItem> lockedSubCategory = new ArrayList<>();

    private static ArrayList<HashMap<String, String>> parentItems = new ArrayList<>();
    private static ArrayList<ArrayList<HashMap<String, String>>> childItems =new ArrayList<>();

    private static ArrayList<HashMap<String, String>> prevParentItems = new ArrayList<>();
    private static ArrayList<ArrayList<HashMap<String, String>>> prevChildItems = new ArrayList<>();

    private static ArrayList<HashMap<String, String>> lockedParentItems = new ArrayList<>();
    private static ArrayList<ArrayList<HashMap<String, String>>> lockedChildItems = new ArrayList<>();

    private static MainCategoriesExpandableListAdapter mainEListAdapter;

    private static LockedCategoriesExpandableListAdapter prevEListAdapter;

    private static LockedCategoriesExpandableListAdapter lockedEListAdapter;

    private Button btn_prevQuest;

    private static ProgressBar hpbar;

    private static TextView tv_hp;

    private static ProgressBar xpbar;

    private static TextView tv_xp;

    private static TextView tv_money;

    private static TextView tv_level;

    private static TextView tv_quest;

    private EditText ed_mainQuest;

    protected TextView tv_preAlarm;

    protected TextView tv_postAlarm;

    private static PlayerData playerData;

    private static int focusTime = 0;

    protected String timeTypeHolder;
    private String unlockTimeHolder ="";
    private String dueTimeHolder = "";
    boolean ifLocked,ifDue = false;
    private int ulYear,ulMonth, ulDay,ulHour, ulMin;
    private int duYear,duMonth, duDay,duHour, duMin;
    private DateTimePickerDialog dateTimePickerDialog;



    //store
    protected TextView tv_StoreDialogue;
    protected LinearLayout ll_healthPotionPlus;
    protected LinearLayout ll_healthPotion;
    protected LinearLayout ll_challengeScroll;
    protected NumberPicker numberPicker;
    protected Button btn_deal;
    protected Button btn_cancelDeal;
    protected int i = 0;
    protected int num = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        CurrentActivity = this;

        mainQuestList = findViewById(R.id.questsList);
        prevQuestList = findViewById(R.id.elv_previous_questa);
        lockedQuestList = findViewById(R.id.lockedQuestsList);

        Button btn_option = findViewById(R.id.btn_option);
        btn_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOptionDialog();
            }
        });
        Button btn_store = findViewById(R.id.btn_store);
        btn_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store();
            }
        });

        playerData = new PlayerData(this);

        setFinishOnTouchOutside(false);

            Player player = playerData.readJSON();

            parentItems = player.getMainParentItems();
            childItems = player.getMainChildItems();
            mainEListAdapter = new MainCategoriesExpandableListAdapter(this,parentItems,
                    childItems,false, false,Main2Activity.this);
            mainQuestList.setAdapter(mainEListAdapter);
            mainQuestList.setGroupIndicator(null);
            mainQuestList.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            setupReferences(null);

            prevParentItems = player.getPrevParentItems();
            prevChildItems = player.getPrevChildItems();
            prevEListAdapter = new LockedCategoriesExpandableListAdapter(this,prevParentItems,
                    prevChildItems,Main2Activity.this);
            prevQuestList.setAdapter(prevEListAdapter);
            prevQuestList.setGroupIndicator(null);
            prevQuestList.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);



            lockedParentItems = player.getLockedParentItems();
            lockedChildItems = player.getLockedChildItems();
            lockedEListAdapter = new LockedCategoriesExpandableListAdapter(this,lockedParentItems,
                lockedChildItems,Main2Activity.this);
            lockedQuestList.setAdapter(lockedEListAdapter);
            lockedQuestList.setGroupIndicator(null);
            lockedQuestList.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

            if(lockedParentItems.size() == 0){
                lockedQuestList.setVisibility(View.GONE);
            }

        weLog = findViewById(R.id.welcome_to);
        weLog.setText("Welcome," + player.getName());

        Button addQuest = findViewById(R.id.addQuest);
        addQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddQuest(v);
            }
        });

        tv_money = findViewById(R.id.tv_money);
        tv_money.setText(String.valueOf(player.getMoney()));
        tv_level = findViewById(R.id.tv_level_count);
        tv_level.setText(String.valueOf(player.getLevel()));

        tv_quest = findViewById(R.id.tv_quest_count);
        tv_quest.setText(String.valueOf(parentItems.size()));

        xpbar = findViewById(R.id.pb_exp_bar);
        xpbar.setProgress(0);
        xpbar.setMax(player.getLevel()*100);
        tv_xp = findViewById(R.id.tv_exp_num);
        tv_xp.setText(String.valueOf(xpbar.getProgress()));


        hpbar = findViewById(R.id.pb_health_bar);
        hpbar.setProgress(0);
        hpbar.setMax(player.getHealth());
        tv_hp = findViewById(R.id.tv_hp_num);
        tv_hp.setText(String.valueOf(hpbar.getProgress()));

        prevQuestList = findViewById(R.id.elv_previous_questa);

        lockedQuestList = findViewById(R.id.lockedQuestsList);

        btn_prevQuest = findViewById(R.id.btn_previous_questa);

        btn_prevQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(prevQuestList.getVisibility() == View.INVISIBLE) {
                    btn_prevQuest.setText(R.string.hidepreviousquesta);
                    prevQuestList.setVisibility(View.VISIBLE);
                    setupPrevReference();
                }else{
                    btn_prevQuest.setText(R.string.previousquesta);
                    prevQuestList.setVisibility(View.INVISIBLE);
                }

            }
        });

    }



    public void store(){
        AlertDialog.Builder storeDialogBuilder = new AlertDialog.Builder(Main2Activity.this);
        View storeView = getLayoutInflater().inflate(R.layout.store_dialog,null);
        storeDialogBuilder.setView(storeView);
        final AlertDialog storeDialogue = storeDialogBuilder.create();
        storeDialogue.setCanceledOnTouchOutside(false);
        storeDialogue.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(hpbar.getProgress() != playerData.readJSON().getHealth()){
                    initalBar();
                }
            }
        });
        storeDialogue.show();

        playerData = new PlayerData(Main2Activity.this);
        final String name = playerData.readJSON().getName();
        tv_StoreDialogue = storeView.findViewById(R.id.tv_store_dialogue);
        tv_StoreDialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i <= 11) {
                    tv_StoreDialogue.setText(NPCDialogue.getMythSellerDialogue(i, name));
                    i += 1;
                }else{
                    i = 0;
                }

            }});

        ll_healthPotionPlus = storeView.findViewById(R.id.ll_health_potion_plus);
        ll_healthPotion = storeView.findViewById(R.id.ll_health_potion);
        ll_challengeScroll = storeView.findViewById(R.id.ll_challenge_scroll);

        AlertDialog.Builder purchaseDialogue = new AlertDialog.Builder(Main2Activity.this);
        View purchaseView = getLayoutInflater().inflate(R.layout.alert_purchase, null);

        numberPicker = purchaseView.findViewById(R.id.number_picker);
        btn_cancelDeal = purchaseView.findViewById(R.id.btn_cancel_deal);
        btn_deal = purchaseView.findViewById(R.id.btn_deal);

        purchaseDialogue.setView(purchaseView);
        final AlertDialog dialog = purchaseDialogue.create();

        ll_healthPotionPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                num = numberPicker.getValue();
                btn_deal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Player player = playerData.readJSON();
                        if(player.getMoney() >= 1500) {
                            player.healthInc(false);
                            player.moneyDec(1500);
                            playerData.writeJSON(player);
                            dialog.dismiss();
                        }else{
                            Toast.makeText(Main2Activity.this, "This is not charity. Come back when you can pay me enough!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btn_cancelDeal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


            }
        });

        ll_healthPotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                num = numberPicker.getValue();
                btn_deal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Player player = playerData.readJSON();
                        if(player.getMoney() >= 500) {
                            player.healthInc(false);
                            player.moneyDec(500);
                            playerData.writeJSON(player);
                            dialog.dismiss();
                        }else{
                            Toast.makeText(Main2Activity.this,"That's not enough money!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btn_cancelDeal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        ll_challengeScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main2Activity.this, "These are classified items. Come back later.", Toast.LENGTH_SHORT).show();
            }
        });


        Button btn_pullDown = storeView.findViewById(R.id.btn_pull_down);
        btn_pullDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeDialogue.dismiss();
            }
        });
    }

    public void death(){
        AlertDialog.Builder deathDialogueBuilder = new AlertDialog.Builder(Main2Activity.this);
        View deathView = getLayoutInflater().inflate(R.layout.alert_death, null);
        deathDialogueBuilder.setView(deathView);
        final AlertDialog deathDialogue = deathDialogueBuilder.create();
        deathDialogue.setCanceledOnTouchOutside(false);
        deathDialogue.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                initalBar();
            }
        });
        deathDialogue.show();

        Button btn_revive = deathView.findViewById(R.id.btn_revive);
        Button btn_newChar = deathView.findViewById(R.id.btn_new_character);

        btn_revive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playerData.readJSON().getMoney() >= 5000) {
                    Player rePlayer = playerData.readJSON();
                    rePlayer.setHealth(100);
                    rePlayer.reviveMoney();
                    playerData.writeJSON(rePlayer);
                    deathDialogue.dismiss();

                }
            }
        });

        btn_newChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player newPlayer = playerData.readJSON();
                newPlayer.setHealth(100);
                newPlayer.setExp(0);
                newPlayer.setMoney(100);
                newPlayer.setLevel(0);
                playerData.writeJSON(newPlayer);
                deathDialogue.dismiss();

            }
        });

    }

    public void setAddQuest(View v){
        AlertDialog.Builder addDialogue = new AlertDialog.Builder(Main2Activity.this);
        View addView = getLayoutInflater().inflate(R.layout.activity_dynamic, null);

        addSubQuestView = addView.findViewById(R.id.ll_addView);

        ed_mainQuest = addView.findViewById(R.id.ed_mainQuest);
        addViewItem(null,null);

        Button addButton = addView.findViewById(R.id.btn_getData);
        Button cancelButton = addView.findViewById(R.id.btn_cancel);
        tv_preAlarm = addView.findViewById(R.id.tv_pre_alarm);
        tv_postAlarm = addView.findViewById(R.id.tv_post_alarm);
        ToggleButton btn_preAlarm = addView.findViewById(R.id.btn_pre_alarm);
        ToggleButton btn_postAlarm = addView.findViewById(R.id.btn_post_alarm);

        btn_preAlarm.setChecked(false);
        btn_postAlarm.setChecked(false);
        tv_preAlarm.setAlpha(0.4f);
        tv_postAlarm.setAlpha(0.4f);


        btn_preAlarm.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton butt0onView, boolean isChecked) {
                if (isChecked) {
                    ifLocked = true;
                    tv_preAlarm.setAlpha(1f);
                    setUlTimeDialog();
                    tv_preAlarm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setUlTimeDialog();
                        }
                    });

                } else {
                    ifLocked = false;
                    tv_preAlarm.setAlpha(0.4f);
                }

            }
        }));



        btn_postAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ifDue = true;
                    tv_postAlarm.setAlpha(1f);
                    setDuTimeDialog();
                    tv_postAlarm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setDuTimeDialog();
                        }
                    });

                } else {
                    ifDue = false;
                    tv_postAlarm.setAlpha(0.4f);
                }

            }
        });

        addDialogue.setView(addView);
        final AlertDialog dialog = addDialogue.create();
        dialog.show();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_mainQuest.getText().toString().isEmpty()){
                    Toast.makeText(Main2Activity.this,"Please enter your quest",Toast.LENGTH_SHORT).show();
                }else{

                    ArrayList<String> ed_subQuestList = new ArrayList<>();//temp save subQ

                    for (int i = 0; i < addSubQuestView.getChildCount(); i++) {
                        View childAt = addSubQuestView.getChildAt(i);
                        EditText subQuest = childAt.findViewById(R.id.ed_subQuest);
                        ed_subQuestList.add(subQuest.getText().toString());
                    }

                    if(ifLocked == true && ifDue == false){
                        int requestCode = PlayerData.getID();
                        addLockedQuest(ed_mainQuest.getText().toString(), unlockTimeHolder, requestCode,ed_subQuestList);
                        Quest newQuest = new Quest(requestCode,ulYear,ulMonth, ulDay,ulHour,ulMin,ed_mainQuest.getText().toString(),true,false);
                        setReminderTime(requestCode,newQuest,"unlock");
                        setupLockedReferences();
                    }else if(ifDue == true && ifLocked == false){
                        int requestCode = PlayerData.getID();
                        Quest newQuest = new Quest(requestCode,duYear,duMonth, duDay,duHour,duMin,ed_mainQuest.getText().toString(),false,true);
                        setReminderTime(requestCode,newQuest,"due");
                        addNewQuest(requestCode,ed_mainQuest.getText().toString(), dueTimeHolder, ed_subQuestList, ConstantManager.IS_LTQ_TRUE);
                        setupReferences(null);
                    }else if(ifLocked && ifDue){
                        int requestCode = PlayerData.getID();
                        addLockedQuest(ed_mainQuest.getText().toString(), unlockTimeHolder, requestCode,ed_subQuestList);
                        Quest newQuest = new Quest(requestCode,ulYear,ulMonth, ulDay,ulHour,ulMin,duYear,duMonth,duDay,duHour,duMin,ed_mainQuest.getText().toString());
                        setReminderTime(requestCode,newQuest,"unlock");
                        setupLockedReferences();
                    }else{
                        addNewQuest(null,ed_mainQuest.getText().toString(), "", ed_subQuestList,ConstantManager.IS_LTQ_TRUE);
                        setupReferences(null);
                    }

                    setupQuestCount();
                    dialog.cancel();

                    unlockTimeHolder = "";
                    dueTimeHolder = "";

                    ifLocked = false;
                    ifDue = false;
                }
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    public void setUlTimeDialog(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        timeTypeHolder = "unlock";
        dateTimePickerDialog = new DateTimePickerDialog();
        dateTimePickerDialog.show(ft, "dialog");
    }

    public void setDuTimeDialog(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        timeTypeHolder = "due";
        dateTimePickerDialog = new DateTimePickerDialog();
        dateTimePickerDialog.show(ft, "dialog");
    }

    public void setOptionDialog(){

        AlertDialog.Builder optionDialogueBuilder = new AlertDialog.Builder(Main2Activity.this);
        View optionView = getLayoutInflater().inflate(R.layout.option_dialog, null);
        LinearLayout ll_editName = optionView.findViewById(R.id.ll_edit_name);
        LinearLayout ll_reportBug = optionView.findViewById(R.id.ll_report_bug);
        LinearLayout ll_credit = optionView.findViewById(R.id.ll_credit);
        optionDialogueBuilder.setView(optionView);
        final AlertDialog optionDialog = optionDialogueBuilder.create();
        optionDialog.show();

        ll_editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder editNameDialogue = new AlertDialog.Builder(Main2Activity.this);
                View editNameView = getLayoutInflater().inflate(R.layout.alert_editname,null);
                final EditText ed_newName = editNameView.findViewById(R.id.ed_new_name);

                editNameDialogue.setView(editNameView);
                final AlertDialog dialogEditName = editNameDialogue.create();
                dialogEditName.setCanceledOnTouchOutside(false);
                dialogEditName.show();

                Button btn_newName = editNameView.findViewById(R.id.btn_confirm_name);
                btn_newName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
                        Matcher m = p.matcher(ed_newName.getText().toString());

                        if(ed_newName.getText().toString().length() <= 0){
                            Toast toast = Toast.makeText(Main2Activity.this, "Please enter your name!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,250);
                            toast.show();
                        }
                        else if(ed_newName.getText().toString().length() > 0 && m.matches()) {
                            //transfer given username to the next activity
                            String name = ed_newName.getText().toString();
                            Player player =  playerData.readJSON();
                            player.setName(name);
                            playerData.writeJSON(player);
                            dialogEditName.dismiss();
                            weLog.setText("Welcome, "+player.getName());
                        }else if(!m.matches()){
                            Toast toast =Toast.makeText(Main2Activity.this, "Only numbers and letters are allowed", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,250);
                            toast.show();
                        }

                    }
                });

                Button btn_cancelName = editNameView.findViewById(R.id.btn_cancel_name);
                btn_cancelName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogEditName.dismiss();
                    }
                });
            }
        });

        ll_reportBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder reportDialogue = new AlertDialog.Builder(Main2Activity.this);
                View reportView = getLayoutInflater().inflate(R.layout.alert_report, null);
                final EditText ed_email_title = reportView.findViewById(R.id.ed_email_title);



                final EditText ed_email_body = reportView.findViewById(R.id.ed_email_body);

                Button btn_sendEmail = reportView.findViewById(R.id.btn_send_email);
                Button btn_cancel_send = reportView.findViewById(R.id.btn_cancel_email);

                reportDialogue.setView(reportView);
                final AlertDialog dialogReport = reportDialogue.create();
                dialogReport.setCanceledOnTouchOutside(false);
                dialogReport.show();

                btn_sendEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ed_email_body.getText().toString().isEmpty()){
                            Toast.makeText(Main2Activity.this,"Please enter your report",Toast.LENGTH_SHORT).show();
                        }else{
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("message/rfc822");
                            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"kris_questa@outlook.com"});
                            i.putExtra(Intent.EXTRA_SUBJECT, ed_email_title.getText().toString());
                            i.putExtra(Intent.EXTRA_TEXT   , ed_email_body.getText().toString());
                            try {
                                startActivity(Intent.createChooser(i, "Send mail..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(Main2Activity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                            }
                                dialogReport.dismiss();
                        }
                }});


                btn_cancel_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogReport.dismiss();
                    }
                });
                    }
                });


        ll_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder creditDialogue = new AlertDialog.Builder(Main2Activity.this);
                View creditView = getLayoutInflater().inflate(R.layout.alert_credit, null);
                TextView tv_credit = creditView.findViewById(R.id.tv_credit);
                tv_credit.setMovementMethod(new ScrollingMovementMethod());
                tv_credit.setText(Html.fromHtml("<b>" + "Dear Questa Users," + "</b>" +  "<br />" +
                        "<font size=40sp>" + "&emsp &emsp Thank you so much for using my first ever Android app!" + "</font>" + "<br />" +
                        "<b>" + "Why Beta?" + "</b>"+ "<br />"+
                        "<small>" + "I am still pretty new to either Java or Android Studio and there are a lot more features I want to add on this app that haven’t been done yet.\n So, I released this as a beta version to make the current stage of Questa clear for all users." + "</small>"+ "<br />"
                        +"<b>" + "When is the next update?" + "</b>"+ "<br />"+
                        "<small>" + "Small updates will be here and there to hotfix small issues. All major updates will happen around the end of each month during beta."+"</small>"+ "<br />"
                ));
                creditDialogue.setView(creditView);
                final AlertDialog dialogCredit = creditDialogue.create();
                dialogCredit.setCanceledOnTouchOutside(false);
                dialogCredit.show();
            }
        });


    }



    public void setUnlockTime(int ulYear,int ulMonth,int ulDay,int ulHour,int ulMin){
        this.ulYear = ulYear;
        this.ulMonth = ulMonth;
        this.ulDay = ulDay;
        this.ulHour = ulHour;
        this.ulMin = ulMin;
    }

    public void setDueTime(int duYear,int duMonth,int duDay,int duHour,int duMin){
        this.duYear = duYear;
        this.duMonth = duMonth;
        this.duDay = duDay;
        this.duHour = duHour;
        this.duMin = duMin;
    }

    public void setUnlockTimeHolder(String unlockTimeHolder) {
        this.unlockTimeHolder = unlockTimeHolder;
    }

    public void setDueTimeHolder(String dueTimeHolder) {
        this.dueTimeHolder = dueTimeHolder;
    }

    public static void setupQuestCount(){
        tv_quest.setText(String.valueOf(parentItems.size()));
    }

    public void setEditQuest(String questName, Queue<String> subQuestList, final Integer groupPos){

        final AlertDialog.Builder addDialogue = new AlertDialog.Builder(Main2Activity.this);
        View addView = getLayoutInflater().inflate(R.layout.activity_dynamic, null);

        addSubQuestView = (LinearLayout) addView.findViewById(R.id.ll_addView);

        final EditText mainQuest = (EditText) addView.findViewById(R.id.ed_mainQuest);
        mainQuest.setText(questName);
        addViewItem(null,subQuestList);

        Button addButton = (Button) addView.findViewById(R.id.btn_getData);
        Button cancelButton = (Button) addView.findViewById(R.id.btn_cancel);

        addDialogue.setView(addView);
        final AlertDialog dialog = addDialogue.create();
        dialog.show();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainQuest.getText().toString().isEmpty()){
                    Toast.makeText(Main2Activity.this,"Please enter your quest",Toast.LENGTH_SHORT).show();
                }else{

                    ArrayList<String> ed_subQuestList = new ArrayList<>();//temp save subQ

                    for (int i = 0; i < addSubQuestView.getChildCount(); i++) {
                        View childAt = addSubQuestView.getChildAt(i);
                        EditText subQuest = (EditText) childAt.findViewById(R.id.ed_subQuest);
                        ed_subQuestList.add(subQuest.getText().toString());
                    }

                    mainEListAdapter.notifyDataSetChanged();
                    addNewQuest(null,mainQuest.getText().toString(),null,ed_subQuestList,"false");
                    setupReferences(groupPos);
                    dialog.cancel();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    private void setupReferences(final Integer groupPos) {
        mainQuestList.setGroupIndicator(null);
        mainQuestList.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        for(DataItem data : arCategory){

            ArrayList<HashMap<String, String>> childArrayList =new ArrayList<HashMap<String, String>>();
            HashMap<String, String> mapParent = new HashMap<String, String>();

            mapParent.put(ConstantManager.Parameter.CATEGORY_ID,data.getCategoryId());
            mapParent.put(ConstantManager.Parameter.CATEGORY_NAME,data.getCategoryName());
            mapParent.put(ConstantManager.Parameter.CATEGORY_INFO,data.getCategoryInfo());
            mapParent.put(ConstantManager.Parameter.CATEGORY_ISLTQ, data.getCategoryIsLTQ());


            for(SubCategoryItem subCategoryItem : data.getSubCategory()) {

                HashMap<String, String> mapChild = new HashMap<String, String>();
                mapChild.put(ConstantManager.Parameter.SUB_ID,subCategoryItem.getSubId());
                mapChild.put(ConstantManager.Parameter.SUB_CATEGORY_NAME,subCategoryItem.getSubCategoryName());
                mapChild.put(ConstantManager.Parameter.CATEGORY_ID,subCategoryItem.getCategoryId());
                mapChild.put(ConstantManager.Parameter.IS_CHECKED,subCategoryItem.getIsChecked());

                childArrayList.add(mapChild);
            }

            mapParent.put(ConstantManager.Parameter.IS_CHECKED,data.getIsChecked());

            if(groupPos == null){
                childItems.add(childArrayList);
                parentItems.add(mapParent);
            }else{
                childItems.set(groupPos,childArrayList);
                parentItems.set(groupPos,mapParent);
            }


        }

        ConstantManager.parentItems = parentItems;
        ConstantManager.childItems = childItems;

        mainEListAdapter = new MainCategoriesExpandableListAdapter(this,parentItems,childItems,
                false,false,Main2Activity.this);
        mainQuestList.setAdapter(mainEListAdapter);

        //write into memory
        Player player = playerData.readJSON();
        player.setMainChildItems(childItems);
        player.setMainParentItems(parentItems);
        playerData.writeJSON(player);


        //set Divider
        mainQuestList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(childItems.get(groupPosition).size() == 0){
                    return false;
                }

                if (parent.isGroupExpanded(groupPosition)) {
                    v.findViewById(R.id.view_divider).setVisibility(View.GONE);return false;
                } else {
                    v.findViewById(R.id.view_divider).setVisibility(View.VISIBLE);return false;
                }

            }});

        //Abandon or Edit
        mainQuestList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
                int npos = mainQuestList.pointToPosition((int)view.getX(), (int)view.getY());
                if (npos != AdapterView.INVALID_POSITION) {
                    long pos = mainQuestList.getExpandableListPosition(npos);
                    final int groupPos = ExpandableListView.getPackedPositionGroup(pos);

                    final AlertDialog.Builder deleteDialogue = new AlertDialog.Builder(Main2Activity.this);
                    View deleteView = getLayoutInflater().inflate(R.layout.alert_delete, null);

                    Button btnAbandon = deleteView.findViewById(R.id.btn_abandon);

                    Button btnEdit = deleteView.findViewById(R.id.btn_editQ);
                    deleteDialogue.setView(deleteView);
                    final AlertDialog dialog = deleteDialogue.create();

                    //group long click
                    dialog.show();
                    btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.cancel(); //cancel previous dialogue

                            Queue<String> ed_subQuestList = new LinkedList<String>();

                            for(int i = 0; i < childItems.get(groupPos).size(); i++){
                                ed_subQuestList.offer(childItems.get(groupPos).get(i).get(ConstantManager.Parameter.SUB_CATEGORY_NAME));
                            }

                            NotificationScheduler.cancelReminder(Main2Activity.this, AlarmReceiver.class,
                                    Integer.parseInt(parentItems.get(groupPos).get(ConstantManager.Parameter.CATEGORY_ID)),"");

                            setEditQuest(parentItems.get(groupPos).get(ConstantManager.Parameter.CATEGORY_NAME),ed_subQuestList,groupPos);
                        }
                    });

                    btnAbandon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NotificationScheduler.cancelReminder(Main2Activity.this, AlarmReceiver.class,
                                    Integer.parseInt(parentItems.get(groupPos).get(ConstantManager.Parameter.CATEGORY_ID)),"");
                            parentItems.remove(groupPos);
                            childItems.remove(groupPos);
                            mainEListAdapter.notifyDataSetChanged();
                            questAbandon();
                            dialog.cancel();
                        }
                    });


                }
                return true;
            }
        });
    }

    private ArrayList<DataItem> addNewQuest(Integer requestCode, String mainQuest, String questInfo, ArrayList<String> subQuests, String isLTQ){

            arCategory = new ArrayList<>();
            arSubCategory = new ArrayList<>();

            DataItem dataItem = new DataItem();
            if(requestCode != null){
                dataItem.setCategoryId(String.valueOf(requestCode));
            }else{
                dataItem.setCategoryId(String.valueOf(arCategory.size()));
            }
            dataItem.setCategoryName(mainQuest);
            dataItem.setCategoryInfo(questInfo);
            dataItem.setCategoryIsLTQ(isLTQ);

            subQuests.removeAll(Arrays.asList(null, ""));

            if (subQuests.size() != 0) {
                for (int i = 0; i < subQuests.size(); i++) {

                    SubCategoryItem subCategoryItem = new SubCategoryItem();
                    subCategoryItem.setCategoryId(String.valueOf(i));
                    subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
                    subCategoryItem.setSubCategoryName(subQuests.get(i));
                    arSubCategory.add(subCategoryItem);
                }
            }
            dataItem.setSubCategory(arSubCategory);
            arCategory.add(dataItem);

            return arCategory;

    }

    private ArrayList<DataItem> addLockedQuest(String mainQuest, String questInfo, int requestCode, ArrayList<String> subQuests){

        lockedCategory = new ArrayList<>();
        lockedSubCategory = new ArrayList<>();


        DataItem dataItem = new DataItem();
        dataItem.setCategoryId(String.valueOf(requestCode));
        dataItem.setCategoryName(mainQuest);
        dataItem.setCategoryInfo(questInfo);
        dataItem.setCategoryIsLTQ(ConstantManager.IS_LTQ_FALSE);

        subQuests.removeAll(Arrays.asList(null, ""));

        if (subQuests.size() != 0) {
            for (int i = 0; i < subQuests.size(); i++) {
                SubCategoryItem subCategoryItem = new SubCategoryItem();
                subCategoryItem.setCategoryId(String.valueOf(requestCode));
                subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
                subCategoryItem.setSubCategoryName(subQuests.get(i));
                lockedSubCategory.add(subCategoryItem);
            }
        }
        dataItem.setSubCategory(lockedSubCategory);
        lockedCategory.add(dataItem);

        return lockedCategory;

    }

    private void setupPrevReference(){
        prevQuestList = findViewById(R.id.elv_previous_questa);
        prevQuestList.setGroupIndicator(null);
        prevQuestList.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        prevQuestList.setOnGroupExpandListener(null);

        prevEListAdapter = new LockedCategoriesExpandableListAdapter(this,prevParentItems,
                prevChildItems,Main2Activity.this);
        prevQuestList.setAdapter(prevEListAdapter);

        prevQuestList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
                int npos = prevQuestList.pointToPosition((int)view.getX(), (int)view.getY());
                if (npos != AdapterView.INVALID_POSITION) {
                    long pos = prevQuestList.getExpandableListPosition(npos);
                    final int groupPos = ExpandableListView.getPackedPositionGroup(pos);

                    final AlertDialog.Builder recreateDialogue = new AlertDialog.Builder(Main2Activity.this);
                    View recreateView = getLayoutInflater().inflate(R.layout.alert_recreate, null);

                    Button btnDelete = recreateView.findViewById(R.id.btn_delete);

                    Button btnRecreate = recreateView.findViewById(R.id.btn_recreate);
                    recreateDialogue.setView(recreateView);
                    final AlertDialog dialog = recreateDialogue.create();

                    //group long click
                    dialog.show();
                    btnRecreate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.cancel(); //cancel previous dialogue

                            Queue<String> ed_subQuestList = new LinkedList<String>();

                            for (int i = 0; i < prevChildItems.get(groupPos).size(); i++) {
                                ed_subQuestList.offer(prevChildItems.get(groupPos).get(i).get(ConstantManager.Parameter.SUB_CATEGORY_NAME));
                            }

                            setEditQuest(prevParentItems.get(groupPos).get(ConstantManager.Parameter.CATEGORY_NAME),ed_subQuestList,null);
                        }
                    });

                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            prevParentItems.remove(groupPos);
                            prevChildItems.remove(groupPos);
                            prevEListAdapter.notifyDataSetChanged();
                            dialog.cancel();
                        }
                    });



                }
                return true;
            }
        });




    }

    private void setupLockedReferences() {
        lockedQuestList.setVisibility(View.VISIBLE);
        lockedQuestList.setGroupIndicator(null);
        lockedQuestList.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        lockedQuestList.setOnGroupExpandListener(null);

        for (DataItem data : lockedCategory) {

            ArrayList<HashMap<String, String>> childArrayList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> mapParent = new HashMap<String, String>();

            mapParent.put(ConstantManager.Parameter.CATEGORY_ID, data.getCategoryId());
            mapParent.put(ConstantManager.Parameter.CATEGORY_NAME, data.getCategoryName());
            mapParent.put(ConstantManager.Parameter.CATEGORY_INFO, data.getCategoryInfo());
            mapParent.put(ConstantManager.Parameter.CATEGORY_ISLTQ,data.getCategoryIsLTQ());


            for (SubCategoryItem subCategoryItem : data.getSubCategory()) {

                HashMap<String, String> mapChild = new HashMap<String, String>();
                mapChild.put(ConstantManager.Parameter.SUB_ID, subCategoryItem.getSubId());
                mapChild.put(ConstantManager.Parameter.SUB_CATEGORY_NAME, subCategoryItem.getSubCategoryName());
                mapChild.put(ConstantManager.Parameter.CATEGORY_ID, subCategoryItem.getCategoryId());
                mapChild.put(ConstantManager.Parameter.IS_CHECKED, subCategoryItem.getIsChecked());

                childArrayList.add(mapChild);
            }

            mapParent.put(ConstantManager.Parameter.IS_CHECKED, data.getIsChecked());

            lockedChildItems.add(childArrayList);
            lockedParentItems.add(mapParent);
    }

            lockedEListAdapter = new LockedCategoriesExpandableListAdapter(this, lockedParentItems,
                    lockedChildItems, Main2Activity.this);
            lockedQuestList.setAdapter(lockedEListAdapter);

            lockedQuestList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int npos = lockedQuestList.pointToPosition((int)view.getX(), (int)view.getY());
                    if (npos != AdapterView.INVALID_POSITION) {
                        long pos = lockedQuestList.getExpandableListPosition(npos);
                        final int groupPos = ExpandableListView.getPackedPositionGroup(pos);

                        final AlertDialog.Builder recreateDialogue = new AlertDialog.Builder(Main2Activity.this);
                        View recreateView = getLayoutInflater().inflate(R.layout.alert_recreate, null);

                        Button btnDelete = recreateView.findViewById(R.id.btn_delete);

                        Button btnRecreate = recreateView.findViewById(R.id.btn_recreate);
                        recreateDialogue.setView(recreateView);
                        final AlertDialog dialog = recreateDialogue.create();

                        //group long click
                        dialog.show();
                        btnRecreate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.cancel(); //cancel previous dialogue

                                Queue<String> ed_subQuestList = new LinkedList<String>();

                                for (int i = 0; i < lockedChildItems.get(groupPos).size(); i++) {
                                    ed_subQuestList.offer(lockedChildItems.get(groupPos).get(i).get(ConstantManager.Parameter.SUB_CATEGORY_NAME));
                                }

                                setEditQuest(lockedParentItems.get(groupPos).get(ConstantManager.Parameter.CATEGORY_NAME),ed_subQuestList,null);
                            }
                        });

                        btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NotificationScheduler.cancelReminder(Main2Activity.this, AlarmReceiver.class,
                                        Integer.parseInt(lockedParentItems.get(groupPos).get(ConstantManager.Parameter.CATEGORY_ID)),"");
                                lockedParentItems.remove(groupPos);
                                lockedChildItems.remove(groupPos);
                                lockedEListAdapter.notifyDataSetChanged();
                                dialog.cancel(); }});
            }return true;
                }});

    }

    private void sortSubQuestViewItem() {

        for (int i = 0; i < addSubQuestView.getChildCount(); i++) {
            final View childAt = addSubQuestView.getChildAt(i);
            final Button btn_remove = childAt.findViewById(R.id.btn_addSubQuest);
            btn_remove.setBackground(getDrawable(R.drawable.ic_delete_button));
            btn_remove.setTag("remove");
            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addSubQuestView.removeView(childAt);
                }
            });

            if (i == (addSubQuestView.getChildCount() - 1)) {
                Button btn_add = childAt.findViewById(R.id.btn_addSubQuest);
                btn_add.setBackground(getDrawable(R.drawable.ic_add_button));
                btn_add.setTag("add");
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addViewItem(view, null);
                    }
                });
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            View v = getCurrentFocus();
            if(v instanceof EditText){
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if(!outRect.contains((int)ev.getRawX(),(int)ev.getRawY())){
                    v.clearFocus();
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void addViewItem(View view, Queue<String> subQuestList) {
        if (subQuestList ==null) {
            if (addSubQuestView.getChildCount() == 0) {
                View subQuestView = View.inflate(this, R.layout.item_subquest_list, null);
                Button btn_add = subQuestView.findViewById(R.id.btn_addSubQuest);
                btn_add.setBackground(getDrawable(R.drawable.ic_add_button));
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addViewItem(view, null);
                    }
                });
                addSubQuestView.addView(subQuestView);

            } else {
                View subQuestView = View.inflate(this, R.layout.item_subquest_list, null);
                addSubQuestView.addView(subQuestView);
                sortSubQuestViewItem();
            }
        }else{
            View subQuestView = View.inflate(this, R.layout.item_subquest_list,null);
            EditText ed_subQ = subQuestView.findViewById(R.id.ed_subQuest);
            ed_subQ.setText(subQuestList.poll());
            addSubQuestView.addView(subQuestView);

            if(subQuestList.size() != 0){
                addViewItem(view,subQuestList);
            }else{
                addViewItem(view,null);
            }

        }
    }

    public static void animateTextView(int initialValue, int finalValue, final TextView  textview) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                textview.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        valueAnimator.start();
    }

    public static void questDone(HashMap<String, String> parentItem,ArrayList<HashMap<String, String>> childItem,String isLTQ) {
        boolean LTQ = Boolean.valueOf(isLTQ);
        prevParentItems.add(parentItem);
        prevChildItems.add(childItem);
        if(prevEListAdapter != null){
        prevEListAdapter.notifyDataSetChanged();}

        Player player = playerData.readJSON();
        player.moneyInc(LTQ);
        player.expInc(LTQ);

        int diff;
        if(xpbar.getMax() <= player.getExp()){
            diff = player.getExp() - xpbar.getMax();
            xpbar.setMax(player.getLevel()*100);
            player.setExp(diff);
            xpbar.setProgress(0);
            tv_level.setText(String.valueOf(player.levelInc()));
            ProgressBarAnimation anim = new ProgressBarAnimation(xpbar, xpbar.getProgress(), player.getExp());
            anim.setDuration(300);
            xpbar.startAnimation(anim);
            xpbar.setProgress(player.getExp());
            animateTextView(0,player.getExp(),tv_xp);
            playerData.writeJSON(player);
        }else{
            ProgressBarAnimation anim = new ProgressBarAnimation(xpbar, xpbar.getProgress(), player.getExp());
            anim.setDuration(300);
            xpbar.startAnimation(anim);
            xpbar.setProgress(player.getExp());
            animateTextView(Integer.parseInt(tv_xp.getText().toString()),player.getExp(),tv_xp);
            playerData.writeJSON(player);
        }

        setupQuestCount();
        animateTextView(Integer.parseInt(tv_money.getText().toString()),player.getMoney(),tv_money);
    }


    public static void questAbandon(){
        Player player = playerData.readJSON();
        player.healthDec();
        playerData.writeJSON(player);

        animateTextView(hpbar.getProgress(),player.getHealth(),tv_hp);

        ProgressBarAnimation anim = new ProgressBarAnimation(hpbar, hpbar.getProgress(), player.getHealth());
        anim.setDuration(1000);
        hpbar.startAnimation(anim);
        hpbar.setProgress(player.getHealth());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CurrentActivity = null;
    }

    public String getFormatedTime(int h, int m) {
        final String OLD_FORMAT = "HH:mm";
        final String NEW_FORMAT = "hh:mm a";

        String oldDateString = h + ":" + m;
        String newDateString = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.US);
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDateString;
    }

    public String getFormatedDate(int m, int day) {
        final String OLD_FORMAT = "MM/dd";
        final String NEW_FORMAT = "MMM.dd";

        String oldDateString = m + "/" + day;
        String newDateString = m + "." + day;


        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDateString;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            return getResources().getConfiguration().locale;
        }
    }

    @Override
    public void setReminderTime(int requestCode,Quest newQuest,String type) {
        Player player = playerData.readJSON();
        player.saveNewNotification(requestCode,newQuest);
        playerData.writeJSON(player);
        if (type.equals("due")) {
            NotificationScheduler.setReminder(Main2Activity.this, AlarmReceiver.class,
                    player.getDuTime(requestCode),requestCode,newQuest.getDetail(),true);
        }else{
            NotificationScheduler.setReminder(Main2Activity.this, AlarmReceiver.class,
                    player.getUlTime(requestCode),requestCode,newQuest.getDetail(),false);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
        focusTime = 0;

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //retrieve notification data
        int ID = this.getIntent().getIntExtra("requestCode",999);
        String type = this.getIntent().getStringExtra("type");

        if (ID != 999){
            if(type.equals("due")){
                questDue(ID);
            }else if(type.equals("unlock")){
                questUnlock(ID);
            }
        }

        this.getIntent().removeExtra("requestCode");
        this.getIntent().removeExtra("type");

    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        Handler handler = new Handler();

        if(focusTime == 0) {
            handler.postDelayed(new Runnable() {
                public void run() {
                    initalBar();
                }
            }, 1000);

            focusTime += 1;
        }

        if(playerData.readJSON().isIfdead()) {
            Player player = playerData.readJSON();
            player.setIfdead(false);
            playerData.writeJSON(player);
            handler.postDelayed(new Runnable() {
                public void run() {
                    death();
                }
            }, 1200);

        }

    }

    public void initalBar(){
        ProgressBarAnimation anim = new ProgressBarAnimation(hpbar, 0, playerData.readJSON().getHealth());
        anim.setDuration(1000);
        hpbar.startAnimation(anim);

        ProgressBarAnimation anim2 = new ProgressBarAnimation(xpbar, 0, playerData.readJSON().getExp());
        anim2.setDuration(1000);
        xpbar.startAnimation(anim2);

        animateTextView(0,playerData.readJSON().getMoney(),tv_money);
        animateTextView(0,playerData.readJSON().getHealth(),tv_hp);
        animateTextView(0,playerData.readJSON().getExp(),tv_xp);
        animateTextView(0,playerData.readJSON().getLevel(),tv_level);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Player player = playerData.readJSON();
        player.setMainParentItems(parentItems);
        player.setMainChildItems(childItems);
        player.setPrevChildItems(prevChildItems);
        player.setPrevParentItems(prevParentItems);
        player.setLockedParentItems(lockedParentItems);
        player.setLockedChildItems(lockedChildItems);
        playerData.writeJSON(player);
    }

    public static void questUnlock(int requestCode) {

        Player player = playerData.readJSON();
        String RC = String.valueOf(requestCode);
        for(HashMap<String, String> item:lockedParentItems) {
            if (item.get(ConstantManager.Parameter.CATEGORY_ID).equals(RC)) {
                if(player.getDue(requestCode)){
                    NotificationScheduler.setReminder(getCurrentActivity(), AlarmReceiver.class,
                            player.getDuTime(requestCode),requestCode,player.getDetail(requestCode),true);
                }
                int ID = lockedParentItems.indexOf(item);
                parentItems.add(lockedParentItems.get(ID));
                childItems.add(lockedChildItems.get(ID));
                mainEListAdapter.notifyDataSetChanged();

                lockedParentItems.remove(ID);
                lockedChildItems.remove(ID);
                lockedEListAdapter.notifyDataSetChanged();

                player.setMainParentItems(parentItems);
                player.setMainChildItems(childItems);
                player.setLockedParentItems(lockedParentItems);
                player.setLockedChildItems(lockedChildItems);
                playerData.writeJSON(player);
            }
        }
    }

    public static void questDue(int requestCode){
        String RC = String.valueOf(requestCode);
        for(int i = 0; i<parentItems.size(); i++) {
            HashMap<String, String> item = parentItems.get(i);
            if (item.get(ConstantManager.Parameter.CATEGORY_ID).equals(RC)) {
                if (item.get(ConstantManager.Parameter.IS_CHECKED).equals(ConstantManager.CHECK_BOX_CHECKED_FALSE)) {
                    Player player = playerData.readJSON();
                    player.healthDec();
                    playerData.writeJSON(player);
                    ProgressBarAnimation anim = new ProgressBarAnimation(hpbar, hpbar.getProgress(), player.getHealth());
                    anim.setDuration(1000);
                    hpbar.startAnimation(anim);
                    hpbar.setProgress(player.getHealth());
                    animateTextView(hpbar.getProgress(),playerData.readJSON().getHealth(),tv_hp);

                }
            }
        }

    }

    @Override
    public void onBackPressed() { }

}












