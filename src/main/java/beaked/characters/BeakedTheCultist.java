package beaked.characters;

import java.util.ArrayList;

import beaked.Beaked;
import beaked.cards.Ceremony;
import beaked.cards.CrazyRituals;
import beaked.cards.Defend_Y;
import beaked.cards.Strike_Y;
import beaked.relics.MendingPlumage;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.DailyMods;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import basemod.abstracts.CustomPlayer;
import beaked.patches.BeakedEnum;

public class BeakedTheCultist extends CustomPlayer {
    public static final int ENERGY_PER_TURN = 3;

    public static final String[] orbTextures = {
            "img/char/beaked/orb/layer1.png",
            "img/char/beaked/orb/layer2.png",
            "img/char/beaked/orb/layer3.png",
            "img/char/beaked/orb/layer4.png",
            "img/char/beaked/orb/layer5.png",
            "img/char/beaked/orb/layer6.png",
            "img/char/beaked/orb/layer1d.png",
            "img/char/beaked/orb/layer2d.png",
            "img/char/beaked/orb/layer3d.png",
            "img/char/beaked/orb/layer4d.png",
            "img/char/beaked/orb/layer5d.png",
    };

    public BeakedTheCultist(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures, "img/char/beaked/orb/vfx.png", (String)null, null);

        initializeClass(null, beaked.Beaked.makePath(Beaked.BEAKED_SHOULDER_2),
                beaked.Beaked.makePath(Beaked.BEAKED_SHOULDER_1),
                beaked.Beaked.makePath(Beaked.BEAKED_CORPSE),
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));

        this.loadAnimation("img/char/beaked/skeleton.atlas", "img/char/beaked/skeleton.json", 1.0f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    public static ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Strike_Y.ID);
        retVal.add(Strike_Y.ID);
        retVal.add(Strike_Y.ID);
        retVal.add(Strike_Y.ID);
        retVal.add(Strike_Y.ID);
        retVal.add(Strike_Y.ID);
        retVal.add(Defend_Y.ID);
        retVal.add(Defend_Y.ID);
        retVal.add(Defend_Y.ID);
        retVal.add(Defend_Y.ID);
        if (Beaked.crazyRituals) {
            retVal.add(Ceremony.ID);
            retVal.add(CrazyRituals.ID);
            retVal.add(CrazyRituals.ID);
        }
        else{
            retVal.add(Ceremony.ID);
            retVal.add(Ceremony.ID);
            retVal.add(Ceremony.ID);
        }
        return retVal;
    }

    public static ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(MendingPlumage.ID);
        UnlockTracker.markRelicAsSeen(MendingPlumage.ID);
        return retVal;
    }

    public static CharSelectInfo getLoadout() {
        return new CharSelectInfo("Beaked the Cultist",
                "Relies on healing and familiar techniques to power through, NL " +
                        "resorting to potent but hard to replicate spells.",
                50, 50, 0, 99, 5,
                BeakedEnum.BEAKED_THE_CULTIST, getStartingRelics(), getStartingDeck(), false);
    }

}
