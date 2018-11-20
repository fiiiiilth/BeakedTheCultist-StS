package beaked.relics;

import basemod.abstracts.CustomRelic;
import beaked.actions.ForcedEndTurnAction;
import beaked.powers.RitualPlayerPower;
import beaked.tools.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;

import java.util.Iterator;

public class RitualPlumage extends CustomRelic {
    public static final String ID = "beaked:RitualPlumage";
    private boolean used = false;
    private AbstractPlayer p = AbstractDungeon.player;
    private GameActionManager am = AbstractDungeon.actionManager;
    private static int ritualAmount = 2;

    public RitualPlumage() {
        super(ID, TextureLoader.getTexture("beaked_images/relics/RitualPlumage.png"),
                TextureLoader.getTexture("beaked_images/relics/outline/RitualPlumage.png"),
                RelicTier.SHOP, LandingSound.MAGICAL);
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
        if (!this.used) this.flash();
    }

    @Override
    public void onDrawOrDiscard(){
        if (!this.used) this.pulse = AbstractDungeon.player.cardsPlayedThisTurn == 0;
    }

    @Override
    public void onPlayerEndTurn() {
        if (AbstractDungeon.player.cardsPlayedThisTurn == 0 && !this.used) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new RitualPlayerPower(AbstractDungeon.player, ritualAmount), ritualAmount));
            this.used = true;
        }
    }

    @Override
    public void atBattleStart() {
        this.used= false;
    }
}