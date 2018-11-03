package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import beaked.powers.FlightPlayerPower;
import beaked.powers.HexingPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LiftOff extends CustomCard {
    public static final String ID = "beaked:LiftOff";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADED_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final int COST = 3;
    public static final int UPGRADE_NEW_COST = 2;
    public static final int FLIGHT_AMT = 3;

    public LiftOff() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.POWER, AbstractCardEnum.BEAKED_YELLOW, CardRarity.RARE, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = FLIGHT_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FlightPlayerPower(p, this.magicNumber),this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new LiftOff();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_NEW_COST);
        }
    }
}
