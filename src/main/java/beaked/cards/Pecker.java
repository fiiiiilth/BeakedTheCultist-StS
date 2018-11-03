package beaked.cards;

import beaked.Beaked;
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
    private static final int COST = 2;
    private static final int DAMAGE = 1;
    private static final int NUM_HITS = 5;
    private static final int WITHER_MINUS_NUM_HITS = 1;
    private static final int UPGRADE_PLUS_NUM_HITS = 2;
    private static final int UPGRADE_NEW_COST = 1;

    public Pecker() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON, CardTarget.ENEMY);

        this.baseDamage = this.damage = DAMAGE;
        this.baseMisc = this.misc = NUM_HITS;
        this.witherEffect = "Decreases the number of hits.";
        this.witherAmount = WITHER_MINUS_NUM_HITS;
        Beaked.setDescription(this,DESCRIPTION);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new WitherAction(this));
        for (int i=0;i<misc;i++){
            AbstractDungeon.actionManager.addToBottom(new PummelDamageAction(m, new DamageInfo(p,this.damage,this.damageTypeForTurn)));
        }
    }

    public AbstractCard makeCopy() {
        return new Pecker();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMisc(UPGRADE_PLUS_NUM_HITS);
            this.upgradeBaseCost(UPGRADE_NEW_COST);
        }
    }
}
