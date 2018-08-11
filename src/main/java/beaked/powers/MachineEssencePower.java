package beaked.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MachineEssencePower extends AbstractPower {
    public static final String POWER_ID = "beaked:MachineEssencePower";
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
        this.loadRegion("ai");
    }

    @Override
    public int onHeal(final int healAmount) {
        if (!AbstractDungeon.player.hasPower(NoHealPower.POWER_ID)){
            this.flash();
        }
        return healAmount + amount;
    }

    @Override
    public void updateDescription() {
        this.description = MachineEssencePower.DESCRIPTIONS[0] + this.amount + MachineEssencePower.DESCRIPTIONS[1];
    }

}
