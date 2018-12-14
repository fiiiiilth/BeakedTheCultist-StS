package beaked.cards;

import basemod.abstracts.CustomCard;
import beaked.Beaked;
import beaked.actions.RemoveBuffsAction;
import beaked.patches.AbstractCardEnum;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

import static beaked.patches.CardTagsEnum.ELITE_CARD;

public class SentriesPulse extends CustomCard {
    public static final String ID = "beaked:SentriesPulse";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final int COST = 0;

    public SentriesPulse() {
        super(ID, NAME, "beaked_img/cards/"+ Beaked.getActualID(ID)+".png", COST, DESCRIPTION, CardType.SKILL, AbstractCardEnum.BEAKED_YELLOW, CardRarity.SPECIAL, CardTarget.ENEMY);

        this.tags.add(ELITE_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new ShockWaveEffect(this.hb.cX, this.hb.cY, Color.ROYAL,
                ShockWaveEffect.ShockWaveType.ADDITIVE), 0.1f));
        if(this.upgraded) {
            for(AbstractMonster m2 : AbstractDungeon.getCurrRoom().monsters.monsters) {
                AbstractDungeon.actionManager.addToBottom(new RemoveBuffsAction(m2));
            }
        } else {
            AbstractDungeon.actionManager.addToBottom(new RemoveBuffsAction(m));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SentriesPulse();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.target = CardTarget.ALL_ENEMY;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
