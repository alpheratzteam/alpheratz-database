package me.hp888.database.api.packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author hp888 on 19.04.2020.
 */

public final class PacketRegistry
{
    private final Map<PacketDirection, BiMap<Integer, Class<? extends Packet>>> packetRegistry;

    public PacketRegistry() {
        packetRegistry = new HashMap<>();
    }

    public int getPacketId(final PacketDirection packetDirection, final Class<? extends Packet> packetClass) {
        final BiMap<Class<? extends Packet>, Integer> packetMap = packetRegistry.get(packetDirection).inverse();
        if (Objects.isNull(packetMap) || !packetMap.containsKey(packetClass))
            return -1;

        return packetMap.get(packetClass);
    }

    public Packet newInstance(final PacketDirection packetDirection, final int packetId) {
        try {
            final BiMap<Integer, Class<? extends Packet>> packetMap = packetRegistry.get(packetDirection);
            if (Objects.isNull(packetMap))
                throw new RuntimeException("invalid-connection-state");

            final Constructor<?> constructor = packetMap.get(packetId).getConstructor();
            if (Objects.isNull(constructor))
                throw new RuntimeException("invalid-packet-id");

            return (Packet) constructor.newInstance();
        } catch (final InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException ex) {
            throw new RuntimeException("something-went-wrong");
        }
    }

    public void registerPacket(final PacketDirection packetDirection, final int id, final Class<? extends Packet> packetClass) {
        final BiMap<Integer, Class<? extends Packet>> packetMap = packetRegistry.computeIfAbsent(packetDirection, dir -> HashBiMap.create());
        if (packetMap.containsKey(id))
            throw new IllegalArgumentException("Packet with this id is already registered! (id=" + id + ", class=" + packetMap.get(id).getSimpleName() + ")");

        packetMap.put(id, packetClass);
    }
}