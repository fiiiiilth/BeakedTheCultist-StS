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
            } while (card.rarity == AbstractCard.CardRarity.SPECIAL || card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS);

            // if the card is a power, purging it just gives a weird visual effect. It goes away anyway so no need.
            if (card.type != AbstractCard.CardType.POWER) card.purgeOnUse = true;
            card.freeToPlayOnce = true;
            AbstractDungeon.player.limbo.addToTop(card);
            card.target_x = Settings.WIDTH / 2 + (i*Settings.WIDTH*0.15f);
            card.target_y = Settings.HEIGHT / 2;
            card.targetDrawScale = card.targetDrawScale*1.2f;
            AbstractDungeon.actionManager.addToBottom(new QueueCardAction(card,AbstractDungeon.getRandomMonster()));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
        }
    }

    @Override
    public void updateDescription() {
        this.description = CrazyRitualsPower.DESCRIPTIONS[0];
    }

}
