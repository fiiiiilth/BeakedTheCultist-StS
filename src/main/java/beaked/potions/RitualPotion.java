package beaked.potions;

import basemod.abstracts.CustomPotion;
import beaked.powers.RitualPlayerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class RitualPotion extends CustomPotion {
    public static final String POTION_ID = "beaked:RitualPotion";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String DESCRIPTIONS[] = potionStrings.DESCRIPTIONS;

    public RitualPotion() {
    super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.SPHERE, PotionColor.POWER);

        this.potency = this.getPotency();
        this.description = RitualPotion.DESCRIPTIONS[0] + this.potency + RitualPotion.DESCRIPTIONS[1];
        this.isThrown = false;
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(RitualPlayerPower.NAME, RitualPlayerPower.DESCRIPTIONS[0] + this.getPotency() +
                RitualPlayerPower.DESCRIPTIONS[1]));
    }

    @Override
    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;
        if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, target, new RitualPlayerPower(target, this.potency),this.potency));
        }
    }

    @Override
    public AbstractPotion makeCopy() {
        return new RitualPotion();
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        return 1;
    }
}
