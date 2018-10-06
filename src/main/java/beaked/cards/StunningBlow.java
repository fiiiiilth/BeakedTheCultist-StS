package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.actions.WitherAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

public class StunningBlow extends AbstractWitherCard {
    public static final String ID = "beaked:StunningBlow";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 3;
    public static final int DAMAGE = 9;
    public static final int INTANGIBLE = 1;
    public static final int WITHER_MINUS_DAMAGE = 3;
    public static final int UPGRADE_PLUS_WITHER = -1;

    public StunningBlow() {
        super(ID, NAME, "img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.ATTACK, AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseMisc = this.misc = DAMAGE;
        this.baseDamage = this.damage = this.misc;
        this.baseMagicNumber = this.magicNumber = WITHER_MINUS_DAMAGE;
        this.witherEffect = "Decreases damage.";
        this.linkWitherAmountToMagicNumber = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
       AbstractDungeon.actionManager.addToBottom(new WitherAction(this));
       AbstractDungeon.actionManager.addToBottom(new DamageAction(m,new DamageInfo(p,this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SMASH));
       AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new IntangiblePlayerPower(p,INTANGIBLE),INTANGIBLE));
    }

    @Override
    public void applyPowers(){
        this.baseDamage = this.damage = this.misc;
        super.applyPowers();
    }

    @Override
    public AbstractCard makeCopy() {
        return new StunningBlow();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_WITHER);
        }
    }

}
