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

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/*********
 * Classe abstraite BaseActivity : Permet de gérer de manière commune les
 * Toolbars des différentes activités.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        setupToolbar();
    }

    protected abstract int getLayoutResourceId();

    private void setupToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
            }
        }
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }
}
