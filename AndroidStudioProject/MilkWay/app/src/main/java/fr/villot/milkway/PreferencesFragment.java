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
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class PreferencesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference ipAddressPreference = findPreference("ip_address");
        if (ipAddressPreference !=null) {
            ipAddressPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                String input = newValue.toString();
                if (!isValidIpAddressOrDomainName(input)) {
                    Toast.makeText(getContext(), R.string.msg_err_invalid_ip_address, Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            });
        }

        Preference portNumberPreference = findPreference("port_number");
        if (portNumberPreference != null) {
            portNumberPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                String input = newValue.toString();
                if (!isValidPortNumber(input)) {
                    Toast.makeText(getContext(), R.string.msg_err_invalid_port_number, Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            });
        }
    }

    private boolean isValidIpAddressOrDomainName(String input) {
        // Vérifie si l'input correspond à une adresse IP
        if (input.matches("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")) {
            return true;
        }
        // Vérifie si l'input correspond à un nom de domaine
        else
            return input.matches("^(([a-zA-Z\\d]|[a-zA-Z\\d][a-zA-Z\\d\\-]*[a-zA-Z\\d])\\.)*([A-Za-z\\d]|[A-Za-z\\d][A-Za-z\\d\\-]*[A-Za-z\\d])$");
    }

    public boolean isValidPortNumber(String input) {
        try {
            int port = Integer.parseInt(input);
            if (port < 1 || port > 65535) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}