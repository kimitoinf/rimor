package kimit.rimor.client;

import kimit.rimor.client.registry.CommandRegistry;
import net.fabricmc.api.ClientModInitializer;

public class RimorClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		CommandRegistry.Initialize();
	}
}