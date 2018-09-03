package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

public class PrayerOfSafety extends CustomCard {
    public static final String ID = "beaked:PrayerOfSafety";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 1;
    public static final int INTANGIBLE = 1;
    public static final int BLOCK = 5;
    public static final int LOSE_DEX = 4;
    public static final int UPGRADE_PLUS_LOSE_DEX = -1;

    public PrayerOfSafety() {
        super(ID, NAME, null, COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON, CardTarget.SELF);
        this.block = this.baseBlock = BLOCK;
        this.magicNumber = this.baseMagicNumber = LOSE_DEX;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new IntangiblePlayerPower(p,INTANGIBLE),INTANGIBLE));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p,p,this.block));
        if (this.magicNumber != 0) AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new DexterityPower(p,-this.magicNumber),-this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PrayerOfSafety();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_LOSE_DEX);
        }
    }

}
