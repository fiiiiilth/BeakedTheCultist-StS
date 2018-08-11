package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.actions.IncreaseMiscOnKillAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Sacrifice extends CustomCard {
    public static final String ID = "beaked:Sacrifice";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 2;
    private static final int ATTACK_DMG = 18;
    private static final int UPGRADE_PLUS_DMG = 9;

    public Sacrifice() {
        super(ID, NAME, null, COST, DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.BEAKED_YELLOW, CardRarity.RARE,
                CardTarget.ENEMY);

        this.baseDamage = this.damage = ATTACK_DMG;
        this.magicNumber = this.baseMagicNumber = ATTACK_DMG;
        this.misc = 0;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new IncreaseMiscOnKillAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn), 1, this));
        AbstractDungeon.actionManager.addToBottom(new HealAction(m,p,this.baseDamage));
        if (this.misc > 0) AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new StrengthPower(p,this.misc),this.misc));
    }

    public AbstractCard makeCopy() {
        return new Sacrifice();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
            this.upgradeMagicNumber(UPGRADE_PLUS_DMG);
        }
    }
}
