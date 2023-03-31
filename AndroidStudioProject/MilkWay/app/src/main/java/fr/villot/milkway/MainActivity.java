package fr.villot.milkway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    // constantes
    private static final int DEFAULT_SERVERPORT = 8080;
    private static final String DEFAULT_SERVER_IP = "192.168.1.103";
    private static final int DEFAULT_TIMEOUT = 3;
    private static final int DEFAULT_NB_RELAY = 2;
    static final int MAX_BUTTON_NUMBER = 4;

    // Messages de commande des relais
    private static final byte[] B1_ON = new byte[] { (byte) 0xA0, (byte) 0x01, (byte) 0x01, (byte) 0xA2};
    private static final byte[] B1_OFF = new byte[] { (byte) 0xA0, (byte) 0x01, (byte) 0x00, (byte) 0xA1};
    private static final byte[] B2_ON = new byte[] { (byte) 0xA0, (byte) 0x02, (byte) 0x01, (byte) 0xA3};
    private static final byte[] B2_OFF = new byte[] { (byte) 0xA0, (byte) 0x02, (byte) 0x00, (byte) 0xA2};
    private static final byte[] B3_ON = new byte[] { (byte) 0xA0, (byte) 0x02, (byte) 0x01, (byte) 0xA3};
    private static final byte[] B3_OFF = new byte[] { (byte) 0xA0, (byte) 0x02, (byte) 0x00, (byte) 0xA2};
    private static final byte[] B4_ON = new byte[] { (byte) 0xA0, (byte) 0x02, (byte) 0x01, (byte) 0xA3};
    private static final byte[] B4_OFF = new byte[] { (byte) 0xA0, (byte) 0x02, (byte) 0x00, (byte) 0xA2};

    // membres
    private Button [] mButtonList = new Button [MAX_BUTTON_NUMBER];

    // Preferences (en static pour etre utilises dans le thread)
    private static int sServerPort = DEFAULT_SERVERPORT;
    private static String sServerIp = DEFAULT_SERVER_IP;
    private static int sTimeout = DEFAULT_TIMEOUT;
    private static int sNbRelay = DEFAULT_NB_RELAY;

    // Preferences
    public static final String PREF_IP_ADDRESS = "ipAdress";
    public static final String PREF_PORT = "portNumber";
    public static final String PREF_TIMEOUT = "timeout";
    public static final String PREF_NB_RELAY = "nbRelay";

    // Log
//    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Log.d(LOG_TAG, "onCreate");

        // config Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Recuperation des boutons dans la vue
        mButtonList[0] = (Button) findViewById(R.id.bouton1);
        mButtonList[1] = (Button) findViewById(R.id.bouton2);
        mButtonList[2] = (Button) findViewById(R.id.bouton3);
        mButtonList[3] = (Button) findViewById(R.id.bouton4);

        // Action des boutons
        for (int i = 0 ; i < mButtonList.length ; i++) {
            mButtonList[i].setOnTouchListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Recuperation des preferences
        Context context = getApplicationContext();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        sServerIp = pref.getString(PREF_IP_ADDRESS, DEFAULT_SERVER_IP);
        sServerPort = Integer.parseInt(pref.getString(PREF_PORT, String.valueOf(DEFAULT_SERVERPORT)));
        sTimeout = Integer.parseInt(pref.getString(PREF_TIMEOUT, String.valueOf(DEFAULT_TIMEOUT)));
        sNbRelay = Integer.parseInt(pref.getString(PREF_NB_RELAY, String.valueOf(DEFAULT_NB_RELAY)));

        // Visibilité des boutons.
        for (int i = 0 ; i < mButtonList.length ; i++) {
            if (i < sNbRelay) {
                mButtonList[i].setVisibility(View.VISIBLE);
            }
            else {
                mButtonList[i].setVisibility(View.GONE);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Bouton de configuration
                Intent settingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(settingsActivity);
                return true;
            case R.id.action_apropos:
                // Bouton A propos
                Intent aproposActivity = new Intent(this,AproposActivity.class);
                startActivity(aproposActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int relayNumber = Integer.parseInt((String) v.getTag());

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Effet couleur appui
            v.getBackground().setColorFilter(0x55E0E0E0, PorterDuff.Mode.SRC_ATOP);
            v.invalidate();

            if (isNetworkAvailable()) {
                RelayTask relayTask = new RelayTask(MainActivity.this);
                relayTask.execute(relayNumber);
            } else {
                Toast.makeText(getApplicationContext(), R.string.reseau_indisponible, Toast.LENGTH_SHORT).show();
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            v.getBackground().clearColorFilter();
            v.invalidate();
        }
        return true;
    }

    // Détermine si le réseau est disponible.
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // L'AsyncTask est bien une classe interne statique
    static class RelayTask extends AsyncTask<Integer, Integer, String> {

        // Référence faible à l'activité
        private WeakReference<MainActivity> mActivity = null;

        public RelayTask (MainActivity pActivity) {
            link(pActivity);
        }

        @Override
        protected void onPostExecute (String result) {
            MainActivity mainActivity = mActivity.get();
            if (mainActivity != null) {
                if (result != null)
                    Toast.makeText(mainActivity, result, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground (Integer... relayNumber) {

            InetSocketAddress serverSocketAddr = null;
            Socket socket = null;
            OutputStream out = null;
            String result = App.getRes().getString(R.string.erreur_inconnue);
            byte[] relayOn = null;
            byte[] relayOff = null;

            // Détermine le relai concerné
            if (relayNumber[0] == 1) {
                relayOn = B1_ON;
                relayOff = B1_OFF;
                result=App.getRes().getString(R.string.relai1_ok);
            }
            else if (relayNumber[0] == 2) {
                relayOn = B2_ON;
                relayOff = B2_OFF;
                result=App.getRes().getString(R.string.relai2_ok);
            }
            else if (relayNumber[0] == 3) {
                relayOn = B3_ON;
                relayOff = B3_OFF;
                result=App.getRes().getString(R.string.relai3_ok);
            }
            else if (relayNumber[0] == 4) {
                relayOn = B4_ON;
                relayOff = B4_OFF;
                result=App.getRes().getString(R.string.relai4_ok);
            }

            // Connexion du socket
            try {
                serverSocketAddr = new InetSocketAddress(sServerIp, sServerPort);
                socket = new Socket();
                socket.connect(serverSocketAddr, sTimeout * 1000);
            } catch (Exception ex) {
                ex.printStackTrace();
                result = App.getRes().getString(R.string.erreur_connexion_wifi, sServerIp, sServerPort);
                try {
                    socket.close();
                    socket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    socket = null;
                }
            }

            if (socket != null) {
                try {
                    out = socket.getOutputStream();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    result = App.getRes().getString(R.string.erreur_flux_sortant);
                }
            }

            if (out != null) {

                try {
                    //Relay on
                    out.write(relayOn,0,4);
                    Thread.sleep(300);

                    // Relay off
                    out.write(relayOff,0,4);
                    Thread.sleep(200);
                } catch (IOException e) {
                    e.printStackTrace();
                    result = App.getRes().getString(R.string.erreur_commande_relai);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    result = App.getRes().getString(R.string.erreur_pause_on_off);
                }

            }

            // Close the socket
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    result = App.getRes().getString(R.string.erreur_fermeture_socket);
                    socket = null;
                }
            }

            return result;
        }

        // Cree une reference faible de l'activité dans le thread pour eviter les fuites mémoires
        public void link (MainActivity pActivity) {
            mActivity = new WeakReference<MainActivity>(pActivity);
        }

    }
}