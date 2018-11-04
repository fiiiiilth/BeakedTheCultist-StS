package beaked;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import basemod.ModLabeledToggleButton;
import basemod.ReflectionHacks;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import beaked.cards.*;
import beaked.characters.BeakedTheCultist;
import beaked.monsters.SuperParasite;
import beaked.patches.AbstractCardEnum;
import beaked.patches.BeakedEnum;
import beaked.patches.CardTagsEnum;
import beaked.patches.PluralizeFieldsPatch;
import beaked.potions.RitualPotion;
import beaked.relics.*;
import beaked.variables.*;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
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
import com.megacrit.cardcrawl.helpers.CardHelper;

import basemod.BaseMod;
import basemod.ModPanel;

@SpireInitializer
public class Beaked implements PostInitializeSubscriber,
        EditCardsSubscriber, EditRelicsSubscriber, EditCharactersSubscriber,
        EditStringsSubscriber, SetUnlocksSubscriber, EditKeywordsSubscriber, OnCardUseSubscriber, PostBattleSubscriber,
        StartGameSubscriber, OnStartBattleSubscriber, PostDungeonInitializeSubscriber{
    public static final Logger logger = LogManager.getLogger(Beaked.class.getName());

    private static final String MODNAME = "BeakedTheCultist the Cultist";
    private static final String AUTHOR = "fiiiiilth, Moocowsgomoo";
    private static final String DESCRIPTION = "v0.1\n Adds Beaked the Cultist as a new playable characters.";

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
    private static final String BEAKED_PORTRAIT = "charSelect/beakedPortrait.jpg";
    private static final String BEAKED_PORTRAIT_Y = "charSelect/beakedPortrait_y.jpg";
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
    public static final String PROP_BLUE_COSTUME = "blueCostume";
    public static boolean crazyRituals = false;
    public static boolean enableParasite = true;
    public static boolean customModeCeremony = true;
    public static boolean relicSharing = true;
    public static boolean blueCostume = false;

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
        beakedDefaults.setProperty(PROP_BLUE_COSTUME, "FALSE");

        try {
            SpireConfig config = new SpireConfig("TheBeaked", "BeakedConfig",beakedDefaults);
            config.load();
            crazyRituals = config.getBool(PROP_CRAZY_RITUALS);
            enableParasite = config.getBool(PROP_ENABLE_PARASITE);
            customModeCeremony = config.getBool(PROP_CUSTOM_CEREMONY);
            relicSharing = config.getBool(PROP_RELIC_SHARING);
            blueCostume = config.getBool(PROP_BLUE_COSTUME);
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

        Beaked.logger.debug("==========================================POST INITIALIZE");

        ModLabeledToggleButton crazyBtn = new ModLabeledToggleButton("Replace 2 of your starting Ceremony cards with Crazy Rituals.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                crazyRituals, settingsPanel, (label) -> {
        }, (button) -> {
            crazyRituals = button.enabled;
            try {
                SpireConfig config = new SpireConfig("TheBeaked", "BeakedConfig",beakedDefaults);
                config.setBool(PROP_CRAZY_RITUALS, crazyRituals);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
            resetCharSelect();
        });
        settingsPanel.addUIElement(crazyBtn);

        ModLabeledToggleButton customModeCeremonyBtn = new ModLabeledToggleButton("Start with some Ceremony cards in deck-modifying Custom modes.",
                350.0f, 650.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                customModeCeremony, settingsPanel, (label) -> {
        }, (button) -> {
            customModeCeremony = button.enabled;
            try {
                SpireConfig config = new SpireConfig("TheBeaked", "BeakedConfig",beakedDefaults);
                config.setBool(PROP_CUSTOM_CEREMONY, customModeCeremony);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        settingsPanel.addUIElement(customModeCeremonyBtn);

        ModLabeledToggleButton bossesBtn = new ModLabeledToggleButton("Enable Content: Giant Parasite Act 3 Elite (REQUIRES RESTART)",
                350.0f, 550.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                enableParasite, settingsPanel, (label) -> {
        }, (button) -> {
            enableParasite = button.enabled;
            try {
                SpireConfig config = new SpireConfig("TheBeaked", "BeakedConfig",beakedDefaults);
                config.setBool(PROP_ENABLE_PARASITE, enableParasite);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        settingsPanel.addUIElement(bossesBtn);

        ModLabeledToggleButton relicSharingBtn = new ModLabeledToggleButton("Enable Beaked relics for other characters (REQUIRES RESTART)",
                350.0f, 500.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                relicSharing, settingsPanel, (label) -> {
        }, (button) -> {
           relicSharing = button.enabled;
            try {
                SpireConfig config = new SpireConfig("TheBeaked", "BeakedConfig",beakedDefaults);
                config.setBool(PROP_RELIC_SHARING, relicSharing);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        settingsPanel.addUIElement(relicSharingBtn);

        ModLabeledToggleButton blueCostumeBtn = new ModLabeledToggleButton("Costume change: Blue",
                350.0f, 400.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                blueCostume, settingsPanel, (label) -> {
        }, (button) -> {
            blueCostume = button.enabled;
            try {
                SpireConfig config = new SpireConfig("TheBeaked", "BeakedConfig",beakedDefaults);
                config.setBool(PROP_BLUE_COSTUME, blueCostume);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
            BaseMod.playerPortraitMap.remove(BeakedEnum.BEAKED_THE_CULTIST);
            BaseMod.playerPortraitMap.put(BeakedEnum.BEAKED_THE_CULTIST, makePath(blueCostume?BEAKED_PORTRAIT:BEAKED_PORTRAIT_Y));
            beakedCharacter.reloadAnimation();
            resetCharSelect();
        });
        settingsPanel.addUIElement(blueCostumeBtn);

        Settings.isDailyRun = false;
        Settings.isTrial = false;
        Settings.isDemo = false;

        if (enableParasite){
            BaseMod.addMonster(SuperParasite.ID, "Giant Parasite", () -> new SuperParasite());
            BaseMod.addEliteEncounter(TheBeyond.ID, new MonsterInfo(SuperParasite.ID,1.0f));
        }
        BaseMod.addPotion(RitualPotion.class, YELLOW, YELLOW, YELLOW, RitualPotion.POTION_ID, BeakedEnum.BEAKED_THE_CULTIST);
    }

    @SuppressWarnings("unchecked")
    private void resetCharSelect() {
        ((ArrayList<CharacterOption>) ReflectionHacks.getPrivate(CardCrawlGame.mainMenuScreen.charSelectScreen, CharacterSelectScreen.class, "options")).clear();
        CardCrawlGame.mainMenuScreen.charSelectScreen.initialize();
    }

    @Override
    public void receiveEditCharacters() {
        logger.info("begin editing characters");

        logger.info("add " + BeakedEnum.BEAKED_THE_CULTIST.toString());
        beakedCharacter = new BeakedTheCultist("Beaked The Cultist", BeakedEnum.BEAKED_THE_CULTIST);
        BaseMod.addCharacter(beakedCharacter, makePath(BEAKED_BUTTON),
                makePath(blueCostume?BEAKED_PORTRAIT:BEAKED_PORTRAIT_Y),
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
        if (relicSharing){
            BaseMod.addRelic(new ShinyBauble(), RelicType.SHARED);
            BaseMod.addRelic(new MawFillet(), RelicType.SHARED);
            BaseMod.addRelic(new SacredNecklace(), RelicType.SHARED);
        }
        else {
            BaseMod.addRelicToCustomPool(new ShinyBauble(), AbstractCardEnum.BEAKED_YELLOW);
            BaseMod.addRelicToCustomPool(new MawFillet(), AbstractCardEnum.BEAKED_YELLOW);
            BaseMod.addRelicToCustomPool(new SacredNecklace(), AbstractCardEnum.BEAKED_YELLOW);
        }

        logger.info("done editing relics");
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
        BaseMod.addCard(new SowTheSeeds());
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
        BaseMod.addCard(new PrayerOfSafety());
        BaseMod.addCard(new BarbedWire());
        BaseMod.addCard(new SacrificialAttack());
        BaseMod.addCard(new DigDeeper());
        BaseMod.addCard(new BloodRitual());
        BaseMod.addCard(new RuinSticks());
        BaseMod.addCard(new Overexert());
        BaseMod.addCard(new SacrificialDive());
        BaseMod.addCard(new Mantra());
        BaseMod.addCard(new MaddeningMurmurs());
        BaseMod.addCard(new TurnTheTables());
        BaseMod.addCard(new StunningBlow());
        BaseMod.addCard(new WindBringer());
        BaseMod.addCard(new MimicWarrior());
        BaseMod.addCard(new MimicHuntress());
        BaseMod.addCard(new MimicMachine());
        BaseMod.addCard(new CrazyRituals());
        BaseMod.addCard(new RitualOfBody());


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
        UnlockTracker.unlockCard(SowTheSeeds.ID);
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
        UnlockTracker.unlockCard(PrayerOfSafety.ID);
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
        UnlockTracker.unlockCard(TurnTheTables.ID);
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

        logger.info("done editing cards");
    }

    @Override
    public void receiveEditStrings() {
        logger.info("begin editing strings");

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
        //MonsterStrings
        String monsterStrings = Gdx.files.internal("localization/Beaked-MonsterStrings.json").readString(
                String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);
        //PotionStrings
        String potionStrings = Gdx.files.internal("localization/Beaked-PotionStrings.json").readString(
                String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);

        logger.info("done editing strings");
    }

    @Override
    public void receiveSetUnlocks() {

    }


    @Override
    public void receiveEditKeywords() {
        logger.info("setting up custom keywords");
        BaseMod.addKeyword(new String[] {"ritual"}, "Gain #yStrength at the beginning of your turn.");
        BaseMod.addKeyword(new String[] {"wither"}, "Permanently decrease this card's power by the wither amount. When it reaches #b0, the card becomes #yDepleted.");
        BaseMod.addKeyword(new String[] {"depleted"}, "#yUnplayable unless the #yWither effect is reversed.");
        BaseMod.addKeyword(new String[] {"inspiration"}, "An unplayable status card. When drawn, it #yExhausts and draws #b2 more cards.");
        BaseMod.addKeyword(new String[] {"respite"}, "An unplayable status card. Heals #b2 HP at the end of your turn.");
        BaseMod.addKeyword(new String[] {"psalm"}, "A 0-cost attack card that deals #b3 damage to ALL enemies and has #yWither #b2.");
        BaseMod.addKeyword(new String[] {"stick"}, "A 0-cost skill card that increases Stick Smack damage and returns a Stick Smack to your hand.");
        BaseMod.addKeyword(new String[] {"entangled"}, "You cannot play #yAttack cards for one turn.");
        BaseMod.addKeyword(new String[] {"regret"}, "An unplayable curse card. While in your hand, lose #b1 HP for each card in your hand at the end of the turn.");
        BaseMod.addKeyword(new String[] {"bleed"}, "Deals damage at the end of the round. Does not decrease over time.");
    }

    @Override
    public void receiveCardUsed(AbstractCard c) {
        if(AbstractDungeon.player.hasRelic(CultistMask.ID)) {
            CardCrawlGame.sound.playA("VO_CULTIST_1C", MathUtils.random(-0.2f, 0.2f));
            AbstractDungeon.effectList.add(new SpeechBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 2.0f,
                    "CAW", true));
        }
        cardsPlayedThisCombat ++;
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
            if (isFlying) {
                isFlying = false;
                AbstractDungeon.player.drawY = initialPlayerHeight;
                AbstractDungeon.player.state.setTimeScale(1);
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
            AbstractDungeon.player.drawY = initialPlayerHeight;
            AbstractDungeon.player.state.setTimeScale(1);
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

    public static boolean hasWitheredCards() {
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
}
