package beaked.actions;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.combat.*;
import com.megacrit.cardcrawl.actions.utility.*;

public class DealMultiRandomVampireDamageAction extends AbstractGameAction
{
    private DamageInfo info;
    private static final float DURATION = 0.01f;
    private static final float POST_ATTACK_WAIT_DUR = 0.2f;
    private int numTimes;
    private float healMult;
    private int existingHeal; // amount of healing that's already built up from previous uses of this action.
    // We want to wait until the final hit and do one big burst of healing so the animation doesn't take forever.

    public DealMultiRandomVampireDamageAction(final DamageInfo info, final int numTimes, final float healMult, final AttackEffect atkEffect, final int existingHeal) {
        this.source = AbstractDungeon.player;
        this.info = info;
        this.target = AbstractDungeon.getMonsters().getRandomMonster(true);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = atkEffect;
        this.duration = 0.01f;
        this.numTimes = numTimes;
        this.healMult = healMult;
        this.existingHeal = existingHeal;
    }

    public DealMultiRandomVampireDamageAction(final DamageInfo info, final int numTimes, final float healMult, final AttackEffect atkEffect) {
        this(info,numTimes,healMult,atkEffect,0);
    }

    @Override
    public void update() {
        if (this.target == null) {
            this.isDone = true;
            return;
        }
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
            this.isDone = true;
            return;
        }
        if (this.target.currentHealth > 0) {
            this.target.damageFlash = true;
            this.target.damageFlashFrames = 4;
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
            //this.info.applyPowers(this.info.owner, this.target);
            this.heal(this.info);
            this.target.damage(this.info);
            if (this.numTimes > 1 && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                --this.numTimes;
                AbstractDungeon.actionManager.addToTop(new DealMultiRandomVampireDamageAction(this.info, this.numTimes, this.healMult, this.attackEffect, this.existingHeal));
            }
            else {
                AbstractDungeon.actionManager.addToTop(new HealAction(this.source, this.source, this.existingHeal));
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.1f));
            }
            AbstractDungeon.actionManager.addToTop(new WaitAction(0.2f));
        }
        this.isDone = true;
    }

    private void heal(final DamageInfo info) {
        int healAmount = (int)(info.output * healMult); // rounds down
        if (healAmount < 0) {
            return;
        }
        healAmount -= this.target.currentBlock;
        if (healAmount > this.target.currentHealth) {
            healAmount = this.target.currentHealth;
        }
        if (healAmount > 0) {
            this.existingHeal += healAmount;
        }
    }
}
