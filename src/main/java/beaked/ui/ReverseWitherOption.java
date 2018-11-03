package beaked.ui;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

public class ReverseWitherOption extends AbstractCampfireOption {

    public static final String LABEL = "Reverse Withering";
    public static final String DESCRIPTION = "Restore a cards Wither back to its default.";

    public ReverseWitherOption(boolean active) {
        this.label = LABEL;
        this.usable = active;
        if(active) {
            this.description = DESCRIPTION;
            this.img = new Texture("beaked_img/ui/campfire/reversewither.png");
        } else {
            this.description = DESCRIPTION;
            this.img = new Texture("beaked_img/ui/campfire/reversewither_disabled.png");
        }
    }

    @Override
    public void useOption() {
        if(this.usable) {
            AbstractDungeon.effectList.add(new ReverseWitherEffect());
        }
    }
}
