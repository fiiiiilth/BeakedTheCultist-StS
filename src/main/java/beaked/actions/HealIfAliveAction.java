package beaked.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class HealIfAliveAction extends AbstractGameAction {
    public HealIfAliveAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.setValues(target, source, amount);
        this.actionType = ActionType.HEAL;
    }

    public void update() {
        if (this.duration == 0.5F && !this.target.isDeadOrEscaped()) {
            this.target.heal(this.amount);
        }

        this.tickDuration();
    }
}
