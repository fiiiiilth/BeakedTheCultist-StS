//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package beaked.actions;

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
        boolean success = false;
        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c.cardID.equals(card.cardID) && c.misc == card.misc) {
                c.misc += this.miscIncrease;
                c.applyPowers();
                success = true;
                break;
            }
        }

        card.misc += this.miscIncrease;
        card.applyPowers();

        if (success) {
            System.out.println("Success!");
        }

        this.isDone = true;
    }
}
