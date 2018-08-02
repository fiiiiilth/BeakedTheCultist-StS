package beaked.actions;

import beaked.cards.AbstractWitherCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.Iterator;

public class FlashHandAction extends AbstractGameAction {

    public FlashHandAction() {
    }

    public void update() {

        Iterator var1 = AbstractDungeon.player.hand.group.iterator();

        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            c.flash();
        }

        this.isDone = true;
    }
}
