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

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.BuildConfig;

import java.lang.reflect.Field;

public class AproposActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_apropos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // setContentView fait dans BaseActivity

        // Mise à jour de la version de MilkWay
        TextView versionNameTextView = findViewById(R.id.version_name);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            versionNameTextView.setText(getString(R.string.version, versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        // Action sur click du bouton
        Button githubButton = findViewById(R.id.buttonGitHub);
        githubButton.setOnClickListener(v -> onGitHubButtonClicked());
    }

    /**
     * Ouvre l'URL du projet MilkWay sur GitHub dans le navigateur.
     */
    private void onGitHubButtonClicked() {
        String url = getString(R.string.url_github);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}