package beaked.cards;

import beaked.Beaked;
import beaked.actions.WitherAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class FullHouse extends AbstractWitherCard {
    public static final String ID = "beaked:FullHouse";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 0;
    public static final int DRAW = 9;
    public static final int WITHER_MINUS_CARDS = 3;
    public static final int UPGRADE_PLUS_WITHER = -1;

    public FullHouse() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.RARE, CardTarget.NONE);
        this.baseMagicNumber = this.magicNumber = WITHER_MINUS_CARDS;
        this.baseMisc = this.misc = DRAW;
        this.witherEffect = "Decreases the number of cards drawn.";
        this.linkWitherAmountToMagicNumber = true;
        Beaked.setDescription(this,DESCRIPTION);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new WitherAction(this));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p,this.misc,false));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FullHouse();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_WITHER);
        }
    }

}
