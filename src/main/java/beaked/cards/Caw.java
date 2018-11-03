package beaked.cards;

import beaked.Beaked;
import beaked.actions.CawAction;
import beaked.actions.WitherAction;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

import java.util.Iterator;

public class Caw extends AbstractWitherCard {
    public static final String ID = "beaked:Caw";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADED_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final int COST = -1;
    public static final int DAMAGE = 5;
    public static final int WITHER_MINUS_DAMAGE = 1;

    public Caw() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.ATTACK,
                AbstractCardEnum.BEAKED_YELLOW, CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.baseDamage = this.damage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = WITHER_MINUS_DAMAGE;
        this.baseMisc = this.misc = this.damage;
        this.isMultiDamage = true;
        this.witherEffect = "Decreases damage.";
        this.linkWitherAmountToMagicNumber = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractDungeon.actionManager.addToBottom(new WitherAction(this));

        if (this.energyOnUse < EnergyPanel.totalCount) {
            this.energyOnUse = EnergyPanel.totalCount;
        }
        if (this.upgraded) this.energyOnUse += 1;
        if (p.hasRelic(ChemicalX.ID)) {
            this.energyOnUse += 2;
            p.getRelic(ChemicalX.ID).flash();
        }

        AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_CULTIST_1B"));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3f));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Settings.BLUE_TEXT_COLOR, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.5F));

        AbstractDungeon.actionManager.addToBottom(new CawAction(p, this.multiDamage, this.damageTypeForTurn, this.freeToPlayOnce, this.energyOnUse));

        if (this.energyOnUse > 0) {
            Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
            while (var3.hasNext()) {
                AbstractMonster mo = (AbstractMonster) var3.next();
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new WeakPower(mo, this.energyOnUse, false), this.energyOnUse, true, AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new VulnerablePower(mo, this.energyOnUse, false), this.energyOnUse, true, AbstractGameAction.AttackEffect.NONE));
            }
        }
    }

    @Override
    public void applyPowers() {
        this.baseDamage = this.damage = this.misc;
        super.applyPowers();
    }

    public AbstractCard makeCopy() {
        return new Caw();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADED_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
