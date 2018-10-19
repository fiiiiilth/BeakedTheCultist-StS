package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static beaked.patches.CardTagsEnum.ELITE_CARD;

public class BooksFlurry extends CustomCard {
    public static final String ID = "beaked:BooksFlurry";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 0;
    public static final int DAMAGE = 2;
    public static final int UPGRADE_DAMAGE = 1;

    public BooksFlurry() {
        super(ID, NAME, null, COST, DESCRIPTION, CardType.ATTACK, AbstractCardEnum.BEAKED_YELLOW, CardRarity.SPECIAL, CardTarget.ENEMY);

        this.magicNumber = this.baseMagicNumber = DAMAGE;
        this.tags.add(ELITE_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(Beaked.cardsPlayedThisCombat > 0) {
            for (int i = 0; i < Beaked.cardsPlayedThisCombat - 1; i++) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.magicNumber, DamageInfo.DamageType.THORNS),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_BOOK_STAB_" + MathUtils.random(0, 3)));
            }
        }
    }

    public void setDescription() {
        if(Beaked.cardsPlayedThisCombat > 1) {
            this.rawDescription = DESCRIPTION + " NL (" + Beaked.cardsPlayedThisCombat + " cards)";
        } else {
            this.rawDescription = DESCRIPTION + " NL (" + Beaked.cardsPlayedThisCombat + " card)";
        }
        this.initializeDescription();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if(Beaked.cardsPlayedThisCombat > 0) {
            this.setDescription();
        }
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();

    }

    @Override
    public AbstractCard makeCopy() {
        return new BooksFlurry();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DAMAGE);
        }
    }
}
