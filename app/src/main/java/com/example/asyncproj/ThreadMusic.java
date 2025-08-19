package com.example.asyncproj;

import android.content.DialogInterface;
import android.content.Intent;
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

public class ThreadMusic extends AppCompatActivity {


    private Button btnStart, btnStop;
    private runnableMusic runnable = new runnableMusic();
    private Thread thread = new Thread(runnable);
    private AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thread_music);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnStart = findViewById(R.id.play2);
        btnStop = findViewById(R.id.stop2);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                adb = new AlertDialog.Builder(ThreadMusic.this);
                adb.setTitle("are you sure?");
                adb.setMessage("This is a two button's alert");

                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        thread.start();
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
            public void onClick(View v)
            {
                thread.interrupt();
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