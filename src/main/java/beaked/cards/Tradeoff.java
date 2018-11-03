package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.actions.WitherAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Tradeoff extends AbstractWitherCard {
    public static final String ID = "beaked:Tradeoff";
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 1;
    public static final int EXHAUST = 1;
    public static final int WITHER_MINUS_EXHAUST = -1;
    public static final int DRAW = 5;
    public static final int UPGRADE_PLUS_DRAW = 1;

    public Tradeoff() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.COMMON, CardTarget.NONE);
        this.misc = this.baseMisc = EXHAUST;
        this.magicNumber = this.baseMagicNumber = DRAW;
        this.witherEffect = "Increases number of #yExhausted cards.";
        this.witherAmount = WITHER_MINUS_EXHAUST;
        Beaked.setDescription(this,DESCRIPTION);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new WitherAction(this));
        AbstractDungeon.actionManager.addToBottom(new ExhaustAction(p,p,this.misc,true));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p,this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Tradeoff();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_DRAW);
        }
    }
}
