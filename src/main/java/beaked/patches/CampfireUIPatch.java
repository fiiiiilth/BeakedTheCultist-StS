package beaked.patches;

import basemod.ReflectionHacks;
import beaked.Beaked;
import beaked.characters.BeakedTheCultist;
import beaked.ui.ReverseWitherOption;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import java.util.ArrayList;

@SpirePatch(clz=CampfireUI.class,
        method="initializeButtons")

public class CampfireUIPatch {
    public static void Postfix(Object meObj) {
        CampfireUI campfire = (CampfireUI)meObj;
        try {
            @SuppressWarnings("unchecked")
            ArrayList<AbstractCampfireOption> campfireButtons = (ArrayList<AbstractCampfireOption>)
                    ReflectionHacks.getPrivate(campfire, CampfireUI.class, "buttons");

            int height = 180;

            if(AbstractDungeon.player instanceof BeakedTheCultist) {
                if(Beaked.hasWitheredCards()) {
                    campfireButtons.add(new ReverseWitherOption(true));
                } else {
                    campfireButtons.add(new ReverseWitherOption(false));
                }
                campfireButtons.get(campfireButtons.size() - 1).setPosition(950 * Settings.scale, height * Settings.scale);
            }

        } catch (SecurityException | IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
