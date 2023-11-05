package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.brett.BrettNote;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CMDftsengine implements CommandExecutor {

    Engine plugin;

    public CMDftsengine(Engine plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("ftsengine").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!cs.hasPermission("ftsengine.helfer")) {
            cs.sendMessage(Messages.NO_PERMISSIONS);
            return true;
        }

        if (args.length == 5) {

            if (args[0].equals("brett")) {
                if (args[1].equals("delete")) {
                    String slotS = args[2];
                    String pageS = args[3];
                    String name = args[4].replace("_", " ");

                    int slot = Integer.parseInt(slotS);
                    int page = Integer.parseInt(pageS);

                    Brett brett = null;

                    for (Brett value : plugin.bretter.values()) {
                        if (value.getName().equals(name)) {
                            brett = value;
                            break;
                        }
                    }

                    if (brett == null) {
                        cs.sendMessage(Messages.PREFIX + "Da hat was nicht geklappt! Ich habe das Brett nicht gefunden");
                        return true;
                    }

                    BrettNote note = null;
                    for (BrettNote notes : brett.getNotes()) {
                        if (notes.invslot == slot && notes.page == page) {
                            note = notes;
                            break;
                        }
                    }

                    if (note == null) {
                        cs.sendMessage(Messages.PREFIX + "Diese Notiz wurde nicht gefunden.");
                        return true;
                    }

                    if (note.getCreator().equals(cs.getName()) || cs.hasPermission("brett.admin")) {
                        note.remove();
                        cs.sendMessage(Messages.PREFIX + "Die Notiz wurde entfernt.");
                        return true;
                    } else {
                        cs.sendMessage(Messages.PREFIX + "Du bist nicht dazu in der Lage diese Notiz zu löschen. Tut mir leid, aber so sind die Regeln");
                        return true;
                    }


                }
            }


        }

        return false;
    }
}
