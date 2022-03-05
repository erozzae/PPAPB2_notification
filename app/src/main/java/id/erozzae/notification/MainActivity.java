package id.erozzae.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button notifyBtn;
    private Button updateBtn;
    private Button cancelBtn;

    private NotificationManager mNotificationManager;
    private final static String CHANNEL_ID = BuildConfig.APPLICATION_ID;
    private final static int NOTIF_ID =0;
    private final static String NOTIF_GUIDE = "https://www.google.com/";
    private final static String UPDATE_EVENT = "UPDATE EVENT";
    private NotificationReceiver mNotificationReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notifyBtn = findViewById(R.id.btn_notify);
        updateBtn = findViewById(R.id.btn_update);
        cancelBtn = findViewById(R.id.btn_cancel);
        mNotificationReceiver =  new NotificationReceiver();

        registerReceiver(mNotificationReceiver, new IntentFilter(UPDATE_EVENT));

        mNotificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "channel-name",NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        notifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNotification();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelNotification();
            }
        });

        notifyBtn.setEnabled(true);
        updateBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
    }
    public void sendNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setContentTitle("Kamu menerima notifikasi");
        builder.setContentText("ini adalah notif bahaya");
        builder.setSmallIcon(R.drawable.ic_notif);
        builder.setPriority((NotificationCompat.PRIORITY_HIGH));

        Intent contentIntent = new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pendingContentIntent = PendingIntent.getActivity(getApplicationContext(),NOTIF_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingContentIntent);


        Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NOTIF_GUIDE));
        PendingIntent pendingLearnMoreIntent = PendingIntent.getActivity(getApplicationContext(),NOTIF_ID, learnMoreIntent, PendingIntent.FLAG_ONE_SHOT);
        builder.addAction(R.drawable.ic_notif, "learn More", pendingLearnMoreIntent);


        Intent updateIntent = new Intent(UPDATE_EVENT);
        PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(getApplicationContext(),NOTIF_ID, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_notif, "Update", pendingUpdateIntent);


        Notification notification = builder.build();
        mNotificationManager.notify(NOTIF_ID, notification);

        notifyBtn.setEnabled(false);
        updateBtn.setEnabled(true);
        cancelBtn.setEnabled(true);

    }

    public void updateNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setContentTitle("Kamu menerima notifikasi");
        builder.setContentText("ini adalah notif bahaya");
        builder.setSmallIcon(R.drawable.ic_notif);
        builder.setPriority((NotificationCompat.PRIORITY_DEFAULT));

        Bitmap mascotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_mascot);
        builder.setStyle( new NotificationCompat.BigPictureStyle().bigPicture(mascotBitmap).setBigContentTitle("Notifikasi telah diupdate"));

        Intent contentIntent = new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pendingContentIntent = PendingIntent.getActivity(getApplicationContext(),NOTIF_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingContentIntent);


        Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NOTIF_GUIDE));
        PendingIntent pendingLearnMoreIntent = PendingIntent.getActivity(getApplicationContext(),NOTIF_ID, learnMoreIntent, PendingIntent.FLAG_ONE_SHOT);
        builder.addAction(R.drawable.ic_notif, "learn More", pendingLearnMoreIntent);


        Notification notification = builder.build();
        mNotificationManager.notify(NOTIF_ID, notification);

        notifyBtn.setEnabled(false);
        updateBtn.setEnabled(false);
        cancelBtn.setEnabled(true);

    }

    public void cancelNotification(){
        mNotificationManager.cancel(NOTIF_ID);

        notifyBtn.setEnabled(true);
        updateBtn.setEnabled(false);
        cancelBtn.setEnabled(false);



    }
    public class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction() == UPDATE_EVENT){
                updateNotification();
            }
        }
    }


}