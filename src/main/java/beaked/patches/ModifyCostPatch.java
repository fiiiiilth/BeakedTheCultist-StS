package beaked.patches;

import beaked.powers.AwakenedPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ModifyCostPatch {

    // The current rationale is that Awakened Form is the ONLY effect that can reduce card costs into the negatives.
    // If something else reduces the cost of a card with already negative cost, it is ignored.

    @SpirePatch(cls = "com.megacrit.cardcrawl.cards.AbstractCard", method = "modifyCostForTurn")
    public static class ModifyCostForTurn {
        public static SpireReturn Prefix(AbstractCard obj, int amt) {
            // if the costForTurn would be negative, skip it - otherwise it resets to 0.
            if (CardAwakenedPatch.negativeCost.get(obj) && obj.costForTurn + amt < 0){
                if (amt > 0) obj.costForTurn += amt; // increase cost but it's still below 0
                return SpireReturn.Return(null);
            }
            else return SpireReturn.Continue();
        }
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.cards.AbstractCard", method = "modifyCostForCombat")
    public static class ModifyCostForCombat {
        public static SpireReturn Prefix(AbstractCard obj, int amt) {
            // if the costForTurn would be negative, skip it - otherwise it resets to 0.
            // reduce cost (for combat) up to 0. Since Awakened Form only affects costForTurn this doesn't matter.
            if (CardAwakenedPatch.negativeCost.get(obj) && obj.costForTurn + amt < 0){
                if (amt > 0) obj.costForTurn += amt; // increase cost but it's still below 0
                obj.cost -= amt;
                if (obj.cost < 0) obj.cost = 0;
                return SpireReturn.Return(null);
            }
            else return SpireReturn.Continue();
        }
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.cards.AbstractCard", method = "updateCost")
    public static class UpdateCost {
        public static SpireReturn Prefix(AbstractCard obj, int amt) {
            // if the costForTurn would be negative, skip it - otherwise it resets to 0.
            // reduce cost (for combat) up to 0. Since Awakened Form only affects costForTurn this doesn't matter.
            if (CardAwakenedPatch.negativeCost.get(obj) && obj.costForTurn + amt < 0){
                if (amt > 0) obj.costForTurn += amt; // increase cost but it's still below 0
                obj.cost -= amt;
                if (obj.cost < 0) obj.cost = 0;
                return SpireReturn.Return(null);
            }
            else return SpireReturn.Continue();
        }
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.cards.AbstractCard", method = "resetAttributes")
    public static class ResetAttributes {
        public static void Postfix(AbstractCard obj) {
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(AwakenedPower.POWER_ID)){
                AwakenedPower pow = ((AwakenedPower)AbstractDungeon.player.getPower(AwakenedPower.POWER_ID));
                pow.awakenModifyCostForTurn(obj,-pow.amount);
            }
        }
    }
}