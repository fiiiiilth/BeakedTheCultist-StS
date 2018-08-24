package beaked.relics;

import basemod.abstracts.CustomRelic;
import beaked.cards.AbstractWitherCard;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class FlawlessSticks extends CustomRelic {
    public static final String ID = "beaked:FlawlessSticks";

    public FlawlessSticks() {
        super(ID, new Texture("img/relics/FlawlessSticks.png"),
                RelicTier.BOSS, LandingSound.SOLID);
    }

	@Override
    public void atBattleStartPreDraw() {
        boolean foundCard = false;
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof AbstractWitherCard && ((AbstractWitherCard) c).isDepleted) {
                foundCard = true;
                AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(c,AbstractDungeon.player.drawPile,true));
            }
        }
        if (foundCard){
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player,this));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FlawlessSticks();
    }
}
