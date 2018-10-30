package beaked.powers;

import beaked.actions.CrazyRitualsPlayAction;
import beaked.actions.QueueCardFrontAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;

public class CrazyRitualsPower extends AbstractPower {

    public static final String POWER_ID = "beaked:CrazyRitualsPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static ArrayList<AbstractCard> cards;
    public static AbstractCard nextCard;
    public boolean isFinishedThisTurn = false;
    public int maxAmount;


    public CrazyRitualsPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = this.maxAmount = amount;
        this.isTurnBased = false;
        // have to set isFinishedThisTurn, otherwise starts trying to trigger for cards that we play after playing the power.
        this.isFinishedThisTurn = true;
        updateDescription();
        this.loadRegion("hex");
    }

    @Override
    public void stackPower(final int stackAmount) {
        super.stackPower(stackAmount);
        this.maxAmount += stackAmount;
    }

    @Override
    public void onAfterUseCard(AbstractCard c, UseCardAction action)
    {
        // if the next card in the queue is the same uuid as the one just played, it's a copy created from Necronomicon/Echo Form.
        // Let it play out immediately, then resume the chain afterwards (when this condition isn't true)
        if ((!AbstractDungeon.actionManager.cardQueue.isEmpty() && AbstractDungeon.actionManager.cardQueue.get(0).card.uuid == nextCard.uuid) || isFinishedThisTurn) return;
        playCardEffect(0);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (cards == null){
            cards = CardLibrary.getAllCards();
        }
        this.amount = this.maxAmount;
        isFinishedThisTurn = false;
        // start the chain
        playCardEffect(0);
    }

    public void playCardEffect(int queueIndex){

        if (this.amount-- <= 0){
            this.amount = this.maxAmount;
            isFinishedThisTurn = true;
            return;
        }

        do{
            nextCard = cards.get(AbstractDungeon.cardRandomRng.random(0, cards.size() - 1)).makeCopy();
        } while ((nextCard.rarity == AbstractCard.CardRarity.SPECIAL && nextCard.color.toString() != "INFINITE_BLACK") ||
                nextCard.type == AbstractCard.CardType.CURSE ||
                nextCard.rarity == AbstractCard.CardRarity.CURSE ||
                nextCard.type == AbstractCard.CardType.STATUS ||
                // Servant's Vision cards currently cause a crash when used by a non-Servant character.
                nextCard.cardID == "Read" || nextCard.cardID == "Deadline" ||
                nextCard.cardID == "ReturningBlade" || nextCard.cardID == "Snipe" ||
                nextCard.cardID == "TimeTheft" || nextCard.cardID == "TrueSight");

        nextCard.purgeOnUse = true;
        nextCard.freeToPlayOnce = true;
        AbstractDungeon.player.limbo.addToTop(nextCard);
        nextCard.target_x = Settings.WIDTH / 2;
        nextCard.target_y = Settings.HEIGHT / 2;
        nextCard.targetDrawScale = nextCard.targetDrawScale*1.4f;
        if (AbstractDungeon.player.hasRelic("Quantum Egg")) nextCard.upgrade();
        AbstractMonster targetMonster = AbstractDungeon.getRandomMonster();
        // Typically, cards are added to the front of the queue so they can't be interrupted by player actions.
        AbstractDungeon.actionManager.addToBottom(new QueueCardFrontAction(nextCard,targetMonster,queueIndex));
        // Wait action can't wait for more than 0.1s on fast mode, so just add a bunch of them
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
        // If a card can't be used, it doesn't trigger onAfterUseCard, which would break the chain.
        // Instead, after the typical wait time, we add the NEXT card to the queue early, placing it after the current unusable card.
        if (!nextCard.canUse(AbstractDungeon.player,targetMonster)){
            AbstractDungeon.actionManager.addToBottom(new CrazyRitualsPlayAction(this));
        }
    }

    @Override
    public void updateDescription() {
        this.description = CrazyRitualsPower.DESCRIPTIONS[0];
    }

}
