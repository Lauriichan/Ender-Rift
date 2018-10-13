package gigaherz.enderRift.rift;

import gigaherz.common.ItemRegistered;
import gigaherz.enderRift.EnderRiftMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemEnderRift extends ItemRegistered
{
    public ItemEnderRift(String name)
    {
        super(name);
        this.maxStackSize = 16;
        this.setCreativeTab(EnderRiftMod.tabEnderRift);
    }

    @Override
    public String getTranslationKey(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || !tag.hasKey("RiftId"))
            return this.getTranslationKey() + ".empty";
        else
            return this.getTranslationKey() + ".bound";
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey("RiftId"))
        {
            tooltip.add("Rift ID: " + tag.getInteger("RiftId"));
        }
    }


    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = playerIn.getHeldItem(hand);

        if (worldIn.isRemote)
            return EnumActionResult.SUCCESS;

        if (!playerIn.canPlayerEdit(pos, facing, stack))
            return EnumActionResult.PASS;

        IBlockState state = worldIn.getBlockState(pos);

        if (state.getBlock() != EnderRiftMod.rift)
            return EnumActionResult.PASS;

        if (state.getValue(BlockEnderRift.ASSEMBLED))
        {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null || !tag.hasKey("RiftId"))
            {
                if (!RiftStructure.duplicateOrb(worldIn, pos, playerIn))
                    return EnumActionResult.PASS;
            }
        }
        else
        {
            if (!RiftStructure.assemble(worldIn, pos, stack))
                return EnumActionResult.PASS;
        }

        if (!playerIn.capabilities.isCreativeMode)
        {
            stack.shrink(1);
        }

        return EnumActionResult.SUCCESS;
    }
}