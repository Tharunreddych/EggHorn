package com.example.tharunreddychinthala.eggtimer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Max time in timer is 10 min or 600 sec.
    private static final int MAX_SEC = 600;
    // Seconds in min 60.
    private static final int SECONDS_IN_MIN = 60;
    // Base Unit is sec in millsec
    private static final int BASE_TIME_UNIT = 1000;
    // Default Time in sec.
    private static final int DEFAULT_TIME = 30;
    private static final String GO = "GO";
    private static final String STOP = "STOP";
    private static String CLOCK_VIEW_STRING = "%s:%s";

    // previousEggClockState : true - Go
    // previousEggClockState : false - Stop
    private boolean previousEggClockState = false;

    private CountDownTimer countDownTimer;

    // current time remaining in timer.
    private int timerTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setOnClickListnerForSeekBar();
        setSeekBarMax(MAX_SEC);
        setSeekBar(DEFAULT_TIME);
        setEggTimerButtonStateToGo();
    }

    private void setOnClickListnerForSeekBar() {
        SeekBar seekBar = getEggSeekBar();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setClockTextView(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do Nothing.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do Nothing.
            }
        });
    }

    /**
     * Will stopState the timer to 30.
     */
    public void reset(View view) {
        if (previousEggClockState) {
            // When the button if click to Go.
            stopState();
        } else {
            goState();
        }
    }

    private void goState() {
        previousEggClockState = true;
        timerTime = getSeekBarProgress();
        setClockTextView(timerTime);

        setEggTimerButtonStateToStop();

        startCountDown(timerTime);

        disableSeekBar();
    }


    private void stopState() {
        previousEggClockState = false;
        setSeekBar(DEFAULT_TIME);
        setClockTextView(DEFAULT_TIME);
        setEggTimerButtonStateToGo();

        stopCountDown();

        enableSeekBar();
    }

    private void startCountDown(int eggClockTimeInSec) {
        final Context context = this;
        final int eggClockTimeInMilliSec = eggClockTimeInSec * BASE_TIME_UNIT;
        countDownTimer = new CountDownTimer(eggClockTimeInMilliSec, BASE_TIME_UNIT) {
            @Override
            public void onTick(long l) {
                timerTime--;
                setSeekBar(timerTime);
                setClockTextView(timerTime);

            }

            @Override
            public void onFinish() {
                // Play sound.
                final MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.airhorn);
                mediaPlayer.start();
            }
        };

        countDownTimer.start();
    }

    private void stopCountDown() {
        countDownTimer.cancel();
    }


    /**
     * Takes the time in sec and converts that to min and sec and shows it in the text view.
     * @param timeInSec time in seconds.
     */
    private void setClockTextView(int timeInSec) {
        int min = timeInSec/60;

        String eggTimer = String.format(CLOCK_VIEW_STRING,
                getRemainingMin(timeInSec), getRemainingSecInDoubleDigit(timeInSec));;

        TextView eggTimerClock = (TextView) findViewById(R.id.eggTimerClock);

        eggTimerClock.setText(eggTimer);
    }

    private String getRemainingMin(int timeInSec) {
        return String.valueOf(timeInSec/60);
    }

    /**
     * Will give the sec after removeing the min.
     * @param timeInSec time in seconds
     * @return remaining sec after min are removed.
     */
    private String getRemainingSecInDoubleDigit(int timeInSec) {
        int remainingSec = timeInSec%60;
        if (remainingSec < 10) {
            return String.format("0" + remainingSec);
        } else {
            return String.valueOf(remainingSec);
        }
    }

    /**
     * Will set the eggTimerSeekBar to given progress.
     * @param progress progress
     */
    private void setSeekBar(int progress) {
        getEggSeekBar().setProgress(progress);
    }

    private void setSeekBarMax(int progress) {
        getEggSeekBar().setMax(progress);
    }

    private int getSeekBarProgress() {
        return getEggSeekBar().getProgress();
    }

    private void setEggTimerButtonStateToGo() {
        getEggTimerButton().setText(GO);
    }

    private void setEggTimerButtonStateToStop() {
        getEggTimerButton().setText(STOP);
    }

    private void disableSeekBar() {
        getEggSeekBar().setEnabled(false);
    }

    private void enableSeekBar() {
        getEggSeekBar().setEnabled(true);
    }

    private Button getEggTimerButton() {
        return (Button) findViewById(R.id.eggTimerButton);
    }

    private SeekBar getEggSeekBar() {
        return (SeekBar) findViewById(R.id.eggTimerSeekBar);
    }
}
