package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class SacrificialAttack extends CustomCard {
    public static final String ID = "beaked:SacrificialAttack";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final int COST = 2;
    public static final int BASE_DMG = 0;
    public static final int STR_APPLY_TIMES = 2;
    public static final int UPGRADE_PLUS_STR_APPLY_TIMES = 1;

    public SacrificialAttack() {
        super(ID, NAME, null, COST, DESCRIPTION, CardType.ATTACK, AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON, CardTarget.ALL);
        this.damage = this.baseDamage = BASE_DMG;
        this.magicNumber = this.baseMagicNumber = STR_APPLY_TIMES;
        this.isMultiDamage = true;
        Beaked.setDescription(this,DESCRIPTION);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p,this.multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,StrengthPower.POWER_ID));
    }

    @Override
    public void applyPowers(){
        if (AbstractDungeon.player.hasPower(StrengthPower.POWER_ID)) {
            // heavy blade effect - just cheat and do it the easy way
            this.baseDamage = AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount * (this.magicNumber-1);
        }
        super.applyPowers();

        /*if (AbstractDungeon.player.hasPower(StrengthPower.POWER_ID)){
            for (int m=1;m<this.magicNumber;m++) { // already applied once in applyPowers()
                for (int i = 0; i < this.multiDamage.length; i++) {
                    this.multiDamage[i] = (int) AbstractDungeon.player.getPower(StrengthPower.POWER_ID).atDamageGive(this.multiDamage[i], this.damageTypeForTurn);
                    if (this.baseDamage != multiDamage[i]) {
                        this.isDamageModified = true;
                    }
                }
            }
        }*/
    }

    @Override
    public AbstractCard makeCopy() {
        return new SacrificialAttack();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_STR_APPLY_TIMES);
        }
    }

}
