package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Daydream extends CustomCard {
    public static final String ID = "beaked:Daydream";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 1;
    public static final int INSPIRATION_COUNT = 3;
    public static final int UPGRADE_INSPIRATION_COUNT = 1;

    public Daydream() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.COMMON, CardTarget.SELF);

        this.magicNumber = this.baseMagicNumber = INSPIRATION_COUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Inspiration(), this.magicNumber, true, true));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Daydream();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_INSPIRATION_COUNT);
        }
    }
}
