package beaked.relics;

import basemod.abstracts.CustomRelic;
import beaked.actions.SacrificeDollAction;
import beaked.actions.SacrificeDollInitialAction;
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
        super(ID, TextureLoader.getTexture("beaked_img/relics/SacrificeDoll.png"),
                TextureLoader.getTexture("beaked_img/relics/outline/SacrificeDoll.png"),
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
        AbstractDungeon.actionManager.addToBottom(new SacrificeDollInitialAction());
    }
}