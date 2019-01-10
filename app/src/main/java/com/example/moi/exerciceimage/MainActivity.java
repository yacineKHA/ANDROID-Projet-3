package com.example.moi.exerciceimage;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.bt_history)
    ImageView bt_history;
    @BindView(R.id.bt_addNote)
    ImageView bt_addNote;
    @BindView(R.id.mybackground)
    ConstraintLayout mybackground;
    @BindView(R.id.btShare)
    Button btShare;
    int[] mood = new int[]{
            R.drawable.smiley_sad,
            R.drawable.smiley_disappointed,
            R.drawable.smiley_normal,
            R.drawable.smiley_happy,
            R.drawable.smiley_super_happy
    };
    int[] backgroundcolor = new int[]{
            R.color.faded_red,
            R.color.warm_grey,
            R.color.cornflower_blue_65,
            R.color.light_sage,
            R.color.banana_yellow,
            R.color.gris_perle,
    };
    int currentsmile = 2;
    int currentbackgroundcolor = 2;

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEdit;
    Button bt_cancel;
    Button bt_ok;
    EditText et_comment;
    String comment;
    TextView tv_comment;
    String tempsactuel;
    String dateKey;
    Dialog mydialog;
    String getCurrentDateTime;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mydialog = new Dialog(this);
        mydialog.setContentView(R.layout.activity_image2);
        tempsactuel = getCurrentDateTime;
        bt_cancel = mydialog.findViewById(R.id.bt_cancel);
        bt_ok = mydialog.findViewById(R.id.bt_ok);
        et_comment = mydialog.findViewById(R.id.et_comment);
        tv_comment = mydialog.findViewById(R.id.tv_comment);

        //date key
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        Date today = new Date();
        dateKey = dateFormat.format(today);

        loadPreferences();

        mybackground.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeDown() {
                smileDown();
                savePreferences();
            }

            @Override
            public void onSwipeUp() {
                smileUp();
                savePreferences();
            }
        });
    }

    @OnClick(R.id.btShare)
    public void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @OnClick(R.id.bt_addNote)
    public void note() {
        mydialog.show();
        bt_cancel.setOnClickListener(v -> mydialog.cancel());
        bt_ok.setOnClickListener(v -> {
            // save comment
            comment = et_comment.getText().toString();
            mPref = getSharedPreferences("preferences", MODE_PRIVATE);
            mEdit = mPref.edit();
            mEdit.putString("comment" + dateKey, comment);
            mEdit.apply();
            mydialog.cancel();
        });
    }

    @OnClick(R.id.bt_history)
    public void history() {

        Intent historyIntent = new Intent(this, HistoricActivity.class);
        startActivity(historyIntent);
    }

    public void savePreferences() {
        mPref = getSharedPreferences("preferences", MODE_PRIVATE);
        mEdit = mPref.edit();
        mEdit.putInt("smilevalue" + dateKey, currentsmile);
        mEdit.putInt("backcolorvalue" + dateKey, currentbackgroundcolor);
        mEdit.apply();
    }

    public void loadPreferences() {
        mPref = getSharedPreferences("preferences", MODE_PRIVATE);
        currentsmile = mPref.getInt("smilevalue" + dateKey, 3);
        image1.setImageResource(mood[currentsmile]);
        currentbackgroundcolor = mPref.getInt("backcolorvalue" + dateKey, 3);
        mybackground.setBackgroundResource(backgroundcolor[currentbackgroundcolor]);
    }

    public void setCurrentbackgroundcolor() {
        mybackground.setBackgroundResource(backgroundcolor[currentbackgroundcolor]);
    }

    public void setCurrentsmile() {
        image1.setImageResource(mood[currentsmile]);
    }

    public void smileUp() {
        if (currentsmile == 4 && currentbackgroundcolor == 4) {
            Toast.makeText(this, "êtes vous vraiment si heureux", Toast.LENGTH_SHORT).show();
            return;
        }
        currentbackgroundcolor++;
        currentsmile++;
        setCurrentsmile();
        setCurrentbackgroundcolor();
    }

    public void smileDown() {
        if (currentsmile == 0 && currentbackgroundcolor == 0) {
            Toast.makeText(this, "êtes vous vraiment si triste", Toast.LENGTH_SHORT).show();
            return;
        }
        currentsmile--;
        currentbackgroundcolor--;
        setCurrentbackgroundcolor();
        setCurrentsmile();
    }
}


