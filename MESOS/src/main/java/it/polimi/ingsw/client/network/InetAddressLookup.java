package it.polimi.ingsw.client.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class InetAddressLookup {

    public static String getLocalHostAddress(String remoteHost, int remotePort) {
        // We try an UDP ping to a server to see if we receive a response, in that case we know for sure that
        // The ip we found can be used to connect with the client server
        if (remoteHost != null && !remoteHost.isBlank() && remotePort > 0) {
            try (java.net.DatagramSocket socket = new java.net.DatagramSocket()) {
                // la connect qui imposta la route locale; non invia nulla
                socket.connect(java.net.InetAddress.getByName(remoteHost), remotePort);
                InetAddress local = socket.getLocalAddress();
                if (local != null && !local.isLoopbackAddress() && local.getAddress().length == 4) {
                    return local.getHostAddress();
                }
            } catch (Exception ignored) {
                //Ignore and go to fallback
            }
        }

        // The fallback scans all the ip with some word key that are likely to be in our wifi adapter,
        // if we find one we return it, otherwise we return the first valid one
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
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

            // If we are not lucky with the key word searched before we search for all the possible ips
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

    // When we send the UDP message to see of the ip works for the connection, we use a public DNS server as destination,
    // so we are sure that it is always up and reachable, and that the port is open
    public static String getLocalHostAddress() {
        return getLocalHostAddress("8.8.8.8", 53);
    }

    // See if the inferface has some keyword that we don't need so the interface for us is useless or not
    private static boolean isGoodInterface(NetworkInterface ni) {
        try {
            if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()) return false;
            String display = ni.getDisplayName().toLowerCase();
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

