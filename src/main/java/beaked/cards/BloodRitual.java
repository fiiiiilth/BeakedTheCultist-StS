package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RitualPower;

public class BloodRitual extends CustomCard {
    public static final String ID = "beaked:BloodRitual";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 0;
    public static final int HP_LOSS = 12;
    public static final int RITUAL = 1;
    public static final int UPGRADE_PLUS_RITUAL = 1;

    public BloodRitual() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = RITUAL;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
       AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p,p,HP_LOSS));
       AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new RitualPower(p,this.magicNumber,true),this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BloodRitual();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_RITUAL);
        }
    }

}
