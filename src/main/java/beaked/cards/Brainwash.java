package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.actions.ExhaustAllDepletedAction;
import beaked.actions.FlashHandAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

public class Brainwash extends CustomCard {
    public static final String ID = "beaked:Brainwash";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 3;
    public static final int UPGRADED_COST = 2;

    public Brainwash() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.RARE, CardTarget.NONE);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ExhaustAllDepletedAction());
        AbstractDungeon.actionManager.addToBottom(new FlashHandAction());
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new EquilibriumPower(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Brainwash();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADED_COST);
        }
    }

}
