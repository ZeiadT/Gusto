package iti.mad.gusto.core.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

public abstract class NetworkManager {
    public static boolean isNetworkDisconnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) return true;

            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(network);
            if (actNw != null) {
                return !actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
        }
        return true;
    }

    public static void addConnectivityListener(Context context, ConnectivityManager.NetworkCallback callback){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            connectivityManager.registerDefaultNetworkCallback(callback);
        }
    }

    public static void removeConnectivityListener(Context context, ConnectivityManager.NetworkCallback callback){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            connectivityManager.unregisterNetworkCallback(callback);
        }
    }
}
