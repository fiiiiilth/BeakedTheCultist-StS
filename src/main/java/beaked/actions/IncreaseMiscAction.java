package beaked.actions;

import beaked.powers.NegationPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public class IncreaseMiscAction extends AbstractGameAction {
    private AbstractCard card;
    private int miscIncrease;

    public IncreaseMiscAction(AbstractCard card, int miscIncrease) {
        this.miscIncrease = miscIncrease;
        this.card = card;
    }

    public void update() {

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
