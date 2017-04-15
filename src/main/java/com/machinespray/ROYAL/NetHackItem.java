package com.machinespray.ROYAL;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NetHackItem extends Item {

	public NetHackItem(String unlocalizedName) {
		this.setUnlocalizedName(unlocalizedName);
		this.setRegistryName("royal", unlocalizedName);
	}

	public void register() {
		GameRegistry.register(this);
	}

	@SideOnly(Side.CLIENT)
	public void registerClient() {
		Minecraft
				.getMinecraft()
				.getRenderItem()
				.getItemModelMesher()
				.register(
						this,
						0,
						new ModelResourceLocation(getRegistryName(),
								"inventory"));
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		NBTTagCompound nbt = new NBTTagCompound();
		if (stack.getTagCompound() != null)
			nbt = stack.getTagCompound();
		switch (Main.random.nextInt(10)) {
		case 8:
			nbt.setString("BUC", "blessed");
			break;
		case 9:
			nbt.setString("BUC", "cursed");
			break;
		default:
			nbt.setString("BUC", "uncursed");
		}
		stack.setTagCompound(nbt);
		ArrayList<String> info = new ArrayList<String>();
		info.add(stack.getTagCompound().getString("BUC"));

	}
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
		if(stack.getTagCompound()!=null)
		if(stack.getTagCompound().getString("BUC")!=null)
		tooltip.add(stack.getTagCompound().getString("BUC"));
	}
}
