package beaked.relics;

import basemod.abstracts.CustomRelic;
import beaked.actions.SacrificeDollAction;
import beaked.tools.TextureLoader;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SacrificeDoll extends CustomRelic {
    public static final String ID = "beaked:SacrificeDoll";
    private AbstractPlayer p = AbstractDungeon.player;
    private GameActionManager am = AbstractDungeon.actionManager;

    public SacrificeDoll() {
        super(ID, TextureLoader.getTexture("beaked_images/relics/SacrificeDoll.png"),
                TextureLoader.getTexture("beaked_images/relics/outline/SacrificeDoll.png"),
                RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SacrificeDoll();
    }

    @Override
    public void atBattleStart() {
        am.addToBottom(new SacrificeDollAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null,true, AbstractDungeon.monsterRng)));
    }
}