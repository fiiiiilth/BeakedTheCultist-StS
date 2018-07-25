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

    public static final String DEPLETED_DESCRIPTION = "Unplayable. NL ";
    public int baseMisc;
    public boolean isDepleted = false;

    public AbstractWitherCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m){
        return super.canUse(p,m) && this.misc > 0;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (!this.isDepleted && ((this.baseMisc > 0 && this.misc <= 0) || (this.baseMisc < 0 && this.misc >= 0))){
            isDepleted = true;
            this.misc = 0;
            onDepleted();
        } else if (this.isDepleted && ((this.baseMisc < 0 && this.misc < 0) || (this.baseMisc > 0 && this.misc > 0))){
            isDepleted = false;
            onRestored();
        }
        super.applyPowers();
    }

    public abstract void onDepleted();
    public abstract void onRestored();
}
