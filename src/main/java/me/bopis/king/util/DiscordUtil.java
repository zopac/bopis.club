package me.bopis.king.util;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.bopis.king.Bopis;
import me.bopis.king.features.modules.misc.RPC;
import me.bopis.king.manager.ModuleManager;
import net.minecraft.client.Minecraft;

/**
 * @Author evaan on 6/16/2021
 * https://github.com/evaan
 */
public class DiscordUtil {
    public static final String APP_ID = "854563660961284151";

    public static DiscordRichPresence presence;

    public static boolean connected;

    public static void start() {
        if (connected)
            return;
        connected = true;
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        rpc.Discord_Initialize(APP_ID, handlers, true, "");
        presence.startTimestamp = System.currentTimeMillis() / 1000L;
        setRpcFromSettings();
        (new Thread(DiscordUtil::setRpcFromSettingsNonInt, "Discord-RPC-Callback-Handler")).start();
    }

    public static void end() {
        connected = false;
        rpc.Discord_Shutdown();
    }

    public static String getIP() {
        if (Minecraft.getMinecraft().getCurrentServerData() != null)
            return "playing on " + (Minecraft.getMinecraft().getCurrentServerData()).serverIP;
        if (Minecraft.getMinecraft().isIntegratedServerRunning())
            return "singleplayer";
        return "main menu";
    }

    private static void setRpcFromSettingsNonInt() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                rpc.Discord_RunCallbacks();
                details = getIP();
                state = Bopis.moduleManager.getModuleT(RPC.class).text.getValue();
                presence.details = details;
                presence.state = state;
                rpc.Discord_UpdatePresence(presence);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                Thread.sleep(4000L);
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
        }
    }

    private static void setRpcFromSettings() {
        details = getIP();
        state = "bopis on top!";
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.largeImageKey = "bopis";
        presence.largeImageText = Bopis.moduleManager.getModuleT(RPC.class).text.getValue();
        presence.smallImageKey = "";
        presence.smallImageText = "";
    }

    private static final DiscordRPC rpc = DiscordRPC.INSTANCE;

    private static String details;

    private static String state;

    static {
        presence = new DiscordRichPresence();
        connected = false;
    }
}