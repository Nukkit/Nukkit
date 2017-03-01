package cn.nukkit.network.protocol;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.utils.Zlib;


/**
 * Created by on 15-10-13.
 */
public class LoginPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.LOGIN_PACKET;

    public String username;
    public int protocol;
    public byte gameEdition;
    public UUID clientUUID;
    public String identityPublicKey;

    public Skin skin;

    public int ADRole;
    public long clientId;
    public int CurrentInputMode;
    public int DefaultInputMode;
    public String DeviceModel;
    public int DeviceOS;
    public String GameVersion;
    public int GuiScale;
    public String serverAddress;
    public String TenantId;
    public int UIProfile;

    public boolean isXbox = false;

    public static final int GUI_CLASSIC = 0;
    public static final int GUI_POCKET = 1;

    public final String MOJANG_PUBKEY = "MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAE8ELkixyLcwlZryUQcu1TvPOmI2B7vX83ndnWRUaXm74wFfa5f/lwQNTfrLVHa2PmenpGI6JhIMUJaWZrjmMj90NoKNFSNBuKdm8rYiXsfaz3K36x/1U26HpG0ZxK/V1V";

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.protocol = this.getInt();
        this.gameEdition = (byte) this.getByte();
        byte[] str;
        try {
            str = Zlib.inflate(this.get((int) this.getUnsignedVarInt()));
        } catch (Exception e) {
            return;
        }
        this.setBuffer(str, 0);
        decodeChainData();
        decodeSkinData();
    }

    @Override
    public void encode() {

    }

    public int getProtocol() {
        return protocol;
    }

    private void decodeChainData() {
        Map<String, List<String>> map = new Gson().fromJson(new String(this.get(getLInt()), StandardCharsets.UTF_8),
                new TypeToken<Map<String, List<String>>>() {
                }.getType());
        if (map.isEmpty() || !map.containsKey("chain") || map.get("chain").isEmpty()) return;
        List<String> chains = map.get("chain");
        for (String c : chains) {
            JsonObject chainMap = decodeToken(c);
            if (chainMap == null) continue;
            if (chainMap.has("extraData")) {
                JsonObject extra = chainMap.get("extraData").getAsJsonObject();
                if (extra.has("displayName")) this.username = extra.get("displayName").getAsString();
                if (extra.has("identity")) this.clientUUID = UUID.fromString(extra.get("identity").getAsString());
            }
            if (chainMap.has("identityPublicKey")){
                this.identityPublicKey = chainMap.get("identityPublicKey").getAsString();
                if(this.identityPublicKey.equals(MOJANG_PUBKEY)) this.isXbox = true;
            }
        }
    }

    private void decodeSkinData() {
        JsonObject skinToken = decodeToken(new String(this.get(this.getLInt())));
        String skinId = null;
        if (skinToken.has("ADRole")) this.ADRole = skinToken.get("ADRole").getAsInt();
        if (skinToken.has("ClientRandomId")) this.clientId = skinToken.get("ClientRandomId").getAsLong();
        if (skinToken.has("CurrentInputMode")) this.CurrentInputMode = skinToken.get("CurrentInputMode").getAsInt();
        if (skinToken.has("DefaultInputMode")) this.DefaultInputMode = skinToken.get("DefaultInputMode").getAsInt();
        if (skinToken.has("DeviceModel")) this.DeviceModel = skinToken.get("DeviceModel").getAsString();
        if (skinToken.has("DeviceOS")) this.DeviceOS = skinToken.get("DeviceOS").getAsInt();
        if (skinToken.has("GameVersion")) this.GameVersion = skinToken.get("GameVersion").getAsString();
        if (skinToken.has("GuiScale")) this.GuiScale = skinToken.get("GuiScale").getAsInt();
        if (skinToken.has("ServerAddress")) this.serverAddress = skinToken.get("ServerAddress").getAsString();
        if (skinToken.has("SkinId")) skinId = skinToken.get("SkinId").getAsString();
        if (skinToken.has("SkinData")) this.skin = new Skin(skinToken.get("SkinData").getAsString(), skinId);
        if (skinToken.has("TenantId")) this.TenantId = skinToken.get("TenantId").getAsString();
        if (skinToken.has("UIProfile")) this.UIProfile = skinToken.get("UIProfile").getAsInt();
    }

    private JsonObject decodeToken(String token) {
        String[] base = token.split("\\.");
        if (base.length < 2) return null;
        return new Gson().fromJson(new String(Base64.getDecoder().decode(base[1]), StandardCharsets.UTF_8), JsonObject.class);
    }

    @Override
    public Skin getSkin() {
        return this.skin;
    }
}
