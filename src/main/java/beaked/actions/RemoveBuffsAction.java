package beaked.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;

public class RemoveBuffsAction extends AbstractGameAction {

    private AbstractCreature c;

    public RemoveBuffsAction(final AbstractCreature c) {
        this.c = c;
        this.duration = 0.5f;
    }

    @Override
    public void update() {
        for(final AbstractPower p : this.c.powers) {
            if(p.type == AbstractPower.PowerType.BUFF
                    && !p.ID.equals(TimeWarpPower.POWER_ID)
                    && !p.ID.equals(CuriosityPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.c, AbstractDungeon.player, p.ID));
            }
        }
        this.isDone = true;
    }
}
