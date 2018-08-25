package beaked.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(cls="com.megacrit.cardcrawl.characters.AbstractPlayer", method = "useCard")
public class UseAwakenedCardPatch {
    public static void Postfix(AbstractPlayer obj, final AbstractCard c, final AbstractMonster monster, final int energyOnUse) {
        // card cost is skipped entirely in useCard if cost is <= 0.
        // costs of <0 denote special cards (-1 is X-cost, -2 is unplayable), but 0-cost cards are fair game.
        if (c.cost == 0) obj.energy.use(c.costForTurn);
    }
}