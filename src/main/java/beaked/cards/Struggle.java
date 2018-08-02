package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.actions.BackfireDamageAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

public class Struggle extends CustomCard {
    public static final String ID = "Struggle";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 0;
    private static final int ATTACK_DMG = 5;
    private static final int SELF_DMG = 1;
    private static final int DMG_PER_CARD = 3;
    private static final int UPGRADE_PLUS_DMG_PER_CARD = 2;

    public Struggle() {
        super(ID, NAME, null, COST, DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.BEAKED_YELLOW, CardRarity.UNCOMMON,
                CardTarget.ENEMY);
        this.baseDamage = this.damage = ATTACK_DMG;
        this.magicNumber = this.baseMagicNumber = DMG_PER_CARD;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m){
        boolean seenStruggle = false;
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        } else {
            Iterator var4 = p.hand.group.iterator();

            while(var4.hasNext()) {
                AbstractCard c = (AbstractCard)var4.next();
                if (c.cardID == Struggle.ID){
                    // TODO make this so that only the first struggle in hand is playable
                    continue;
                }
                else if (c.canUse(p,m) || c.cardPlayable(m)) {
                    canUse = false;
                    this.cantUseMessage = EXTENDED_DESCRIPTION[0];
                }
            }

            return canUse;
        }
    }

    @Override
    public void applyPowers(){
        this.baseDamage = ATTACK_DMG + ((AbstractDungeon.player.hand.size()-1)*this.magicNumber);
        super.applyPowers();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        // this action ignores self damage if the battle is now over
        AbstractDungeon.actionManager.addToBottom(new BackfireDamageAction(SELF_DMG));
    }

    public AbstractCard makeCopy() {
        return new Struggle();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_DMG_PER_CARD);
        }
    }
}
