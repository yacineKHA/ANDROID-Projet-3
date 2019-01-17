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
import java.util.Locale;

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
    int[] backgroundColor = new int[]{
            R.color.faded_red,
            R.color.warm_grey,
            R.color.cornflower_blue_65,
            R.color.light_sage,
            R.color.banana_yellow,
            R.color.gris_perle,
    };
    String[] stringMoodList = new String[]{
            "très mauvaise humeur :(",
            "plutôt mauvaise humeur :/",
            "neutre :|",
            "plutôt joyeux :)",
            "très joyeux :D"
    };
    int currentSmile = 2;
    int currentBackgroundColor = 2;

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEdit;
    Button bt_cancel;
    Button bt_ok;
    EditText et_comment;
    String comment;
    TextView tv_comment;
    String dateKey;
    Dialog myDialog;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.activity_image2);
        bt_cancel = myDialog.findViewById(R.id.bt_cancel);
        bt_ok = myDialog.findViewById(R.id.bt_ok);
        et_comment = myDialog.findViewById(R.id.et_comment);
        tv_comment = myDialog.findViewById(R.id.tv_comment);

        //date key
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.FRANCE);
        Date today = new Date();
        dateKey = dateFormat.format(today);
        mPref = getSharedPreferences("preferences", MODE_PRIVATE);
        loadPreferences();
        shareButtonColor();

        mybackground.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeDown() {
                smileDown();
                savePreferences();
                shareButtonColor();
            }

            @Override
            public void onSwipeUp() {
                smileUp();
                savePreferences();
                shareButtonColor();
            }
        });
    }

    /**
     * share
     * Method for get the mood of the day and share it
     */
    @OnClick(R.id.btShare)
    public void share() {
        String sharedMood = "";
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        currentSmile = mPref.getInt("smilevalue" + dateKey, 3);
        for (int i = 0; i < 5; i++) {
            if (currentSmile == i) {
                sharedMood = stringMoodList[i];
            }
        }
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Voici mon humeur du jour: " + sharedMood);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    /**
     * note
     * Method to display a dialog window
     * that integrates a save preferences for save the comment
     */
    @OnClick(R.id.bt_addNote)
    public void note() {
        myDialog.show();
        bt_cancel.setOnClickListener(v -> myDialog.cancel());
        bt_ok.setOnClickListener(v -> {
            // save comment
            comment = et_comment.getText().toString();
            mEdit = mPref.edit();
            mEdit.putString("comment" + dateKey, comment);
            mEdit.apply();
            myDialog.cancel();
        });
    }

    /**
     * history
     * Method intent for start a new activity (HistoricActivity)
     */
    @OnClick(R.id.bt_history)
    public void history() {
        Intent historyIntent = new Intent(this, HistoricActivity.class);
        startActivity(historyIntent);
    }

    /**
     * savePreferences
     * Method for save the smilevalue and colorvalue to the preferences
     */
    public void savePreferences() {
        mPref = getSharedPreferences("preferences", MODE_PRIVATE);
        mEdit = mPref.edit();
        mEdit.putInt("smilevalue" + dateKey, currentSmile);
        mEdit.putInt("backcolorvalue" + dateKey, currentBackgroundColor);
        mEdit.apply();
    }

    /**
     * loadPreferences
     * Method for load the smilevalue and colorvalue that are saved in shared preferences
     */
    public void loadPreferences() {
        currentSmile = mPref.getInt("smilevalue" + dateKey, 3);
        image1.setImageResource(mood[currentSmile]);
        currentBackgroundColor = mPref.getInt("backcolorvalue" + dateKey, 3);
        mybackground.setBackgroundResource(backgroundColor[currentBackgroundColor]);
    }

    public void setCurrentbackgroundcolor() {
        mybackground.setBackgroundResource(backgroundColor[currentBackgroundColor]);
    }

    public void setCurrentsmile() {
        image1.setImageResource(mood[currentSmile]);
    }

    /**
     * smileUp
     * Method for slide up (mood), synchronise mood with background's color
     */
    public void smileUp() {
        if (currentSmile == 4 && currentBackgroundColor == 4) {
            Toast.makeText(this, "Êtes-vous vraiment si heureux que ça ?", Toast.LENGTH_SHORT).show();
            return;
        }
        currentBackgroundColor++;
        currentSmile++;
        setCurrentsmile();
        setCurrentbackgroundcolor();
    }

    /**
     * smileDown
     * Method for slide down (mood), synchronise the mood with the background's color
     */
    public void smileDown() {
        if (currentSmile == 0 && currentBackgroundColor == 0) {
            Toast.makeText(this, "Êtes-vous vraiment si triste que ça ?", Toast.LENGTH_SHORT).show();
            return;
        }
        currentSmile--;
        currentBackgroundColor--;
        setCurrentbackgroundcolor();
        setCurrentsmile();
    }

    /**
     * shareButtonColor
     * Method for synchronise the button's color with the background's color
     */
    public void shareButtonColor() {
        for (int i = 0; i < 5; i++) {
            if (currentBackgroundColor == i) {
                btShare.setBackgroundResource(backgroundColor[i]);
            }
        }
    }
}


