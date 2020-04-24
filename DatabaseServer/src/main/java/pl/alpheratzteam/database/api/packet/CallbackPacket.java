package pl.alpheratzteam.database.api.packet;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hp888 on 20.04.2020.
 */

@Getter
@Setter
public abstract class CallbackPacket extends Packet
{
    protected long callbackId;
    protected boolean response;
}