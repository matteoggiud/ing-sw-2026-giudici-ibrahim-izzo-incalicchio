package it.polimi.ingsw.client.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class InetAddressLookup {

    /**
     * Versione raccomandata che prova a determinare l'IP locale usato per raggiungere remoteHost:remotePort.
     * Se remoteHost è null o non raggiungibile, fa un fallback con scansione delle interfacce
     * preferendo nomi tipici di schede Wi-Fi e filtrando virtual adapters.
     */
    public static String getLocalHostAddress(String remoteHost, int remotePort) {
        // 1) Prova UDP connect verso il server (non invia pacchetti)
        if (remoteHost != null && !remoteHost.isBlank() && remotePort > 0) {
            try (java.net.DatagramSocket socket = new java.net.DatagramSocket()) {
                // la connect qui imposta la route locale; non invia nulla
                socket.connect(java.net.InetAddress.getByName(remoteHost), remotePort);
                InetAddress local = socket.getLocalAddress();
                if (local != null && !local.isLoopbackAddress() && local.getAddress().length == 4) {
                    return local.getHostAddress();
                }
            } catch (Exception ignored) {
                // ignora e passa al fallback
            }
        }

        // 2) Fallback: scansione interfacce con preferenza per Wi-Fi / interfacce non virtuali
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            // Prima passata: cerca indirizzi che sembrano "Wi-Fi"
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (!isGoodInterface(ni)) continue;
                String display = ni.getDisplayName().toLowerCase();
                String name = ni.getName().toLowerCase();
                if (display.contains("wi") || display.contains("wireless") || display.contains("wlan") ||
                        display.contains("wi-fi") || name.contains("wlan") || name.contains("wifi") || name.contains("wl")) {
                    Enumeration<InetAddress> addrs = ni.getInetAddresses();
                    while (addrs.hasMoreElements()) {
                        InetAddress a = addrs.nextElement();
                        if (isGoodAddress(a)) return a.getHostAddress();
                    }
                }
            }

            // Seconda passata: qualsiasi interfaccia valida (site-local preferita)
            nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (!isGoodInterface(ni)) continue;
                Enumeration<InetAddress> addrs = ni.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress a = addrs.nextElement();
                    if (isGoodAddress(a)) return a.getHostAddress();
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting IP address: " + e.getMessage());
        }

        return "localhost";
    }

    // Mantieni un overload compatibile per chiamate senza parametri (usa 8.8.8.8 come heuristica)
    public static String getLocalHostAddress() {
        return getLocalHostAddress("8.8.8.8", 53);
    }

    private static boolean isGoodInterface(NetworkInterface ni) {
        try {
            if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()) return false;
            String display = ni.getDisplayName().toLowerCase();
            // escludi esplicitamente adattatori virtuali comuni
            if (display.contains("virtual") || display.contains("vmware") ||
                    display.contains("hyper") || display.contains("vbox") ||
                    display.contains("docker") || display.contains("wsl") || display.contains("vpn")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isGoodAddress(InetAddress a) {
        return a != null && !a.isLoopbackAddress() && a.getAddress().length == 4 && a.isSiteLocalAddress();
    }
}

