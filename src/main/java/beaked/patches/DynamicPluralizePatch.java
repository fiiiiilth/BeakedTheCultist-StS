package beaked.patches;

import beaked.Beaked;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(cls="com.megacrit.cardcrawl.cards.AbstractCard", method="render",paramtypes={"com.badlogic.gdx.graphics.g2d.SpriteBatch","boolean"})
public class DynamicPluralizePatch {
	public static void Prefix(AbstractCard obj) {

		// Only do this if the card has used Beaked.setDescription to set trueRawDescription, indicating it uses pluralization.
		if (obj.cardID.startsWith("beaked:")) {
			if (PluralizeFieldsPatch.trueRawDescription.get(obj) != PluralizeFieldsPatch.DEFAULT_DESCRIPTION && (
					PluralizeFieldsPatch.savedMagicNumber.get(obj) != obj.magicNumber ||
					PluralizeFieldsPatch.savedMisc.get(obj) != obj.misc )){
				Beaked.createPluralizedDescription(obj);
			}
		}
	}
}
