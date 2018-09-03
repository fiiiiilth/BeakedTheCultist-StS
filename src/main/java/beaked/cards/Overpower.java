package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Overpower extends CustomCard {
    public static final String ID = "beaked:Overpower";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 1;
    public static final int DAMAGE = 9;
    public static final int DIVIDE_BY = 5;
    public static final int UPGRADED_DIVIDE_BY = -1;

    public Overpower() {
        super(ID, NAME, null, COST, DESCRIPTION, CardType.ATTACK, AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON, CardTarget.ENEMY);

        this.magicNumber = this.baseMagicNumber = DIVIDE_BY;
        this.baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int dmg = this.damage + (p.currentHealth / this.magicNumber);

        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, dmg, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Overpower();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADED_DIVIDE_BY);
        }
    }
}
