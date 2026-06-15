package com.sydders.mcciutils;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.CyclingListControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.createBuilder()
                .title(Component.literal("MCCI Utils Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("General"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("Auto Instance"))
                                .description(OptionDescription.of(Component.literal("Automatically connects you to a specific instance")))
                                .binding(false,
                                        () -> Config.HANDLER.instance().autoInstance,
                                        val -> Config.HANDLER.instance().autoInstance = val)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Component.literal("Auto Instance Number"))
                                .description(OptionDescription.of(Component.literal("The number of the instance you want to automatically connect to")))
                                .binding(8,
                                        () -> Config.HANDLER.instance().autoInstanceNumber,
                                        val -> Config.HANDLER.instance().autoInstanceNumber = val)
                                .controller(IntegerFieldControllerBuilder::create)
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Fishing"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("Fish HUD"))
                                .description(OptionDescription.of(Component.literal("Displays the fishing hud when catching fish.")))
                                .binding(true,
                                        () -> Config.HANDLER.instance().fishHud,
                                        val -> Config.HANDLER.instance().fishHud = val)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<FishingHudPosition>createBuilder()
                                .name(Component.literal("Fishing Hud Position"))
                                .description(OptionDescription.of(Component.literal("The position of the fishing HUD")))
                                .binding(FishingHudPosition.TOP_LEFT,
                                        () -> Config.HANDLER.instance().fishingHudPosition,
                                        val -> Config.HANDLER.instance().fishingHudPosition = val)
                                .controller(opt -> CyclingListControllerBuilder.create(opt)
                                        .values(List.of(FishingHudPosition.values()))
                                        .formatValue(val -> Component.literal(switch (val) {
                                            case TOP_LEFT -> "Top Left";
                                            case TOP_RIGHT -> "Top Right";
                                            case BOTTOM_LEFT -> "Bottom Left";
                                            case BOTTOM_RIGHT -> "Bottom Right";
                                        })))
                                .build())
                        .build())
                .build()
                .generateScreen(parentScreen);
    }
}
