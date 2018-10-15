package beaked.patches;

import basemod.BaseMod;
import beaked.Beaked;
import beaked.powers.AwakenedPower;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import java.lang.reflect.Field;

public class DynamicPluralizePatch{

	@SpirePatch(cls="com.megacrit.cardcrawl.cards.AbstractCard", method="renderDescription",paramtypes={"com.badlogic.gdx.graphics.g2d.SpriteBatch"})
	public static class RenderDescription {
		public static void Prefix(AbstractCard obj, final SpriteBatch sb) {
			//Beaked.logger.debug(PluralizeFieldsPatch.savedMagicNumber.get(obj));
			// Only do this if the card has used Beaked.setDescription to set trueRawDescription, indicating it uses pluralization.
			if (obj.cardID != null && obj.cardID.startsWith("beaked:") && obj.isSeen && !obj.isLocked) {
				if (PluralizeFieldsPatch.trueRawDescription.get(obj) != PluralizeFieldsPatch.DEFAULT_DESCRIPTION && (
						PluralizeFieldsPatch.savedMagicNumber.get(obj) != obj.magicNumber || PluralizeFieldsPatch.savedMisc.get(obj) != obj.misc )){
					Beaked.createPluralizedDescription(obj);
				}
			}
		}
	}

	@SpirePatch(cls="com.megacrit.cardcrawl.cards.AbstractCard", method="renderDescriptionCN",paramtypes={"com.badlogic.gdx.graphics.g2d.SpriteBatch"})
	public static class RenderDescriptionCN {
		public static void Prefix(AbstractCard obj, final SpriteBatch sb) {
			//Beaked.logger.debug(PluralizeFieldsPatch.savedMagicNumber.get(obj));
			// Only do this if the card has used Beaked.setDescription to set trueRawDescription, indicating it uses pluralization.
			if (obj.cardID.startsWith("beaked:") && obj.isSeen && !obj.isLocked) {

				if (PluralizeFieldsPatch.trueRawDescription.get(obj) != PluralizeFieldsPatch.DEFAULT_DESCRIPTION && (
						PluralizeFieldsPatch.savedMagicNumber.get(obj) != obj.magicNumber || PluralizeFieldsPatch.savedMisc.get(obj) != obj.misc )){
					Beaked.createPluralizedDescription(obj);
				}
			}
		}
	}

	@SpirePatch(clz= SingleCardViewPopup.class, method="renderDescription",paramtypez={SpriteBatch.class})
	public static class SCVRenderDescription {
		private static Field cardF = null;
		public static void Prefix(SingleCardViewPopup obj, final SpriteBatch sb) {
			try {
				if (cardF == null){
					cardF = SingleCardViewPopup.class.getDeclaredField("card");
					cardF.setAccessible(true);
				}
				AbstractCard card = (AbstractCard)cardF.get(obj);
				//Beaked.logger.debug(PluralizeFieldsPatch.savedMagicNumber.get(obj));
				// Only do this if the card has used Beaked.setDescription to set trueRawDescription, indicating it uses pluralization.
				if (card.cardID.startsWith("beaked:") && card.isSeen && !card.isLocked) {
					if (PluralizeFieldsPatch.trueRawDescription.get(card) != PluralizeFieldsPatch.DEFAULT_DESCRIPTION && (
							PluralizeFieldsPatch.savedMagicNumber.get(card) != card.magicNumber || PluralizeFieldsPatch.savedMisc.get(card) != card.misc )){
						Beaked.createPluralizedDescription(card);
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@SpirePatch(clz= SingleCardViewPopup.class, method="renderDescriptionCN",paramtypez={SpriteBatch.class})
	public static class SCVRenderDescriptionCN {
		private static Field cardF = null;
		public static void Prefix(SingleCardViewPopup obj, final SpriteBatch sb) {
			try {
				if (cardF == null){
					cardF = SingleCardViewPopup.class.getDeclaredField("card");
					cardF.setAccessible(true);
				}
				AbstractCard card = (AbstractCard)cardF.get(obj);
				//Beaked.logger.debug(PluralizeFieldsPatch.savedMagicNumber.get(obj));
				// Only do this if the card has used Beaked.setDescription to set trueRawDescription, indicating it uses pluralization.
				if (card.cardID.startsWith("beaked:") && card.isSeen && !card.isLocked) {
					if (PluralizeFieldsPatch.trueRawDescription.get(card) != PluralizeFieldsPatch.DEFAULT_DESCRIPTION && (
							PluralizeFieldsPatch.savedMagicNumber.get(card) != card.magicNumber || PluralizeFieldsPatch.savedMisc.get(card) != card.misc )){
						Beaked.createPluralizedDescription(card);
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/*@SpirePatch(cls="com.megacrit.cardcrawl.cards.AbstractCard", method = "makeStatEquivalentCopy")
	public static class CopyBaseValues{
		public static AbstractCard Postfix(AbstractCard retVal, AbstractCard obj) {
			if (obj.cardID.startsWith("beaked:")) {
				Beaked.setDescription(obj, PluralizeFieldsPatch.trueRawDescription.get(retVal));
				return retVal;
			}
		}
	}*/
}
