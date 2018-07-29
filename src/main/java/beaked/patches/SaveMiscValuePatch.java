package beaked.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

@SpirePatch(cls="com.megacrit.cardcrawl.helpers.CardLibrary", method = "getCopy")
public class SaveMiscValuePatch {
    public static AbstractCard Postfix(AbstractCard retVal, final String key, final int upgradeTime, final int misc) {
        if ((retVal.misc = misc) != 0) {
            retVal.applyPowers();
        }
        return retVal;
    }
}