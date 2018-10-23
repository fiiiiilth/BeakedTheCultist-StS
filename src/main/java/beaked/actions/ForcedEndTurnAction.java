package beaked.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

public class ForcedEndTurnAction extends AbstractGameAction
{
    public ForcedEndTurnAction() {
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        // do this even if the player has already hit end turn manually, so they can't cheat out more cards.
        AbstractDungeon.actionManager.cardQueue.clear();
        for (final AbstractCard c : AbstractDungeon.player.limbo.group) {
            AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
        }
        AbstractDungeon.player.limbo.group.clear();
        AbstractDungeon.player.releaseCard();

        if (AbstractDungeon.overlayMenu.endTurnButton.enabled) AbstractDungeon.overlayMenu.endTurnButton.disable(true);
        this.isDone = true;
    }
}
