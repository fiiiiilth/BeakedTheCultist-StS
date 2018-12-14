package beaked.cards.fakeCards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BackButtonCard extends CustomCard
{
    public static final String ID = "beaked:BackButtonCard";


    public BackButtonCard()
    {
        super(ID, "Back",
                "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", -2, "Choose a different card color.", CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
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
