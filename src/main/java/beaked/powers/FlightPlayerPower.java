package beaked.powers;

import basemod.BaseMod;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostDungeonInitializeSubscriber;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class FlightPlayerPower extends AbstractPower implements PostBattleSubscriber, PostDungeonInitializeSubscriber {
	public static final String POWER_ID = "beaked:FlightPlayerPower";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	public static float FLY_HEIGHT = 0.15f;

	public int maxAmount = 0;

	public FlightPlayerPower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.maxAmount = amount;
		updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.loadRegion("flight");
	}

	@Override
	public void onInitialApplication(){
		AbstractDungeon.player.state.setTimeScale(5);
		AbstractDungeon.player.drawY += Settings.HEIGHT*FLY_HEIGHT * Settings.scale;
		BaseMod.subscribe(this);
	}

	@Override
	public void stackPower(final int stackAmount) {
		super.stackPower(stackAmount);
		this.maxAmount += stackAmount;
	}

	@Override
	public void atStartOfTurn() {
		super.atStartOfTurn();
		this.amount = this.maxAmount;
		updateDescription();
	}
	
	@Override
	public void updateDescription() {
		this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
	}

	@Override
	public float atDamageReceive(float damage, DamageInfo.DamageType type) {
		if (type == DamageInfo.DamageType.NORMAL) {
			return damage * 0.5f;
		} else {
			return damage;
		}
	}

	@Override
	public int onAttacked(final DamageInfo info, final int damageAmount){
		if (info.type == DamageInfo.DamageType.NORMAL) {
			AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.POWER_ID, 1));
		}
		return damageAmount;
	}

	@Override
	public void onRemove() {
		AbstractDungeon.player.drawY -= Settings.HEIGHT*FLY_HEIGHT * Settings.scale;
		AbstractDungeon.player.state.setTimeScale(1);
		AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.owner, new StunnedPower(this.owner,1), 1));
		BaseMod.unsubscribe(this);
	}

	@Override
	public void receivePostBattle(AbstractRoom battleRoom) {
		AbstractDungeon.player.drawY -= Settings.HEIGHT*FLY_HEIGHT * Settings.scale;
		AbstractDungeon.player.state.setTimeScale(1);
		BaseMod.unsubscribeLater(this);
	}

	@Override
	public void receivePostDungeonInitialize() {
		AbstractDungeon.player.drawY -= Settings.HEIGHT*FLY_HEIGHT * Settings.scale;
		AbstractDungeon.player.state.setTimeScale(1);
		BaseMod.unsubscribeLater(this);
	}
}