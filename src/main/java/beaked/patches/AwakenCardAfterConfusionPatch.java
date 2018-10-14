package beaked.patches;

import beaked.cards.AwakenedForm;
import beaked.powers.AwakenedPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(clz = AbstractPlayer.class, method = "draw", paramtypez = {int.class})
public class AwakenCardAfterConfusionPatch {
    @SpireInsertPatch(locator = TriggerWhenDrawnLocator.class, localvars = {"c"})
    public static void atSingleFinalDamageGive(AbstractPlayer obj, int numCards, AbstractCard c) {
        try {
            if (AbstractDungeon.player.hasPower(AwakenedPower.POWER_ID) && AbstractDungeon.player.hasPower(ConfusionPower.POWER_ID)){
                if (c.cost >= 0) c.costForTurn = c.cost; // cost is set by confusion right before this
                ((AwakenedPower)AbstractDungeon.player.getPower(AwakenedPower.POWER_ID)).awakenSpecificCard(c,true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class TriggerWhenDrawnLocator extends SpireInsertLocator {

        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {

            Matcher finalMatcher = new Matcher.MethodCallMatcher(
                    AbstractCard.class, "triggerWhenDrawn");

            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
        }
    }
}