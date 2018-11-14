package beaked.patches;

import beaked.Beaked;
import beaked.powers.AwakenedPlusPower;
import beaked.powers.AwakenedPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ModifyCostPatch {

    // Cost-modifying actions normally set the cost to 0 if it goes below 0, for obvious reasons.
    // With awakened form+, we need to patch and set a new minimum.

    @SpirePatch(cls = "com.megacrit.cardcrawl.cards.AbstractCard", method = "modifyCostForTurn")
    public static class ModifyCostForTurn {
        public static SpireReturn Prefix(AbstractCard obj, int amt) {
            // allow for reducing costForTurn below 0 if awakened
            if (obj.cost >= 0 && AbstractDungeon.player != null && AbstractDungeon.player.hasPower(AwakenedPlusPower.POWER_ID)){
                // can't be reduced below awakened stack amount
                obj.costForTurn = Math.max(-AbstractDungeon.player.getPower(AwakenedPlusPower.POWER_ID).amount, obj.costForTurn + amt);
                if (obj.costForTurn != obj.cost) {
                    obj.isCostModifiedForTurn = true;
                }
                return SpireReturn.Return(null);
            }
            else return SpireReturn.Continue();
        }
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.cards.AbstractCard", method = "modifyCostForCombat")
    public static class ModifyCostForCombat {
        public static SpireReturn Prefix(AbstractCard obj, int amt) {
            // allow for reducing costForTurn below 0 if awakened
            if (obj.cost >= 0 && AbstractDungeon.player != null && AbstractDungeon.player.hasPower(AwakenedPlusPower.POWER_ID)){
                // can't be reduced below awakened stack amount
                obj.costForTurn = Math.max(-AbstractDungeon.player.getPower(AwakenedPlusPower.POWER_ID).amount, obj.costForTurn + amt);
                if (obj.costForTurn != obj.cost) {
                    obj.isCostModifiedForTurn = true;
                }
                obj.cost = Math.max(0, obj.costForTurn); // actual ModifyCostForCombat logic is more complicated, but this makes the most sense.
                return SpireReturn.Return(null);
            }
            else return SpireReturn.Continue();
        }
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.cards.AbstractCard", method = "updateCost")
    public static class UpdateCost {
        public static SpireReturn Prefix(AbstractCard obj, int amt) {
            // allow for reducing costForTurn below 0 if awakened
            if (obj.cost >= 0 && AbstractDungeon.player != null && AbstractDungeon.player.hasPower(AwakenedPlusPower.POWER_ID)){
                // can't be reduced below awakened stack amount
                obj.costForTurn = Math.max(-AbstractDungeon.player.getPower(AwakenedPlusPower.POWER_ID).amount, obj.costForTurn + amt);
                if (obj.costForTurn != obj.cost) {
                    obj.isCostModifiedForTurn = true;
                }
                obj.cost = Math.max(0, obj.cost+amt); // Reduces cost in a *slightly* different way than ModifyCostForCombat
                return SpireReturn.Return(null);
            }
            else return SpireReturn.Continue();
        }
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.cards.AbstractCard", method = "resetAttributes")
    public static class ResetAttributes {
        public static void Postfix(AbstractCard obj) {
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(AwakenedPower.POWER_ID)){
                obj.costForTurn = obj.cost; // I have NO freaking idea why this isn't always true already, but it's not.
                // if you want to test, disable this line and try retaining cards - their cost keeps decreasing.
                // costForTurn is being lowered and not reset to cost, even though that shouldn't happen.
                AwakenedPower pow = ((AwakenedPower)AbstractDungeon.player.getPower(AwakenedPower.POWER_ID));
                //Beaked.logger.debug(obj.originalName + " COSTFORTURN: "+obj.costForTurn + " COST: " + obj.cost +  " POW " + -pow.amount);
                pow.awakenModifyCostForTurn(obj,-pow.amount);
            }
        }
    }
}