package beaked.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.AbstractCard",
        method=SpirePatch.CLASS
)
public class CardAwakenedPatch
{
    // True if the card's cost is reduced below 0 by Awakened Form, which forces costForTurn to stay negative.
    // Not a guarantee that the cost is always negative, just that it can and probably will be most of the time.
    public static SpireField<Boolean> negativeCost = new SpireField<>(Boolean.FALSE);
}