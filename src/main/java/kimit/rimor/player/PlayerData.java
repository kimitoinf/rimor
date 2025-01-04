package kimit.rimor.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerData
{
	public static final Codec<PlayerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("username").forGetter(PlayerData::getUsername),
			Codec.INT.fieldOf("cash").forGetter(PlayerData::getCash),
			Codec.INT.fieldOf("storage_row").forGetter(PlayerData::getStorageRow),
			Codec.list(ItemStack.CODEC).fieldOf("storage").forGetter(PlayerData::getStorage)
	).apply(instance, PlayerData::new));
	public static final int STORAGE_SLOT_PER_LINE = 9;
	public static final int STORAGE_MAX_LINE = 6;
	private String Username;
	private int Cash;
	private int StorageRow;
	private List<ItemStack> Storage;
	
	public PlayerData(String username, int cash, int storageRow, List<ItemStack> storage)
	{
		Username = username;
		Cash = cash;
		StorageRow = storageRow;
		Storage = storage;
	}
	
	public PlayerData(String username)
	{
		Username = username;
		Cash = 0;
		StorageRow = 1;
		Storage = new ArrayList<>();
	}
	
	public PlayerData(String username, PlayerData data)
	{
		Username = username;
		Cash = data.getCash();
		StorageRow = data.getStorageRow();
		Storage = data.getStorage();
	}
	
	public String getUsername()
	{
		return Username;
	}
	
	public void setUsername(String username)
	{
		Username = username;
	}
	
	public int getCash()
	{
		return Cash;
	}
	
	public void setCash(int cash)
	{
		Cash = cash;
	}
	
	public int getStorageRow()
	{
		return StorageRow;
	}
	
	public void setStorageRow(int row)
	{
		StorageRow = row;
	}
	
	public List<ItemStack> getStorage()
	{
		return Storage;
	}
	
	public void setStorage(List<ItemStack> storage)
	{
		Storage = storage;
	}
}
