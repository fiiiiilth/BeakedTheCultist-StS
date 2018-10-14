package beaked.actions;

import beaked.cards.CrazyRituals;
import beaked.powers.CrazyRitualsPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public class CrazyRitualsPlayAction extends AbstractGameAction {

    public CrazyRitualsPower power;

    public CrazyRitualsPlayAction(CrazyRitualsPower power) {
        this.power = power;
    }

    public void update() {

        power.playCardEffect(1);

        this.isDone = true;
    }
}
