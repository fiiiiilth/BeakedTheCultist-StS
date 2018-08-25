package beaked.patches;

import beaked.cards.AbstractWitherCard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

@SpirePatch(cls="com.megacrit.cardcrawl.helpers.CardLibrary", method = "getCopy", paramtypes = {"java.lang.String","int","int"})
public class SaveMiscValuePatch {
    public static AbstractCard Postfix(AbstractCard retVal, final String key, final int upgradeTime, final int misc) {
        if ((retVal.cardID.startsWith("beaked:") && retVal.misc != 0) || retVal instanceof AbstractWitherCard) {
            retVal.misc = misc;
            retVal.applyPowers();
        }
        return retVal;
    }
}