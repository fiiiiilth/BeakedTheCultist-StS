package beaked.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import beaked.Beaked;
import beaked.cards.fakeCards.BackButtonCard;
import beaked.cards.fakeCards.CharacterSelectCard;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.SuperRareRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;

import java.util.ArrayList;

public class SacredDeck extends CustomRelic implements SuperRareRelic{
    public static final String ID = "beaked:SacredDeck";
    public boolean isChoosingClass = false;
    public boolean isChoosingCard = false;

    public SacredDeck() {
        super(ID, new Texture("beaked_img/relics/" + "SacredDeck" + ".png"),
                new Texture("beaked_img/relics/outline/" + "SacredDeck" + ".png"),
                RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public void onEquip() {
        flash();

        isChoosingCard = false;
        isChoosingClass = true;
        openCharSelectMenu();
    }

    public void openCharSelectMenu(){
        isChoosingClass = true;
        isChoosingCard = false;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard.CardColor color : AbstractCard.CardColor.values()) {
            group.addToTop(new CharacterSelectCard(color));
        }

        AbstractDungeon.gridSelectScreen.open(group, 1, "Choose a card color to view.", false);
    }

    public void openCardSelectMenu(CharacterSelectCard selected){
        isChoosingClass = false;
        isChoosingCard = true;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        ArrayList<AbstractCard> cards = CardLibrary.getCardList(CardLibrary.LibraryType.valueOf(selected.cardColor.name()));

        // add ALL cards
        for (AbstractCard card:cards){
            group.addToTop(card.makeCopy());
        }
        group.sortByType(true);
        group.sortByRarityPlusStatusCardType(true);

        // add back buttons to the top and bottom for convenience
        group.addToTop(new BackButtonCard());
        group.addToBottom(new BackButtonCard());
        AbstractDungeon.gridSelectScreen.open(group, 1, "Choose a card to obtain.", false);
    }

    @Override
    public void update()
    {
        super.update();

        if (isChoosingClass && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            CharacterSelectCard selected = (CharacterSelectCard) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            openCardSelectMenu(selected);
        }
        else if (isChoosingCard && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){
            isChoosingCard = false;
            AbstractCard selected = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            if (selected instanceof BackButtonCard){
                openCharSelectMenu();
            }
            else {
                UnlockTracker.markCardAsSeen(selected.cardID);
                AbstractDungeon.effectsQueue.add(new FastCardObtainEffect(selected, selected.current_x, selected.current_y));
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
