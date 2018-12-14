package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import replayTheSpire.patches.CardFieldStuff;

public class Resilience extends CustomCard {
    public static final String ID = "beaked:Resilience";
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static int COST = 1;
    public static int UPGRADED_COST = 0;
    public static int STRENGTH = -3;
    public static int ARTIFACT = 2;
    public static int HP_LOSS = 4;

    public Resilience() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.COMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = HP_LOSS;
        if (Beaked.isReplayLoaded) {
            this.tags.add(CardFieldStuff.CHAOS_NEGATIVE_MAGIC); // higher magic number is worse
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, STRENGTH), STRENGTH));
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p,p,this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ArtifactPower(p, ARTIFACT), ARTIFACT));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Resilience();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADED_COST);
        }
    }
}
