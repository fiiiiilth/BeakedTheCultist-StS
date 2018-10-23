package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.actions.ForcedEndTurnAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.EndTurnAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Random;

public class Struggle extends CustomCard {
    public static final String ID = "beaked:Struggle";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String EXTENDED_DESCRIPTION[] = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 0;
    private static final int ATTACK_DMG = 4;
    private static final int UPGRADE_PLUS_DMG = 1;

    public static final Random randomAtk = new Random();
    public static final ArrayList<AbstractGameAction.AttackEffect> atkEffects = new ArrayList<>();

    public Struggle() {
        super(ID, NAME, "img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON,
                CardTarget.ENEMY);
        if (atkEffects.isEmpty()) {
            atkEffects.add(AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
            atkEffects.add(AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
            atkEffects.add(AbstractGameAction.AttackEffect.SLASH_VERTICAL);
            atkEffects.add(AbstractGameAction.AttackEffect.SLASH_HEAVY);
            atkEffects.add(AbstractGameAction.AttackEffect.SMASH);
            atkEffects.add(AbstractGameAction.AttackEffect.BLUNT_HEAVY);
            atkEffects.add(AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        }
        this.baseDamage = this.damage = ATTACK_DMG;
        Beaked.setDescription(this,DESCRIPTION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i=0;i<this.magicNumber;i++) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), getRandomAttackEffect()));
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.05f));
        }
        AbstractDungeon.actionManager.addToBottom(new ForcedEndTurnAction());
    }

    public AbstractGameAction.AttackEffect getRandomAttackEffect(){
        return atkEffects.get(randomAtk.nextInt(atkEffects.size()));
    }

    @Override
    public void applyPowers(){
        this.baseMagicNumber = AbstractDungeon.player.hand.size();
        if (AbstractDungeon.player.hand.contains(this)) this.baseMagicNumber -= 1;
        this.magicNumber = this.baseMagicNumber;
        Beaked.setDescription(this, EXTENDED_DESCRIPTION[0] + DESCRIPTION);
        super.applyPowers();
    }

    @Override
    public void onMoveToDiscard() {
        Beaked.setDescription(this,DESCRIPTION);
    }

    public AbstractCard makeCopy() {
        return new Struggle();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
