package beaked.patches;

import basemod.ReflectionHacks;
import beaked.Beaked;
import beaked.powers.AwakenedPower;
import beaked.relics.RitualPlumage;
import beaked.tools.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.awt.*;
import java.util.ArrayList;

@SpirePatch(clz = EndTurnButton.class, method = "render", paramtypez = {SpriteBatch.class})
public class EndTurnButtonGlowPatch {
    @SpireInsertPatch(locator = EndTurnButtonGlowLocator.class, localvars = {"buttonImg"})
    public static void InsertGlow(EndTurnButton obj, SpriteBatch sb, @ByRef Texture[] buttonImg) {
        try {
            // if end turn button was going to be glowing and our relic's condition is met...
            if (buttonImg[0] == ImageMaster.END_TURN_BUTTON_GLOW &&
                    AbstractDungeon.player.hasRelic(RitualPlumage.ID) &&
                    ((RitualPlumage)AbstractDungeon.player.getRelic(RitualPlumage.ID)).isRelicActive()){
                // set button image to our end turn button image instead.
                buttonImg[0] = RitualPlumage.specialEndTurnButton;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SpireInsertPatch(locator = EndTurnButtonRenderLocator.class, localvars = {"tmpY"})
    public static void InsertRelicImg(EndTurnButton obj, SpriteBatch sb, float tmpY) {
        try {
            // if our relic's condition is met...
            if (AbstractDungeon.player.hasRelic(RitualPlumage.ID) &&
                    ((RitualPlumage)AbstractDungeon.player.getRelic(RitualPlumage.ID)).isRelicActive()){
                // display its image beside the end turn button.
                // only draws when the end turn button would be drawn anyway, thanks to the insert location.
                float current_x = (float)ReflectionHacks.getPrivate(obj, EndTurnButton.class, "current_x");
                sb.setColor(Color.WHITE);
                sb.draw(AbstractDungeon.player.getRelic(RitualPlumage.ID).img, current_x - 48.0f, tmpY - 142.0f, 128.0f, 128.0f, 256.0f, 256.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 256, 256, false, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ExprEditor Instrument()
    {
        return new ExprEditor() {
            public void edit(MethodCall m)
                    throws CannotCompileException
            {
                // find TipHelper.renderGenericTip in EndTurnButton render (rendering end turn description)
                if (m.getClassName().equals(TipHelper.class.getName())
                        && m.getMethodName().equals("renderGenericTip")) {
                    // My understanding is that the replacement actually happens on initialization, so in order to
                    // do this check during gameplay, the code needs to be in this weird string format.
                    m.replace(
                            // If you will get the end turn bonus...
                            "if (" + AbstractDungeon.class.getName() + ".player.hasRelic(\"" + RitualPlumage.ID + "\") &&" +
                                    "((" + RitualPlumage.class.getName() + ")" + AbstractDungeon.class.getName() + ".player.getRelic(\"" + RitualPlumage.ID +  "\")).isRelicActive()){" +
                                // concatenate our ritual plumage description onto arg 4 of that method call (the description text).
                                "$4 = $4 + \" NL NL \" + " + RitualPlumage.class.getName() + ".endTurnString; " +
                                // raise the height of the text box a bit since we added more text
                                "$2 = $2 + 40f;" +
                                // proceed just means leave the rest of the stuff unchanged
                                "$_ = $proceed($$);" +
                            // If you won't get the end turn bonus...
                            "} else {" +
                                // proceed without changing anything
                                "$proceed($$);" +
                            "}");
                }
            }
        };
    }

    public static class EndTurnButtonGlowLocator extends SpireInsertLocator {

        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {

            Matcher finalMatcher = new Matcher.MethodCallMatcher(
                    SpriteBatch.class, "draw");

            // aiming for sb.draw(buttonImg) on line 272
            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[1]};
        }
    }

    public static class EndTurnButtonRenderLocator extends SpireInsertLocator {

        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {

            Matcher finalMatcher = new Matcher.MethodCallMatcher(
                    Hitbox.class, "render");

            // aiming for this.hb.render(sb) on line 289
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
        }
    }
}