package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HuntressEssence extends CustomCard {
    public static final String ID = "HuntressEssence";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final int COST = 2;
    public static final int HEAL_DRAW_AMT = 1;
    public static final int UPGRADE_HEAL_DRAW_AMT = 1;

    public HuntressEssence() {
        super(ID, NAME, null, COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON, CardTarget.SELF);

        this.magicNumber = this.baseMagicNumber = HEAL_DRAW_AMT;
        this.exhaustOnUseOnce = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int exhausted = AbstractDungeon.player.exhaustPile.size();

        AbstractDungeon.player.heal(this.magicNumber * exhausted);
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber * exhausted));
    }

    @Override
    public AbstractCard makeCopy() {
        return new HuntressEssence();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_HEAL_DRAW_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
            this.upgradeMagicNumber(UPGRADE_HEAL_DRAW_AMT);
        }
    }
}
