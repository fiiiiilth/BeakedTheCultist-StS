package beaked.characters;

import java.util.ArrayList;

import beaked.Beaked;
import beaked.cards.Ceremony;
import beaked.cards.CrazyRituals;
import beaked.cards.Defend_Y;
import beaked.cards.Strike_Y;
import beaked.patches.AbstractCardEnum;
import beaked.patches.BeakedEnum;
import beaked.relics.MendingPlumage;
import beaked.relics.RitualPlumage;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Bash;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import basemod.abstracts.CustomPlayer;

public class BeakedTheCultist extends CustomPlayer {
    public static final int ENERGY_PER_TURN = 3;

    public static final String[] orbTextures = {
            "beaked_img/char/beaked/orb/layer1.png",
            "beaked_img/char/beaked/orb/layer2.png",
            "beaked_img/char/beaked/orb/layer3.png",
            "beaked_img/char/beaked/orb/layer4.png",
            "beaked_img/char/beaked/orb/layer5.png",
            "beaked_img/char/beaked/orb/layer6.png",
            "beaked_img/char/beaked/orb/layer1d.png",
            "beaked_img/char/beaked/orb/layer2d.png",
            "beaked_img/char/beaked/orb/layer3d.png",
            "beaked_img/char/beaked/orb/layer4d.png",
            "beaked_img/char/beaked/orb/layer5d.png",
    };

    public BeakedTheCultist(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures, "beaked_img/char/beaked/orb/vfx.png", (String)null, null);

        initializeClass(null, beaked.Beaked.makePath(Beaked.BEAKED_SHOULDER_2),
                beaked.Beaked.makePath(Beaked.BEAKED_SHOULDER_1),
                beaked.Beaked.makePath(Beaked.BEAKED_CORPSE),
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));

        reloadAnimation();
        AnimationState.TrackEntry e = this.state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    public void reloadAnimation(){
        this.loadAnimation(Beaked.blueCostume?"beaked_img/char/beaked/skeleton.atlas":"beaked_img/char/beaked/skeleton_y.atlas",
                "beaked_img/char/beaked/skeleton.json", 1.0f);
    }

    @Override
    public void initializeStarterDeck(){
        super.initializeStarterDeck();
        if (!Beaked.customModeCeremony) return;
        if (ModHelper.isModEnabled("Draft") || ModHelper.isModEnabled("Chimera") || ModHelper.isModEnabled("SealedDeck") || ModHelper.isModEnabled("Shiny") || ModHelper.isModEnabled("Insanity")) {
            this.masterDeck.addToTop(new Ceremony());
            this.masterDeck.addToTop(new Ceremony());
            if (ModHelper.isModEnabled("Insanity")){
                this.masterDeck.addToTop(new Ceremony());
                this.masterDeck.addToTop(new Ceremony());
                this.masterDeck.addToTop(new Ceremony());
            }
        }
    }

    @Override
    public String getLocalizedCharacterName(){
        return "Beaked The Cultist";
    }

    @Override
    public AbstractPlayer newInstance() {
        return new BeakedTheCultist("Beaked The Cultist", BeakedEnum.BEAKED_THE_CULTIST);
    }

    @Override
    public String getSpireHeartText() {
        return "CA-CAW!!!";
    }

    @Override
    public Color getSlashAttackColor() {
        return Color.YELLOW;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY };
    }

    @Override
    public String getVampireText() {
        return "CA-CAW!!!";
    }

    @Override
    public Color getCardTrailColor(){
        return Color.YELLOW;
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("VO_CULTIST_1C", MathUtils.random(-0.2f, 0.2f));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "VO_CULTIST_1C";
    }

    @Override
    public ArrayList<String> getStartingDeck() {
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

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        if (Beaked.ritualPlumage) {
            retVal.add(RitualPlumage.ID);
            UnlockTracker.markRelicAsSeen(RitualPlumage.ID);
        } else {
            retVal.add(MendingPlumage.ID);
            UnlockTracker.markRelicAsSeen(MendingPlumage.ID);
        }
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo("Beaked the Cultist",
                "Relies on healing and familiar techniques to power through, NL " +
                        "resorting to potent but hard to replicate spells.",
                50, 50, 0, 99, 5, this,
                getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return "The Beaked";
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.BEAKED_YELLOW;
    }

    @Override
    public Color getCardRenderColor() {
        return Color.YELLOW;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Ceremony();
    }

}
