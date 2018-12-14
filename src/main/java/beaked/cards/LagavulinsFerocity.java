package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import beaked.powers.LagavulinsFerocityPlusPower;
import beaked.powers.LagavulinsFerocityPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static beaked.patches.CardTagsEnum.ELITE_CARD;

public class LagavulinsFerocity extends CustomCard {
    public static final String ID = "beaked:LagavulinsFerocity";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static int COST = 0;

    public LagavulinsFerocity() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.POWER, AbstractCardEnum.BEAKED_YELLOW, CardRarity.SPECIAL, CardTarget.SELF);

        this.tags.add(ELITE_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(this.upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LagavulinsFerocityPlusPower(p, 1), 1));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LagavulinsFerocityPower(p, 1), 1));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LagavulinsFerocity();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
