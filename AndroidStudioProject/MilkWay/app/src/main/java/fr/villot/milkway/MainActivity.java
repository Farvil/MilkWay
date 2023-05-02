/*
 * Copyright (c) 2023 Vincent VILLOT
 *
 * Ce fichier fait partie de l'application MilkWay.
 *
 * MilkWay est un logiciel libre : vous pouvez le redistribuer
 * et/ou le modifier selon les termes de la Licence Publique Générale GNU
 * telle que publiée par la Free Software Foundation, version 3 de la licence
 * ou toute version ultérieure.
 *
 * MilkWay est distribué dans l'espoir qu'il sera utile,
 * mais SANS AUCUNE GARANTIE ; sans même la garantie implicite de QUALITÉ
 * MARCHANDE ou D'ADÉQUATION À UN USAGE PARTICULIER. Consultez la Licence
 * Publique Générale GNU pour plus de détails.
 *
 * Vous devez avoir reçu une copie de la Licence Publique Générale GNU
 * en même temps que ce programme. Si ce n'est pas le cas, consultez
 * <https://www.gnu.org/licenses/>.
 *
 * URL des sources et de la documentation du projet MilkWay : https://github.com/Farvil/MilkWay
 */


package fr.villot.milkway;

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

import androidx.appcompat.app.ActionBar;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;

// TODO : Vérifier les donnees rentrées par l'utilisateur dans les preferences pour eviter plantage application
// TODO : Remplacer l'async task dépréciée par un thread.

public class MainActivity extends BaseActivity implements View.OnTouchListener {

    // constantes
    private static final int DEFAULT_SERVERPORT = App.getRes().getInteger(R.integer.default_port_number);
    private static final String DEFAULT_SERVER_IP = App.getRes().getString(R.string.default_server_ip);
    private static final int DEFAULT_TIMEOUT = App.getRes().getInteger(R.integer.default_timeout);
    private static final int DEFAULT_NB_RELAY = App.getRes().getInteger(R.integer.default_nb_relay);
    static final int MAX_BUTTON_NUMBER = 4;

    // Messages de commande des relais
    // TODO : Externaliser les messages des relais dans res/raw en utilisant Resources.openRawResource
    private static final byte[] B1_ON = new byte[] { (byte) 0xA0, (byte) 0x01, (byte) 0x01, (byte) 0xA2};
    private static final byte[] B1_OFF = new byte[] { (byte) 0xA0, (byte) 0x01, (byte) 0x00, (byte) 0xA1};
    private static final byte[] B2_ON = new byte[] { (byte) 0xA0, (byte) 0x02, (byte) 0x01, (byte) 0xA3};
    private static final byte[] B2_OFF = new byte[] { (byte) 0xA0, (byte) 0x02, (byte) 0x00, (byte) 0xA2};
    private static final byte[] B3_ON = new byte[] { (byte) 0xA0, (byte) 0x03, (byte) 0x01, (byte) 0xA4};
    private static final byte[] B3_OFF = new byte[] { (byte) 0xA0, (byte) 0x03, (byte) 0x00, (byte) 0xA3};
    private static final byte[] B4_ON = new byte[] { (byte) 0xA0, (byte) 0x04, (byte) 0x01, (byte) 0xA5};
    private static final byte[] B4_OFF = new byte[] { (byte) 0xA0, (byte) 0x04, (byte) 0x00, (byte) 0xA4};

    // membres
    private final Button [] mButtonList = new Button [MAX_BUTTON_NUMBER];

    // Preferences (en static pour etre utilises dans le thread)
    private static int sServerPort = DEFAULT_SERVERPORT;
    private static String sServerIp = DEFAULT_SERVER_IP;
    private static int sTimeout = DEFAULT_TIMEOUT;

    // Preferences
    public static final String PREF_IP_ADDRESS = "ip_address";
    public static final String PREF_PORT = "port_number";
    public static final String PREF_TIMEOUT = "timeout";
    public static final String PREF_NB_RELAY = "nb_relay";

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Suppression bouton retour de la Toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        // Recuperation des boutons dans la vue
        mButtonList[0] = (Button) findViewById(R.id.bouton1);
        mButtonList[1] = (Button) findViewById(R.id.bouton2);
        mButtonList[2] = (Button) findViewById(R.id.bouton3);
        mButtonList[3] = (Button) findViewById(R.id.bouton4);

        // Action des boutons
        for (Button button : mButtonList) {
            button.setOnTouchListener(this);
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
        int sNbRelay = Integer.parseInt(pref.getString(PREF_NB_RELAY, String.valueOf(DEFAULT_NB_RELAY)));

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
        // Deserialise le menu XML et ajoute les items dans l'action bar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            // Bouton de configuration
            Intent settingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(settingsActivity);
            return true;
        }
        else if (item.getItemId() == R.id.action_apropos) {
            // Bouton A propos
            Intent aproposActivity = new Intent(this, AproposActivity.class);
            startActivity(aproposActivity);
            return true;
        } else {
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
                    relayTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, relayNumber);

            } else {
                Toast.makeText(getApplicationContext(), R.string.msg_err_unavailable_network, Toast.LENGTH_SHORT).show();
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            v.performClick();
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
            super();
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

            InetSocketAddress serverSocketAddr;
            Socket socket = null;
            OutputStream out = null;
            String result = App.getRes().getString(R.string.msg_err_unknown_error);
            byte[] relayOn = null;
            byte[] relayOff = null;

            // Détermine le relai concerné
            if (relayNumber[0] == 1) {
                relayOn = B1_ON;
                relayOff = B1_OFF;
                result=App.getRes().getString(R.string.msg_info_relay1_ok);
            }
            else if (relayNumber[0] == 2) {
                relayOn = B2_ON;
                relayOff = B2_OFF;
                result=App.getRes().getString(R.string.msg_info_relay2_ok);
            }
            else if (relayNumber[0] == 3) {
                relayOn = B3_ON;
                relayOff = B3_OFF;
                result=App.getRes().getString(R.string.msg_info_relay3_ok);
            }
            else if (relayNumber[0] == 4) {
                relayOn = B4_ON;
                relayOff = B4_OFF;
                result=App.getRes().getString(R.string.msg_info_relay4_ok);
            }

            // Connexion du socket
            try {
                serverSocketAddr = new InetSocketAddress(sServerIp, sServerPort);
                socket = new Socket();
                socket.connect(serverSocketAddr, sTimeout * 1000);
            } catch (Exception ex) {
                ex.printStackTrace();
                result = App.getRes().getString(R.string.msg_err_wifi_connection, sServerIp, sServerPort);
                try {
                    if (socket != null) {
                        socket.close();
                        socket = null;
                    }
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
                    result = App.getRes().getString(R.string.msg_err_output_stream);
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
                    result = App.getRes().getString(R.string.msg_err_relay_command);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    result = App.getRes().getString(R.string.msg_err_pause_on_off);
                }

            }

            // Close the socket
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    result = App.getRes().getString(R.string.msg_err_socket_close);
                }
            }

            return result;
        }

        // Cree une reference faible de l'activité dans le thread pour eviter les fuites mémoires
        public void link (MainActivity pActivity) {
            mActivity = new WeakReference<>(pActivity);
        }

    }
}