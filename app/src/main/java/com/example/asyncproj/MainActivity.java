package com.example.asyncproj;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jetbrains.annotations.Async;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button btnStart, btnStop;
    private musAsync async = new musAsync(this);
    private AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnStart = findViewById(R.id.play1);
        btnStop = findViewById(R.id.stop1);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("are you sure?");
                adb.setMessage("This is a two button's alert");

                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        async = new musAsync(MainActivity.this);
                        async.execute();
                    }
                });

                adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog ad = adb.create();
                ad.show();
            }

        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("are you sure?");
                adb.setMessage("This is a two button's alert");

                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (async != null) {
                            async.cancel(true);
                            async = null;
                        }
                    }
                });

                adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog ad = adb.create();
                ad.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("ASYNC music");
        menu.add("Thread music");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String str = item.getTitle().toString();

        if(str == "ASYNC music")
        {
            startActivity(new Intent(this, MainActivity.class));
        }
        else if(str == "Thread music")
        {
            //change to thread music screen
            startActivity(new Intent(this, ThreadMusic.class));
        }

        return super.onOptionsItemSelected(item);
    }


}

class musAsync extends AsyncTask<Void, Void, Void> {
    private MediaPlayer mediaPlayer;
    private final WeakReference<MainActivity> activityRef;
    private AtomicBoolean isRunning = new AtomicBoolean(true);

    public musAsync(MainActivity context) {
        activityRef = new WeakReference<>(context);
    }

    @Override
    protected void onPreExecute() {
        MainActivity activity = activityRef.get();
        if (activity != null) {
            // create MediaPlayer with a valid Context
            mediaPlayer = MediaPlayer.create(activity, R.raw.dead);
            mediaPlayer.setLooping(true);
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (mediaPlayer != null) {
            mediaPlayer.start(); // just start once
        }
        // Keep thread alive until cancelled
        while (isRunning.get() && !isCancelled()) {
            try {
                Thread.sleep(500); // idle loop
            } catch (InterruptedException e) {
                break;
            }
        }
        return null;
    }

    @Override
    protected void onCancelled() {
        stopPlayer();
    }

    @Override
    protected void onPostExecute(Void result) {
        stopPlayer();
    }

    private void stopPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isRunning.set(false);
    }
}
