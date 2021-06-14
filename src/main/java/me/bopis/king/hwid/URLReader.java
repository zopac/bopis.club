package me.bopis.king.hwid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class URLReader {

    public static List<String> readURL(String url) {
        List<String> s = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            String hwid;
            while ((hwid = bufferedReader.readLine()) != null) {
                s.add(hwid);
            }
        } catch (Exception e) {
        }
        return s;
    }
}
