package beaked.cards;

import beaked.actions.WitherAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.PummelDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Pecker extends AbstractWitherCard {
    public static final String ID = "beaked:Pecker";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[0];
    private static final int COST = 2;
    private static final int DAMAGE = 1;
    private static final int NUM_HITS = 5;
    private static final int WITHER = 1;
    private static final int UPGRADE_PLUS_NUM_HITS = 2;
    private static final int UPGRADE_NEW_COST = 1;

    public Pecker() {
        super(ID, NAME, null, COST, DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON, CardTarget.ENEMY);

        this.baseDamage = this.damage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = NUM_HITS;
        this.baseMisc = this.misc = this.magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new WitherAction(this, WITHER));
        for (int i=0;i<magicNumber;i++){
            AbstractDungeon.actionManager.addToBottom(new PummelDamageAction(m, new DamageInfo(p,this.damage,this.damageTypeForTurn)));
        }
    }

    @Override
    public void applyPowers() {
        this.baseMagicNumber = this.magicNumber = this.misc;
        super.applyPowers();
        // pluralization
        this.rawDescription = (this.magicNumber == 1 ? EXTENDED_DESCRIPTION : DESCRIPTION);
        this.initializeDescription();
    }

    public AbstractCard makeCopy() {
        return new Pecker();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_NUM_HITS);
            this.upgradeMisc(UPGRADE_PLUS_NUM_HITS);  // use upgradeMisc to update the witherable value (calls ApplyPowers)
            this.upgradeBaseCost(UPGRADE_NEW_COST);
        }
    }
}
