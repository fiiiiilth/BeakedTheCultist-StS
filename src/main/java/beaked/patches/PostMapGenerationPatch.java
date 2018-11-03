package beaked.patches;

import beaked.interfaces.IPostMapGenerationRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

@SpirePatch(clz= AbstractDungeon.class, method = "generateMap")
public class PostMapGenerationPatch {
    public static void Postfix() {
        for (AbstractRelic r:AbstractDungeon.player.relics){
            if (r instanceof IPostMapGenerationRelic){
                ((IPostMapGenerationRelic)r).postMapGeneration();
            }
        }
    }
}