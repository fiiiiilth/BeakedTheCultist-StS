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

    private static final float DUR = 1.5f;
    private boolean openedScreen = false;
    private boolean selectedCard = false;
    private Color screenColor = AbstractDungeon.fadeColor.cpy();

    public ReverseWitherEffect() {
        this.duration = DUR;
        this.screenColor.a = 0.0f;
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }

    @Override
    public void update() {
        if(!AbstractDungeon.isScreenUp) {
            this.duration -= Gdx.graphics.getDeltaTime();
            this.updateBlackScreenColor();
        }
        if((!this.selectedCard) && (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())) {
            AbstractWitherCard card = (AbstractWitherCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            card.misc = card.baseMisc;
            card.applyPowers();
            AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(card, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
            AbstractDungeon.topLevelEffects.add(new CardGlowBorder(card));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        if(this.duration < 1.0f && !this.openedScreen) {
            this.openedScreen = true;
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for(AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                if(card instanceof AbstractWitherCard && ((AbstractWitherCard)card).misc != ((AbstractWitherCard)card).baseMisc) {
                    group.group.add(card);
                }
            }
            AbstractDungeon.gridSelectScreen.open(group, 1, ReverseWitherOption.LABEL, false);
        }
        if(this.duration < 0.0f) {
            this.isDone = true;
            if(CampfireUI.hidden) {
                AbstractRoom.waitTimer = 0.0f;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                ((RestRoom)AbstractDungeon.getCurrRoom()).cutFireSound();
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
}
