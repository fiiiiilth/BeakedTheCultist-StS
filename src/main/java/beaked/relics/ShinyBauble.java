package beaked.relics;

import basemod.abstracts.CustomRelic;
import beaked.Beaked;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ShinyBauble extends CustomRelic {
    public static final String ID = "beaked:ShinyBauble";

    public ShinyBauble() {
        super(ID, new Texture("beaked_img/relics/" + Beaked.getActualID(ID) + ".png"),
                new Texture("beaked_img/relics/outline/" + Beaked.getActualID(ID) + ".png"),
                RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        if (this.counter == -2) {
            this.pulse = false;
            this.counter = -1;
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new RitualPower(AbstractDungeon.player,1,true),1));
        }
    }

    @Override
    public void setCounter(final int counter) {
        super.setCounter(counter);
        if (counter == -2) {
            this.pulse = true;
        }
    }

    @Override
    public void onEnterRestRoom() {
        this.flash();
        this.counter = -2;
        this.pulse = true;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ShinyBauble();
    }
}
