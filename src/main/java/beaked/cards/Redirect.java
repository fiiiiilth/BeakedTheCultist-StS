package beaked.cards;

import beaked.Beaked;
import beaked.actions.WitherAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Redirect extends AbstractWitherCard {
    public static final String ID = "beaked:Redirect";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[0];
    private static final int COST = 1;
    private static final int BLOCK_PER_STRENGTH = 5;
    private static final int WITHER_MINUS_BLOCK_PER_STRENGTH = 1;
    private static final int UPGRADE_NEW_COST = 0;

    public Redirect() {
        super(ID, NAME, "img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.BEAKED_YELLOW, CardRarity.COMMON, CardTarget.SELF);

        this.baseMisc = this.misc = BLOCK_PER_STRENGTH;
        this.witherEffect = "Decreases the #yStrength multiplier.";
        this.witherAmount = WITHER_MINUS_BLOCK_PER_STRENGTH;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new WitherAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

        if (AbstractDungeon.player.hasPower(StrengthPower.POWER_ID)){
            final int str = AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount;
            if (str > 0) AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p,-str),-str));
        }
    }

    @Override
    public void applyPowers() {
        this.baseBlock = 0;
        if (AbstractDungeon.player.hasPower(StrengthPower.POWER_ID) && AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount > 0){
            this.baseBlock = AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount * this.misc;
        }
        super.applyPowers();
        if (!this.isDepleted && AbstractDungeon.player.hand.contains(this)) this.rawDescription = EXTENDED_DESCRIPTION + DESCRIPTION;
        else this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }

    public AbstractCard makeCopy() {
        return new Redirect();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_NEW_COST);
        }
    }
}
