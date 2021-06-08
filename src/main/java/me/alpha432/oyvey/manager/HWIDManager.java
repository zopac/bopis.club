package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.hwid.*;

import java.util.ArrayList;
import java.util.List;

public class HWIDManager {

    // https://raw.githubusercontent.com/zopac/bopisclub/main/hwid
    // https://pastebin.com/raw/KKfPxVaT
    // links with hwids incase something explodes
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
}
