package beaked.cards;

import beaked.Beaked;
import beaked.actions.WitherAction;
import beaked.patches.AbstractCardEnum;
import beaked.powers.NegationPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

public class CheekyTricks extends AbstractWitherCard {
    public static final String ID = "beaked:CheekyTricks";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 2;
    private static final int NEGATION = 4;
    private static final int WITHER_MINUS_NEGATION = 1;
    private static final int UPGRADE_PLUS_NEGATION = 1;

    public CheekyTricks() {
        super(ID, NAME, null, COST, DESCRIPTION, CardType.POWER,
                AbstractCardEnum.BEAKED_YELLOW, CardRarity.RARE, CardTarget.SELF);
        this.baseMisc = this.misc = NEGATION;
        this.baseMagicNumber = this.magicNumber = WITHER_MINUS_NEGATION;
        this.witherEffect = "Decreases #yWither effects ignored. This card does not prevent its own #yWither effect.";
        this.linkWitherAmountToMagicNumber = true;
        Beaked.setDescription(this,DESCRIPTION);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new WitherAction(this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new NegationPower(p,this.misc),this.misc));
    }

    public AbstractCard makeCopy() {
        return new CheekyTricks();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMisc(UPGRADE_PLUS_NEGATION);
        }
    }
}
