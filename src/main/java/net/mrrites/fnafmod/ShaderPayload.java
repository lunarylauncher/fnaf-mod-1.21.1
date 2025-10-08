package net.mrrites.fnafmod;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

/**
 * Класс для отправки простого пакетного сообщения (включить/выключить шейдеры).
 */
public class ShaderPayload {
    public static final Identifier ID = Identifier.of("fnafmod", "shader_admin");

    // Отправка с сервера на клиента
    public static void sendTo(ServerPlayNetworking.Player player, boolean enable) {
        PacketByteBuf buf = new PacketByteBuf(net.minecraft.network.PacketByteBufAllocator.DEFAULT.buffer());
        buf.writeBoolean(enable);
        ServerPlayNetworking.send(player, ID, buf);
    }

    // Регистрация обработчика на клиенте
    public static void registerClient(java.util.function.BiConsumer<net.minecraft.client.MinecraftClient, Boolean> handler) {
        ClientPlayNetworking.registerGlobalReceiver(ID, (client, packetBuf) -> {
            boolean enable = packetBuf.readBoolean();
            handler.accept(client, enable);
        });
    }
}