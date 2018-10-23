package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class ShadowFade extends CustomCard {
    public static final String ID = "beaked:ShadowFade";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final int COST = 3;
    public static final int STR_PER_INTANGIBLE = 10;
    public static final int UPGRADE_PLUS_STR_PER_INTANGIBLE = -3;

    public ShadowFade() {
        super(ID, NAME, "img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.RARE, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = STR_PER_INTANGIBLE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int str = 0;
        if (AbstractDungeon.player.hasPower(StrengthPower.POWER_ID)){
            str = AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount;

            for (int i=this.magicNumber;i<=str;i+=this.magicNumber){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new IntangiblePlayerPower(p,1),1));
            }

            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new StrengthPower(p,-str),-str));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShadowFade();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_STR_PER_INTANGIBLE);
        }
    }

}
