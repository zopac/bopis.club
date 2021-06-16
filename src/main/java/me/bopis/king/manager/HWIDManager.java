package me.bopis.king.manager;

import me.bopis.king.hwid.DisplayUtil;
import me.bopis.king.hwid.NoStackTraceThrowable;
import me.bopis.king.hwid.SystemUtil;
import me.bopis.king.hwid.URLReader;

import java.util.ArrayList;
import java.util.List;

public class HWIDManager {

    /**
     * Your pastebin URL goes inside the empty string below.
     * It should be a raw pastebin link, for example: pastebin.com/raw/pasteid
     */

    public static final String hwidURL = "https://raw.githubusercontent.com/zopac/bopisclub/main/hwid";

    public static List<String> hwids = new ArrayList<>();

    public static void hwidCheck() {
        hwids = URLReader.readURL();
        boolean isHwidPresent = hwids.contains(SystemUtil.getSystemInfo());
        if (!isHwidPresent) {
            DisplayUtil.Display();
            throw new NoStackTraceThrowable("");
        }
    }
}
