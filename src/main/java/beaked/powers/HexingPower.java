package beaked.powers;

import beaked.cards.Inspiration;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class HexingPower extends AbstractPower {

    public static final String POWER_ID = "HexingPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public HexingPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;
        updateDescription();
        this.img = new Texture("img/powers/ritual.png");
    }

    @Override
    public void atStartOfTurnPostDraw() {
        boolean foundMonster = false;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters){
            if (!m.isDeadOrEscaped() &&
                    m.intent != AbstractMonster.Intent.ATTACK &&
                    m.intent != AbstractMonster.Intent.ATTACK_BUFF &&
                    m.intent != AbstractMonster.Intent.ATTACK_DEBUFF &&
                    m.intent != AbstractMonster.Intent.ATTACK_DEFEND) {

                foundMonster = true;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new WeakPower(m, this.amount * 2, false), this.amount * 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new VulnerablePower(m, this.amount, false), this.amount));
            }
        }
        if (foundMonster) this.flash();
    }

    @Override
    public void updateDescription() {
        this.description = HexingPower.DESCRIPTIONS[0] + this.amount*2 + HexingPower.DESCRIPTIONS[1] + this.amount + HexingPower.DESCRIPTIONS[2];
    }

}
