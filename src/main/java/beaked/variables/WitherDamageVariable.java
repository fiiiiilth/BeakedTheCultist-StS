package beaked.variables;

import basemod.abstracts.DynamicVariable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class WitherDamageVariable extends DynamicVariable
{
    @Override
    public String key()
    {
        return "beaked:wD";
    }

    @Override
    public boolean isModified(AbstractCard card)
    {
        return card.isDamageModified;
    }

    @Override
    public int value(AbstractCard card)
    {
        return card.damage;
    }

    @Override
    public int baseValue(AbstractCard card)
    {
        return card.baseDamage;
    }

    @Override
    public boolean upgraded(AbstractCard card)
    {
        return card.upgradedDamage;
    }

    @Override
    public Color getNormalColor() {
        return Color.VIOLET;
    }
}