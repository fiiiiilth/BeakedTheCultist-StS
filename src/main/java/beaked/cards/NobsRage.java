package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.patches.AbstractCardEnum;
import beaked.powers.NobsRagePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static beaked.patches.CardTagsEnum.ELITE_CARD;

public class NobsRage extends CustomCard {
    public static final String ID = "beaked:NobsRage";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final int COST = 0;
    public static final int POWER_STACKS = 2;
    public static final int UPGRADE_POWER_STACKS = 1;

    public NobsRage() {
        super(ID, NAME, null, COST, DESCRIPTION, CardType.POWER, AbstractCardEnum.BEAKED_YELLOW, CardRarity.SPECIAL, CardTarget.SELF);

        this.magicNumber = this.baseMagicNumber = POWER_STACKS;
        this.tags.add(ELITE_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NobsRagePower(p, magicNumber), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new NobsRage();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_POWER_STACKS);
        }
    }
}
