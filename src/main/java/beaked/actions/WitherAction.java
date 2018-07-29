package beaked.actions;

import beaked.cards.Negation;
import beaked.powers.NegationPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public class WitherAction extends AbstractGameAction {
    private AbstractCard card;
    private int miscIncrease;

    public WitherAction(AbstractCard card, int witherDecrease) {
        this.miscIncrease = -witherDecrease;
        this.card = card;
    }

    public void update() {

        if (AbstractDungeon.player.hasPower(NegationPower.POWER_ID)){
            AbstractDungeon.player.getPower(NegationPower.POWER_ID).onSpecificTrigger();
            this.isDone = true;
            return;
        }

        this.card.darkFlash(Color.RED);

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
