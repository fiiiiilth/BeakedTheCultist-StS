package beaked.powers;

import beaked.actions.IncreaseMiscAction;
import com.megacrit.cardcrawl.cards.blue.GeneticAlgorithm;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.actions.utility.*;
import com.megacrit.cardcrawl.monsters.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class HuntressSpiritPlusPower extends AbstractPower
{
    public static final String POWER_ID = "beaked:HuntressSpiritPlusPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public HuntressSpiritPlusPower(final AbstractCreature owner, final int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.img = ImageMaster.loadImage("img/powers/huntress_spirit.png");
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[3];
        }
        else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2] + DESCRIPTIONS[3];
        }
    }

    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        if (!card.purgeOnUse && card.type == AbstractCard.CardType.SKILL && this.amount > 0) {
            this.flash();
            AbstractMonster m = null;
            if (action.target != null) {
                m = (AbstractMonster)action.target;
            }
            final AbstractCard tmp = card.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = Settings.WIDTH / 2.0f - 300.0f * Settings.scale;
            tmp.target_y = Settings.HEIGHT / 2.0f;
            tmp.freeToPlayOnce = true;
            if (m != null) {
                tmp.calculateCardDamage(m);
            }
            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(tmp, m, card.energyOnUse));
            int cost = card.costForTurn;
            if(card.cost == -1) {
                cost = EnergyPanel.getCurrentEnergy();
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, cost*2), cost*2));
            --this.amount;
            if (this.amount == 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
        }
    }

    @Override
    public void atEndOfTurn(final boolean isPlayer) {
        if (isPlayer) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}
