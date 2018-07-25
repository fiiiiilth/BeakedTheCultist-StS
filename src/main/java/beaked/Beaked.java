package beaked;

import java.nio.charset.StandardCharsets;

import basemod.interfaces.*;
import beaked.cards.*;
import beaked.characters.BeakedTheCultist;
import beaked.patches.AbstractCardEnum;
import beaked.patches.BeakedEnum;
import beaked.relics.MendingPlumage;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.relics.CultistMask;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;

import basemod.BaseMod;
import basemod.ModPanel;

@SpireInitializer
public class Beaked implements PostInitializeSubscriber,
        EditCardsSubscriber, EditRelicsSubscriber, EditCharactersSubscriber,
        EditStringsSubscriber, SetUnlocksSubscriber, EditKeywordsSubscriber, OnCardUseSubscriber {
    public static final Logger logger = LogManager.getLogger(Beaked.class.getName());

    private static final String MODNAME = "BeakedTheCultist the Cultist";
    private static final String AUTHOR = "fiiiiilth";
    private static final String DESCRIPTION = "v0.1\n Adds BeakedTheCultist the Cultist as a new playable characters.";

    private static final Color YELLOW = CardHelper.getColor(255.0f, 255.0f, 0.0f);
    private static final String BEAKED_ASSETS_FOLDER = "img";

    // card backgrounds
    private static final String ATTACK_YELLOW = "512/bg_attack_yellow.png";
    private static final String SKILL_YELLOW = "512/bg_skill_yellow.png";
    private static final String POWER_YELLOW = "512/bg_power_yellow.png";
    private static final String ENERGY_ORB_YELLOW = "512/card_purple_orb.png";
    private static final String CARD_ENERGY_ORB = "512/card_small_orb.png";

    private static final String ATTACK_YELLOW_PORTRAIT = "1024/bg_attack_purple.png";
    private static final String SKILL_YELLOW_PORTRAIT = "1024/bg_skill_purple.png";
    private static final String POWER_YELLOW_PORTRAIT = "1024/bg_power_purple.png";
    private static final String ENERGY_ORB_YELLOW_PORTRAIT = "1024/card_purple_orb.png";

    // card images



    // power images


    // relic images


    // beaked assets
    private static final String BEAKED_BUTTON = "charSelect/beakedButton.png";
    private static final String BEAKED_PORTRAIT = "charSelect/beakedPortraitBG.jpg";
    public static final String BEAKED_SHOULDER_1 = "char/beaked/shoulder.png";
    public static final String BEAKED_SHOULDER_2 = "char/beaked/shoulder2.png";
    public static final String BEAKED_CORPSE = "char/beaked/corpse.png";

    // badge
    public static final String BADGE_IMG = "RelicBadge.png";

    // texture loaders


    /**
     * Makes a full path for a resource path
     * @param resource the resource, must *NOT* have a leading "/"
     * @return the full path
     */
    public static final String makePath(String resource) {
        return BEAKED_ASSETS_FOLDER + "/" + resource;
    }

    public Beaked() {
        BaseMod.subscribe(this);

        logger.info("creating the color " + AbstractCardEnum.BEAKED_YELLOW.toString());
        BaseMod.addColor(AbstractCardEnum.BEAKED_YELLOW.toString(),
                YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW,
                makePath(ATTACK_YELLOW), makePath(SKILL_YELLOW),
                makePath(POWER_YELLOW), makePath(ENERGY_ORB_YELLOW),
                makePath(ATTACK_YELLOW_PORTRAIT), makePath(SKILL_YELLOW_PORTRAIT),
                makePath(POWER_YELLOW_PORTRAIT), makePath(ENERGY_ORB_YELLOW_PORTRAIT), makePath(CARD_ENERGY_ORB));
    }

    public static void initialize() {
        logger.info("========================= BEAKED INIT =========================");

        new Beaked();

        logger.info("================================================================");
    }

    @Override
    public void receivePostInitialize() {
        // Mod badge
        Texture badgeTexture = new Texture(makePath(BADGE_IMG));
        ModPanel settingsPanel = new ModPanel();
        settingsPanel.addLabel("This mod does not have any settings (yet)!", 400.0f, 700.0f, (me) -> {});
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        Settings.isDailyRun = false;
        Settings.isTrial = false;
        Settings.isDemo = false;
    }

    @Override
    public void receiveEditCharacters() {
        logger.info("begin editting characters");

        logger.info("add " + BeakedEnum.BEAKED_THE_CULTIST.toString());
        BaseMod.addCharacter(BeakedTheCultist.class, "The Beaked", "BeakedTheCultist class string",
                AbstractCardEnum.BEAKED_YELLOW.toString(), "Beaked the Cultist",
                makePath(BEAKED_BUTTON), makePath(BEAKED_PORTRAIT),
                BeakedEnum.BEAKED_THE_CULTIST.toString());

        logger.info("done editting characters");
    }


    @Override
    public void receiveEditRelics() {
        logger.info("begin editting relics");

        // Add relics
        BaseMod.addRelicToCustomPool(new MendingPlumage(), AbstractCardEnum.BEAKED_YELLOW.toString());

        logger.info("done editting relics");
    }

    @Override
    public void receiveEditCards() {
        logger.info("begin editting cards");

        logger.info("add cards for " + BeakedEnum.BEAKED_THE_CULTIST.toString());

        //Basic
        BaseMod.addCard(new Strike_Y());
        BaseMod.addCard(new Defend_Y());
        BaseMod.addCard(new Ceremony());

        //Special
        BaseMod.addCard(new Inspiration());

        //Common
        BaseMod.addCard(new WarriorEssence());
        BaseMod.addCard(new Tweet());
        BaseMod.addCard(new Insight());
        BaseMod.addCard(new Evade());
        BaseMod.addCard(new Daydream());

        //Uncommon
        BaseMod.addCard(new Overpower());
        BaseMod.addCard(new Roost());
        BaseMod.addCard(new HuntressEssence());
        BaseMod.addCard(new Hexing());

        //Rare
        BaseMod.addCard(new FakeOut());
        BaseMod.addCard(new DarkPact());
        BaseMod.addCard(new MachineEssence());

        // make sure everything is always unlocked
        UnlockTracker.unlockCard("Strike_Y");
        UnlockTracker.unlockCard("Defend_Y");
        UnlockTracker.unlockCard("Ceremony");
        UnlockTracker.unlockCard("WarriorEssence");
        UnlockTracker.unlockCard("Tweet");
        UnlockTracker.unlockCard("Insight");
        UnlockTracker.unlockCard("Evade");
        UnlockTracker.unlockCard("Daydream");
        UnlockTracker.unlockCard("Flinch");
        UnlockTracker.unlockCard("Overpower");
        UnlockTracker.unlockCard("Roost");
        UnlockTracker.unlockCard("HuntressEssence");
        UnlockTracker.unlockCard("Hexing");
        UnlockTracker.unlockCard("FakeOut");
        UnlockTracker.unlockCard("DarkPact");
        UnlockTracker.unlockCard("MachineEssence");

        UnlockTracker.unlockCard("Inspiration");

        logger.info("done editting cards");
    }

    @Override
    public void receiveEditStrings() {
        logger.info("begin editting strings");

        // RelicStrings
        String relicStrings = Gdx.files.internal("localization/Beaked-RelicStrings.json").readString(
                String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        // CardStrings
        String cardStrings = Gdx.files.internal("localization/Beaked-CardStrings.json").readString(
                String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        //PowerStrings
        String powerStrings = Gdx.files.internal("localization/Beaked-PowerStrings.json").readString(
                String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);

        logger.info("done editting strings");
    }

    @Override
    public void receiveSetUnlocks() {

    }


    @Override
    public void receiveEditKeywords() {
        logger.info("setting up custom keywords");
        BaseMod.addKeyword(new String[] {"ritual", "Ritual"}, "Gain #yStrength at the beginning of your turn.");
        BaseMod.addKeyword(new String[] {"inspiration"}, "An unplayable status card. When drawn, it #yExhausts and draws #b2 more cards.");
    }

    @Override
    public void receiveCardUsed(AbstractCard c) {
        if(AbstractDungeon.player.hasRelic(CultistMask.ID)) {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_CULTIST_1A"));
            AbstractDungeon.actionManager.addToBottom(new TalkAction(true, "CAW", 1.0f, 2.0f));
        }
    }
}
