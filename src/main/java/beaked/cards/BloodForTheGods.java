package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import beaked.powers.NextTurnHealPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BloodForTheGods extends CustomCard {
    public static final String ID = "beaked:BloodForTheGods";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final int COST = 2;
    private static final int SELF_ATTACK_DMG = 10;
    public static final int UPGRADE_PLUS_SELF_DAMAGE = 5;

    public DamageInfo dmg;

    public BloodForTheGods() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON,
                CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = SELF_ATTACK_DMG;
        Beaked.setDescription(this,DESCRIPTION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg = new DamageInfo(p, this.magicNumber, DamageInfo.DamageType.THORNS);
        AbstractDungeon.actionManager.addToBottom(new DamageAction(p, dmg, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new NextTurnHealPower(p,dmg.output*2),dmg.output*2));
    }

    public AbstractCard makeCopy() {
        return new BloodForTheGods();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_SELF_DAMAGE);
        }
    }
}
