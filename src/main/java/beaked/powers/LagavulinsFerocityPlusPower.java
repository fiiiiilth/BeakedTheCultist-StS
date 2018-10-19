package beaked.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class LagavulinsFerocityPlusPower extends AbstractPower {

    public static final String POWER_ID = "beaked:LagavulinsFerocityPlusPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int attacksPlayedThisTurn;


    public LagavulinsFerocityPlusPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;
        updateDescription();
        this.loadRegion("doubleTap");
        this.attacksPlayedThisTurn = 0;
        this.type = PowerType.BUFF;
    }

    @Override
    public void atStartOfTurn() {
        this.attacksPlayedThisTurn = 0;
    }

    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        if(card.type == AbstractCard.CardType.ATTACK) {
            ++this.attacksPlayedThisTurn;
        }
    }

    @Override
    public float atDamageGive(final float damage, final DamageInfo.DamageType info) {
        if(this.attacksPlayedThisTurn < this.amount) {
            return damage * 3;
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        if(this.amount == 1) {
            this.description = LagavulinsFerocityPlusPower.DESCRIPTIONS[0];
        } else {
            this.description = LagavulinsFerocityPlusPower.DESCRIPTIONS[1] + this.amount + LagavulinsFerocityPlusPower.DESCRIPTIONS[2];
        }
    }

}
