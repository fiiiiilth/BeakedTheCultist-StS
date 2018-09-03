package beaked.cards;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.helpers.BaseModTags;
import basemod.helpers.CardTags;
import basemod.interfaces.StartActSubscriber;
import beaked.patches.AbstractCardEnum;
import beaked.powers.AwakenedPlusPower;
import beaked.powers.AwakenedPower;
import beaked.powers.HexingPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AwakenedEyeParticle;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;

public class AwakenedForm extends CustomCard {
    public static final String ID = "beaked:AwakenedForm";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADED_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final int COST = 5;

    public AwakenedForm() {
        super(ID, NAME, null, COST, DESCRIPTION, CardType.POWER, AbstractCardEnum.BEAKED_YELLOW, CardRarity.RARE, CardTarget.SELF);
        this.cost = this.costForTurn = COST - AbstractDungeon.bossCount;
        this.isCostModified = this.cost < COST;
        CardTags.addTags(this, BaseModTags.FORM);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractDungeon.effectList.add(new AwakenedEyeParticle(p.hb.cX + p.hb.width*0.06f, p.hb.cY + p.hb.height*0.2f));
        AbstractDungeon.effectList.add(new AwakenedEyeParticle(p.hb.cX + p.hb.width*0.06f, p.hb.cY + p.hb.height*0.2f));
        AbstractDungeon.effectList.add(new AwakenedEyeParticle(p.hb.cX + p.hb.width*0.06f, p.hb.cY + p.hb.height*0.2f));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new IntenseZoomEffect(p.hb.cX, p.hb.cY, false), 0.05f, true));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(1.5f));

        if (this.upgraded && !p.hasPower(AwakenedPlusPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new AwakenedPlusPower(p)));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new AwakenedPower(p, 1),1));
    }

    public void updateAwakenCost(){
        BaseMod.logger.debug("BOSSES: " + AbstractDungeon.bossCount);
        this.cost = this.costForTurn = AwakenedForm.COST - AbstractDungeon.bossCount;
        this.isCostModified = this.cost < COST;
        this.applyPowers();
    }

    @Override
    public AbstractCard makeCopy() {
        return new AwakenedForm();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.rawDescription = DESCRIPTION + UPGRADED_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
