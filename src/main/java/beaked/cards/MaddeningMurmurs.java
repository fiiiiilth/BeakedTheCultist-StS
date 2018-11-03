package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import beaked.powers.HexingPower;
import beaked.powers.MurmursPlusPower;
import beaked.powers.MurmursPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MaddeningMurmurs extends CustomCard {
    public static final String ID = "beaked:MaddeningMurmurs";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADED_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final int COST = 2;
    public static final int CARDS = 1;

    public MaddeningMurmurs() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.POWER, AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded) AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new MurmursPower(p, CARDS),CARDS));
        else AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new MurmursPlusPower(p, CARDS),CARDS));
    }

    @Override
    public AbstractCard makeCopy() {
        return new MaddeningMurmurs();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADED_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
