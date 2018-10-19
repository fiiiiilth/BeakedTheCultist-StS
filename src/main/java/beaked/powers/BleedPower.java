package beaked.powers;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BleedPower extends AbstractPower {

    public static final String POWER_ID = "beaked:BleedPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public BleedPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;
        updateDescription();
        this.img = ImageMaster.loadImage("img/powers/bleed.png");
        this.type = PowerType.DEBUFF;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if(isPlayer) {
            AbstractDungeon.actionManager.addToTop(new DamageAction(this.owner, new DamageInfo(this.owner, this.amount,
                    DamageInfo.DamageType.HP_LOSS)));
        }
    }

    @Override
    public void updateDescription() {
        this.description = BleedPower.DESCRIPTIONS[0] + this.amount + BleedPower.DESCRIPTIONS[1];
    }

}
