package beaked.cards;

import beaked.actions.WitherAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

public class ShadowFade extends AbstractWitherCard {
    public static final String ID = "beaked:ShadowFade";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 3;
    private static final int INTANGIBLE = 5;
    private static final int WITHER_MINUS_INTANGIBLE = 2;
    private static final int UPGRADE_PLUS_WITHER = -1;

    public ShadowFade() {
        super(ID, NAME, null, COST, DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.BEAKED_YELLOW, CardRarity.RARE, CardTarget.SELF);
        this.baseMisc = this.misc = INTANGIBLE;
        this.baseMagicNumber = this.magicNumber = WITHER_MINUS_INTANGIBLE;
        this.witherEffect = "Decreases #yIntangible.";
        this.linkWitherAmountToMagicNumber = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new WitherAction(this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new IntangiblePlayerPower(p,this.misc),this.misc));
    }

    public AbstractCard makeCopy() {
        return new ShadowFade();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_WITHER);
        }
    }
}
