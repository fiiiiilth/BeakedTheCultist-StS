package beaked.relics;

import basemod.abstracts.CustomRelic;
import beaked.Beaked;
import beaked.powers.NegationPower;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ThroatLozenge extends CustomRelic {
    public static final String ID = "beaked:ThroatLozenge";

    public ThroatLozenge() {
        super(ID, new Texture("beaked_img/relics/" + Beaked.getActualID(ID) + ".png"),
                new Texture("beaked_img/relics/outline/" + Beaked.getActualID(ID) + ".png"),
                RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new NegationPower(AbstractDungeon.player,1),1));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ThroatLozenge();
    }
}
