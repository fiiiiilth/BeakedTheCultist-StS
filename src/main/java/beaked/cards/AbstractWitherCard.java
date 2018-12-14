package beaked.cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import beaked.actions.WitherAction;
import beaked.patches.AbstractCardEnum;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWitherCard extends CustomCard {

    public static final String DEPLETED_DESCRIPTION = "Depleted. NL ";
    // misc (found in AbstractCard):    // the current power of the card. Wither effects change this. Use !I! in card descriptions to show this value.
    public int baseMisc;                // the starting power of the card. Does not change as the card withers.
    public boolean isDepleted = false;  // if the card's power has withered to 0.
    public String witherEffect = "";    // a description of what value the Wither effect changes.
    public int witherAmount = 0;        // how much the card withers, ie. the 2 in "Wither 2." Also used by replenish effects.
    public boolean linkWitherAmountToMagicNumber = false; // is witherAmount always equal to this card's magicNumber?

    // Use dynamic variables !beaked:wI!, !beaked:wD!, !beaked:wB! to represent the card's current power in purple.

    // IN THE CONSTRUCTOR OF A NEW WITHER CARD:
    // this.misc = this.baseMisc = <Initial Card Value>;
    // witherEffect = "Effect Description"
    // witherAmount = <Wither Amount> /OR/ linkWitherAmountToMagicNumber = true, and set magicNumber

    // IN USE:
    // You probably want a WitherAction. It automatically uses your card's witherAmount for the decrease.
    // I usually put it first in the action queue so that the red flash effect shows up before the card moves to the discard pile.

    // IN APPLYPOWERS:
    // nothing required here for basic Wither cards.
    // If a defined value like damage, block, etc. is what withers, set this.baseDamage = this.misc BEFORE calling super.ApplyPowers().

    // IN UPGRADE:
    // if the wither amount is being upgraded and linkWitherAmountToMagicNumber == false, just change witherAmount manually.
    // if the value that withers is being upgraded, call upgradeBaseMisc().
    // if a defined value like damage, block, etc. is what withers and is being upgraded, call upgradeDamage() AND THEN upgradeBaseMisc().

    public AbstractWitherCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    @Override
    public boolean canUse (AbstractPlayer p, AbstractMonster m){
        return super.canUse(p,m) && !this.isDepleted;
    }

    @Override
    public String getCantPlayMessage(){
        if (this.isDepleted) return "This card is #rDepleted!";
        else return super.getCantPlayMessage();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy(){
        AbstractCard c = super.makeStatEquivalentCopy();
        if (c.misc == 0) ((AbstractWitherCard)c).onDepleted();
        return c;
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        List<TooltipInfo> tips = new ArrayList<>();
        tips.add(new TooltipInfo("Wither Effect",this.witherEffect + this.getMiscValueText()));
        return tips;
    }

    public String getMiscValueText(){
        return " NL (Current value: #b" + this.misc + ")";
    }

    @Override
    public void applyPowers() {

        if (!this.isDepleted && ((this.baseMisc > 0 && this.misc <= 0) || (this.baseMisc < 0 && this.misc >= 0))){
            onDepleted();
        } else if (this.isDepleted && ((this.baseMisc < 0 && this.misc < 0) || (this.baseMisc > 0 && this.misc > 0))){
            onRestored();
        }

        // game crashes if calculating multidamage in a null room (ie. when loading a file), so avoid that.
        boolean tmp = this.isMultiDamage;
        if (AbstractDungeon.currMapNode == null){
            this.isMultiDamage = false;
        }
        // Only do base applyPowers for cards in hand. This causes their displayed dmg/block to change based on in-battle effects.
        if (AbstractDungeon.player != null && AbstractDungeon.player.hand.contains(this)) super.applyPowers();
        this.isMultiDamage = tmp;

        if (linkWitherAmountToMagicNumber){
            this.witherAmount = this.magicNumber;
        }
    }

    public void onDepleted(){
        if (this.isDepleted) return;

        this.isDepleted = true;
        this.misc = 0;
        this.applyPowers(); // updates damage/block values to 0 instead of negative if misc dips too low.
        this.initializeDescription();
    }
    public void onRestored(){
        if (!this.isDepleted) return;

        this.isDepleted = false;
        this.applyPowers();
        this.initializeDescription();
    }

    public void replenish(int numUses){
        int tmp = this.misc;

        this.misc -= this.witherAmount * numUses;
        if (tmp <= this.baseMisc && this.misc > this.baseMisc ||
            tmp >= this.baseMisc && this.misc < this.baseMisc){
            // can't replenish beyond original value
            this.misc = this.baseMisc;
        }

        this.flash();
        applyPowers();
    }

    @Override
    public void initializeDescription(){
        // This is fine to not interact with pluralization, it just adds and removes "Depleted" from description.
        if (this.isDepleted && !this.rawDescription.startsWith(DEPLETED_DESCRIPTION)){
            this.rawDescription = DEPLETED_DESCRIPTION + this.rawDescription;
        } else if (!this.isDepleted && this.rawDescription.startsWith(DEPLETED_DESCRIPTION)){
            this.rawDescription = this.rawDescription.replaceFirst(DEPLETED_DESCRIPTION,"");
        }
        super.initializeDescription();
    }

    protected void upgradeMisc(int amount){
        this.baseMisc += amount;
        this.misc += amount;
        applyPowers();
    }
}
