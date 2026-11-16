package com.example.modlicense.forge;

import com.example.modlicense.common.ProtocolVersion;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("modlicense")
public final class ModLicenseForge {
    private static final Logger LOGGER = LoggerFactory.getLogger("modlicense");

    public ModLicenseForge(FMLJavaModLoadingContext context) {
        LOGGER.info("ModLicense Forge spike loaded, protocol {}", ProtocolVersion.CURRENT);
    }
}
