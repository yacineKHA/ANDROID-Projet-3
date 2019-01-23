package com.example.moi.exerciceimage;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Yacine
 * @since 2018
 * HistoricActivity gives an access to the mood's historic and comments of the last seven days.
 */
public class HistoricActivity extends AppCompatActivity {

    Date today;
    SimpleDateFormat dateFormat;
    Calendar c, cal;
    String yesterday, getHistoricComment;
    int getBackgroundColor;
    private SharedPreferences mPref;

    int[] backgroundColor = new int[]{
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
    int[] px = new int[]{
            80,
            130,
            180,
            270
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historic_layout);
        ButterKnife.bind(this);

        mPref = getSharedPreferences("preferences", MODE_PRIVATE);
        dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.FRANCE);
        today = new Date();
        c = Calendar.getInstance();
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        setHistoricLayouts();
        setVisibleIcons();
    }

    @OnClick({R.id.ll_1, R.id.ll_2, R.id.ll_3, R.id.ll_4, R.id.ll_5, R.id.ll_6, R.id.ll_7})
    public void onHistoryClick(View v) {
        int i = Integer.parseInt(v.getTag().toString());
        i--;
        getComments(i);
        refreshDate(i);
    }

    /**
     * refreshDate
     * Set the calendar to the current date
     * @param i to get the last seven days, to add them
     */
    public void refreshDate(int i) {
        Date today = cal.getTime();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, i);
    }

    /**
     * getComments
     * Method for get saved comments and display them with toast
     *
     * @param i to get the last seven days comments
     */
    public void getComments(int i) {
        Date today = cal.getTime();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, -i);
        Date yesterDayDate;
        yesterDayDate = cal.getTime();
        yesterday = dateFormat.format(yesterDayDate);
        getHistoricComment = mPref.getString("comment" + yesterday, null);
        if (getHistoricComment == null) {
            Toast.makeText(HistoricActivity.this, " ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(HistoricActivity.this, getHistoricComment, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * setVisibleIcons
     * Method for set the visibility of the icons when there is a saved comment
     * "For loop" for synchronise to the last seven days
     */
    public void setVisibleIcons() {
        for (int i = 0; i < 7; i++) {
            cal.add(Calendar.DAY_OF_MONTH, -i);
            Date dayBefore = cal.getTime();
            yesterday = dateFormat.format(dayBefore);
            getHistoricComment = mPref.getString("comment" + yesterday, null);
            if (getHistoricComment == null) {
                ImageView setIcons = findViewById(icons[i]);
                setIcons.setVisibility(View.INVISIBLE);
            }
            refreshDate(i);
        }
    }

    /**
     * setHistoricLayouts
     * It will get the saved background colors of the last seven days to set the colors of the historic layouts
     */
    public void setHistoricLayouts() {
        Date date;
        c.add(Calendar.DAY_OF_MONTH, -1);
        date = c.getTime();
        for (int i = 0; i < 7; i++) {
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH, -i);
            Date yesterdayDate = c.getTime();
            yesterday = dateFormat.format(yesterdayDate);
            getBackgroundColor = mPref.getInt("backcolorvalue" + yesterday, 5);
            LinearLayout currentLayout = findViewById(linear[i]);
            currentLayout.setBackgroundResource(backgroundColor[getBackgroundColor]);

            if (getBackgroundColor == 5) {
                currentLayout.setVisibility(View.INVISIBLE);
            } else {
                for (int a = 0; a < 4; a++) {
                    if (getBackgroundColor == a) {
                        LinearLayout.LayoutParams dimensions = (LinearLayout.LayoutParams) currentLayout.getLayoutParams();
                        dimensions.width = dpToPixel(px[a]);
                        currentLayout.setLayoutParams(dimensions);
                    }
                }
            }
        }
    }

    /**
     * dpToPixel
     *
     * @param dp -> number of dp that will be converted to pixel
     * @return -> return the result in pixel
     */
    public static int dpToPixel(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}