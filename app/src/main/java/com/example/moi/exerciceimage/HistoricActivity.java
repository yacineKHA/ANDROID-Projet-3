package com.example.moi.exerciceimage;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.OnClick;

public class HistoricActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ll_1, ll_2, ll_3, ll_4, ll_5, ll_6, ll_7, currentLayout;
    Date today;
    SimpleDateFormat dateFormat;
    Calendar c, cal;
    String yesterday;
    int[] backgroundcolor = new int[]{
            R.color.faded_red,
            R.color.warm_grey,
            R.color.cornflower_blue_65,
            R.color.light_sage,
            R.color.banana_yellow,
            R.color.gris_perle,
    };
    int[] linear = new int[]{
            R.id.ll_1,
            R.id.ll_2,
            R.id.ll_3,
            R.id.ll_4,
            R.id.ll_5,
            R.id.ll_6,
            R.id.ll_7
    };
    int[] icons = new int[]{
            R.id.icon1,
            R.id.icon2,
            R.id.icon3,
            R.id.icon4,
            R.id.icon5,
            R.id.icon6,
            R.id.icon7
    };

    ImageView icon1, icon2, icon3, icon4, icon5, icon6, icon7;
    int getBackgroundColor;
    String getHistoricComment;
    SharedPreferences mPref;
//    TextView tvYesterday, tvBefore, tvThreeDays, tvFourDays, tvFiveDays, tvSixDays, tvOneweek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historic_layout);

        ll_1 = findViewById(R.id.ll_1);
        ll_2 = findViewById(R.id.ll_2);
        ll_3 = findViewById(R.id.ll_3);
        ll_4 = findViewById(R.id.ll_4);
        ll_5 = findViewById(R.id.ll_5);
        ll_6 = findViewById(R.id.ll_6);
        ll_7 = findViewById(R.id.ll_7);
        ll_1.setOnClickListener(this);
        ll_2.setOnClickListener(this);
        ll_3.setOnClickListener(this);
        ll_4.setOnClickListener(this);
        ll_5.setOnClickListener(this);
        ll_6.setOnClickListener(this);
        ll_7.setOnClickListener(this);
        icon1 = findViewById(R.id.icon1);
        icon2 = findViewById(R.id.icon2);
        icon3 = findViewById(R.id.icon3);
        icon4 = findViewById(R.id.icon4);
        icon5 = findViewById(R.id.icon5);
        icon6 = findViewById(R.id.icon6);
        icon7 = findViewById(R.id.icon7);

        dateFormat = new SimpleDateFormat("ddMMyyyy");
        today = new Date();
        c = Calendar.getInstance();
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        soustraire();
        setVisibleIcons();

    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < 7; i++) {
            LinearLayout currentLayout = findViewById(linear[i]);
            if (v == currentLayout) {
                getComments(i);
                refreshDate(i);
            }
        }
    }

    /**
     * @param i
     */
    public void refreshDate(int i) {
        Date today = cal.getTime();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, i);
    }

    public void getComments(int i) {
        Date today = cal.getTime();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -i);
        Date yesterDayDate;
        yesterDayDate = cal.getTime();
        yesterday = dateFormat.format(yesterDayDate);
        mPref = getSharedPreferences("preferences", MODE_PRIVATE);
        getHistoricComment = mPref.getString("comment" + yesterday, null);
        if (getHistoricComment == null) {
            Toast.makeText(HistoricActivity.this, " " + yesterday, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(HistoricActivity.this, getHistoricComment + yesterday, Toast.LENGTH_SHORT).show();
        }

    }

    public void setVisibleIcons() {
        Date today = cal.getTime();
        cal.setTime(today);
        for (int i = 0; i < 7; i++) {
            cal.add(Calendar.DAY_OF_MONTH, -i);
            Date yesterDayDate;
            yesterDayDate = cal.getTime();
            yesterday = dateFormat.format(yesterDayDate);
            mPref = getSharedPreferences("preferences", MODE_PRIVATE);
            getHistoricComment = mPref.getString("comment" + yesterday, null);
            if (getHistoricComment == null) {
                ImageView setIcons = findViewById(icons[i]);
                setIcons.setVisibility(View.INVISIBLE);
            }
            refreshDate(i);
        }
    }

    public void soustraire() {
        Date yester;
        c.add(Calendar.DAY_OF_MONTH, -1);
        yester = c.getTime();
        for (int i = 0; i < 7; i++) {
            c.setTime(yester);
            c.add(Calendar.DAY_OF_MONTH, -i);
            Date yesterDayDate = c.getTime();
            yesterday = dateFormat.format(yesterDayDate);
            mPref = getSharedPreferences("preferences", MODE_PRIVATE);
            getBackgroundColor = mPref.getInt("backcolorvalue" + yesterday, 5);
            LinearLayout currentLayout = findViewById(linear[i]);
            currentLayout.setBackgroundResource(backgroundcolor[getBackgroundColor]);
            if (getBackgroundColor == 5) {
                currentLayout.setVisibility(View.INVISIBLE);
            }
            if (getBackgroundColor == 0) {
                LinearLayout.LayoutParams dimensions = (LinearLayout.LayoutParams) currentLayout.getLayoutParams();
                dimensions.width = dpToPixel(80);
                currentLayout.setLayoutParams(dimensions);
            }
            if (getBackgroundColor == 1) {
                LinearLayout.LayoutParams dimensions = (LinearLayout.LayoutParams) currentLayout.getLayoutParams();
                dimensions.width = dpToPixel(130);
                currentLayout.setLayoutParams(dimensions);
            }
            if (getBackgroundColor == 2) {
                LinearLayout.LayoutParams dimensions = (LinearLayout.LayoutParams) currentLayout.getLayoutParams();
                dimensions.width = dpToPixel(180);
                currentLayout.setLayoutParams(dimensions);
            }
            if (getBackgroundColor == 3) {
                LinearLayout.LayoutParams dimensions = (LinearLayout.LayoutParams) currentLayout.getLayoutParams();
                dimensions.width = dpToPixel(270);
                currentLayout.setLayoutParams(dimensions);
            }
        }
    }

    //Convert DP to pixels
    public static int dpToPixel(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}