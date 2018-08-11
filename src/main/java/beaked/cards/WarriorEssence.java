package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.actions.WarriorEssenceAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class WarriorEssence extends CustomCard {
    public static final String ID = "beaked:WarriorEssence";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = -1;
    public static final int DAMAGE = 2;
    public static final int TIMES = 2;
    public static final float HEALING_MULTIPLIER = 0.5f;
    public static final int UPGRADE_PLUS_TIMES = 1;

    public WarriorEssence() {
        super(ID, NAME, null, COST, DESCRIPTION, CardType.ATTACK, AbstractCardEnum.BEAKED_YELLOW, CardRarity.COMMON, CardTarget.ALL_ENEMY);

        this.magicNumber = this.baseMagicNumber = TIMES;
        this.baseDamage = DAMAGE;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new WarriorEssenceAction(p, this.damage, this.damageTypeForTurn, this.freeToPlayOnce, this.energyOnUse, this.magicNumber, HEALING_MULTIPLIER));
    }

    @Override
    public AbstractCard makeCopy() {
        return new WarriorEssence();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_TIMES);
        }
    }
}
