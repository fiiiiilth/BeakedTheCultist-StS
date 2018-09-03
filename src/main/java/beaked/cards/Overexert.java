package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.actions.BackfireDamageAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.defect.DoubleEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class Overexert extends CustomCard {
    public static final String ID = "beaked:Overexert";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 0;
    public static final int SELF_DMG_PER_ENERGY = 3;
    public static final int UPGRADE_PLUS_SELF_DMG_PER_ENERGY = -1;

    public Overexert() {
        super(ID, NAME, null, COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = SELF_DMG_PER_ENERGY;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DoubleEnergyAction());
        for (int i=0;i< EnergyPanel.getCurrentEnergy();i++){
            AbstractDungeon.actionManager.addToBottom(new BackfireDamageAction(this.magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Overexert();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_SELF_DMG_PER_ENERGY);
        }
    }

}
