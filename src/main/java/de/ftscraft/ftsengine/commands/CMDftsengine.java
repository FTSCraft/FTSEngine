package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.brett.BrettNote;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CMDftsengine implements CommandExecutor {

    Engine plugin;

    public CMDftsengine(Engine plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("ftsengine").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

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
                        cs.sendMessage("§cDa hat was nicht geklappt! Ich habe das Brett nicht gefunden");
                        return true;
                    }

                    BrettNote note = null;
                    for (BrettNote notes : brett.getNotes()) {
                        if (notes.invslot == slot && notes.page == page) {
                            note = notes;
                            break;
                        }
                    }

                    if(note == null) {
                        cs.sendMessage("§7[§bSchwarzes Brett§7] Diese Notiz wurde nicht gefunden.");
                        return true;
                    }

                    if(note.getCreator().equals(cs.getName()) || cs.hasPermission("brett.admin")) {
                        note.remove();
                        cs.sendMessage("§7[§bSchwarzes Brett§7] Die Notiz wurde entfernt.");
                        return true;
                    } else {
                        cs.sendMessage("§7[§bSchwarzes Brett§7] Du bist nicht dazu in der Lage diese Notiz zu löschen. Tut mir leid, aber so sind die Regeln");
                        return true;
                    }


                }
            }

            

        }

        return false;
    }
}