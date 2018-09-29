package beaked.patches;

import basemod.BaseMod;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.RenderCustomDynamicVariable;
import beaked.Beaked;
import beaked.cards.AbstractWitherCard;
import beaked.powers.AwakenedPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(clz= RenderCustomDynamicVariable.Inner.class, method = "myRenderDynamicVariable")
public class ColorDynamicVariablePatch {
    @SpireInsertPatch(rloc = 10, localvars = "textColor")
    public static void Insert(Object __obj_instance, String key, char ckey, float start_x, float draw_y, int i, BitmapFont font, SpriteBatch sb, Character cend, @ByRef Color[] textColor) {
        if (key.trim().equals("!beaked:wD!") || key.trim().equals("!beaked:wI!") || key.trim().equals("!beaked:wB!")){
            textColor[0] = Color.VIOLET;
        }
    }
}