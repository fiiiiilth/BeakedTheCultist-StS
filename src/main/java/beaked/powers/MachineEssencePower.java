package beaked.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MachineEssencePower extends AbstractPower {
    public static final String POWER_ID = "MachineEssencePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public MachineEssencePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;
        updateDescription();
        this.img = new Texture("img/powers/ritual.png");
    }

    @Override
    public int onHeal(final int healAmount) {
        this.flash();
        return healAmount + amount;
    }

    @Override
    public void updateDescription() {
        this.description = MachineEssencePower.DESCRIPTIONS[0] + this.amount + MachineEssencePower.DESCRIPTIONS[1];
    }

}
