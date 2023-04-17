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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AproposActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_apropos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Toolbar toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar=getSupportActionBar();
//        if (actionBar != null)
//            actionBar.setDisplayHomeAsUpEnabled(true);

        Button b_browser = (Button)findViewById(R.id.buttonGitHub);
        b_browser.setOnClickListener(v -> {
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( getString(R.string.url_github) ) );
            startActivity(intent);
        });

        // Mise à jour de la version
        TextView versionNameTextView = findViewById(R.id.version_name);
        versionNameTextView.setText(getString(R.string.version, BuildConfig.VERSION_NAME));


    }
}