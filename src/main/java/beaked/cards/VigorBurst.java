package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class VigorBurst extends CustomCard {
    public static final String ID = "beaked:VigorBurst";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final int COST = -2;
    public static final int MAX_HP = 5;
    public static final int HEAL = 3;
    public static final int UPGRADE_PLUS_HEAL = 2;

    public VigorBurst() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON, CardTarget.NONE);
        this.magicNumber = this.baseMagicNumber = HEAL;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    public void onAddedToMasterDeck(){
        AbstractDungeon.player.increaseMaxHp(MAX_HP,true);
    }

    public void onRemovedFromMasterDeck(){
        if (!this.upgraded) {
            AbstractDungeon.player.decreaseMaxHealth(MAX_HP);
            CardCrawlGame.sound.play("BLOOD_SWISH");
        }
    }

    @Override
    public void triggerWhenDrawn() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player,AbstractDungeon.player,this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new VigorBurst();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_HEAL);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
