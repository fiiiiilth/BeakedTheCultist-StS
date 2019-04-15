package beaked.patches;

import basemod.ReflectionHacks;
import beaked.Beaked;
import beaked.characters.BeakedTheCultist;
import beaked.ui.ReverseWitherOption;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.FaceTrader;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.CultistMask;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import java.lang.reflect.Field;
import java.util.ArrayList;

@SpirePatch(clz= FaceTrader.class,
            method="buttonEffect")

public class FaceTraderPatch {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("beaked:FaceTrader");

    public static void Postfix(FaceTrader meObj, final int buttonPressed) {
        if (!(AbstractDungeon.player instanceof BeakedTheCultist)) return;

        try {
            Field screenField = FaceTrader.class.getDeclaredField("screen");
            screenField.setAccessible(true);
            //FaceTrader.class.getEnumConstants()[0].getClass().getDeclaredMethod("get")
            //Field curScreenEnumField = FaceTrader.class.getDeclaredField("CurScreen");
            //curScreenEnumField.setAccessible(true);
            switch (screenField.get(meObj).toString()) {
                case "MAIN":
                    meObj.imageEventText.updateDialogOption(1,uiStrings.TEXT[0]);
                    break;

                case "RESULT":
                    if (buttonPressed == 1 && !AbstractDungeon.player.hasRelic(CultistMask.ID)) AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f, RelicLibrary.getRelic(CultistMask.ID).makeCopy());
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
