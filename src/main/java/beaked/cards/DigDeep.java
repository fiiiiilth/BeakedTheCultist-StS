package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import beaked.powers.RitualPlayerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

public class DigDeep extends CustomCard {
    public static final String ID = "beaked:DigDeep";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 1;
    public static final int HEAL_PER_RITUAL = 3;
    public static final int UPGRADE_PLUS_HEAL_PER_RITUAL = 2;

    public DigDeep() {
        super(ID, NAME, "img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.COMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = HEAL_PER_RITUAL;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int ritual = 0;
        if (AbstractDungeon.player.hasPower(RitualPlayerPower.POWER_ID)){
            ritual = AbstractDungeon.player.getPower(RitualPlayerPower.POWER_ID).amount;
        }

        for (int i=0;i<ritual;i++){
            AbstractDungeon.actionManager.addToBottom(new HealAction(p,p,this.magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DigDeep();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_HEAL_PER_RITUAL);
        }
    }

}
