package beaked.actions;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.actions.common.DamageAction;

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
            for (final AbstractCard drawn : FeathersDrawAction.drawnCards) {
                blockGain += drawn.costForTurn;
            }
            FeathersDrawAction.drawnCards.clear();
            AbstractDungeon.actionManager.addToTop(new GainBlockAction(p,p,blockGain));
        }
        this.tickDuration();
    }
}
