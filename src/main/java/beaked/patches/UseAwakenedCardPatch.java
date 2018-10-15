package beaked.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(cls="com.megacrit.cardcrawl.characters.AbstractPlayer", method = "useCard")
public class UseAwakenedCardPatch {
    public static void Postfix(AbstractPlayer obj, final AbstractCard c, final AbstractMonster monster, final int energyOnUse) {
        // card cost is skipped entirely in useCard if costForTurn is <= 0.
        // we still want to trigger it as long as costForTurn isn't actually = 0.
        if (c.costForTurn < 0 && c.cost >= 0) obj.energy.use(c.costForTurn);
    }
}