package beaked.cards.fakeCards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BackButtonCard extends CustomCard
{
    public static final String ID = "beaked:BackButtonCard";
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("beaked:UICard");

    public BackButtonCard()
    {
        super(ID, uiStrings.TEXT[0],
                "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", -2, uiStrings.TEXT[1], CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new BackButtonCard();
    }
}
