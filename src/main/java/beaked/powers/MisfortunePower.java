package beaked.powers;

import beaked.cards.AbstractWitherCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class MisfortunePower extends AbstractPower {

    public static final String POWER_ID = "beaked:MisfortunePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public MisfortunePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;
        updateDescription();
        this.loadRegion("modeShift");
        //this.img = new Texture("img/powers/ritual.png");
    }

    @Override
    public void atEndOfTurn(final boolean isPlayer) {
        if (isPlayer){
            boolean hitCard = false;
            for (AbstractCard c :AbstractDungeon.player.hand.group){
                boolean isPlayable = false;
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters){
                    if (c.cost > -2 && !(c instanceof AbstractWitherCard && ((AbstractWitherCard) c).isDepleted)){
                        isPlayable = true;
                        break;
                    }
                }
                if (!isPlayable){
                    hitCard = true;
                    c.retain = true;
                    c.flash();
                    AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player,getDamage(), DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.POISON));
                }
            }
            if (hitCard) this.flash();
        }
    }

    public int getDamage(){
        if (AbstractDungeon.player.hasPower(StrengthPower.POWER_ID)){
            // if stacked more than once, strength affects it multiple times
            return this.amount * (AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount + 1);
        }
        else return this.amount;
    }

    @Override
    public void onDrawOrDiscard() {
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = MisfortunePower.DESCRIPTIONS[0] + getDamage() + MisfortunePower.DESCRIPTIONS[1];
    }

}
