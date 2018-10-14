package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

public class Brace extends CustomCard {
    public static final String ID = "beaked:Brace";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 1;
    public static final int BASE_BLOCK = 0;
    public static final int INCREASE_BLOCK = 1;
    public static final int UPGRADE_PLUS_INCREASE_BLOCK = 1;

    public Brace() {
        super(ID, NAME, "img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.COMMON, CardTarget.SELF);
        this.baseBlock = this.block = BASE_BLOCK;
        this.magicNumber = this.baseMagicNumber = INCREASE_BLOCK;
        this.exhaust = true;
        this.retain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
       AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p,p,this.block));
    }

    @Override
    public void applyPowers(){
        this.retain = true;
        super.applyPowers();
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c){
        this.flash();
        this.baseBlock += this.magicNumber;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Brace();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_INCREASE_BLOCK);
        }
    }

}
