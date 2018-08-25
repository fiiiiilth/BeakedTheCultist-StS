package beaked;

import java.nio.charset.StandardCharsets;

import basemod.interfaces.*;
import beaked.cards.*;
import beaked.characters.BeakedTheCultist;
import beaked.monsters.SuperParasite;
import beaked.patches.AbstractCardEnum;
import beaked.patches.BeakedEnum;
import beaked.patches.PluralizeFieldsPatch;
import beaked.relics.FlawlessSticks;
import beaked.relics.MendingPlumage;
import beaked.variables.MiscVariable;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.relics.CultistMask;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
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
        EditStringsSubscriber, SetUnlocksSubscriber, EditKeywordsSubscriber, OnCardUseSubscriber, PostBattleSubscriber,
        StartGameSubscriber{
    public static final Logger logger = LogManager.getLogger(Beaked.class.getName());

    private static final String MODNAME = "BeakedTheCultist the Cultist";
    private static final String AUTHOR = "fiiiiilth, Moocowsgomoo";
    private static final String DESCRIPTION = "v0.1\n Adds Beaked the Cultist as a new playable characters.";

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
    private static final String BEAKED_PORTRAIT = "charSelect/beakedPortrait.jpg";
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
        BaseMod.addColor(AbstractCardEnum.BEAKED_YELLOW,
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
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        Settings.isDailyRun = false;
        Settings.isTrial = false;
        Settings.isDemo = false;

        BaseMod.addMonster(SuperParasite.ID, ()->new SuperParasite());
        BaseMod.addBoss(TheCity.ID, SuperParasite.ID,"img/ui/map/boss/parasite.png","img/ui/map/bossOutline/parasite.png");
    }

    @Override
    public void receiveEditCharacters() {
        logger.info("begin editing characters");

        logger.info("add " + BeakedEnum.BEAKED_THE_CULTIST.toString());
        BaseMod.addCharacter(BeakedTheCultist.class, "The Beaked", "BeakedTheCultist class string",
                AbstractCardEnum.BEAKED_YELLOW, "Beaked the Cultist",
                makePath(BEAKED_BUTTON), makePath(BEAKED_PORTRAIT),
                BeakedEnum.BEAKED_THE_CULTIST);

        logger.info("done editing characters");
    }


    @Override
    public void receiveEditRelics() {
        logger.info("begin editing relics");

        // Add relics
        BaseMod.addRelicToCustomPool(new MendingPlumage(), AbstractCardEnum.BEAKED_YELLOW);
        BaseMod.addRelicToCustomPool(new FlawlessSticks(), AbstractCardEnum.BEAKED_YELLOW);

        logger.info("done editing relics");
    }

    @Override
    public void receiveEditCards() {
        logger.info("begin editing cards");

        logger.info("add cards for " + BeakedEnum.BEAKED_THE_CULTIST.toString());

        BaseMod.addDynamicVariable(new MiscVariable());

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


        UnlockTracker.unlockCard(Inspiration.ID);
        UnlockTracker.unlockCard(Respite.ID);
        UnlockTracker.unlockCard(Psalm.ID);
        UnlockTracker.unlockCard(Stick.ID);

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

        logger.info("done editing strings");
    }

    @Override
    public void receiveSetUnlocks() {

    }


    @Override
    public void receiveEditKeywords() {
        logger.info("setting up custom keywords");
        BaseMod.addKeyword(new String[] {"ritual", "Ritual"}, "Gain #yStrength at the beginning of your turn.");
        BaseMod.addKeyword(new String[] {"wither"}, "Permanently decrease this card's power by the wither amount. When it reaches #b0, the card becomes #yDepleted.");
        BaseMod.addKeyword(new String[] {"depleted"}, "#yUnplayable unless the #yWither effect is reversed.");
        BaseMod.addKeyword(new String[] {"inspiration"}, "An unplayable status card. When drawn, it #yExhausts and draws #b2 more cards.");
        BaseMod.addKeyword(new String[] {"respite"}, "An unplayable status card. Heals #b2 HP at the end of your turn.");
        BaseMod.addKeyword(new String[] {"psalm"}, "A 0-cost card that deals #b3 damage to ALL enemies and has #yWither #b2.");
        BaseMod.addKeyword(new String[] {"stick"}, "A 0-cost card that increases Stick Smack damage and returns a Stick Smack to your hand.");
    }

    @Override
    public void receiveCardUsed(AbstractCard c) {
        if(AbstractDungeon.player.hasRelic(CultistMask.ID)) {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_CULTIST_1A"));
            AbstractDungeon.actionManager.addToBottom(new TalkAction(true, "CAW", 1.0f, 2.0f));
        }
    }

    @Override
    public void receivePostBattle(AbstractRoom battleRoom){
        if (battleRoom instanceof MonsterRoomBoss) {
            //BaseMod.logger.debug("BOSSES KILLED: " + AbstractDungeon.bossCount);
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c instanceof AwakenedForm) {
                    ((AwakenedForm) c).updateAwakenCost();
                }
            }
        }
    }

    @Override
    public void receiveStartGame(){
       // BaseMod.logger.debug("BOSSES KILLED: " + AbstractDungeon.bossCount);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c instanceof AwakenedForm) {
                ((AwakenedForm) c).updateAwakenCost();
            }
        }
    }

    public static String getActualID(String cardID){
        return cardID.replaceFirst("beaked:","");
    }

    public static void setDescription(AbstractCard card, String desc){

        // This should be called whenever you would normally use:

        // this.rawDescription = desc;
        // this.initializeDescription(desc);

        // in a card, IF you want that card to use auto-pluralization. Make sure to call it in the card's constructor as well.

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
}
