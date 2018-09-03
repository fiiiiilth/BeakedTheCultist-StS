package beaked.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class BlockPlusMagicVariable extends DynamicVariable
{
    @Override
    public String key()
    {
        return "beaked:B+M";
    }

    @Override
    public boolean isModified(AbstractCard card)
    {
        return card.isBlockModified || card.isMagicNumberModified;
    }

    @Override
    public int value(AbstractCard card)
    {
        return card.block + card.magicNumber;
    }

    @Override
    public int baseValue(AbstractCard card)
    {
        return card.baseBlock + card.baseMagicNumber;
    }

    @Override
    public boolean upgraded(AbstractCard card)
    {
        return card.upgradedBlock || card.upgradedMagicNumber;
    }
}