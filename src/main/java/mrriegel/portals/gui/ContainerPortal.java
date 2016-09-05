package mrriegel.portals.gui;

import java.util.List;

import mrriegel.limelib.gui.CommonContainer;
import mrriegel.portals.init.ModItems;
import mrriegel.portals.tile.TileController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

import com.google.common.collect.Lists;

public class ContainerPortal extends CommonContainer {

	public TileController tile;
	IInventory tmp;

	public ContainerPortal(final TileController tile, InventoryPlayer invPlayer) {
		super(invPlayer, InvEntry.of("tile", new InventoryBasic("null", false, tile.getStacks().length)));
		this.tile = tile;
		tmp = invs.get("tile");
		for (int i = 0; i < tile.getStacks().length; i++) {
			ItemStack k = tile.getStacks()[i];
			tmp.setInventorySlotContents(i, k);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tile.isUseableByPlayer(playerIn);
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		inventoryChanged();
	}

	@Override
	protected void inventoryChanged() {
		for (int i = 0; i < tile.getStacks().length; i++) {
			tile.getStacks()[i] = tmp.getStackInSlot(i);
		}
		tile.sync();
	}

	@Override
	protected void initSlots() {
		initPlayerSlots(8, 156);
		setSlots(invs.get("tile"), 8, 8, 1, 8);
	}

	@Override
	protected List<Area> allowedSlots(ItemStack stack, IInventory inv, int index) {
		List<Area> lis = Lists.newArrayList();
		lis.add(inv == invPlayer ? stack.getItem() == ModItems.upgrade ? getAreaforEntire(tmp) : null : getAreaforEntire(invPlayer));
		return lis;
	}

}
