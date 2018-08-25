package beaked.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.AbstractCard",
        method=SpirePatch.CLASS
)
public class PluralizeFieldsPatch
{
    public static final String DEFAULT_DESCRIPTION = "ERROR";
    public static SpireField<Integer> savedMagicNumber = new SpireField<>(() -> (0));
    public static SpireField<Integer> savedMisc = new SpireField<>(() -> (0));
    public static SpireField<String> trueRawDescription = new SpireField<>(() -> (DEFAULT_DESCRIPTION));
}