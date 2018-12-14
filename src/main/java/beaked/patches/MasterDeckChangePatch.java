package beaked.patches;

import beaked.Beaked;
import beaked.cards.VigorBurst;
import beaked.characters.BeakedTheCultist;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.Parasite;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.FaceTrader;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.CultistMask;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.lang.reflect.Field;

public class MasterDeckChangePatch {

    // TODO: Note for Yourself event doesn't use an ObtainEffect for whatever reason, so patch that too.

    public static class FastCardObtainEffectPatch {
        public static void Postfix(FastCardObtainEffect obj) {
            if (obj.duration <= 0.0F){
                try{
                    Field cardField = FastCardObtainEffect.class.getDeclaredField("card");
                    cardField.setAccessible(true);
                    AbstractCard card = (AbstractCard)cardField.get(obj);
                    if (card instanceof VigorBurst){
                        ((VigorBurst) card).onAddedToMasterDeck();
                    }
                    Beaked.onMasterDeckChange();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @SpirePatch(clz = ShowCardAndObtainEffect.class, method = "update")
    public static class ShowCardAndObtainEffectPatch {
        public static void Postfix(ShowCardAndObtainEffect obj) {
            if (obj.duration <= 0.0F){
                try{
                    Field cardField = ShowCardAndObtainEffect.class.getDeclaredField("card");
                    cardField.setAccessible(true);
                    AbstractCard card = (AbstractCard)cardField.get(obj);
                    if (card instanceof VigorBurst){
                        ((VigorBurst) card).onAddedToMasterDeck();
                    }
                    Beaked.onMasterDeckChange();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @SpirePatch(clz= CardGroup.class, method="removeCard",paramtypez = {AbstractCard.class})
    public static class onRemoveCard {
        public static void Prefix(CardGroup obj, final AbstractCard c) {
            if (c instanceof VigorBurst && obj.type == CardGroup.CardGroupType.MASTER_DECK) {
                ((VigorBurst) c).onRemovedFromMasterDeck();
            }
            Beaked.onMasterDeckChange();
        }
    }
}
