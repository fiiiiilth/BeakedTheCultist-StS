package beaked.patches;

import beaked.powers.AwakenedPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(cls="com.megacrit.cardcrawl.cards.AbstractCard", method = "makeStatEquivalentCopy")
public class CopyAwakenedPatch{
    public static AbstractCard Postfix(AbstractCard retVal, AbstractCard obj) {
        // when an awakened card makes a copy in combat, we also want the copy to be marked as awakened
        // so the cost reduction doesn't apply twice.
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(AwakenedPower.POWER_ID) &&
                ((AwakenedPower)AbstractDungeon.player.getPower(AwakenedPower.POWER_ID)).awakenedCards.contains(obj)){
            ((AwakenedPower)AbstractDungeon.player.getPower(AwakenedPower.POWER_ID)).awakenedCards.add(retVal);
        }
        CardAwakenedPatch.negativeCost.set(retVal, CardAwakenedPatch.negativeCost.get(obj));
        return retVal;
    }
}