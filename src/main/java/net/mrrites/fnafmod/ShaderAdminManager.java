package net.mrrites.fnafmod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.irisshaders.iris.config.IrisConfig;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.irisshaders.iris.apiimpl.IrisApiV0ConfigImpl;
public class ShaderAdminManager {

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                net.minecraft.server.command.CommandManager.literal("shaderadmin")
                        .then(net.minecraft.server.command.CommandManager.argument("enable", BoolArgumentType.bool())
                                .requires(source -> source.hasPermissionLevel(4))
                                .executes(ShaderAdminManager::execute))
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        boolean enable = BoolArgumentType.getBool(context, "enable");
        for (ServerPlayerEntity player : context.getSource().getServer().getPlayerManager().getPlayerList()) {
            ShaderPayload.sendTo(player, enable);
        }
        context.getSource().sendFeedback(() -> Text.literal("Шейдеры " + (enable ? "включены" : "выключены") + " для всех игроков!"), false);
        return 1;
    }
}