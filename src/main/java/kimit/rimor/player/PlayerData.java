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
			Codec.LONG.fieldOf("cash").forGetter(PlayerData::getCash),
			Codec.BYTE.fieldOf("storage_row").forGetter(PlayerData::getStorageRow),
			Codec.list(ItemStack.CODEC).fieldOf("storage").forGetter(PlayerData::getStorage)
	).apply(instance, PlayerData::new));
	public static final int STORAGE_SLOT_PER_LINE = 9;
	public static final int STORAGE_MAX_LINE = 6;
	private String Username;
	private long Cash;
	private byte StorageRow;
	private List<ItemStack> Storage;
	
	public PlayerData(String username, long cash, byte storageRow, List<ItemStack> storage)
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
	
	public long getCash()
	{
		return Cash;
	}
	
	public void setCash(long cash)
	{
		Cash = cash;
	}
	
	public byte getStorageRow()
	{
		return StorageRow;
	}
	
	public void setStorageRow(byte row)
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
