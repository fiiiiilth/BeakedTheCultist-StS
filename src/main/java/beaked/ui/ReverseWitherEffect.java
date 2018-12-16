package beaked.ui;

import beaked.cards.AbstractWitherCard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;


public class ReverseWitherEffect extends AbstractGameEffect {

    // modified from base game's CampfireSmithEffect if you want to see how something works.

    private static final float DUR = 1.5f;
    private boolean openedScreen = false;
    private Color screenColor = AbstractDungeon.fadeColor.cpy();
    public boolean isFree = false;
    public boolean choseCard = false;
    public ReverseWitherOption button;

    public ReverseWitherEffect(ReverseWitherOption button, boolean isFree) {
        this.duration = DUR;
        this.screenColor.a = 0.0f;
        this.isFree = isFree;
        this.button = button;
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }

    @Override
    public void update() {
        if(!AbstractDungeon.isScreenUp) {
            this.duration -= Gdx.graphics.getDeltaTime();
            this.updateBlackScreenColor();
        }
        if(!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractWitherCard card = (AbstractWitherCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            card.misc = card.baseMisc;
            card.applyPowers();
            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
            AbstractDungeon.topLevelEffects.add(new CardGlowBorder(card));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            choseCard = true;
            ((RestRoom)AbstractDungeon.getCurrRoom()).fadeIn();
        }
        if(this.duration < 1.0f && !this.openedScreen) {
            this.openedScreen = true;
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for(AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                if(card instanceof AbstractWitherCard && ((AbstractWitherCard)card).misc != ((AbstractWitherCard)card).baseMisc) {
                    group.group.add(card);
                }
            }
            AbstractDungeon.gridSelectScreen.open(group, 1, ReverseWitherOption.LABEL, false, false, true, true);
        }
        if(this.duration < 0.0f) {
            this.isDone = true;
            if (this.isFree && choseCard) {
                // reopen options menu
                if (AbstractDungeon.getCurrRoom() instanceof RestRoom)
                    button.setImageAndDescription();
                    ((RestRoom) AbstractDungeon.getCurrRoom()).campfireUI.reopen();
            }else{
                // complete room and bring up continue button
                if(CampfireUI.hidden) {
                    AbstractRoom.waitTimer = 0.0f;
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                    ((RestRoom) AbstractDungeon.getCurrRoom()).cutFireSound();
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0f, 0.0f, Settings.WIDTH, Settings.HEIGHT);
        if(AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID) {
            AbstractDungeon.gridSelectScreen.render(sb);
        }
    }

    private void updateBlackScreenColor() {
        if(this.duration > 1.0f) {
            this.screenColor.a = Interpolation.fade.apply(1.0f, 0.0f, (this.duration - 1.0f) * 2.0f);
        } else {
            this.screenColor.a = Interpolation.fade.apply(0.0f, 1.0f, this.duration / 1.5f);
        }
    }

    @Override
    public void dispose() {
    }
}
