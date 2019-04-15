package beaked.ui;

import beaked.Beaked;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

public class ReverseWitherOption extends AbstractCampfireOption {

    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("beaked:ReverseWitherCampfireButton");
    public static final String LABEL = uiStrings.TEXT[0];
    public static final String FREE_DESCRIPTION = uiStrings.TEXT[1];
    public static final String DESCRIPTION = uiStrings.TEXT[2];
    public static final String UNUSABLE_DESCRIPTION = uiStrings.TEXT[3];
    public boolean isFree = true;

    public ReverseWitherOption() {
        this.label = LABEL;
        setImageAndDescription();
    }

    public void setImageAndDescription() {
        this.usable = Beaked.hasWitheredCards();
        if (this.usable) {
            if (this.isFree) {
                this.description = FREE_DESCRIPTION;
                this.img = new Texture("beaked_img/ui/campfire/reversewither_free.png");
            } else {
                this.description = DESCRIPTION;
                this.img = new Texture("beaked_img/ui/campfire/reversewither.png");
            }
        } else {
            this.description = UNUSABLE_DESCRIPTION;
            this.img = new Texture("beaked_img/ui/campfire/reversewither_disabled.png");
        }
    }

    @Override
    public void useOption() {
        if(this.usable) {
            AbstractDungeon.effectList.add(new ReverseWitherEffect(this,this.isFree));
        }
    }
}
