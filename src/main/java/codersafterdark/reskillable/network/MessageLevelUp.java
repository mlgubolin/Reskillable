package codersafterdark.reskillable.network;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.base.PlayerData;
import codersafterdark.reskillable.base.PlayerDataHandler;
import codersafterdark.reskillable.base.PlayerSkillInfo;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageLevelUp implements IMessage, IMessageHandler<MessageLevelUp, IMessage> {
    
    public ResourceLocation skillName;
    
    public MessageLevelUp() {
    }
    
    public MessageLevelUp(ResourceLocation skillName) {
        this.skillName = skillName;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        skillName = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, skillName.toString());
    }
    
    @Override
    public IMessage onMessage(MessageLevelUp message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handleMessage(message, ctx));
        return null;
    }
    
    public IMessage handleMessage(MessageLevelUp message, MessageContext context) {
        EntityPlayer player = context.getServerHandler().player;
        Skill skill = ReskillableRegistries.SKILLS.getValue(message.skillName);
        PlayerData data = PlayerDataHandler.get(player);
        PlayerSkillInfo info = data.getSkillInfo(skill);
        if(!info.isCapped()) {
            int cost = info.getLevelUpCost();
            if(player.experienceLevel >= cost || player.isCreative()) {
                if(!player.isCreative()) {
                    player.addExperienceLevel(-cost);
                }
                info.levelUp();
                data.saveAndSync();
            }
        }
        return null;
    }
    
    
}
