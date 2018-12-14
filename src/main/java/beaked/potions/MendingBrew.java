package beaked.potions;

import beaked.powers.MendingPower;
import beaked.powers.RitualPlayerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.MagnetismPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class MendingBrew extends AbstractPotion {
    public static final String POTION_ID = "beaked:MendingBrew";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String DESCRIPTIONS[] = potionStrings.DESCRIPTIONS;

    public MendingBrew() {
    super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.HEART, PotionColor.GREEN);

        this.potency = this.getPotency();
        this.description = String.format(MendingBrew.DESCRIPTIONS[0], this.potency,1);
        this.isThrown = false;
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;
        if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, target, new MendingPower(target, this.potency),this.potency));
        }
    }

    @Override
    public AbstractPotion makeCopy() {
        return new MendingBrew();
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        return 1;
    }
}
