package difficultLife;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import difficultLife.utils.ContainerVanityArmor;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class DLServer implements IGuiHandler {
	
	public void clientInfo()
	{
		
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) 
	{
		return new ContainerVanityArmor(player.inventory, player.worldObj.isRemote, player);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	public EntityPlayer getClientPlayer()
	{
		return null;
	}
	
}
