package beaked.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class PokeFollowupAction extends AbstractGameAction
{
    private final AbstractPlayer p = AbstractDungeon.player;

    public PokeFollowupAction(AbstractCreature source, AbstractMonster target, int dmg) {
        this.duration = Settings.ACTION_DUR_FASTER;
        this.amount = dmg;
        this.target = target;
        this.source = source;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FASTER) {
            for (final AbstractCard drawn : DrawAndLogCardsAction.drawnCards) {
                if (drawn.type == AbstractCard.CardType.ATTACK){
                    drawn.flash();
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target,new DamageInfo(this.source,this.amount, DamageInfo.DamageType.NORMAL),AttackEffect.BLUNT_LIGHT));
                }
            }
            DrawAndLogCardsAction.drawnCards.clear();
        }
        this.tickDuration();
    }
}
