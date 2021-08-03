package com.luxx.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.math.BigInteger;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class OsUtil {
    private static final Logger logger = LoggerFactory.getLogger(OsUtil.class);

    public static String getHostName() {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            inetAddress = null;
        }
        if (inetAddress != null) {
            return inetAddress.getHostName();
        } else {
            return execReadToString();
        }
    }

    public static List<String> getIpAddress() {
        List<String> ipList = new ArrayList<>();
        try {
            Enumeration allNetInterfaces = null;
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            if (allNetInterfaces != null) {
                InetAddress ipAddress = null;
                while (allNetInterfaces.hasMoreElements()) {
                    NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                    Enumeration addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ipAddress = (InetAddress) addresses.nextElement();
                        if (ipAddress instanceof Inet4Address && !ipAddress.isLoopbackAddress()) {
                            String ip = ipAddress.getHostAddress();
                            if (!ip.equals("0.0.0.0")) {
//                                ipList.add(netInterface.getName() + ":" + ipAddress.getHostAddress());
                                ipList.add(ip);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to get IP information", e);
        }
        return ipList;
    }

    public static String getOs() {
        String os = System.getProperty("os.name");
        if (os == null) {
            return "";
        }
        os = os.toLowerCase();
        if (os.contains("linux")) return "Linux";
        if (os.contains("windows")) return "Windows";
        if (os.contains("mac")) return "Mac";
        if (os.contains("sunos")) return "SunOS";
        if (os.contains("freebsd")) return "FreeBSD";
        return os;
    }

    private static LinkedList<String> getMacAddress() throws SocketException {
        LinkedList<String> macs = new LinkedList<>();

        Enumeration<NetworkInterface> el = null;
        try {
            el = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            logger.error("Failed to get mac information", e);
            throw e;
        }
        while (el.hasMoreElements()) {
            NetworkInterface netInterface = el.nextElement();
            try {
                if (!netInterface.isLoopback()) {
                    byte[] mac = netInterface.getHardwareAddress();
                    if (mac == null || mac.length == 0)
                        continue;
                    String macString = "";
                    for (byte b : mac) {
                        macString += (hexByte(b) + "-");
                    }
                    macs.add(macString.substring(0, macString.length() - 1));
                }
            } catch (SocketException e) {
                String message = "failed to handle mac information, name: " +
                        netInterface.getName() + ", DisplayName:" + netInterface.getDisplayName();
                logger.error(message, e);
            }
        }
        if (macs.isEmpty()) {
            throw new IllegalStateException("the mac information is empty");
        }
        Collections.sort(macs);
        return macs;
    }

    private static String hexByte(byte b) {
        String s = "0" + Integer.toHexString(b);
        return s.substring(s.length() - 2);
    }

    public static boolean isWindows() {
        return System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public static boolean isLinux() {
        return System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isMacOS() {
        return System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("mac os x");
    }

    public static String getMasterIp(String requestUrl) throws Exception {
        URL url = new URL(requestUrl);
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(url.getHost(), getPort(url)), 10000);
        String ip = socket.getLocalAddress().getHostAddress();
        return ip != null ? ip : "";
    }

    public static int getPort(URL url) {
        int port = url.getPort();
        if (port < 0) {
            if ("https".equals(url.getProtocol().toLowerCase())) {
                port = 443;
            } else {
                port = 80;
            }
        }
        return port;
    }

    private static String execReadToString() {
        try {
            InputStream in = Runtime.getRuntime().exec("hostname").getInputStream();
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "im-not-resolvable";
        }
    }

    public static String getDigestMd5(String data) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("md5");
        md5.update(data.getBytes());
        BigInteger bigInt = new BigInteger(1, md5.digest());
        return bigInt.toString(16);
    }
}