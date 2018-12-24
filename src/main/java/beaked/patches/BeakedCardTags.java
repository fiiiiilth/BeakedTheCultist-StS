package beaked.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class BeakedCardTags {
    @SpireEnum
    public static AbstractCard.CardTags NO_CRAZY_RITUALS; // this card won't be pulled by Crazy Rituals.
    public static AbstractCard.CardTags NO_SACRED_DECK; // this card won't appear in Sacred Deck's selection.
}
