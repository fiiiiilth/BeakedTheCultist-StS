package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import beaked.powers.WindmillPower;
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

public class Windmill extends CustomCard {
    public static final String ID = "beaked:Windmill";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 0;
    public static final int DAMAGE = 3;
    public static final int SKILL_DAMAGE = 6;
    public static final int UPGRADE_PLUS_SKILL_DAMAGE = 3;

    public Windmill() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.ATTACK, AbstractCardEnum.BEAKED_YELLOW, CardRarity.COMMON, CardTarget.ENEMY);
        this.baseDamage = this.damage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = SKILL_DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
       AbstractDungeon.actionManager.addToBottom(new DamageAction(m,new DamageInfo(p,this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
       AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new WindmillPower(p, this.magicNumber),this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Windmill();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_SKILL_DAMAGE);
        }
    }

}
