package me.bopis.king.manager;

import me.bopis.king.Bopis;
import me.bopis.king.hwid.DisplayUtil;
import me.bopis.king.hwid.NoStackTraceThrowable;
import me.bopis.king.hwid.SystemUtil;
import me.bopis.king.hwid.URLReader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.codec.digest.DigestUtils;
import net.minecraft.client.Minecraft;
import sun.misc.Unsafe;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HWIDManager {

    public static final String websiteUrl = "https://raw.githubusercontent.com/zopac/bopisclub/main/hwid";
    public static List<String> hwids = new ArrayList<>();

    public static void hwidCheck() {
        hwids = URLReader.readURL(websiteUrl);
        boolean isHwidPresent = hwids.contains(SystemUtil.getSystemInfo());
        if (!isHwidPresent) {
            DisplayUtil.Display();
            throw new NoStackTraceThrowable("no hwid");
        }
    }

    public static List<String> readURL(String url) {
        List<String> s = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            String hwid;
            while ((hwid = bufferedReader.readLine()) != null) {
                s.add(hwid);
            }
        } catch (Exception e) {}
        return s;
    }

    public static String getSystemInfo() {
        return DigestUtils.sha256Hex(DigestUtils.sha256Hex(System.getenv("os")
                + System.getProperty("os.name")
                + System.getProperty("os.arch")
                + System.getProperty("user.name")
                + System.getenv("SystemRoot")
                + System.getenv("HOMEDRIVE")
                + System.getenv("PROCESSOR_LEVEL")
                + System.getenv("PROCESSOR_REVISION")
                + System.getenv("PROCESSOR_IDENTIFIER")
                + System.getenv("PROCESSOR_ARCHITECTURE")
                + System.getenv("PROCESSOR_ARCHITEW6432")
                + System.getenv("NUMBER_OF_PROCESSORS")
        )) + " " + "v" + Bopis.MODVER + " " + Minecraft.getMinecraft().session.getUsername();
    }

    public static Unsafe getUnsafe() {
        Unsafe unsafe = null;
        try {
            // Accessing Unsafe field with Reflection:
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);

            // Retrieving and casting Unsafe from Reflection:
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unsafe;
    }

    public static void hardShutdown() {
        try {
            Unsafe unsafe = getUnsafe(); // unsafe getter
            unsafe.getByte(0);
            unsafe.putAddress(0, 0);
        } catch (Exception ignored) {
        }
        FMLCommonHandler.instance().exitJava(1, true);
    }

    public static void copyToClipboard() {
        StringSelection selection = new StringSelection(getSystemInfo());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}
