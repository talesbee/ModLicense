package com.example.modlicense.neoforge;

import com.example.modlicense.common.ProtocolVersion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("modlicense")
public final class ModLicenseNeoForge {
    private static final Logger LOGGER = LoggerFactory.getLogger("modlicense");

    public ModLicenseNeoForge(IEventBus modBus, ModContainer container) {
        LOGGER.info("ModLicense NeoForge spike loaded, protocol {}", ProtocolVersion.CURRENT);
    }
}
