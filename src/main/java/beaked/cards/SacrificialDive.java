package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import java.lang.reflect.Field;

public class SacrificialDive extends CustomCard {
    public static final String ID = "beaked:SacrificialDive";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final int COST = 1;

    public SacrificialDive() {
        super(ID, NAME, "img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(m != null && (m.intent == AbstractMonster.Intent.ATTACK || m.intent == AbstractMonster.Intent.ATTACK_BUFF ||
                m.intent == AbstractMonster.Intent.ATTACK_DEBUFF || m.intent == AbstractMonster.Intent.ATTACK_DEFEND)) {
            try {
                Field enemyMoveField = AbstractMonster.class.getDeclaredField("move");
                enemyMoveField.setAccessible(true);
                Field intentDmgField = AbstractMonster.class.getDeclaredField("intentDmg");
                intentDmgField.setAccessible(true);

                int hits = 1;
                if (((EnemyMoveInfo) enemyMoveField.get(m)).isMultiDamage){
                    hits = ((EnemyMoveInfo) enemyMoveField.get(m)).multiplier;
                }

                AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p,p,(int)(intentDmgField.getInt(m)*hits*0.5f)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,p,new StrengthPower(m,-99),-99));
                if (m != null && !m.hasPower("Artifact")) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new GainStrengthPower(m, 99), 99));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        } else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0f, EXTENDED_DESCRIPTION[0], true));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SacrificialDive();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.exhaust = false;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

}
