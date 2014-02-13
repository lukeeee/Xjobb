package se.xjobb.scardfeud;

        import android.content.Context;
        import android.media.MediaPlayer;
        import android.os.Vibrator;
        import android.util.Log;

/**
 * Created by Lukas on 2014-02-07.
 */
public class SoundsVibration {


    public static void start(int source, Context context){
        if (User.UserDetails.getSound() == true){
            try{
                MediaPlayer mp = MediaPlayer.create(context, source);
                mp.start();
            } catch (Exception e){
                Log.i("MPerror", "" + e);
            }
        }
    }
    public static void vibrate(Context context){
        if (User.UserDetails.getVibration() == true){
            Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(50);
        }
    }
}