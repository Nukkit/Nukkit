package cn.nukkit.network.protocol;

import cn.nukkit.command.data.CommandDataVersions;
import cn.nukkit.command.data.CommandOverload;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.BinaryStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AvailableCommandsPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.AVAILABLE_COMMANDS_PACKET;
    public Map<String, CommandDataVersions> commands;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() { //TODO: check if this is able to be simplified
        this.reset();
        List<String> enumValues = new ArrayList<>();
        Map<String, Integer> enumAdditional = new HashMap<>();
        List<Object[]> enums = new ArrayList<>();
        BinaryStream commandsStream = new BinaryStream();
        commands.forEach((name, versions) -> {
            if (name.equals("help")) { //temp fix for 1.2
                return;
            }
            commandsStream.putString(name);
            commandsStream.putString(versions.versions.get(0).description);
            commandsStream.putByte((byte) 0); //flags
            commandsStream.putByte((byte) 0); //permission level
            int aliasEnumId;
            if (versions.versions.get(0).aliases != null && versions.versions.get(0).aliases.length != 0) {
                List<Integer> aliases = new ArrayList<>();
                for (String alias : versions.versions.get(0).aliases) {
                    int targetIndex;
                    if (!enumAdditional.containsKey(alias)) {
                        enumValues.add(alias);
                        enumAdditional.put(alias, enumValues.size() - 1);
                        targetIndex = enumValues.size() - 1;
                    } else {
                        targetIndex = enumAdditional.get(alias);
                    }
                    aliases.add(targetIndex);
                }
                enums.add(new Object[]{name, aliases});
                aliasEnumId = enums.size() - 1;
            } else {
                aliasEnumId = -1;
            }
            commandsStream.putLInt(aliasEnumId);
            commandsStream.putVarInt(versions.versions.get(0).overloads.size());
            for (CommandOverload overload : versions.versions.get(0).overloads.values()) {
                commandsStream.putVarInt(overload.input.parameters.length);
                for (CommandParameter parameter : overload.input.parameters) {
                    commandsStream.putString(parameter.name);
                    commandsStream.putLInt(0);
                    commandsStream.putBoolean(parameter.optional);
                }
            }
        });

        this.putVarInt(enumValues.size());
        enumValues.forEach(this::putString);
        this.putVarInt(0);
        this.putVarInt(enums.size());
        enums.forEach(o -> {
            this.putString(o[0] + "CommandAliases");
            List<Integer> data = (List<Integer>) o[1];
            this.putVarInt(data.size());
            data.forEach(this::putVarInt);
        });

        this.putVarInt(commands.size());
        this.put(commandsStream.getBuffer());
    }
}
