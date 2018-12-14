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
            ArrayList<AbstractCampfireOption> campfireButtons = (ArrayList<AbstractCampfireOption>)
                    ReflectionHacks.getPrivate(campfire, CampfireUI.class, "buttons");

            if(AbstractDungeon.player instanceof BeakedTheCultist || Beaked.hasWitherCards()) {
                campfireButtons.add(new ReverseWitherOption());
                float x = 950.f;
                float y = 990.0f - (270.0f * (float)((campfireButtons.size() + 1) / 2));
                if (campfireButtons.size() % 2 == 0) {
                    x = 1110.0f;
                    campfireButtons.get(campfireButtons.size() - 2).setPosition(800.0f * Settings.scale, y * Settings.scale);
                }
                campfireButtons.get(campfireButtons.size() - 1).setPosition(x * Settings.scale, y * Settings.scale);
            }

        } catch (SecurityException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
