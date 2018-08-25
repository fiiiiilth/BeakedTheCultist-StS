package beaked.actions;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.*;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class FeathersFollowupAction extends AbstractGameAction
{
    private final AbstractPlayer p = AbstractDungeon.player;

    public FeathersFollowupAction() {
        this.duration = Settings.ACTION_DUR_FASTER;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FASTER) {
            int blockGain = 0;
            for (final AbstractCard drawn : DrawAndLogCardsAction.drawnCards) {
                // when you draw an X-cost card, gain block equal to your current energy.
                if (drawn.cost == -1) blockGain += EnergyPanel.getCurrentEnergy();
                else if (drawn.costForTurn > 0) blockGain += drawn.costForTurn;
            }
            DrawAndLogCardsAction.drawnCards.clear();
            AbstractDungeon.actionManager.addToTop(new GainBlockAction(p,p,blockGain));
        }
        this.tickDuration();
    }
}
