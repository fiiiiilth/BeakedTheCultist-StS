package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

import static beaked.patches.CardTagsEnum.ELITE_CARD;

public class NemesisSlickness extends CustomCard {
    public static final String ID = "beaked:NemesisSlickness";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final int COST = 0;
    public static final int INTANGIBLE = 1;
    public static final int DAMAGE = 6;

    public NemesisSlickness() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.SPECIAL, CardTarget.SELF);

        this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = INTANGIBLE;
        this.isMultiDamage = true;
        this.tags.add(ELITE_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, magicNumber), magicNumber));
        if(this.upgraded) {
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NemesisSlickness();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.target = CardTarget.ALL;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
            this.type = CardType.ATTACK;
        }
    }
}
