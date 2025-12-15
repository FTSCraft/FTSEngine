package de.ftscraft.ftsengine.feature.texturepack.catalog.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.ftscraft.ftsengine.feature.FeatureHandler;
import de.ftscraft.ftsengine.feature.texturepack.catalog.Catalog;
import de.ftscraft.ftsengine.feature.texturepack.catalog.gui.menu.CatalogMainMenuGUI;
import de.ftscraft.ftsengine.main.Engine;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.entity.Player;

/**
 * Command handler for the /catalog command
 */
public class CatalogCommand {

    public CatalogCommand() {
        registerCommand();
    }

    private void registerCommand() {
        Engine.getInstance().getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                commands -> commands.registrar().register(createCatalogCommand())
        );
    }

    private LiteralCommandNode<CommandSourceStack> createCatalogCommand() {
        return Commands.literal("catalog")
                .requires(sender -> sender.getExecutor() instanceof Player)
                .executes(ctx -> {
                    Player player = (Player) ctx.getSource().getExecutor();
                    openCatalogMenu(player);
                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    private void openCatalogMenu(Player player) {
        Catalog catalog = FeatureHandler.getInstance().getCatalog();
        CatalogMainMenuGUI catalogMenu = new CatalogMainMenuGUI(catalog);
        catalogMenu.getGui().open(player);
    }
}
