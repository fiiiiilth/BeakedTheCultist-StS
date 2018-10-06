package beaked.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
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


    public CrazyRitualsPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;
        updateDescription();
        this.loadRegion("hex");
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (cards == null){
            cards = CardLibrary.getAllCards();
        }
        for (int i=0;i<this.amount;i++){
            AbstractCard card;
            do{
                card = cards.get(AbstractDungeon.cardRandomRng.random(0, cards.size() - 1)).makeCopy();
            } while (card.rarity == AbstractCard.CardRarity.SPECIAL ||
                    card.type == AbstractCard.CardType.CURSE ||
                    card.rarity == AbstractCard.CardRarity.CURSE ||
                    card.type == AbstractCard.CardType.STATUS ||
                    // Servant's Vision cards currently cause a crash when used by a non-Servant character.
                    card.cardID == "Read" || card.cardID == "Deadline" ||
                    card.cardID == "ReturningBlade" || card.cardID == "Snipe" ||
                    card.cardID == "TimeTheft" || card.cardID == "TrueSight");

            // if the card is a power, purging it just gives a weird visual effect. It goes away anyway so no need.
            if (card.type != AbstractCard.CardType.POWER) card.purgeOnUse = true;
            card.freeToPlayOnce = true;
            AbstractDungeon.player.limbo.addToTop(card);
            card.target_x = Settings.WIDTH / 2 + (i*Settings.WIDTH*0.15f) - Math.min((this.amount-1) * Settings.WIDTH * .075f, Settings.WIDTH * .375f);
            card.target_y = Settings.HEIGHT / 2;
            card.targetDrawScale = card.targetDrawScale*1.2f;
            AbstractDungeon.actionManager.addToBottom(new QueueCardAction(card,AbstractDungeon.getRandomMonster()));
            for (int waitTimer = 9;waitTimer>i;waitTimer--) {
                // wait a long time PER CARD, getting slightly shorter for each card as more are added.
                // ie. Wait 0.9s for 1 card. Wait 0.9s + 0.8s + 0.7s = 2.4s for 3 cards.
                // It'd be nice to play each card THEN wait for the next, but tough to do w/ action queueing.
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = CrazyRitualsPower.DESCRIPTIONS[0];
    }

}
