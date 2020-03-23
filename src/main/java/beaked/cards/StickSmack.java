package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class StickSmack extends CustomCard {
    public static final String ID = "beaked:StickSmack";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 1;
    private static final int ATTACK_DMG = 6;

    public static StickSmack lastPlayed = null;

    public StickSmack() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON,
                CardTarget.ENEMY);
        this.baseDamage = ATTACK_DMG;
        this.cardsToPreview = new Stick();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        if (!purgeOnUse) lastPlayed = this;

        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        AbstractCard stick = new Stick();
        if (this.upgraded) stick.upgrade();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(stick,1,true,false));
    }

    public AbstractCard makeCopy() {
        return new StickSmack();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
            this.cardsToPreview.upgrade();
        }
    }
}
