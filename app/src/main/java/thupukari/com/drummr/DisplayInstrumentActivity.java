package thupukari.com.drummr;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;


public class DisplayInstrumentActivity extends ActionBarActivity {
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final String intrument = intent.getStringExtra(MainActivity.EXTRA_INSTRUMENT);


        try {
            socket = IO.socket("http://104.236.192.49:3000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.d("MainActivity: ", "socket connected");
                socket.emit("add user", "device");
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
            }

        });

        socket.connect();

        setContentView(R.layout.activity_display_instrument);

        Button buttons[][] = new Button[3][2];

        for(int i = 0; i < buttons.length; i++){
            for(int j = 0; j < buttons[i].length; j++ ){
                String buttonID = "button" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = (Button) findViewById(resID);
                buttons[i][j].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View arg0, MotionEvent arg1) {
                        if (arg1.getAction() == MotionEvent.ACTION_DOWN){
                            socket.emit("button down", intrument, getResources().getResourceEntryName(arg0.getId()));
                            return false;
                        }
                        if (arg1.getAction() == MotionEvent.ACTION_UP){
                            socket.emit("button up", intrument, getResources().getResourceEntryName(arg0.getId()));
                            return false;
                        }
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
