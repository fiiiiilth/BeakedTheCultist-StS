package beaked.cards;

import beaked.Beaked;
import beaked.actions.WitherAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class CursingBlood extends AbstractWitherCard {
    public static final String ID = "beaked:CursingBlood";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 1;
    public static final int STR_DOWN = 4;
    public static final int WITHER_MINUS_STR_DOWN = 1;
    public static final int UPGRADE_PLUS_STR_DOWN = 1;

    public CursingBlood() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.BEAKED_YELLOW, CardRarity.COMMON, CardTarget.ENEMY);

        this.baseMisc = this.misc = STR_DOWN;
        this.witherEffect = "Decreases #yStrength reduction.";
        this.magicNumber = this.baseMagicNumber = WITHER_MINUS_STR_DOWN;
        this.linkWitherAmountToMagicNumber = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new WitherAction(this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,p,new StrengthPower(m,-this.misc),-this.misc));
    }

    public AbstractCard makeCopy() {
        return new CursingBlood();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMisc(UPGRADE_PLUS_STR_DOWN);
        }
    }
}
