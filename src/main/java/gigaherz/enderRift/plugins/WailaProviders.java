package gigaherz.enderRift.plugins;
/*
import gigaherz.enderRift.EnderRiftMod;
import gigaherz.enderRift.automation.TileAggregator;
import gigaherz.enderRift.automation.driver.TileDriver;
import gigaherz.enderRift.generator.TileGenerator;
import gigaherz.enderRift.rift.BlockStructure;
import gigaherz.enderRift.rift.TileEnderRift;
import gigaherz.enderRift.rift.TileEnderRiftCorner;
import gigaherz.graph2.Graph;
import gigaherz.graph2.GraphObject;
import mcp.mobius.waila.api.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import java.util.List;

@WailaPlugin
public class WailaProviders implements IWailaPlugin
{
    private static final String CONFIG_GENERATOR = EnderRiftMod.MODID + ".generator";
    private static final String CONFIG_RIFT = EnderRiftMod.MODID + ".rift";
    private static final String CONFIG_DRIVER = EnderRiftMod.MODID + ".driver";
    private static final String CONFIG_RF = EnderRiftMod.MODID + ".rf";


    @Override
    public void register(IWailaRegistrar registrar)
    {
        registrar.addConfig("Ender-Rift", CONFIG_GENERATOR);
        registrar.addConfig("Ender-Rift", CONFIG_RIFT);
        registrar.addConfig("Ender-Rift", CONFIG_DRIVER);
        registrar.addConfig("Ender-Rift", CONFIG_RF);

        {
            RiftTooltipProvider instance = new RiftTooltipProvider();
            registrar.registerBodyProvider(instance, TileEnderRift.class);
            registrar.registerNBTProvider(instance, TileEnderRift.class);
            registrar.registerStackProvider(instance, TileEnderRiftCorner.class);
            registrar.registerBodyProvider(instance, TileEnderRiftCorner.class);
            registrar.registerNBTProvider(instance, TileEnderRiftCorner.class);
        }

        {
            StructureTooltipProvider instance = new StructureTooltipProvider();
            registrar.registerStackProvider(instance, BlockStructure.class);
        }

        {
            NetworkTooltipProvider instance = new NetworkTooltipProvider();
            registrar.registerBodyProvider(instance, TileAggregator.class);
        }

        {
            DriverTooltipProvider instance = new DriverTooltipProvider();
            registrar.registerBodyProvider(instance, TileDriver.class);
            registrar.registerNBTProvider(instance, TileDriver.class);
        }

        {
            GeneratorTooltipProvider instance = new GeneratorTooltipProvider();
            registrar.registerBodyProvider(instance, TileGenerator.class);
            registrar.registerNBTProvider(instance, TileGenerator.class);
        }
    }

    public static class GeneratorTooltipProvider implements IWailaDataProvider
    {
        @Override
        public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            return ItemStack.EMPTY;
        }

        @Override
        public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            return currenttip;
        }

        @Override
        public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            if (config.getConfig(CONFIG_GENERATOR))
            {
                CompoundNBT tag = accessor.getNBTData();

                if (tag.getInteger("powerGen") > 0)
                {
                    currenttip.add(I18n.format("text.enderrift.generator.status.generating", tag.getInteger("powerGen")));
                }
                else if (tag.getBoolean("isBurning"))
                {
                    currenttip.add(I18n.format("text.enderrift.generator.status.heating"));
                }
                else
                {
                    currenttip.add(I18n.format("text.enderrift.generator.status.idle"));
                }

                currenttip.add(I18n.format("text.enderrift.generator.heat", tag.getInteger("heat")));

                if (config.getConfig(CONFIG_RF))
                    currenttip.add(I18n.format("text.enderrift.generator.energy", tag.getInteger("energy"), TileGenerator.POWER_LIMIT));
            }

            return currenttip;
        }

        @Override
        public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            return currenttip;
        }

        @Override
        public CompoundNBT getNBTData(ServerPlayerEntity player, TileEntity te, CompoundNBT tag, World world, BlockPos pos)
        {
            TileGenerator rift = (TileGenerator) te;

            tag.setBoolean("isBurning", rift.isBurning());
            tag.setInteger("powerGen", rift.getGenerationPower());
            tag.setInteger("energy", rift.getContainedEnergy());
            tag.setInteger("heat", rift.getHeatValue());

            return tag;
        }
    }

    public static class DriverTooltipProvider implements IWailaDataProvider
    {
        @Override
        public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            return ItemStack.EMPTY;
        }

        @Override
        public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            return currenttip;
        }

        @Override
        public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            if (config.getConfig(CONFIG_DRIVER))
            {
                CompoundNBT tag = accessor.getNBTData();

                if (config.getConfig(CONFIG_RF))
                    currenttip.add(I18n.format("text.enderrift.generator.energy", tag.getInteger("energy"), TileDriver.POWER_LIMIT));
            }

            return currenttip;
        }

        @Override
        public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            return currenttip;
        }

        @Override
        public CompoundNBT getNBTData(ServerPlayerEntity player, TileEntity te, CompoundNBT tag, World world, BlockPos pos)
        {
            TileDriver rift = (TileDriver) te;

            tag.setInteger("energy", rift.getInternalBuffer().getEnergyStored());

            return tag;
        }
    }

    public static class NetworkTooltipProvider implements IWailaDataProvider
    {
        @Override
        public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            return ItemStack.EMPTY;
        }

        @Override
        public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            return currenttip;
        }

        @Override
        public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            Graph network = ((GraphObject) accessor.getTileEntity()).getGraph();
            currenttip.add(String.format("Network size: %d", network.getObjects().size()));

            return currenttip;
        }

        @Override
        public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            return currenttip;
        }

        @Override
        public CompoundNBT getNBTData(ServerPlayerEntity player, TileEntity te, CompoundNBT tag, World world, BlockPos pos)
        {
            return tag;
        }
    }

    public static class StructureTooltipProvider implements IWailaDataProvider
    {
        @Override
        public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            return new ItemStack(EnderRiftMod.structure);
        }

        @Override
        public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            return currenttip;
        }

        @Override
        public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            return currenttip;
        }

        @Override
        public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            return currenttip;
        }

        @Override
        public CompoundNBT getNBTData(ServerPlayerEntity player, TileEntity te, CompoundNBT tag, World world, BlockPos pos)
        {
            return tag;
        }
    }

    public static class RiftTooltipProvider implements IWailaDataProvider
    {
        @Override
        public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            return new ItemStack(EnderRiftMod.rift);
        }

        @Override
        public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            return currenttip;
        }

        @Override
        public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            if (config.getConfig(CONFIG_RIFT))
            {
                CompoundNBT tag = accessor.getNBTData();

                if (tag != null && tag.hasKey("isFormed"))
                {
                    currenttip.add(I18n.format("text.enderrift.rift.isFormed", tag.getBoolean("isFormed")));
                    currenttip.add(I18n.format("text.enderrift.rift.is_powered", tag.getBoolean("isPowered")));
                    if (tag.getBoolean("isFormed"))
                    {
                        currenttip.add(I18n.format("text.enderrift.rift.rift_id", tag.getInteger("riftId")));
                        if (config.getConfig(CONFIG_RF))
                            currenttip.add(I18n.format("text.enderrift.rift.rf", tag.getInteger("energy"), TileEnderRift.BUFFER_POWER));
                    }
                    currenttip.add(I18n.format("text.enderrift.rift.used_slots", tag.getInteger("usedSlots")));
                }
                else
                {
                    currenttip.add(I18n.format("text.enderrift.rift.waila.isFormed", false));
                }
            }

            return currenttip;
        }

        @Override
        public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
        {
            return currenttip;
        }

        @Override
        public CompoundNBT getNBTData(ServerPlayerEntity player, TileEntity te, CompoundNBT tag, World world, BlockPos pos)
        {
            TileEnderRift rift;

            if (te instanceof TileEnderRiftCorner)
            {
                rift = ((TileEnderRiftCorner) te).getParent();
            }
            else
            {
                rift = (TileEnderRift) te;
            }

            assert rift != null;

            tag.setInteger("usedSlots", rift.countInventoryStacks());
            tag.setBoolean("isFormed", rift.getBlockMetadata() != 0);
            tag.setBoolean("isPowered", rift.isPowered());
            tag.setInteger("riftId", rift.getRiftId());
            tag.setInteger("energy", rift.getEnergyBuffer().getEnergyStored());

            return tag;
        }
    }
}
*/