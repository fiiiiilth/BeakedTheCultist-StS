package beaked.powers;

import beaked.actions.FetchRandomCardToHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MurmursPlusPower extends AbstractPower {

    public static final String POWER_ID = "beaked:MurmursPlusPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public MurmursPlusPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;
        updateDescription();
        this.loadRegion("skillBurn");
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (!AbstractDungeon.player.discardPile.isEmpty()) flash();
        for (int i=0;i<this.amount;i++){
            AbstractDungeon.actionManager.addToBottom(new FetchRandomCardToHandAction(AbstractDungeon.player.discardPile,-9));
        }
    }

    @Override
    public void updateDescription() {
        this.description = MurmursPlusPower.DESCRIPTIONS[0] + (this.amount==1 ? MurmursPlusPower.DESCRIPTIONS[1] : "#b" + this.amount + MurmursPlusPower.DESCRIPTIONS[2]);
    }

}
