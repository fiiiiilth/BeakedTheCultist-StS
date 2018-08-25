package beaked.actions;

import beaked.cards.AbstractWitherCard;
import beaked.powers.NegationPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public class ReplenishWitherAction extends AbstractGameAction {
    private AbstractWitherCard card;
    private int miscIncrease;

    public ReplenishWitherAction(AbstractWitherCard card){
        this(card,1);
    }

    public ReplenishWitherAction(AbstractWitherCard card, int numTimes) {
        this.miscIncrease = card.witherAmount * numTimes;
        this.card = card;
        this.amount = numTimes;
    }

    public void update() {

        this.card.flash();

        // Can't replenish past initial value
        if (card.misc <= card.baseMisc && card.misc + this.miscIncrease > card.baseMisc){
            this.miscIncrease = card.baseMisc - card.misc;
        }
        else if (card.misc >= card.baseMisc && card.misc + this.miscIncrease < card.baseMisc){
            this.miscIncrease = card.baseMisc - card.misc;
        }

        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c.cardID.equals(card.cardID) && c.misc == card.misc) {
                c.misc += this.miscIncrease;
                c.applyPowers();
                break;
            }
        }

        card.misc += this.miscIncrease;
        card.applyPowers();

        this.isDone = true;
    }
}
