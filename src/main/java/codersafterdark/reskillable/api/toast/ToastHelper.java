package codersafterdark.reskillable.api.toast;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToastHelper {
    //This technically could work for any IToast but as a helper method might as well make sure it is an AbstractToast
    //in case we decide to modify this method to do more things
    @SideOnly(Side.CLIENT)
    public static void sendToast(AbstractToast toast) {
        Minecraft.getMinecraft().getToastGui().add(toast);
    }

    public static void sendUnlockableToast(Unlockable u) {
        sendToast(new UnlockableToast(u));
    }

    public static void sendSkillToast(Skill skill, int level) {
        sendToast(new SkillToast(skill, level));
    }
}