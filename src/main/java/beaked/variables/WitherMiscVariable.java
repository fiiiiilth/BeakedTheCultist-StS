package beaked.variables;

import basemod.abstracts.DynamicVariable;
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
        //if (card instanceof AbstractWitherCard) return (((AbstractWitherCard) card).baseMisc != card.misc);
        //return card.misc != 0;
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
        return false;
    }

    @Override
    public Color getNormalColor() {
        return Color.VIOLET;
    }
}