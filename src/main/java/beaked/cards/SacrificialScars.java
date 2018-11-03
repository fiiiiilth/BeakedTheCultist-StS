package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.actions.GainMaxHPAction;
import beaked.patches.AbstractCardEnum;
import beaked.powers.NoHealPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SacrificialScars extends CustomCard {
    public static final String ID = "beaked:SacrificialScars";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 0;
    public static final int HP_LOSS = 6;
    public static final int UPGRADE_PLUS_HP_LOSS = 1;
    public static final int MAX_HP_GAIN = 2;
    public static final int UPGRADE_PLUS_MAX_HP_GAIN = 1;
    public static final int NO_HEAL_TURNS = 3;

    public int hpLoss;

    public SacrificialScars() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
        this.magicNumber = this.baseMagicNumber = MAX_HP_GAIN;
        this.hpLoss = HP_LOSS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p, p, this.hpLoss));
        AbstractDungeon.actionManager.addToBottom(new GainMaxHPAction(p,p,this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new NoHealPower(p,NO_HEAL_TURNS),NO_HEAL_TURNS));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SacrificialScars();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.hpLoss += UPGRADE_PLUS_HP_LOSS;
            this.upgradeMagicNumber(UPGRADE_PLUS_MAX_HP_GAIN);
        }
    }

}
