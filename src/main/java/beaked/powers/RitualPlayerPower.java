package beaked.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class RitualPlayerPower extends AbstractPower {

    public static final String POWER_ID = "beaked:RitualPlayer";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public RitualPlayerPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;
        updateDescription();
        this.loadRegion("ritual");
        this.canGoNegative = true;
        //this.beaked_img = new Texture("beaked_img/powers/ritual.png");
    }

    @Override
    public void stackPower(final int stackAmount) {
        this.fontScale = 8.0f;
        this.amount += stackAmount;
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        if (this.amount >= 999) {
            this.amount = 999;
        }
        if (this.amount <= -999) {
            this.amount = -999;
        }
    }

    @Override
    public void reducePower(final int reduceAmount) {
        this.fontScale = 8.0f;
        this.amount -= reduceAmount;
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        if (this.amount >= 999) {
            this.amount = 999;
        }
        if (this.amount <= -999) {
            this.amount = -999;
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
    }

    @Override
    public void updateDescription() {
        if (this.amount > 0) {
            this.description = RitualPlayerPower.DESCRIPTIONS[0] + this.amount + RitualPlayerPower.DESCRIPTIONS[2];
            this.type = PowerType.BUFF;
        }
        else{
            final int tmp = -this.amount;
            this.description = RitualPlayerPower.DESCRIPTIONS[1] + tmp + RitualPlayerPower.DESCRIPTIONS[2];
            this.type = PowerType.DEBUFF;
        }
    }

}
