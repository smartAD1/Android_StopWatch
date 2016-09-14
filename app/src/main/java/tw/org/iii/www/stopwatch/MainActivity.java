package tw.org.iii.www.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private TextView clock;
    private Button btnLeft, btnRight;
    private boolean isRunning;
    private int counter;
    private Timer timer;
    private UIHandler handler;
    private CountTask countTask;
    private ListView lapList;
    private SimpleAdapter adapter;
    private String[] from = {"title"};
    private int[] to = {R.id.lapitem_title};
    private LinkedList<HashMap<String,String>> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clock = (TextView)findViewById(R.id.clock);
        btnLeft = (Button)findViewById(R.id.btnLeft);
        btnRight = (Button)findViewById(R.id.btnRight);
        lapList = (ListView)findViewById(R.id.lapList);
        initListView();

        timer = new Timer();
        handler = new UIHandler();
    }

    @Override
    public void finish() {
        timer.purge();
        timer.cancel();
        timer = null;

        super.finish();
    }

    private void initListView(){
        data = new LinkedList<>();
        adapter =
                new SimpleAdapter(
                        this,data, R.layout.layout_lapitem,from,to);
        lapList.setAdapter(adapter);
    }

    // Reset / Lap
    public void doLeft(View v){
        if (isRunning){
            doLap();
        }else{
            doReset();
        }
    }
    // Start / Stop
    public void doRight(View v){
        isRunning = !isRunning;
        btnRight.setText(isRunning?"Stop":"Start");
        btnLeft.setText(isRunning?"Lap":"Reset");
        if (isRunning){
            doStart();
        }else{
            doStop();
        }
    }

    private void doStart(){
        countTask = new CountTask();
        timer.schedule(countTask, 0, 10);
    }
    private void doStop(){
        if (countTask != null) {
            countTask.cancel();
            countTask = null;
        }
    }
    private void doLap(){
        HashMap<String,String> lap =
                new HashMap<>();
        lap.put(from[0], "" + counter);
        data.add(0,lap);
        adapter.notifyDataSetChanged();
    }
    private void doReset(){
        counter = 0;
        handler.sendEmptyMessage(0);
        data.clear();
        adapter.notifyDataSetChanged();
    }

    private class CountTask extends TimerTask {
        @Override
        public void run() {
            counter++;
            handler.sendEmptyMessage(0);
        }
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            clock.setText("" + counter);
        }
    }

}