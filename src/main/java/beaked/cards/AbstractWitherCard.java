package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.actions.WitherAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractWitherCard extends CustomCard {

    public static final String DEPLETED_DESCRIPTION = "Depleted. NL ";
    public int baseMisc;
    public boolean isDepleted = false;

    private String savedDesc = "";

    public AbstractWitherCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    @Override
    public boolean cardPlayable (AbstractMonster m){
        return super.cardPlayable(m) && !this.isDepleted;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy(){
        AbstractCard c = super.makeStatEquivalentCopy();
        if (c.misc == 0) ((AbstractWitherCard)c).onDepleted();
        return c;
    }

    @Override
    public void applyPowers() {
        if (!this.isDepleted && ((this.baseMisc > 0 && this.misc <= 0) || (this.baseMisc < 0 && this.misc >= 0))){
            onDepleted();
        } else if (this.isDepleted && ((this.baseMisc < 0 && this.misc < 0) || (this.baseMisc > 0 && this.misc > 0))){
            onRestored();
        }
        super.applyPowers();
        this.initializeDescription();
    }

    public void onDepleted(){
        if (this.isDepleted) return;

        this.isDepleted = true;
        this.savedDesc = this.rawDescription;
        this.misc = 0;
        this.rawDescription = DEPLETED_DESCRIPTION + this.rawDescription;
        this.initializeDescription();
    }
    public void onRestored(){
        if (!this.isDepleted) return;

        this.isDepleted = false;
        this.rawDescription = this.savedDesc;
        this.initializeDescription();
    }

    protected void upgradeMisc(int amount){
        this.baseMisc += amount;
        this.misc += amount;
        if (AbstractDungeon.player != null) applyPowers();
    }
}
