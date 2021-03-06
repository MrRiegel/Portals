package mrriegel.classicportals.blocks;

import java.util.Random;

import mrriegel.classicportals.items.Upgrade;
import mrriegel.classicportals.tile.TileController;
import mrriegel.classicportals.tile.TilePortaal;
import mrriegel.classicportals.util.PortalEffect;
import mrriegel.limelib.block.CommonBlockContainer;
import mrriegel.limelib.helper.RegistryHelper;
import mrriegel.limelib.helper.TeleportationHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPortaal extends CommonBlockContainer<TilePortaal> {
	public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.<EnumFacing.Axis> create("axis", EnumFacing.Axis.class, new EnumFacing.Axis[] { EnumFacing.Axis.X, EnumFacing.Axis.Z, EnumFacing.Axis.Y });
	protected static final AxisAlignedBB X_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
	protected static final AxisAlignedBB Z_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);
	protected static final AxisAlignedBB Y_AABB = new AxisAlignedBB(0.0D, 0.375D, 0.0D, 1.0D, 0.625D, 1.0D);

	public BlockPortaal() {
		super(Material.PORTAL, "portaal");
		this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.X));
		setBlockUnbreakable();
	}

	@Override
	public void registerBlock() {
		super.registerBlock();
		RegistryHelper.unregister(getItemBlock());
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (state.getValue(AXIS)) {
		case X:
			return X_AABB;
		case Y:
		default:
			return Y_AABB;
		case Z:
			return Z_AABB;
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		if (true)
			return true;
		pos = pos.offset(side);
		EnumFacing.Axis enumfacing$axis = null;

		if (blockState.getBlock() == this) {
			enumfacing$axis = blockState.getValue(AXIS);

			if (enumfacing$axis == null) {
				return false;
			}

			if (enumfacing$axis == EnumFacing.Axis.Z && side != EnumFacing.EAST && side != EnumFacing.WEST) {
				return false;
			}

			if (enumfacing$axis == EnumFacing.Axis.X && side != EnumFacing.SOUTH && side != EnumFacing.NORTH) {
				return false;
			}

			if (enumfacing$axis == EnumFacing.Axis.Y && side != EnumFacing.UP && side != EnumFacing.DOWN) {
				return false;
			}
		}

		boolean flag = blockAccess.getBlockState(pos.west()).getBlock() == this && blockAccess.getBlockState(pos.west(2)).getBlock() != this;
		boolean flag1 = blockAccess.getBlockState(pos.east()).getBlock() == this && blockAccess.getBlockState(pos.east(2)).getBlock() != this;
		boolean flag2 = blockAccess.getBlockState(pos.north()).getBlock() == this && blockAccess.getBlockState(pos.north(2)).getBlock() != this;
		boolean flag3 = blockAccess.getBlockState(pos.south()).getBlock() == this && blockAccess.getBlockState(pos.south(2)).getBlock() != this;
		boolean flag4 = flag || flag1 || enumfacing$axis == EnumFacing.Axis.X;
		boolean flag5 = flag2 || flag3 || enumfacing$axis == EnumFacing.Axis.Z;
		return flag4 && side == EnumFacing.WEST ? true : (flag4 && side == EnumFacing.EAST ? true : (flag5 && side == EnumFacing.NORTH ? true : flag5 && side == EnumFacing.SOUTH));
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return ItemStack.EMPTY;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(AXIS, Axis.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(AXIS).ordinal();
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return 11;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (rand.nextInt(100) == 0) {
			worldIn.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
		}
		if (((TilePortaal) worldIn.getTileEntity(pos)).getController() == null)
			return;
		TileController tile = (TileController) worldIn.getTileEntity(((TilePortaal) worldIn.getTileEntity(pos)).getController());
		if (tile == null)
			return;
		if (tile.getUpgrades().contains(Upgrade.PARTICLE))
			for (int i = 0; i < 4; ++i) {
				double d0 = pos.getX() + rand.nextFloat();
				double d1 = pos.getY() + rand.nextFloat();
				double d2 = pos.getZ() + rand.nextFloat();
				double d3 = (rand.nextFloat() - 0.5D) * 0.5D;
				double d4 = (rand.nextFloat() - 0.5D) * 0.5D;
				double d5 = (rand.nextFloat() - 0.5D) * 0.5D;
				int j = rand.nextInt(2) * 2 - 1;

				if (tile.getAxis() == Axis.Z) {
					d0 = pos.getX() + 0.5D + 0.25D * j;
					d3 = rand.nextFloat() * 2.0F * j;
				} else if (tile.getAxis() == Axis.X) {
					d2 = pos.getZ() + 0.5D + 0.25D * j;
					d5 = rand.nextFloat() * 2.0F * j;
				} else {
					d1 = pos.getY() + 0.5D + 0.25D * j;
					d4 = rand.nextFloat() * 2.0F * j;
				}
				Minecraft.getMinecraft().effectRenderer.addEffect(new PortalEffect(worldIn, d0, d1, d2, d3, d4, d5, tile.getColorParticle()));
			}

	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { AXIS });
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (TeleportationHelper.canTeleport(entityIn)) {
			if (worldIn.getTileEntity(pos) instanceof TilePortaal && ((TilePortaal) worldIn.getTileEntity(pos)).getController() != null && worldIn.getTileEntity(((TilePortaal) worldIn.getTileEntity(pos)).getController()) instanceof TileController) {
				TileController tile = (TileController) worldIn.getTileEntity(((TilePortaal) worldIn.getTileEntity(pos)).getController());
				if (tile.getTarget() != null) {
					//					if (entityIn.getEntityData().getInteger("untilPort") <= 0) {
					//						tile.teleport(entityIn);
					//					}
					if (!entityIn.getEntityData().getBoolean("portForbidden")) {
						tile.teleport(entityIn);
					}
				}
			}
		}
		//		entityIn.getEntityData().setInteger("untilPort", TileController.untilPort);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (worldIn.getTileEntity(pos) instanceof TilePortaal && ((TilePortaal) worldIn.getTileEntity(pos)).getController() != null && worldIn.getTileEntity(((TilePortaal) worldIn.getTileEntity(pos)).getController()) instanceof TileController) {
			((TileController) worldIn.getTileEntity(((TilePortaal) worldIn.getTileEntity(pos)).getController())).validatePortal();
		} else
			worldIn.setBlockToAir(pos);
	}

	@Override
	protected Class<? extends TilePortaal> getTile() {
		return TilePortaal.class;
	}

}
