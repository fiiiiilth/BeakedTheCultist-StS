package beaked.patches;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.runHistory.TinyCard;

public class RenderTinyCardColorPatch {
	
	@SpirePatch(clz=TinyCard.class, method = "getIconBackgroundColor")
	public static class GetIconBackgroundColor {
		public static SpireReturn<Color> Prefix(TinyCard obj, AbstractCard card) {
			if (card.color == AbstractCardEnum.BEAKED_YELLOW) {
				return SpireReturn.Return(Color.GOLDENROD);
			}
			return SpireReturn.Continue();
		}
	}
	
	@SpirePatch(clz=TinyCard.class, method = "getIconDescriptionColor")
	public static class GetIconDescriptionColor {
		public static SpireReturn<Color> Prefix(TinyCard obj, AbstractCard card) {
			if (card.color == AbstractCardEnum.BEAKED_YELLOW) {
				return SpireReturn.Return(Color.GOLD);
			}
			return SpireReturn.Continue();
		}
	}
	
}