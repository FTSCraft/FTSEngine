package de.ftscraft.ftsengine.utils.exceptions;

import de.ftscraft.ftsengine.feature.courier.Briefkasten;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BriefkastenNotFoundException extends Exception {

    public BriefkastenNotFoundException(Briefkasten briefkasten) {
        Logger logger = Logger.getLogger("Minecraft");
        logger.log(Level.WARNING, "Briefkasten ist keine Chest. Koordinaten: " + briefkasten.getLocation().getX() + " " + briefkasten.getLocation().getY() + " " + briefkasten.getLocation().getZ());
    }


}
