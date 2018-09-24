package beaked.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

public class ModifyCostAction extends AbstractGameAction {
    private AbstractCard card;

    public ModifyCostAction(AbstractCard card, int amount) {
        this.card = card;
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
            this.card.modifyCostForCombat(this.amount);
        }

        this.tickDuration();
    }
}
