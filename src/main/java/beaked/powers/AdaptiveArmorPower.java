package beaked.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class AdaptiveArmorPower extends AbstractPower {

    public static final String POWER_ID = "beaked:AdaptiveArmor";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static int ARMOR_CAP = 30;


    public AdaptiveArmorPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;
        updateDescription();
        this.loadRegion("afterImage");
        //this.beaked_img = new Texture("beaked_img/powers/ritual.png");
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.owner instanceof AbstractPlayer == isPlayer) {
            this.flash();
            int existingArmor = this.owner.hasPower(PlatedArmorPower.POWER_ID)?this.owner.getPower(PlatedArmorPower.POWER_ID).amount:0;
            int armorToAdd = existingArmor+this.amount <= ARMOR_CAP?this.amount:ARMOR_CAP-existingArmor;
            if (armorToAdd > 0) AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new PlatedArmorPower(this.owner, armorToAdd), armorToAdd));
        }
    }

    @Override
    public void updateDescription() {
        if (this.amount > 0) {
            this.description = AdaptiveArmorPower.DESCRIPTIONS[0] + FontHelper.colorString(this.owner.name, "y") + AdaptiveArmorPower.DESCRIPTIONS[1] + this.amount + AdaptiveArmorPower.DESCRIPTIONS[2] + ARMOR_CAP + DESCRIPTIONS[3];
            this.type = PowerType.BUFF;
        }
    }

}
