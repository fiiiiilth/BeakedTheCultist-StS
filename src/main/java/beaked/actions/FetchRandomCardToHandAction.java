package beaked.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FetchRandomCardToHandAction extends AbstractGameAction {
    private CardGroup group;

    public FetchRandomCardToHandAction(CardGroup group) {
        this(group,0);
    }

    public FetchRandomCardToHandAction(CardGroup group, int modifyCostForTurn) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.group = group;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = modifyCostForTurn;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {

            if (group == AbstractDungeon.player.hand){
                this.isDone = true;
                return;
            }

            if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE && !group.isEmpty()) {
                AbstractCard card = group.getRandomCard(true);
                AbstractDungeon.player.hand.addToHand(card);
                card.unfadeOut();
                card.unhover();
                card.setAngle(0.0F, true);
                card.lighten(false);
                card.drawScale = 0.12F;
                card.targetDrawScale = 0.75F;
                card.fadingOut = false;
                group.removeCard(card);
                card.applyPowers();
                if (this.amount != 0) card.modifyCostForTurn(this.amount);
            }

            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.glowCheck();
        }

        this.tickDuration();
        this.isDone = true;
    }
}
