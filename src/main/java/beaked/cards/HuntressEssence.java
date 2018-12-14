package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.actions.FastHealAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HuntressEssence extends CustomCard {
    public static final String ID = "beaked:HuntressEssence";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 2;
    public static final int HEAL_AMT = 1;
    public static final int UPGRADE_PLUS_HEAL_AMT = 2;

    public HuntressEssence() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON, CardTarget.SELF);

        this.magicNumber = this.baseMagicNumber = HEAL_AMT;
        this.exhaustOnUseOnce = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int exhausted = AbstractDungeon.player.exhaustPile.size();

        AbstractDungeon.actionManager.addToBottom(new FastHealAction(p,p,this.magicNumber * exhausted));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, exhausted));
    }

    @Override
    public AbstractCard makeCopy() {
        return new HuntressEssence();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_HEAL_AMT);
        }
    }
}
