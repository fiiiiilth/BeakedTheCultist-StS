package beaked.relics;

import basemod.abstracts.CustomRelic;
import beaked.tools.TextureLoader;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RitualPlumage extends CustomRelic {
    public static final String ID = "beaked:RitualPlumage";
    private boolean used = false;
    private AbstractPlayer p = AbstractDungeon.player;
    private GameActionManager am = AbstractDungeon.actionManager;
    private static int ritualAmount = 2;

    public RitualPlumage() {
        super(ID, TextureLoader.getTexture("beaked_img/relics/RitualPlumage.png"),
                TextureLoader.getTexture("beaked_img/relics/outline/RitualPlumage.png"),
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
        this.pulse = false;
        if (AbstractDungeon.player.cardsPlayedThisTurn == 0 && !this.used) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new RitualPower(AbstractDungeon.player, ritualAmount,true), ritualAmount));
            this.used = true;
        }
    }

    @Override
    public void atBattleStart() {
        this.used= false;
    }
}