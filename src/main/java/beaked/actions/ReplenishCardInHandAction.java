package beaked.actions;

import beaked.cards.AbstractWitherCard;
import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.dungeons.*;
import java.util.*;
import com.megacrit.cardcrawl.core.*;

public class ReplenishCardInHandAction extends AbstractGameAction
{
    private AbstractPlayer p;
    private ArrayList<AbstractCard> cannotReplenish;

    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    public ReplenishCardInHandAction(int numUses) {
        this.cannotReplenish = new ArrayList<AbstractCard>();
        this.actionType = ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.amount = numUses;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            for (final AbstractCard c : this.p.hand.group) {
                if (!(c instanceof AbstractWitherCard) || c.misc == ((AbstractWitherCard) c).baseMisc) {
                    this.cannotReplenish.add(c);
                }
            }
            if (this.cannotReplenish.size() == this.p.hand.group.size()) {
                this.isDone = true;
                return;
            }
            if (this.p.hand.group.size() - this.cannotReplenish.size() == 1) {
                for (final AbstractCard c : this.p.hand.group) {
                    if (c instanceof AbstractWitherCard && c.misc != ((AbstractWitherCard) c).baseMisc) {
                        AbstractDungeon.actionManager.addToTop(new ReplenishWitherAction((AbstractWitherCard) c, this.amount));
                        this.isDone = true;
                        return;
                    }
                }
            }
            this.p.hand.group.removeAll(this.cannotReplenish);
            if (this.p.hand.group.size() > 1) {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false);
                this.tickDuration();
                return;
            }
            if (this.p.hand.group.size() == 1) {
                AbstractDungeon.actionManager.addToTop(new ReplenishWitherAction((AbstractWitherCard) this.p.hand.getTopCard(), this.amount));
                this.returnCards();
                this.isDone = true;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (final AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                AbstractDungeon.actionManager.addToTop(new ReplenishWitherAction((AbstractWitherCard) c, this.amount));
                this.p.hand.addToTop(c);
            }
            this.returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
        }
        this.tickDuration();
    }

    private void returnCards() {
        for (final AbstractCard c : this.cannotReplenish) {
            this.p.hand.addToTop(c);
        }
        this.p.hand.refreshHandLayout();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("beaked:ReplenishWitherCardAction");
        TEXT = uiStrings.TEXT;
    }
}
