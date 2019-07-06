package beaked;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import basemod.*;
import basemod.helpers.BaseModCardTags;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import beaked.cards.*;
import beaked.characters.BeakedTheCultist;
import beaked.monsters.SuperParasite;
import beaked.patches.AbstractCardEnum;
import beaked.patches.BeakedEnum;
import beaked.patches.CardTagsEnum;
import beaked.patches.PluralizeFieldsPatch;
import beaked.potions.MendingBrew;
import beaked.potions.RitualPotion;
import beaked.relics.*;
import beaked.ui.ModRelicPreview;
import beaked.variables.*;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.CultistMask;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;

@SpireInitializer
public class Beaked implements PostInitializeSubscriber,
        EditCardsSubscriber, EditRelicsSubscriber, EditCharactersSubscriber,
        EditStringsSubscriber, SetUnlocksSubscriber, EditKeywordsSubscriber, OnCardUseSubscriber, PostBattleSubscriber,
        StartGameSubscriber, OnStartBattleSubscriber, PostDungeonInitializeSubscriber{
    public static final Logger logger = LogManager.getLogger(Beaked.class.getName());

    private static final String MODNAME = "Beaked the Cultist";
    private static final String AUTHOR = "fiiiiilth, Moocowsgomoo";
    private static final String DESCRIPTION = "Adds Beaked the Cultist as a new playable character.";

    private static final Color YELLOW = CardHelper.getColor(255.0f, 255.0f, 0.0f);
    private static final String BEAKED_ASSETS_FOLDER = "beaked_img";

    // card backgrounds
    private static final String ATTACK_YELLOW = "512/bg_attack_yellow.png";
    private static final String SKILL_YELLOW = "512/bg_skill_yellow.png";
    private static final String POWER_YELLOW = "512/bg_power_yellow.png";
    private static final String ENERGY_ORB_YELLOW = "512/card_purple_orb.png";
    private static final String CARD_ENERGY_ORB = "512/card_small_orb.png";

    private static final String ATTACK_YELLOW_PORTRAIT = "1024/bg_attack_yellow.png";
    private static final String SKILL_YELLOW_PORTRAIT = "1024/bg_skill_yellow.png";
    private static final String POWER_YELLOW_PORTRAIT = "1024/bg_power_yellow.png";
    private static final String ENERGY_ORB_YELLOW_PORTRAIT = "1024/card_purple_orb.png";

    // card images



    // power images


    // relic images


    // beaked assets
    private static final String BEAKED_BUTTON = "charSelect/beakedButton.png";
    public static final String BEAKED_SHOULDER_1 = "char/beaked/shoulder.png";
    public static final String BEAKED_SHOULDER_2 = "char/beaked/shoulder2.png";
    public static final String BEAKED_CORPSE = "char/beaked/corpse.png";

    // badge
    public static final String BADGE_IMG = "RelicBadge.png";

    public static BeakedTheCultist beakedCharacter;

    public static boolean isFlying = false;
    public static float initialPlayerHeight=-1.0f; // used to reset player position when flying ends

    public static Properties beakedDefaults = new Properties();
    public static final String PROP_CRAZY_RITUALS = "crazyRituals";
    public static final String PROP_ENABLE_PARASITE = "enableParasite";
    public static final String PROP_CUSTOM_CEREMONY = "customModeCeremony";
    public static final String PROP_RELIC_SHARING = "relicSharing";
    public static final String PROP_COSTUME_COLOR = "costumeColor";
    public static final String PROP_SACRED_NECKLACE = "sacredNecklace";
    public static final String PROP_SACRED_DECK = "sacredDeck";
    public static boolean crazyRituals = false;
    public static boolean enableParasite = true;
    public static boolean customModeCeremony = true;
    public static boolean relicSharing = true;
    public static int costumeColor = 0;
    public static int sacredNecklaceAvailability = 2;
    public static int sacredDeckAvailability = 2;

    public static ArrayList<AbstractRelic> shareableRelics = new ArrayList<>();
    public static HashMap<String, ArrayList<ModLabeledToggleButton>> specialRelicRadioBtns = new HashMap<>();

    public static final int NUM_COSTUMES = 4;
    public static final String[] PORTRAIT_STRINGS = {
            "charSelect/beakedPortrait_y.jpg",
            "charSelect/beakedPortrait.jpg",
            "charSelect/beakedPortrait_y.jpg",
            "charSelect/beakedPortrait.jpg"
    };
    public static final String[] SPRITE_POSTFIX_STRINGS = {
            "_y",
            "",
            "_g",
            "_sticks"
    };

    public String cawText;

    public static final boolean isReplayLoaded;
    public static final boolean isInfiniteLoaded;

    static
    {
        isReplayLoaded = Loader.isModLoaded("ReplayTheSpireMod");
        // CROSSOVER: Add reverse magic number tags so ring of chaos works better.
        if (isReplayLoaded) {
            logger.info("Beaked | Detected Replay The Spire");
        }
        isInfiniteLoaded = Loader.isModLoaded("infinitespire");
        // CROSSOVER: Nothing yet, but planning to add quests.
        if (isInfiniteLoaded) {
            logger.info("Beaked | Detected Infinite Spire");
        }
    }

    // texture loaders


    /**
     * Makes a full path for a resource path
     * @param resource the resource, must *NOT* have a leading "/"
     * @return the full path
     */
    public static final String makePath(String resource) {
        return BEAKED_ASSETS_FOLDER + "/" + resource;
    }

    public static int cardsPlayedThisCombat = 0;

    public Beaked() {
        BaseMod.subscribe(this);

        logger.info("creating the color " + AbstractCardEnum.BEAKED_YELLOW.toString());
        BaseMod.addColor(AbstractCardEnum.BEAKED_YELLOW,
                YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW,
                makePath(ATTACK_YELLOW), makePath(SKILL_YELLOW),
                makePath(POWER_YELLOW), makePath(ENERGY_ORB_YELLOW),
                makePath(ATTACK_YELLOW_PORTRAIT), makePath(SKILL_YELLOW_PORTRAIT),
                makePath(POWER_YELLOW_PORTRAIT), makePath(ENERGY_ORB_YELLOW_PORTRAIT), makePath(CARD_ENERGY_ORB));

        beakedDefaults.setProperty(PROP_CRAZY_RITUALS, "FALSE");
        beakedDefaults.setProperty(PROP_ENABLE_PARASITE, "TRUE");
        beakedDefaults.setProperty(PROP_CUSTOM_CEREMONY, "TRUE");
        beakedDefaults.setProperty(PROP_RELIC_SHARING, "TRUE");
        beakedDefaults.setProperty(PROP_COSTUME_COLOR, "0");
        beakedDefaults.setProperty(PROP_SACRED_NECKLACE, "2");
        beakedDefaults.setProperty(PROP_SACRED_DECK,"2");

        try {
            SpireConfig config = new SpireConfig("TheBeaked", "BeakedConfig",beakedDefaults);
            config.load();
            crazyRituals = config.getBool(PROP_CRAZY_RITUALS);
            enableParasite = config.getBool(PROP_ENABLE_PARASITE);
            customModeCeremony = config.getBool(PROP_CUSTOM_CEREMONY);
            relicSharing = config.getBool(PROP_RELIC_SHARING);
            costumeColor = config.getInt(PROP_COSTUME_COLOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        UIStrings configStrings = CardCrawlGame.languagePack.getUIString("beaked:ConfigMenuText");
        UIStrings costumeStrings = CardCrawlGame.languagePack.getUIString("beaked:CostumeColors");
        UIStrings cawStrings = CardCrawlGame.languagePack.getUIString("beaked:CAW");
        cawText = cawStrings.TEXT[0];

        ModLabeledToggleButton crazyBtn = new ModLabeledToggleButton(configStrings.TEXT[0],
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                crazyRituals, settingsPanel, (label) -> {
        }, (button) -> {
            crazyRituals = button.enabled;
            saveData();
            resetCharSelect();
        });
        settingsPanel.addUIElement(crazyBtn);

        ModLabeledToggleButton customModeCeremonyBtn = new ModLabeledToggleButton(configStrings.TEXT[1],
                350.0f, 650.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                customModeCeremony, settingsPanel, (label) -> {
        }, (button) -> {
            customModeCeremony = button.enabled;
            saveData();
        });
        settingsPanel.addUIElement(customModeCeremonyBtn);

        ModLabeledToggleButton bossesBtn = new ModLabeledToggleButton(configStrings.TEXT[2],
                350.0f, 550.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                enableParasite, settingsPanel, (label) -> {
        }, (button) -> {
            enableParasite = button.enabled;
            saveData();
        });
        settingsPanel.addUIElement(bossesBtn);

        ModLabeledToggleButton relicSharingBtn = new ModLabeledToggleButton(configStrings.TEXT[3],
                350.0f, 500.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                relicSharing, settingsPanel, (label) -> {
        }, (button) -> {
            relicSharing = button.enabled;
            saveData();
            adjustSharedRelics();
        });
        settingsPanel.addUIElement(relicSharingBtn);

        ModLabel costumeLabelTxt = new ModLabel(configStrings.TEXT[4],350.0f, 415.0f,settingsPanel,(me)->{});
        settingsPanel.addUIElement(costumeLabelTxt);
        ModLabel costumeColorTxt = new ModLabel(costumeStrings.TEXT[costumeColor],670.0f, 415.0f,settingsPanel,(me)->{});
        settingsPanel.addUIElement(costumeColorTxt);

        ModButton costumeLeftBtn = new ModButton(605.0f, 400.0f, ImageMaster.loadImage("img/tinyLeftArrow.png"),settingsPanel,(me)->{
            costumeColor = costumeColor-1>=0?costumeColor-1:NUM_COSTUMES-1;
            costumeColorTxt.text = costumeStrings.TEXT[costumeColor];
            changeCostume();
        });
        settingsPanel.addUIElement(costumeLeftBtn);
        ModButton costumeRightBtn = new ModButton(765.0f, 400.0f, ImageMaster.loadImage("img/tinyRightArrow.png"),settingsPanel,(me)->{
            costumeColor = (costumeColor+1)%NUM_COSTUMES;
            costumeColorTxt.text = costumeStrings.TEXT[costumeColor];
            changeCostume();
        });
        settingsPanel.addUIElement(costumeRightBtn);

        ModLabel specialRelicsText = new ModLabel(configStrings.TEXT[5],350.0f, 350.0f,settingsPanel,(me)->{});
        settingsPanel.addUIElement(specialRelicsText);

        addSpecialRelicRadioOptions(settingsPanel,600f,290f,new SacredNecklace(),PROP_SACRED_NECKLACE);
        addSpecialRelicRadioOptions(settingsPanel, 820f,290f, new SacredDeck(),PROP_SACRED_DECK);

        Settings.isDailyRun = false;
        Settings.isTrial = false;
        Settings.isDemo = false;

        logger.debug(configStrings.TEXT[6]);

        if (enableParasite){
            BaseMod.addMonster(SuperParasite.ID, configStrings.TEXT[6], () -> new SuperParasite());
            BaseMod.addEliteEncounter(TheBeyond.ID, new MonsterInfo(SuperParasite.ID,1.0f));
        }

        BaseMod.addPotion(RitualPotion.class, YELLOW, YELLOW, YELLOW, RitualPotion.POTION_ID, BeakedEnum.BEAKED_THE_CULTIST);
        BaseMod.addPotion(MendingBrew.class,Color.GREEN.cpy(),Color.WHITE.cpy(),Color.GREEN.cpy(),MendingBrew.POTION_ID,BeakedEnum.BEAKED_THE_CULTIST);
    }

    public static void saveData() {
        try {
            SpireConfig config = new SpireConfig("ConstructMod", "ConstructSaveData", beakedDefaults);
            config.setBool(PROP_CRAZY_RITUALS, crazyRituals);
            config.setBool(PROP_ENABLE_PARASITE, enableParasite);
            config.setBool(PROP_CUSTOM_CEREMONY, customModeCeremony);
            config.setBool(PROP_RELIC_SHARING, relicSharing);
            config.setInt(PROP_COSTUME_COLOR, costumeColor);
            config.setInt(PROP_SACRED_NECKLACE, sacredNecklaceAvailability);
            config.setInt(PROP_SACRED_DECK, sacredDeckAvailability);
            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSpecialRelicRadioOptions(ModPanel settingsPanel, float x, float y, AbstractRelic relic,String saveProperty){

        UIStrings btnStrings = CardCrawlGame.languagePack.getUIString("beaked:SpecialRelicSettings");

        int relicSetting = -1;
        try {
            SpireConfig config = new SpireConfig("TheBeaked", "BeakedConfig",beakedDefaults);
            config.load();
            relicSetting = config.getInt(saveProperty);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ModRelicPreview sacredNecklaceImg = new ModRelicPreview(x, y,relic,settingsPanel);
        settingsPanel.addUIElement(sacredNecklaceImg);

        ModLabeledToggleButton radioBtnOff = new ModLabeledToggleButton(btnStrings.TEXT[0],
                x, y-10f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                relicSetting == 0, settingsPanel, (label) -> {
        }, (button) -> {
            try {
                SpireConfig config = new SpireConfig("TheBeaked", "BeakedConfig",beakedDefaults);
                config.setInt(saveProperty,0);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
            adjustSharedRelics();
            updateButtonStates();
        });
        settingsPanel.addUIElement(radioBtnOff);

        ModLabeledToggleButton radioBtnBeaked = new ModLabeledToggleButton(btnStrings.TEXT[1],
                x, y-50f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                relicSetting == 1, settingsPanel, (label) -> {
        }, (button) -> {
            try {
                SpireConfig config = new SpireConfig("TheBeaked", "BeakedConfig",beakedDefaults);
                config.setInt(saveProperty,1);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
            adjustSharedRelics();
            updateButtonStates();
        });
        settingsPanel.addUIElement(radioBtnBeaked);

        ModLabeledToggleButton radioBtnAll = new ModLabeledToggleButton(btnStrings.TEXT[2],
                x, y-90f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                relicSetting == 2, settingsPanel, (label) -> {
        }, (button) -> {
            try {
                SpireConfig config = new SpireConfig("TheBeaked", "BeakedConfig",beakedDefaults);
                config.setInt(saveProperty,2);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
            adjustSharedRelics();
            updateButtonStates();
        });
        settingsPanel.addUIElement(radioBtnAll);

        ArrayList<ModLabeledToggleButton> btns = new ArrayList<>();
        btns.add(radioBtnOff);
        btns.add(radioBtnBeaked);
        btns.add(radioBtnAll);
        specialRelicRadioBtns.put(saveProperty,btns);
    }

    public void updateButtonStates(){
        for (String s : specialRelicRadioBtns.keySet()){
            int count = 0;
            int relicSetting=-1;
            try {
                SpireConfig config = new SpireConfig("TheBeaked", "BeakedConfig",beakedDefaults);
                config.load();
                relicSetting = config.getInt(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (ModLabeledToggleButton btn : specialRelicRadioBtns.get(s)){
                btn.toggle.enabled = (count == relicSetting);
                count ++;
            }
        }
    }

    public void adjustSharedRelics(){

        for (AbstractRelic relic : shareableRelics){
            Beaked.logger.debug("Removing relic " + relic.name);
            BaseMod.removeRelic(relic);
            BaseMod.removeRelicFromCustomPool(relic,AbstractCardEnum.BEAKED_YELLOW);
        }
        AbstractRelic sNeck = (RelicLibrary.getRelic(SacredNecklace.ID));
        if (sNeck != null) {
            BaseMod.removeRelic(sNeck);
            BaseMod.removeRelicFromCustomPool(sNeck,AbstractCardEnum.BEAKED_YELLOW);
        }
        AbstractRelic sDeck = (RelicLibrary.getRelic(SacredDeck.ID));
        if (sDeck != null) {
            BaseMod.removeRelic(sDeck);
            BaseMod.removeRelicFromCustomPool(sDeck,AbstractCardEnum.BEAKED_YELLOW);
        }

        addSharedRelics();
    }

    public void addSharedRelics(){

        for (AbstractRelic relic : shareableRelics){
            Beaked.logger.debug("Adding relic " + relic.name);
            if (relicSharing) BaseMod.addRelic(relic, RelicType.SHARED);
            else BaseMod.addRelicToCustomPool(relic, AbstractCardEnum.BEAKED_YELLOW);
        }

        try {
            SpireConfig config = new SpireConfig("TheBeaked", "BeakedConfig",beakedDefaults);
            config.load();

            int necklaceVal = config.getInt(PROP_SACRED_NECKLACE);
            if (necklaceVal == 1) BaseMod.addRelicToCustomPool(new SacredNecklace(),AbstractCardEnum.BEAKED_YELLOW);
            else if (necklaceVal == 2) BaseMod.addRelic(new SacredNecklace(),RelicType.SHARED);
            int deckVal = config.getInt(PROP_SACRED_DECK);
            if (deckVal == 1) BaseMod.addRelicToCustomPool(new SacredDeck(),AbstractCardEnum.BEAKED_YELLOW);
            else if (deckVal == 2) BaseMod.addRelic(new SacredDeck(),RelicType.SHARED);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeCostume(){
        saveData();
        BaseMod.playerPortraitMap.remove(BeakedEnum.BEAKED_THE_CULTIST);
        BaseMod.playerPortraitMap.put(BeakedEnum.BEAKED_THE_CULTIST, makePath(PORTRAIT_STRINGS[costumeColor]));
        beakedCharacter.reloadAnimation();
        resetCharSelect();
    }

    public void resetCharSelect() {
        ((ArrayList<CharacterOption>) ReflectionHacks.getPrivate(CardCrawlGame.mainMenuScreen.charSelectScreen, CharacterSelectScreen.class, "options")).clear();
        CardCrawlGame.mainMenuScreen.charSelectScreen.initialize();
    }

    @Override
    public void receiveEditCharacters() {
        logger.info("begin editing characters");

        logger.info("add " + BeakedEnum.BEAKED_THE_CULTIST.toString());
        beakedCharacter = new BeakedTheCultist("Beaked The Cultist", BeakedEnum.BEAKED_THE_CULTIST);
        BaseMod.addCharacter(beakedCharacter, makePath(BEAKED_BUTTON),
                makePath(PORTRAIT_STRINGS[costumeColor]),
                BeakedEnum.BEAKED_THE_CULTIST);

        logger.info("done editing characters");
    }


    @Override
    public void receiveEditRelics() {
        logger.info("begin editing relics");

        // Add relics
        BaseMod.addRelicToCustomPool(new MendingPlumage(), AbstractCardEnum.BEAKED_YELLOW); // starter
        BaseMod.addRelicToCustomPool(new BlessedCoat(), AbstractCardEnum.BEAKED_YELLOW);    // upgrade
        BaseMod.addRelicToCustomPool(new FlawlessSticks(), AbstractCardEnum.BEAKED_YELLOW);
        BaseMod.addRelicToCustomPool(new ThroatLozenge(), AbstractCardEnum.BEAKED_YELLOW);
        BaseMod.addRelicToCustomPool(new RitualPlumage(), AbstractCardEnum.BEAKED_YELLOW);
        BaseMod.addRelicToCustomPool(new SacrificeDoll(), AbstractCardEnum.BEAKED_YELLOW);
        addShareableRelic(new ShinyBauble());
        addShareableRelic(new MawFillet());

        addSharedRelics();

        logger.info("done editing relics");
    }

    public void addShareableRelic(AbstractRelic relic){
        shareableRelics.add(relic);
    }

    @Override
    public void receiveEditCards() {
        logger.info("begin editing cards");

        logger.info("add cards for " + BeakedEnum.BEAKED_THE_CULTIST.toString());

        BaseMod.addDynamicVariable(new MiscVariable());
        BaseMod.addDynamicVariable(new BlockPlusMagicVariable());
        BaseMod.addDynamicVariable(new WitherMiscVariable());
        BaseMod.addDynamicVariable(new WitherBlockVariable());
        BaseMod.addDynamicVariable(new WitherDamageVariable());

        //Basic
        BaseMod.addCard(new Strike_Y());
        BaseMod.addCard(new Defend_Y());
        BaseMod.addCard(new Ceremony());

        //Special
        BaseMod.addCard(new Inspiration());
        BaseMod.addCard(new Respite());
        BaseMod.addCard(new Psalm());
        BaseMod.addCard(new Stick());

        //Common
        BaseMod.addCard(new WarriorEssence());
        BaseMod.addCard(new Tweet());
        BaseMod.addCard(new Insight());
        BaseMod.addCard(new Evade());
        BaseMod.addCard(new Daydream());
        BaseMod.addCard(new Flinch());
        BaseMod.addCard(new Redirect());
        BaseMod.addCard(new Foresight());
        BaseMod.addCard(new Resilience());
        BaseMod.addCard(new TuckingFeathers());
        BaseMod.addCard(new Poke());
        BaseMod.addCard(new Tradeoff());
        BaseMod.addCard(new BraceForImpact());
        BaseMod.addCard(new Brace());
        BaseMod.addCard(new Molting());
        BaseMod.addCard(new CursingBlood());
        BaseMod.addCard(new DigDeep());
        BaseMod.addCard(new FeelMyPain());
        BaseMod.addCard(new Windmill());
        BaseMod.addCard(new Cower());
        BaseMod.addCard(new SurvivalInstinct());

        //Uncommon
        BaseMod.addCard(new Overpower());
        BaseMod.addCard(new Roost());
        BaseMod.addCard(new HuntressEssence());
        BaseMod.addCard(new Hexing());
        //BaseMod.addCard(new SowTheSeeds());
        BaseMod.addCard(new Pecker());
        BaseMod.addCard(new Negation());
        BaseMod.addCard(new Recite());
        BaseMod.addCard(new DesperateSwing());
        BaseMod.addCard(new Devastation());
        BaseMod.addCard(new Struggle());
        BaseMod.addCard(new BounceBack());
        BaseMod.addCard(new StickSmack());
        BaseMod.addCard(new WarriorSpirit());
        BaseMod.addCard(new HuntressSpirit());
        BaseMod.addCard(new MachineSpirit());
        BaseMod.addCard(new BloodForTheGods());
        BaseMod.addCard(new Replenish());
        //BaseMod.addCard(new PrayerOfSafety());
        BaseMod.addCard(new BarbedWire());
        BaseMod.addCard(new SacrificialAttack());
        BaseMod.addCard(new DigDeeper());
        BaseMod.addCard(new BloodRitual());
        BaseMod.addCard(new RuinSticks());
        BaseMod.addCard(new Overexert());
        BaseMod.addCard(new SacrificialDive());
        BaseMod.addCard(new Mantra());
        BaseMod.addCard(new MaddeningMurmurs());
        //BaseMod.addCard(new TurnTheTables());
        BaseMod.addCard(new StunningBlow());
        BaseMod.addCard(new WindBringer());
        BaseMod.addCard(new MimicWarrior());
        BaseMod.addCard(new MimicHuntress());
        BaseMod.addCard(new MimicMachine());
        BaseMod.addCard(new CrazyRituals());
        BaseMod.addCard(new RitualOfBody());
        BaseMod.addCard(new VigorBurst());


        //Rare
        BaseMod.addCard(new FakeOut());
        BaseMod.addCard(new DarkPact());
        BaseMod.addCard(new MachineEssence());
        BaseMod.addCard(new Sacrifice());
        BaseMod.addCard(new Brainwash());
        BaseMod.addCard(new DarkTribute());
        BaseMod.addCard(new LiftOff());
        BaseMod.addCard(new FullHouse());
        BaseMod.addCard(new SacrificialScars());
        BaseMod.addCard(new Caw());
        BaseMod.addCard(new AwakenedForm());
        BaseMod.addCard(new WildInstinct());
        BaseMod.addCard(new VolatileMisfortune());
        BaseMod.addCard(new ShadowFade());
        BaseMod.addCard(new CheekyTricks());
        BaseMod.addCard(new ScreechingChant());
        BaseMod.addCard(new KnowThyFoe());

        //Elite Pool
        BaseMod.addCard(new NobsRage());
        BaseMod.addCard(new LagavulinsFerocity());
        BaseMod.addCard(new SentriesPulse());
        BaseMod.addCard(new BooksFlurry());
        BaseMod.addCard(new LeadersExecution());
        BaseMod.addCard(new TaskmastersTorture());
        BaseMod.addCard(new NemesisSlickness());
        BaseMod.addCard(new WalkersFury());
        BaseMod.addCard(new HeadsImpatience());
        BaseMod.addCard(new ReptomancersBite());

        // make sure everything is always unlocked
        UnlockTracker.unlockCard(Strike_Y.ID);
        UnlockTracker.unlockCard(Defend_Y.ID);
        UnlockTracker.unlockCard(Ceremony.ID);
        UnlockTracker.unlockCard(WarriorEssence.ID);
        UnlockTracker.unlockCard(Tweet.ID);
        UnlockTracker.unlockCard(Insight.ID);
        UnlockTracker.unlockCard(Evade.ID);
        UnlockTracker.unlockCard(Daydream.ID);
        UnlockTracker.unlockCard(Flinch.ID);
        UnlockTracker.unlockCard(Redirect.ID);
        UnlockTracker.unlockCard(Overpower.ID);
        UnlockTracker.unlockCard(Roost.ID);
        UnlockTracker.unlockCard(HuntressEssence.ID);
        UnlockTracker.unlockCard(Hexing.ID);
        UnlockTracker.unlockCard(FakeOut.ID);
        UnlockTracker.unlockCard(DarkPact.ID);
        UnlockTracker.unlockCard(MachineEssence.ID);
        UnlockTracker.unlockCard(Flinch.ID);
        UnlockTracker.unlockCard(Redirect.ID);
        UnlockTracker.unlockCard(DesperateSwing.ID);
        UnlockTracker.unlockCard(Foresight.ID);
        UnlockTracker.unlockCard(Resilience.ID);
        //UnlockTracker.unlockCard(SowTheSeeds.ID);
        UnlockTracker.unlockCard(Pecker.ID);
        UnlockTracker.unlockCard(TuckingFeathers.ID);
        UnlockTracker.unlockCard(Negation.ID);
        UnlockTracker.unlockCard(Recite.ID);
        UnlockTracker.unlockCard(Sacrifice.ID);
        UnlockTracker.unlockCard(Devastation.ID);
        UnlockTracker.unlockCard(Struggle.ID);
        UnlockTracker.unlockCard(Brainwash.ID);
        UnlockTracker.unlockCard(DarkTribute.ID);
        UnlockTracker.unlockCard(LiftOff.ID);
        UnlockTracker.unlockCard(BounceBack.ID);
        UnlockTracker.unlockCard(FullHouse.ID);
        UnlockTracker.unlockCard(SacrificialScars.ID);
        UnlockTracker.unlockCard(Caw.ID);
        UnlockTracker.unlockCard(AwakenedForm.ID);
        UnlockTracker.unlockCard(StickSmack.ID);
        UnlockTracker.unlockCard(WarriorSpirit.ID);
        UnlockTracker.unlockCard(HuntressSpirit.ID);
        UnlockTracker.unlockCard(MachineSpirit.ID);
        UnlockTracker.unlockCard(WildInstinct.ID);
        UnlockTracker.unlockCard(BloodForTheGods.ID);
        UnlockTracker.unlockCard(Tradeoff.ID);
        UnlockTracker.unlockCard(Poke.ID);
        UnlockTracker.unlockCard(Replenish.ID);
        UnlockTracker.unlockCard(BraceForImpact.ID);
        UnlockTracker.unlockCard(Brace.ID);
        UnlockTracker.unlockCard(Molting.ID);
        UnlockTracker.unlockCard(CursingBlood.ID);
        UnlockTracker.unlockCard(DigDeep.ID);
        UnlockTracker.unlockCard(DigDeeper.ID);
        //UnlockTracker.unlockCard(PrayerOfSafety.ID);
        UnlockTracker.unlockCard(BarbedWire.ID);
        UnlockTracker.unlockCard(SacrificialAttack.ID);
        UnlockTracker.unlockCard(BloodRitual.ID);
        UnlockTracker.unlockCard(RuinSticks.ID);
        UnlockTracker.unlockCard(Overexert.ID);
        UnlockTracker.unlockCard(SacrificialDive.ID);
        UnlockTracker.unlockCard(FeelMyPain.ID);
        UnlockTracker.unlockCard(Mantra.ID);
        UnlockTracker.unlockCard(Windmill.ID);
        UnlockTracker.unlockCard(MaddeningMurmurs.ID);
        //UnlockTracker.unlockCard(TurnTheTables.ID);
        UnlockTracker.unlockCard(Cower.ID);
        UnlockTracker.unlockCard(VolatileMisfortune.ID);
        UnlockTracker.unlockCard(SurvivalInstinct.ID);
        UnlockTracker.unlockCard(ShadowFade.ID);
        UnlockTracker.unlockCard(StunningBlow.ID);
        UnlockTracker.unlockCard(CheekyTricks.ID);
        UnlockTracker.unlockCard(WindBringer.ID);
        UnlockTracker.unlockCard(ScreechingChant.ID);
        UnlockTracker.unlockCard(MimicWarrior.ID);
        UnlockTracker.unlockCard(MimicHuntress.ID);
        UnlockTracker.unlockCard(MimicMachine.ID);
        UnlockTracker.unlockCard(CrazyRituals.ID);
        UnlockTracker.unlockCard(RitualOfBody.ID);
        UnlockTracker.unlockCard(KnowThyFoe.ID);
        UnlockTracker.unlockCard(VigorBurst.ID);


        UnlockTracker.unlockCard(Inspiration.ID);
        UnlockTracker.unlockCard(Respite.ID);
        UnlockTracker.unlockCard(Psalm.ID);
        UnlockTracker.unlockCard(Stick.ID);

        UnlockTracker.unlockCard(NobsRage.ID);
        UnlockTracker.unlockCard(LagavulinsFerocity.ID);
        UnlockTracker.unlockCard(SentriesPulse.ID);
        UnlockTracker.unlockCard(BooksFlurry.ID);
        UnlockTracker.unlockCard(LeadersExecution.ID);
        UnlockTracker.unlockCard(TaskmastersTorture.ID);
        UnlockTracker.unlockCard(NemesisSlickness.ID);
        UnlockTracker.unlockCard(WalkersFury.ID);
        UnlockTracker.unlockCard(HeadsImpatience.ID);
        UnlockTracker.unlockCard(ReptomancersBite.ID);

        logger.info("done editing cards");
    }

    @Override
    public void receiveEditStrings() {
        logger.info("begin editing strings");

        String language = "eng";
        if (Settings.language == Settings.GameLanguage.ZHS) language = "zhs";
        BaseMod.loadCustomStringsFile(RelicStrings.class, "localization/"+language+"/Beaked-RelicStrings.json");
        BaseMod.loadCustomStringsFile(CardStrings.class, "localization/"+language+"/Beaked-CardStrings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, "localization/"+language+"/Beaked-PotionStrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "localization/"+language+"/Beaked-PowerStrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, "localization/"+language+"/Beaked-UIStrings.json");
        BaseMod.loadCustomStringsFile(MonsterStrings.class, "localization/"+language+"/Beaked-MonsterStrings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "localization/"+language+"/Beaked-CharacterStrings.json");

        logger.info("done editing strings");
    }

    @Override
    public void receiveSetUnlocks() {

    }

    private static String loadJson(String jsonPath) {
        return Gdx.files.internal(jsonPath).readString(String.valueOf(StandardCharsets.UTF_8));
    }

    @Override
    public void receiveEditKeywords() {
        logger.info("setting up custom keywords");
        String language = "eng";

        if (Settings.language == Settings.GameLanguage.ZHS) language = "zhs";

        Type typeToken = new TypeToken<Map<String, Keyword>>(){}.getType();
        Gson gson = new Gson();
        String strings = loadJson("localization/" + language + "/Beaked-KeywordStrings.json");
        @SuppressWarnings("unchecked")
        Map<String,Keyword> keywords = (Map<String,Keyword>)gson.fromJson(strings, typeToken);
        for (Keyword kw : keywords.values()) {
            BaseMod.addKeyword(kw.NAMES, kw.DESCRIPTION);
        }
    }

    @Override
    public void receiveCardUsed(AbstractCard c) {
        if(AbstractDungeon.player.hasRelic(CultistMask.ID)) {
            CardCrawlGame.sound.playA("VO_CULTIST_1C", MathUtils.random(-0.2f, 0.2f));
            AbstractDungeon.effectList.add(new SpeechBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 2.0f,
                    cawText, true));
        }
        cardsPlayedThisCombat ++;
    }

    public static void onMasterDeckChange(){
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group){
            if (c instanceof Mantra) {
                ((Mantra) c).recalculateWither();
            }
        }
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        cardsPlayedThisCombat = 0;
    }

    @Override
    public void receivePostBattle(AbstractRoom battleRoom){
        if (battleRoom instanceof MonsterRoomBoss) {
            //Beaked.logger.debug("BOSSES KILLED: " + AbstractDungeon.bossCount);
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c instanceof AwakenedForm) {
                    ((AwakenedForm) c).updateAwakenCost();
                }
            }
        }
        if (isFlying) {
            isFlying = false;
            if (AbstractDungeon.player != null) {
                AbstractDungeon.player.drawY = initialPlayerHeight;
                if (AbstractDungeon.player.state != null) AbstractDungeon.player.state.setTimeScale(1);
            }
        }
    }

    @Override
    public void receiveStartGame(){
        //Beaked.logger.debug("BOSSES KILLED: " + AbstractDungeon.bossCount);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c instanceof AwakenedForm) {
                ((AwakenedForm) c).updateAwakenCost();
            }
        }
    }

    @Override
    public void receivePostDungeonInitialize() {
        if (AbstractDungeon.player != null && isFlying) {
            isFlying = false;
            if (AbstractDungeon.player != null) {
                AbstractDungeon.player.drawY = initialPlayerHeight;
                if (AbstractDungeon.player.state != null) AbstractDungeon.player.state.setTimeScale(1);
            }
        }
    }

    public static String getActualID(String cardID){
        return cardID.replaceFirst("beaked:","");
    }

    public static void setDescription(AbstractCard card, String desc){

        // IF you want a card to use auto-pluralization, this should be called whenever you would normally use:

        // this.rawDescription = desc;
        // this.initializeDescription(desc);

        // Make sure to call it in the card's constructor as well.

        PluralizeFieldsPatch.trueRawDescription.set(card,desc);
        createPluralizedDescription(card);
    }

    public static void createPluralizedDescription(AbstractCard card){

        // {text} displayed when !M! != 1
        // |text| displayed when !M! == 1
        // $text$ repeated as many times as !M!

        // {^text^} displayed when !I! != 1
        // |^text^| displayed when !I! == 1

        // Can be freely used with other dynamic text or keywords like [E] or !M!.

        PluralizeFieldsPatch.savedMagicNumber.set(card,card.magicNumber);
        PluralizeFieldsPatch.savedMisc.set(card,card.misc);
        card.rawDescription = PluralizeFieldsPatch.trueRawDescription.get(card);

        if (card.misc == 1){
            card.rawDescription = card.rawDescription.replaceAll("\\{\\^.+?\\^\\}", "");
        }
        else{
            card.rawDescription = card.rawDescription.replaceAll("\\|\\^.+?\\^\\|", "");
        }

        card.rawDescription = card.rawDescription.replace("{^", "");
        card.rawDescription = card.rawDescription.replace("^}", "");
        card.rawDescription = card.rawDescription.replace("|^", "");
        card.rawDescription = card.rawDescription.replace("^|", "");

        if (card.magicNumber == 1) {
            card.rawDescription = card.rawDescription.replaceAll("\\{.+?\\}", "");
        }
        else {
            card.rawDescription = card.rawDescription.replaceAll("\\|.+?\\|", "");
        }
        String tmp = "";
        for (int i=0;i<card.magicNumber;i++) tmp += "$1 ";
        card.rawDescription = card.rawDescription.replaceAll("(\\$.+?\\$)", tmp);
        card.rawDescription = card.rawDescription.replace("{", "");
        card.rawDescription = card.rawDescription.replace("}", "");
        card.rawDescription = card.rawDescription.replace("|", "");
        card.rawDescription = card.rawDescription.replace("$", "");
        card.initializeDescription();
    }

    public static boolean hasWitherCards() {
        if (AbstractDungeon.player == null) return false;
        for(AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if(card instanceof AbstractWitherCard) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasWitheredCards() {
        if (AbstractDungeon.player == null) return false;
        for(AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if(card instanceof AbstractWitherCard && ((AbstractWitherCard)card).misc != ((AbstractWitherCard)card).baseMisc) {
                return true;
            }
        }
        return false;
    }

    public static AbstractCard returnTrulyRandomEliteCard() {
        final ArrayList<AbstractCard> list = new ArrayList<>();
        for(final Map.Entry<String, AbstractCard> eliteCard : CardLibrary.cards.entrySet()) {
            final AbstractCard card = eliteCard.getValue();
            if(card.hasTag(CardTagsEnum.ELITE_CARD)) {
                list.add(card);
            }
        }
        return list.get(AbstractDungeon.cardRandomRng.random(list.size() -1));
    }

    public static AbstractCard getColorRepresentativeCard(AbstractCard.CardColor color){
        final ArrayList<AbstractCard> cards = CardLibrary.getCardList(CardLibrary.LibraryType.valueOf(color.name()));
        // look for a basic defend
        for (AbstractCard card : cards){
            if (card.hasTag(BaseModCardTags.BASIC_DEFEND)){
                return card;
            }
        }
        // no? any basic skill? (needs to be a skill to fit inside the portrait box)
        for (AbstractCard card : cards){
            if (card.rarity == AbstractCard.CardRarity.BASIC && card.type == AbstractCard.CardType.SKILL){
                return card;
            }
        }
        // ANY skill at all?
        for (AbstractCard card : cards){
            if (card.type == AbstractCard.CardType.SKILL){
                return card;
            }
        }
        // I give up, just take the first card
        return cards.get(0);
    }
}
