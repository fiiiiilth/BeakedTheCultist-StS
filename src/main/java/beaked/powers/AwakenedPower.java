package beaked.powers;

import beaked.patches.CardAwakenedPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class AwakenedPower extends AbstractPower {

    public static final String POWER_ID = "beaked:AwakenedPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ArrayList<AbstractCard> awakenedCards;


    public AwakenedPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;
        updateDescription();
        this.loadRegion("unawakened");
        this.awakenedCards = new ArrayList<AbstractCard>();
    }

    @Override
    public void onInitialApplication(){
        awakenCards();
        for (AbstractCard c : AbstractDungeon.player.hand.group){
            if (c.cost >= 0){
                c.flash();
            }
        }
    }

    @Override
    public void stackPower(int amount){
        super.stackPower(amount);
        applyExtraAwaken(amount);
        for (AbstractCard c : AbstractDungeon.player.hand.group){
            if (c.cost >= 0){
                c.flash();
            }
        }
    }

    @Override
    public void onDrawOrDiscard(){
        awakenCards();
    }

    //@Override
    //public void atEndOfRound(){
    //    awakenedCards.clear();
    //}

    //@Override
    //public void onAfterUseCard(final AbstractCard card, final UseCardAction action) {
    //    awakenCards();
    //}

    public void awakenCards(){

        for (AbstractCard c : AbstractDungeon.player.drawPile.group){
            if (!awakenedCards.contains(c)){
                awakenModifyCostForTurn(c,-this.amount);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group){
            if (!awakenedCards.contains(c)){
                awakenModifyCostForTurn(c,-this.amount);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group){
            if (!awakenedCards.contains(c)){
                awakenModifyCostForTurn(c,-this.amount);
            }
        }
    }

    public void awakenSpecificCard(AbstractCard c, boolean ignoreAwakenedStatus){
        if (ignoreAwakenedStatus || !awakenedCards.contains(c)) {
            awakenModifyCostForTurn(c, -this.amount);
        }
    }

    public void applyExtraAwaken(int amt){

        // when a second copy is played, all card costs are reduced again.
        // all awakened cards are reduced by amt, and all non-awakened cards are reduced by the full power amount.

        for (AbstractCard c : AbstractDungeon.player.drawPile.group){
            if (!awakenedCards.contains(c)){
                awakenModifyCostForTurn(c,-this.amount);
            } else {
                awakenModifyCostForTurn(c, -amt);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group){
            if (!awakenedCards.contains(c)){
                awakenModifyCostForTurn(c,-this.amount);
            } else {
                awakenModifyCostForTurn(c, -amt);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group){
            if (!awakenedCards.contains(c)){
                awakenModifyCostForTurn(c,-this.amount);
            } else {
                awakenModifyCostForTurn(c, -amt);
            }
        }
    }

    public void awakenModifyCostForTurn(AbstractCard c, int amt) {

        // cost < 0 means it's X-cost (-1) or unplayable (-2), don't want to mess with those.
        if (c.cost < 0) return;

        c.costForTurn += amt;

        if (!AbstractDungeon.player.hasPower(AwakenedPlusPower.POWER_ID)){
            if (c.costForTurn < 0) c.costForTurn = 0;
        }

        if (c.cost != c.costForTurn) {
            c.isCostModified = true;
        }

        if (c.costForTurn < 0) CardAwakenedPatch.negativeCost.set(c,true);
        if (!awakenedCards.contains(c)) awakenedCards.add(c);
    }

    @Override
    public void updateDescription() {
        this.description = AwakenedPower.DESCRIPTIONS[0] + this.amount + AwakenedPower.DESCRIPTIONS[1];
    }

}
