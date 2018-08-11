package beaked.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

public class StunnedPower extends AbstractPower {
	public static final String POWER_ID = "beaked:StunnedPower";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	public int storedHandSize;

	public StunnedPower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		updateDescription();
		this.type = PowerType.DEBUFF;
		this.isTurnBased = true;
		this.loadRegion("entangle");
	}

	@Override
	public void atEndOfRound() {
		if (this.amount == 0) {
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.POWER_ID));
		} else {
			AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.POWER_ID, 1));
		}
	}

	@Override
	public void atStartOfTurn() {
		super.atStartOfTurn();
		this.storedHandSize = AbstractDungeon.player.gameHandSize;
		AbstractDungeon.player.gameHandSize = 0;
		AbstractDungeon.actionManager.cardQueue.clear();
		for (final AbstractCard c : AbstractDungeon.player.limbo.group) {
			AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
		}
		AbstractDungeon.player.limbo.group.clear();
		AbstractDungeon.player.releaseCard();
		AbstractDungeon.overlayMenu.endTurnButton.disable(true);
	}

	@Override
	public void atStartOfTurnPostDraw() {
		super.atStartOfTurnPostDraw();
		AbstractDungeon.player.gameHandSize = this.storedHandSize;
	}
	
	@Override
	public void updateDescription() {
		this.description = DESCRIPTIONS[0];
	}
}