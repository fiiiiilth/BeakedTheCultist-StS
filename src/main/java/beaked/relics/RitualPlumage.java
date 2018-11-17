package beaked.relics;

import basemod.abstracts.CustomRelic;
import beaked.tools.TextureLoader;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RitualPlumage extends CustomRelic implements ClickableRelic {
    public static final String ID = "beaked:RitualPlumage";

    public RitualPlumage() {
        super(ID, TextureLoader.getTexture("beaked_images/relics/RitualPlumage.png"),
                TextureLoader.getTexture("beaked_images/relics/outline/RitualPlumage.png"),
                RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RitualPlumage();
    }
}