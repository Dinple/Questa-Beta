package com.kris.questademo2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText usrName;

    private Button chkMark;

    private ImageView diamond;

    private ImageView upCrown;

    private ImageView downCrown;

    private TextView welcome;

    private TextView slogan;

    private int clickTime = 0;



    private TextWatcher editclick = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String input = usrName.getText().toString();
            Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
            Matcher m = p.matcher(input);
            if (m.matches()) {

            } else {
//                Toast toast = Toast.makeText(MainActivity.this, "Only numbers and letters are allowed", Toast.LENGTH_SHORT);
//                toast.show();

            }
        }


        @Override
        public void afterTextChanged(Editable s) {


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlayerData playerData = new PlayerData(this);
        if(playerData.readJSON()!=null){
            Player player = playerData.readJSON();
            Intent i=new Intent(MainActivity.this,Main2Activity.class);
            i.putExtra("userName",player.getName());
            this.startActivity(i);
            this.finishActivity(0);
        }else{
            setContentView(R.layout.activity_main);
            ButterKnife.inject(this);

            chkMark = findViewById(R.id.checkbox);
            chkMark.setOnClickListener(MainActivity.this);

            usrName = findViewById(R.id.userName);
            usrName.addTextChangedListener(editclick);


            diamond = findViewById(R.id.diamond);

            upCrown = findViewById(R.id.upward_crown);

            downCrown = findViewById(R.id.downward_crown);

            welcome = findViewById(R.id.welcome_to);

            slogan = findViewById(R.id.slogan);
        }}


    @Override
    public void onClick(View view) {
        if(clickTime == 0) {
            AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
            usrName.startAnimation(alphaAnimation);
            diamond.startAnimation(alphaAnimation);
            upCrown.startAnimation(alphaAnimation);
            downCrown.startAnimation(alphaAnimation);
            slogan.startAnimation(alphaAnimation);
            welcome.startAnimation(alphaAnimation);
            usrName.setVisibility(View.VISIBLE);
            clickTime ++;

        }else{

            Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
            Matcher m = p.matcher(usrName.getText().toString());

            if(usrName.getText().toString().length() <= 0){
                Toast toast = Toast.makeText(MainActivity.this, "Please enter your name!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,250);
                toast.show();
            }
            else if(usrName.getText().toString().length() > 0 && m.matches()) {
                //transfer given username to the next activity
                String name = usrName.getText().toString();
                PlayerData playerData = new PlayerData(this);
                Player player = new Player(name);
                playerData.writeJSON(player);
                Intent i = new Intent(MainActivity.this,Main2Activity.class);
                i.putExtra("userName",player.getName());
                startActivity(i);

                Toast toast = Toast.makeText(MainActivity.this, "Welcome to Questa!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,250);
                toast.show();
            }else if(!m.matches()){
                Toast toast =Toast.makeText(MainActivity.this, "Only numbers and letters are allowed", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,250);
                toast.show();
            }
            }
    }

    @Override
    public void onBackPressed() { }


}
