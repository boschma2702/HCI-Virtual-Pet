//package example.com.virtualpet;
//
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.media.MediaPlayer.OnErrorListener;
//import android.net.Uri;
//import android.os.Binder;
//import android.os.IBinder;
//import android.widget.Toast;
//
//import java.io.File;
//import java.io.IOException;
//
//public class MusicService extends Service  implements MediaPlayer.OnErrorListener {
//
//    private final IBinder mBinder = new ServiceBinder();
//    MediaPlayer mPlayer;
//    private int length = 0;
//
//    public MusicService() {
//    }
//
//    public class ServiceBinder extends Binder {
//        MusicService getService() {
//            return MusicService.this;
//        }
//    }
//
//    @Override
//    public IBinder onBind(Intent arg0) {
//        return mBinder;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        File mFile = new File("src/res/raw/who_let_the_dogs_out.mp3");
//        Uri uri = Uri.fromFile(mFile);
//
//        mPlayer = new MediaPlayer();
//        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        try {
//            mPlayer.setDataSource(MainActivity.INSTANCE.getMyContext(), uri);
//            mPlayer.prepare();
//            mPlayer.start();
//            Toast.makeText(this, "Music is playing", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mPlayer.setOnErrorListener(this);
//
//        if (mPlayer != null) {
//            mPlayer.setLooping(true);
//            mPlayer.setVolume(30, 30);
//        }
//
//
//        mPlayer.setOnErrorListener(new OnErrorListener() {
//
//            public boolean onError(MediaPlayer mp, int what, int
//                    extra) {
//
//                onError(mPlayer, what, extra);
//                return true;
//            }
//        });
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        mPlayer.start();
//        return START_STICKY;
//    }
//
//    public void pauseMusic() {
//        if (mPlayer.isPlaying()) {
//            mPlayer.pause();
//            length = mPlayer.getCurrentPosition();
//
//        }
//    }
//
//    public void resumeMusic() {
//        if (mPlayer.isPlaying() == false) {
//            mPlayer.seekTo(length);
//            mPlayer.start();
//        }
//    }
//
//    public void stopMusic() {
//        mPlayer.stop();
//        mPlayer.release();
//        mPlayer = null;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mPlayer != null) {
//            try {
//                mPlayer.stop();
//                mPlayer.release();
//            } finally {
//                mPlayer = null;
//            }
//        }
//    }
//
//    public boolean onError(MediaPlayer mp, int what, int extra) {
//
//        Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();
//        if (mPlayer != null) {
//            try {
//                mPlayer.stop();
//                mPlayer.release();
//            } finally {
//                mPlayer = null;
//            }
//        }
//        return false;
//    }
//}