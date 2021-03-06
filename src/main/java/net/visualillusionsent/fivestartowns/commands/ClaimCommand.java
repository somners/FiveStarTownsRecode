package net.visualillusionsent.fivestartowns.commands;

import net.canarymod.chat.Colors;
import net.visualillusionsent.fivestartowns.Config;
import net.visualillusionsent.fivestartowns.flag.FlagValue;
import net.visualillusionsent.fivestartowns.player.IPlayer;
import net.visualillusionsent.fivestartowns.plot.Plot;
import net.visualillusionsent.fivestartowns.plot.PlotManager;
import net.visualillusionsent.fivestartowns.town.Town;
import net.visualillusionsent.fivestartowns.town.TownManager;
import net.visualillusionsent.fivestartowns.town.TownPlayer;

/**
 *
 * @author Somners
 */
public class ClaimCommand {

    public static void execute(IPlayer player, String[] command) {
        Plot plot = PlotManager.get().getFSTPlot(player);
        if (plot != null) {
            player.message(Config.get().getMessageHeader() + "This plots is already owned by: " + plot.getTownName());
            return;
        }
        Town town = TownManager.get().getTownFromPlayer(player);
        if (town == null) {
            player.message(Config.get().getMessageHeader() + "town is null :p");
            return;
        }
        if (town.getCurrentClaimCount() >= town.getMaxClaimCount()) {
            player.message(Config.get().getMessageHeader() + "Your town has already claimed too many plots! "
                    + "Consider unclaiming some.");
            return;
        }
        if (!PlotManager.get().isPlotNextToTown(player.getChunkX(), player.getChunkZ(), player.getWorldName(), town.getName()) && PlotManager.get().getTownPlots(town.getName()).length != 0) {
            player.message(Config.get().getMessageHeader() + "Your town does not sit adjacent to this plot! "
                    + "Try claiming a plot next to your town.");
            return;
        }
        Plot data = new Plot(player.getChunkX(), player.getChunkZ(), player.getWorldName(),
                town.getUUID(), "", FlagValue.NULL, FlagValue.NULL,
                FlagValue.NULL, FlagValue.NULL, FlagValue.NULL, FlagValue.NULL);
        data.setDirty(true);
        PlotManager.get().addNewPlot(data);

        player.message(Config.get().getMessageHeader() + "Plot Claimed for " + Colors.GREEN + town.getName() + "!");

    }

    public static boolean canUseCommand(IPlayer player) {
        TownPlayer tp = TownManager.get().getTownPlayer(player);
        if (tp != null && !tp.isOwner()) {
            player.message(Config.get().getMessageHeader() + "You must be a "
                    + "town " + Colors.GREEN + "Owner " + Colors.WHITE + "to use this command!");
            return false;
        }
        return true;
    }
}
