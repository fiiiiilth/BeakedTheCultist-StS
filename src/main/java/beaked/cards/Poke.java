package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.actions.DrawAndLogCardsAction;
import beaked.actions.FeathersFollowupAction;
import beaked.actions.PokeFollowupAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Poke extends CustomCard {
    public static final String ID = "beaked:Poke";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADED_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final int COST = 1;
    public static final int DRAW = 1;
    public static final int UPGRADE_PLUS_DRAW = 1;
    public static final int DAMAGE = 5;

    public Poke() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.ATTACK, AbstractCardEnum.BEAKED_YELLOW, CardRarity.COMMON, CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = DRAW;
        this.baseDamage = this.damage = DAMAGE;
        Beaked.setDescription(this,DESCRIPTION);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DrawAndLogCardsAction(p,this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3f));
        AbstractDungeon.actionManager.addToBottom(new PokeFollowupAction(p,m,this.damage));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Poke();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_DRAW);
        }
    }
}
