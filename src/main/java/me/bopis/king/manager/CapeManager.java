package me.alpha432.oyvey.manager;

import javafx.util.Pair;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.modules.render.NoRender;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CapeManager extends Feature {

    private final List<UUID> ogCapes = new ArrayList<>();
    private final List<Pair<UUID, BufferedImage>> donatorCapes = new ArrayList<>();
    private final List<UUID> poggersCapes = new ArrayList<>();
    private final List<UUID> contrabutorCapes = new ArrayList<>();

    public CapeManager() {
        try { // dev
            URL capesList = new URL("https://raw.githubusercontent.com/zopac/bopisclub/main/dev.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(capesList.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                contrabutorCapes.add(UUID.fromString(inputLine));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try { // donator
            File tmp = new File("bopis"+ File.separator + "capes");
            if (!tmp.exists()) {
                tmp.mkdirs();
            }
            URL capesList = new URL("https://raw.githubusercontent.com/zopac/bopisclub/main/dev.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(capesList.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String colune = inputLine.trim();
                String uuid = colune.split(":")[0];
                String cape = colune.split(":")[1];
                URL capeUrl = new URL("https://raw.githubusercontent.com/zopac/bopisclub/main/" + cape + ".png");
                BufferedImage capeImage = ImageIO.read(capeUrl);
                ImageIO.write(capeImage, "png", new File("bopis/capes/" + cape + ".png"));
                donatorCapes.add(new Pair<>(UUID.fromString(uuid), capeImage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isOg(UUID uuid) {
        return this.ogCapes.contains(uuid);
    }

    public boolean isDonator(UUID uuid) {
        for (Pair<UUID, BufferedImage> donator : this.donatorCapes) {
            if (donator.getKey() == uuid) {
                return true;
            }
        }
        return false;
    }

    public BufferedImage getCapeFromDonor(UUID uuid) {
        for (Pair<UUID, BufferedImage> donator : this.donatorCapes) {
            if (donator.getKey() == uuid) {
                return donator.getValue();
            }
        }
        return null;
    }

    public boolean isPoggers(UUID uuid) {
        return this.poggersCapes.contains(uuid);
    }

    public boolean isContrabutor(UUID uuid) {
        return this.contrabutorCapes.contains(uuid);
    }

}