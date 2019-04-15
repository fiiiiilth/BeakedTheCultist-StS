package beaked.actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.combat.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.*;

public class MultipliedVampireDamageAction extends AbstractGameAction
{
    private DamageInfo info;
    public int healMult;
    
    public MultipliedVampireDamageAction(final AbstractCreature target, final DamageInfo info, final int healMult, final AttackEffect effect) {
        this.setValues(target, this.info = info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.healMult = healMult;
    }
    
    @Override
    public void update() {
        if (this.duration == 0.5f) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
        }
        this.tickDuration();
        if (this.isDone) {
            this.heal(this.info);
            this.target.damage(this.info);
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
    }
    
    private void heal(final DamageInfo info) {
        int healAmount = info.output;
        if (healAmount < 0) {
            return;
        }
        healAmount -= this.target.currentBlock;
        if (healAmount > this.target.currentHealth) {
            healAmount = this.target.currentHealth;
        }
        if (healAmount > 0) {
            if (healAmount > 1 && this.target.hasPower(BufferPower.POWER_ID)) {
                return;
            }
            if (healAmount > 1 && this.target.hasPower(IntangiblePlayerPower.POWER_ID)) {
                healAmount = 1;
            }
            healAmount *= this.healMult;
            AbstractDungeon.actionManager.addToTop(new HealAction(this.source, this.source, healAmount));
            AbstractDungeon.actionManager.addToTop(new WaitAction(0.1f));
        }
    }
}
