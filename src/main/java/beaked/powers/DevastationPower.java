package beaked.powers;

import basemod.BaseMod;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostDrawSubscriber;
import basemod.interfaces.PostDungeonInitializeSubscriber;
import beaked.cards.AbstractWitherCard;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class DevastationPower extends AbstractPower implements PostDrawSubscriber, PostBattleSubscriber,
		PostDungeonInitializeSubscriber {
	public static final String POWER_ID = "beaked:DevastationPower";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public DevastationPower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.loadRegion("corruption");
	}

	@Override
	public void onInitialApplication() {
		BaseMod.subscribe(this);
	}
	
	@Override
	public void updateDescription() {
		this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
	}

	@Override
	public void receivePostDraw (AbstractCard c) {
		if (c instanceof AbstractWitherCard && ((AbstractWitherCard) c).isDepleted){
			this.flash();
			AbstractDungeon.actionManager.addToTop(new DrawCardAction(this.owner,this.amount));
		}
	}

	@Override
	public void onRemove(){ BaseMod.unsubscribe(this); }

	@Override
	public void receivePostBattle(AbstractRoom battleRoom) {
		BaseMod.unsubscribeLater(this);
	}

	@Override
	public void receivePostDungeonInitialize() {
		BaseMod.unsubscribeLater(this);
	}
}