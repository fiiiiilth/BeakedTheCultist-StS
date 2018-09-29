package beaked.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class WitherBlockVariable extends DynamicVariable
{
    @Override
    public String key()
    {
        return "beaked:wB";
    }

    @Override
    public boolean isModified(AbstractCard card)
    {
        return card.isBlockModified;
    }

    @Override
    public int value(AbstractCard card)
    {
        return card.block;
    }

    @Override
    public int baseValue(AbstractCard card)
    {
        return card.baseBlock;
    }

    @Override
    public boolean upgraded(AbstractCard card)
    {
        return card.upgradedBlock;
    }
}