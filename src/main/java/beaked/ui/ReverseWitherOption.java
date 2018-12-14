package beaked.ui;

import beaked.Beaked;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

public class ReverseWitherOption extends AbstractCampfireOption {

    public static final String LABEL = "Reverse Withering";
    public static final String FREE_DESCRIPTION = "(Free action) Fully replenish a Wither card.";
    public static final String DESCRIPTION = "Fully replenish another Wither card.";
    public static final String UNUSABLE_DESCRIPTION = "You have no Wither cards to replenish.";
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
            this.isFree = false;
        }
    }
}
