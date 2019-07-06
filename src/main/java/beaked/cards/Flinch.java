package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.actions.WitherAction;
import beaked.patches.AbstractCardEnum;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.DarkSmokePuffEffect;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import replayTheSpire.patches.CardFieldStuff;

public class Flinch extends AbstractWitherCard {
    public static final String ID = "beaked:Flinch";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 1;
    private static final int BLOCK_AMT = 16;
    private static final int WITHER_MINUS_BLOCK = 2;
    private static final int UPGRADE_PLUS_WITHER = -1;

    public Flinch() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL,
                AbstractCardEnum.BEAKED_YELLOW, CardRarity.COMMON, CardTarget.SELF);

        this.baseMisc = this.misc = BLOCK_AMT;
        this.baseBlock = this.block = misc;
        this.baseMagicNumber = this.magicNumber = WITHER_MINUS_BLOCK;
        this.witherEffect = EXTENDED_DESCRIPTION[0];
        this.linkWitherAmountToMagicNumber = true;
        if (Beaked.isReplayLoaded) {
            this.tags.add(CardFieldStuff.CHAOS_NEGATIVE_MAGIC); // higher magic number is worse
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new WitherAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    }

    @Override
    public void applyPowers() {
        this.baseBlock = this.block = this.misc;
        super.applyPowers();
    }

    public AbstractCard makeCopy() {
        return new Flinch();
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_WITHER);
        }
    }
}
