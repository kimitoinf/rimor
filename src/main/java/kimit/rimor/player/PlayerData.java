package kimit.rimor.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class PlayerData
{
	public static final Codec<PlayerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("username").forGetter(PlayerData::getUsername),
			Codec.INT.fieldOf("cash").forGetter(PlayerData::getCash)
	).apply(instance, PlayerData::new));
	private String Username;
	private int Cash;
	
	public PlayerData(String username, int cash)
	{
		Username = username;
		Cash = cash;
	}
	
	public PlayerData(String username)
	{
		Username = username;
		Cash = 0;
	}
	
	public PlayerData(String username, PlayerData data)
	{
		Username = username;
		Cash = data.getCash();
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
}
