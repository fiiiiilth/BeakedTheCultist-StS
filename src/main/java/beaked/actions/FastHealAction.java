package beaked.actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.core.*;

public class FastHealAction extends AbstractGameAction
{
    public FastHealAction(final AbstractCreature target, final AbstractCreature source, final int amount) {
        this.setValues(target, source, amount);
        this.actionType = ActionType.HEAL;
        this.duration = 0.02f;
    }
    
    @Override
    public void update() {
        if (this.duration == 0.02f) {
            this.target.heal(this.amount);
        }
        this.tickDuration();
    }
}
