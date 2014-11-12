package cn.edu.seu.sh.main;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import cn.edu.seu.sh.Thread.ReceiveThread;
import cn.edu.seu.sh.Thread.RecordThread;
import cn.edu.seu.sh.multisocket.R;


public class MainActivity extends ActionBarActivity {

	private Button startEncodeButton, stopEncodeButton;
    private Button startPlayButton, stopPlayButton;
    private RecordThread recordThread = null;
    private ReceiveThread receiveThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();
    }

    private void initControls() {
        startEncodeButton = (Button) findViewById(R.id.startEncode);
        startEncodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEncodeButton.setEnabled(false);
                stopEncodeButton.setEnabled(true);
                if(recordThread == null){
                    recordThread = new RecordThread();
                    recordThread.start();
                }
            }
        });

        stopEncodeButton = (Button) findViewById(R.id.stopEncode);
        stopEncodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopEncodeButton.setEnabled(false);
                startEncodeButton.setEnabled(true);
                if(recordThread != null){
                    recordThread.stopThread();
                    recordThread.release();
                    recordThread = null;
                    
                }
            }
        });
        stopEncodeButton.setEnabled(false);

        startPlayButton = (Button) findViewById(R.id.startPlay);
        startPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlayButton.setEnabled(false);
                Toast.makeText(getApplicationContext(), "正在启动播放器，请稍后...", Toast.LENGTH_SHORT).show();
                stopPlayButton.setEnabled(true);
                if(receiveThread == null) {
                    receiveThread = new ReceiveThread();
                    receiveThread.start();
                }
            }
        });

        stopPlayButton = (Button) findViewById(R.id.stopPlay);
        stopPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlayButton.setEnabled(false);
                startPlayButton.setEnabled(true);
                if(receiveThread!=null){
                    receiveThread.stopThread();                    
                    receiveThread.release();
                    receiveThread=null;
                }
            }
        });
        stopPlayButton.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
