package beaked.relics;

import basemod.abstracts.CustomRelic;
import beaked.Beaked;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class MendingPlumage extends CustomRelic {
    public static final String ID = "beaked:MendingPlumage";
    private static final int HEAL_PER_CARD = 1;

    public MendingPlumage() {
        super(ID, new Texture("img/relics/" + Beaked.getActualID(ID) + ".png"),
                RelicTier.STARTER, LandingSound.MAGICAL);
    }

	@Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        AbstractDungeon.player.heal(HEAL_PER_CARD);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MendingPlumage();
    }
}
