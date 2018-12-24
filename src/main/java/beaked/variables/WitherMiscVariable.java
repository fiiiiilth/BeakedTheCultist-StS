package beaked.variables;

import basemod.abstracts.DynamicVariable;
import beaked.Beaked;
import beaked.cards.AbstractWitherCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class WitherMiscVariable extends DynamicVariable
{
    @Override
    public String key()
    {
        return "beaked:wI";
    }

    @Override
    public boolean isModified(AbstractCard card)
    {
        return false;
    }

    @Override
    public int value(AbstractCard card)
    {
        return card.misc;
    }

    @Override
    public int baseValue(AbstractCard card)
    {
        //if (card instanceof AbstractWitherCard) return ((AbstractWitherCard) card).baseMisc;
        //return 0;
        return card.misc;
    }

    @Override
    public boolean upgraded(AbstractCard card)
    {
        return card instanceof AbstractWitherCard && ((AbstractWitherCard) card).upgradedMisc;
    }

    @Override
    public Color getNormalColor() {
        return Color.VIOLET;
    }
}