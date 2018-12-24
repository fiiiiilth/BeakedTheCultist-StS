package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.cards.green.Flechettes;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Mantra extends CustomCard {
    public static final String ID = "beaked:Mantra";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final int COST = 2;
    public static final int DAMAGE = 4;
    public static final int UPGRADE_PLUS_DAMAGE = 1;

    public Mantra() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.ATTACK, AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = this.damage = DAMAGE;
        recalculateWither();
    }

    public void recalculateWither(){
        if (AbstractDungeon.player == null) {
            Beaked.setDescription(this, DESCRIPTION);
        }
        else {
            this.magicNumber = this.baseMagicNumber = countWitherCards();
            Beaked.setDescription(this, DESCRIPTION + EXTENDED_DESCRIPTION[0]);
        }
    }

    @Override
    public void applyPowers(){
        recalculateWither();
        super.applyPowers();
    }

    public int countWitherCards(){

        if (AbstractDungeon.player == null || AbstractDungeon.player.masterDeck == null) return 0;

        int count=0;
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group){
            if (c instanceof AbstractWitherCard){
                count++;
            }
        }
        return count;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i=0;i<this.magicNumber;i++){
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m,new DamageInfo(p,this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.05f));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Mantra();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
        }
    }

}
