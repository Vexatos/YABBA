package com.latmod.yabba.item.upgrade;

import com.feed_the_beast.ftbl.lib.NameMap;
import com.feed_the_beast.ftbl.lib.config.PropertyEnum;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import com.feed_the_beast.ftbl.lib.tile.EnumSaveType;
import com.feed_the_beast.ftbl.lib.util.DataStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

/**
 * @author LatvianModder
 */
public class ItemUpgradeRedstone extends ItemUpgrade
{
	public enum Mode implements IStringSerializable
	{
		EQUAL("=="),
		NOT("!="),
		GREATER_THAN(">"),
		GREATER_THAN_OR_EQUAL(">="),
		LESS_THAN("<"),
		LESS_THAN_OR_EQUAL("<=");

		public static final NameMap<Mode> NAME_MAP = NameMap.create(GREATER_THAN_OR_EQUAL, values());

		private final String name;

		Mode(String s)
		{
			name = s;
		}

		@Override
		public String getName()
		{
			return name;
		}

		public boolean matchesCount(int items1, int items2)
		{
			switch (this)
			{
				case EQUAL:
					return items1 == items2;
				case NOT:
					return items1 != items2;
				case GREATER_THAN:
					return items1 > items2;
				case GREATER_THAN_OR_EQUAL:
					return items1 >= items2;
				case LESS_THAN:
					return items1 < items2;
				case LESS_THAN_OR_EQUAL:
					return items1 <= items2;
				default:
					return false;
			}
		}
	}

	public static class Data extends DataStorage
	{
		public PropertyEnum<Mode> mode = new PropertyEnum<>(Mode.NAME_MAP);
		public PropertyInt count = new PropertyInt(1);

		@Override
		public void serializeNBT(NBTTagCompound nbt, EnumSaveType type)
		{
			Mode.NAME_MAP.writeToNBT(nbt, "Mode", type, mode);
			nbt.setInteger("Count", count.getInt());
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt, EnumSaveType type)
		{
			mode.setValue(Mode.NAME_MAP.readFromNBT(nbt, "Mode", type));
			count.setInt(nbt.getInteger("Count"));
		}

		public int redstoneOutput(EnumFacing facing, int amount)
		{
			return mode.getValue().matchesCount(amount, count.getInt()) ? 15 : 0;
		}
	}

	public ItemUpgradeRedstone(String id)
	{
		super(id);
	}

	@Override
	public DataStorage createBarrelUpgradeData()
	{
		return new Data();
	}
}