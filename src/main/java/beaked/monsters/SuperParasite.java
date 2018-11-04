package beaked.monsters;

import basemod.BaseMod;
import beaked.actions.MultipliedVampireDamageAction;
import beaked.powers.AdaptiveArmorPower;
import beaked.powers.HungryPower;
import com.megacrit.cardcrawl.monsters.*;
import com.megacrit.cardcrawl.localization.*;
import com.badlogic.gdx.math.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.cards.*;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.powers.*;
import com.badlogic.gdx.graphics.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.*;
import com.megacrit.cardcrawl.actions.animations.*;
import com.megacrit.cardcrawl.vfx.*;
import com.megacrit.cardcrawl.actions.unique.*;
import com.megacrit.cardcrawl.actions.utility.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.core.*;

public class SuperParasite extends AbstractMonster
{
    public static final String ID = "beaked:SuperParasite";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private static final float HB_X_F = 20.0f;
    private static final float HB_Y_F = -6.0f;
    private static final float HB_W = 350.0f*1.67f;
    private static final float HB_H = 260.0f*1.67f;

    private static final int HP = 250;
    private static final int A9_HP = 270;

    public int startingPlatedArmorAmt = 25;
    public int startingStrGainAmt = 1;
    public int startingPlatedArmorGainAmt = 2;
    public int multistrikeDmg = 5;
    public int multistrikeHits = 3;
    public int suckDmg = 14;
    public int suckHealMult = 3;
    public int fellDmg = 19;
    public int fellFrail = 2;
    public int fellWeak = 1;
    public static final int A4_MULTISTRIKE_DMG = 6;
    public static final int A4_SUCK_DMG = 16;
    public static final int A4_FELL_DMG = 21;
    public static final int A19_FELL_FRAIL = 3;
    public static final int A19_FELL_WEAK = 2;
    public static final int A19_SUCK_HEAL_MULT = 5;
    public static final int A19_STARTING_STR_GAIN = 2;

    public static final byte MULTISTRIKE = 1;
    public static final byte SUCK = 2;
    public static final byte FELL = 3;
    public static final byte STUNNED = 4;

    private boolean firstMove;

    public SuperParasite(final float x, final float y) {
        super(SuperParasite.NAME, ID, (AbstractDungeon.ascensionLevel>=9?A9_HP:HP), HB_X_F, HB_Y_F, HB_W, HB_H, null, x, y);
        this.loadAnimation("images/monsters/theCity/shellMonster/skeleton.atlas", "images/monsters/theCity/shellMonster/skeleton.json", 0.6f);
        final AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.2f);
        e.setTimeScale(0.8f);
        this.dialogX = -50.0f * Settings.scale;

        this.firstMove = true;
        this.type = EnemyType.ELITE;

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.multistrikeDmg = A4_MULTISTRIKE_DMG;
            this.fellDmg = A4_FELL_DMG;
            this.suckDmg = A4_SUCK_DMG;
        }
        if (AbstractDungeon.ascensionLevel >= 19){
            this.fellFrail = A19_FELL_FRAIL;
            this.fellWeak = A19_FELL_WEAK;
            this.suckHealMult = A19_SUCK_HEAL_MULT;
            this.startingStrGainAmt = A19_STARTING_STR_GAIN;
        }
        this.damage.add(new DamageInfo(this, this.multistrikeDmg));
        this.damage.add(new DamageInfo(this, this.suckDmg));
        this.damage.add(new DamageInfo(this, this.fellDmg));
    }

    public SuperParasite() {
        this(-20.0f, 10.0f);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PlatedArmorPower(this, this.startingPlatedArmorAmt),this.startingPlatedArmorAmt));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new HungryPower(this, this.startingStrGainAmt),this.startingStrGainAmt));
       // AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new AdaptiveArmorPower(this, this.startingPlatedArmorGainAmt),this.startingPlatedArmorGainAmt));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.startingPlatedArmorAmt));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case MULTISTRIKE: {
                for (int i = 0; i < this.multistrikeHits; ++i) {
                    AbstractDungeon.actionManager.addToBottom(new AnimateHopAction(this));
                    AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2f));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                break;
            }
            case SUCK: {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4f));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-25.0f, 25.0f) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-25.0f, 25.0f) * Settings.scale, Color.GOLD.cpy()), 0.0f));
                AbstractDungeon.actionManager.addToBottom(new MultipliedVampireDamageAction(AbstractDungeon.player, this.damage.get(1), this.suckHealMult, AbstractGameAction.AttackEffect.NONE));
                break;
            }
            case FELL: {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3f));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.fellFrail, true), this.fellFrail));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.fellWeak, true), this.fellWeak));
                break;
            }
            case STUNNED: {
                AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, TextAboveCreatureAction.TextType.STUNNED));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void changeState(final String stateName) {
        switch (stateName) {
            case "ATTACK": {
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0.0f);
                break;
            }
            case "ARMOR_BREAK": {
                AbstractDungeon.actionManager.addToBottom(new AnimateHopAction(this));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3f));
                AbstractDungeon.actionManager.addToBottom(new AnimateHopAction(this));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3f));
                AbstractDungeon.actionManager.addToBottom(new AnimateHopAction(this));
                this.setMove(STUNNED, Intent.STUN);
                this.createIntent();
                break;
            }
        }
    }

    @Override
    public void damage(final DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "Hit", false);
            this.state.addAnimation(0, "Idle", true, 0.0f);
        }
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.firstMove = false;
            this.setMove(MULTISTRIKE,Intent.ATTACK,this.multistrikeDmg,this.multistrikeHits,true);
            return;
        }
        if (this.lastMove(MULTISTRIKE)){
            this.setMove(SUCK,Intent.ATTACK_BUFF,this.suckDmg);
        } else if (this.lastMove(SUCK) || this.lastMove(STUNNED)){
            this.setMove(FELL,Intent.ATTACK_DEBUFF,this.fellDmg);
        } else if (this.lastMove(FELL)){
            this.setMove(MULTISTRIKE,Intent.ATTACK,this.multistrikeDmg,this.multistrikeHits,true);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = SuperParasite.monsterStrings.NAME;
        MOVES = SuperParasite.monsterStrings.MOVES;
        DIALOG = SuperParasite.monsterStrings.DIALOG;
    }
}
