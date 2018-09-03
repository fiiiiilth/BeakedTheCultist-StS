package beaked.actions;

import beaked.cards.AbstractWitherCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import java.util.Iterator;

public class MoltingAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;
    public int block;
    public int bonusBlock;
    public static int numExhausted;

    public MoltingAction(AbstractCreature target, AbstractCreature source,int block,int bonusBlock) {
        this.p = (AbstractPlayer)target;
        this.target = target;
        this.source = source;
        this.block = block;
        this.bonusBlock = bonusBlock;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.EXHAUST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p.hand.size() == 0) {
                this.isDone = true;
                return;
            }

            int i;
            if (this.p.hand.size() == 1) {
                numExhausted = 1;

                AbstractCard c = this.p.hand.getTopCard();
                gainBlock(c);
                this.p.hand.moveToExhaustPile(c);

                CardCrawlGame.dungeon.checkForPactAchievement();
                return;
            }

            numExhausted = 1;
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false);
            this.tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            Iterator var4 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

            while(var4.hasNext()) {
                AbstractCard c = (AbstractCard)var4.next();
                gainBlock(c);
                this.p.hand.moveToExhaustPile(c);
            }

            CardCrawlGame.dungeon.checkForPactAchievement();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        this.tickDuration();
    }

    public void gainBlock(AbstractCard card){
        int blk = this.block;
        if (card instanceof AbstractWitherCard && ((AbstractWitherCard) card).isDepleted) blk += this.bonusBlock;
        AbstractDungeon.actionManager.addToTop(new GainBlockAction(p,p,blk));
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");
        TEXT = uiStrings.TEXT;
    }
}
