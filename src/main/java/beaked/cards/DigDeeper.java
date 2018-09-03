package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.patches.AbstractCardEnum;
import beaked.powers.RitualPlayerPower;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class DigDeeper extends CustomCard {
    public static final String ID = "beaked:DigDeeper";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final int COST = 2;
    public static final int HEAL_PER_STR = 1;

    public DigDeeper() {
        super(ID, NAME, null, COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = HEAL_PER_STR;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int str = 0;
        if (AbstractDungeon.player.hasPower(StrengthPower.POWER_ID)){
            str = AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount;

            for (int i=0;i<str;i++){
                AbstractDungeon.actionManager.addToBottom(new HealAction(p,p,this.magicNumber));
            }

            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p,StrengthPower.POWER_ID));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DigDeeper();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
            this.exhaust = false;
        }
    }

}
