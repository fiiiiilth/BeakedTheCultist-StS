package beaked.powers;

import beaked.actions.FetchRandomCardToHandAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class MurmursPower extends AbstractPower {

    public static final String POWER_ID = "beaked:MurmursPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public MurmursPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;
        updateDescription();
        this.loadRegion("attackBurn");
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (!AbstractDungeon.player.discardPile.isEmpty()) flash();
        for (int i=0;i<this.amount;i++){
            AbstractDungeon.actionManager.addToBottom(new FetchRandomCardToHandAction(AbstractDungeon.player.discardPile,-1));
        }
    }

    @Override
    public void updateDescription() {
        this.description = MurmursPower.DESCRIPTIONS[0] + (this.amount==1 ? MurmursPower.DESCRIPTIONS[1] : "#b" + this.amount + MurmursPower.DESCRIPTIONS[2]);
    }

}
