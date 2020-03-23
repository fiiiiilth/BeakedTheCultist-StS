package beaked.relics;

import basemod.abstracts.CustomRelic;
import beaked.tools.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RitualPlumage extends CustomRelic {
    public static final String ID = "beaked:RitualPlumage";
    private boolean used = false;
    private AbstractPlayer p = AbstractDungeon.player;
    private GameActionManager am = AbstractDungeon.actionManager;
    private static int ritualAmount = 2;

    public static String endTurnString;
    public static Texture specialEndTurnButton;

    public RitualPlumage() {
        super(ID, TextureLoader.getTexture("beaked_img/relics/RitualPlumage.png"),
                TextureLoader.getTexture("beaked_img/relics/outline/RitualPlumage.png"),
                RelicTier.SHOP, LandingSound.MAGICAL);

            endTurnString = DESCRIPTIONS[2];
            specialEndTurnButton = TextureLoader.getTexture(("beaked_img/ui/EndTurnButtonGlowYellow.png"));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + ritualAmount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RitualPlumage();
    }

    @Override
    public void atTurnStart(){
        if (!this.used){
            this.flash();
            this.beginLongPulse();
            AbstractDungeon.overlayMenu.endTurnButton.isGlowing = true;
        }
    }

    @Override
    public void onPlayCard(final AbstractCard c, final AbstractMonster m){
        AbstractDungeon.overlayMenu.endTurnButton.isGlowing = false;
        this.stopPulse();
    }

    public boolean isRelicActive(){
        return AbstractDungeon.player.cardsPlayedThisTurn == 0 && !this.used;
    }

    @Override
    public void onPlayerEndTurn() {
        this.stopPulse();
        if (isRelicActive()) {
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new RitualPower(AbstractDungeon.player, ritualAmount,true), ritualAmount));
            this.used = true;
        }
    }

    @Override
    public void atBattleStart() {
        this.used= false;
    }
}