package beaked.actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.actions.unique.SwordBoomerangAction;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.*;

public class WarriorEssenceAction extends AbstractGameAction
{
    public int damage;
    private boolean freeToPlayOnce;
    private DamageInfo.DamageType damageType;
    private AbstractPlayer p;
    private int energyOnUse;
    private boolean upgraded; // +1 hit
    private float healMult; // amount of healing per point of damage dealt

    public WarriorEssenceAction(final AbstractPlayer p, final int damage, final DamageInfo.DamageType damageType, final boolean freeToPlayOnce, final int energyOnUse, final boolean upgraded, final float healMult) {
        this.freeToPlayOnce = false;
        this.energyOnUse = -1;
        this.damage = damage;
        this.damageType = damageType;
        this.p = p;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.DAMAGE;
        this.energyOnUse = energyOnUse;
        this.upgraded = upgraded;
        this.healMult = healMult;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        if (this.p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            this.p.getRelic(ChemicalX.ID).flash();
        }
        if (this.upgraded) effect += 1;
        if (effect > 0) {
            AbstractDungeon.actionManager.addToBottom(new DealMultiRandomVampireDamageAction(
                    new DamageInfo(p, this.damage, this.damageType), effect, this.healMult, AttackEffect.SLASH_VERTICAL));
            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
        }
        this.isDone = true;
    }
}
