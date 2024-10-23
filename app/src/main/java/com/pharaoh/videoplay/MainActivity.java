package com.pharaoh.videoplay;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends Activity {
    VideoView videoView;
    private MediaController mController;
    TextView tv_full;
    TextView tv_cur;
    ProgressBar pb;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            if (videoView.isPlaying()) {
                int current = videoView.getCurrentPosition();
                pb.setProgress(current);
                tv_cur.setText(" " +time(videoView.getCurrentPosition()));
            }
            handler.postDelayed(runnable, 1000);
        }
    };
    protected String time(long millionSeconds) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millionSeconds);
        return simpleDateFormat.format(c.getTime());
    }

    @Override
    protected void onPause() {
        this.finish();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        videoView=(VideoView) findViewById(R.id.videoView);
        tv_full=(TextView) findViewById(R.id.tv_full);
        tv_cur=(TextView) findViewById(R.id.tv_cur);
        pb=(ProgressBar) findViewById(R.id.progressBar);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){

            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                int d = videoView.getDuration();
                tv_full.setText(time(d)+"  ");
                pb.setMax(d);
                handler.postDelayed(runnable, 0);
            }
        });
        mController=new MediaController(this);
        videoView.setMediaController(mController);
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.a;
        videoView.setVideoURI(Uri.parse(uri));
        videoView.start();
    }
    @Override
    protected void onDestroy() {
        //videoView.setKeepScreenOn(false);
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
            case KeyEvent.KEYCODE_SETTINGS:
//            case KeyEvent.KEYCODE_DPAD_UP:
//            case KeyEvent.KEYCODE_DPAD_DOWN:
                if(videoView.isPlaying()) {
                    videoView.pause();
                    mController.show(5000);
                } else {
                    videoView.start();
                }
                break;
//            case KeyEvent.KEYCODE_DPAD_RIGHT:
//                videoView.seekTo(videoView.getCurrentPosition()+3000);
//                break;
//            case KeyEvent.KEYCODE_DPAD_LEFT:
//                videoView.seekTo(videoView.getCurrentPosition()-3000);
//                break;

        }
        return super.onKeyDown(keyCode, event);
    }
}