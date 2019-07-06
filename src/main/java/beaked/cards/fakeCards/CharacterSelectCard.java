package beaked.cards.fakeCards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.lang.reflect.Field;

public class CharacterSelectCard extends CustomCard
{
    public static final String ID = "beaked:CharacterSelectCard";

    public CardColor cardColor;

    public CharacterSelectCard(CardColor cardColor)
    {
        super(ID, "ERROR",
                "beaked_img/cards/BackButtonCard.png", -2, "", CardType.SKILL, cardColor, CardRarity.SPECIAL, CardTarget.NONE);

        this.name = getName(cardColor);
        this.initializeTitle();
        this.cardColor = cardColor;
        AbstractCard c = Beaked.getColorRepresentativeCard(cardColor);
        try {
            Field portraitField = AbstractCard.class.getDeclaredField("portrait");
            portraitField.setAccessible(true);
            this.portrait = (TextureAtlas.AtlasRegion)portraitField.get(c);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getName(CardColor color){
        for (AbstractPlayer character : CardCrawlGame.characterManager.getAllCharacters()) {
            if (character.getCardColor() == color) {
                return character.getLocalizedCharacterName();
            }
        }
        return capitalizeWord(color.toString());
    }

    private static String capitalizeWord(String str)
    {
        // from BaseMod
        if (str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + (str.length() > 1 ? str.substring(1).toLowerCase() : "");
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
        return new CharacterSelectCard(cardColor);
    }
}
