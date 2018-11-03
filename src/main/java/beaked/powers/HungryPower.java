package beaked.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class HungryPower extends AbstractPower {

    public static final String POWER_ID = "beaked:Hungry";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public HungryPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;
        updateDescription();
        this.loadRegion("demonForm");
        //this.beaked_img = new Texture("beaked_img/powers/ritual.png");
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.owner instanceof AbstractPlayer == isPlayer) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
        }
    }

    @Override
    public void updateDescription() {
        if (this.amount > 0) {
            this.description = HungryPower.DESCRIPTIONS[0] + FontHelper.colorString(this.owner.name, "y") + HungryPower.DESCRIPTIONS[1] + this.amount + HungryPower.DESCRIPTIONS[2];
            this.type = PowerType.BUFF;
        }
    }

}
