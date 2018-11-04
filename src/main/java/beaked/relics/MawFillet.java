package beaked.relics;

import basemod.abstracts.CustomRelic;
import beaked.Beaked;
import beaked.interfaces.IPostMapGenerationRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import java.util.ArrayList;

public class MawFillet extends CustomRelic implements IPostMapGenerationRelic {
    public static final String ID = "beaked:MawFillet";
    public static final ArrayList<MapRoomNode> changedNodes = new ArrayList<>();

    public MawFillet() {
        super(ID, new Texture("beaked_img/relics/" + Beaked.getActualID(ID) + ".png"),
                new Texture("beaked_img/relics/outline/" + Beaked.getActualID(ID) + ".png"),
                RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public void postMapGeneration(){
        changedNodes.clear();
        for (ArrayList<MapRoomNode> nodes : AbstractDungeon.map){
            for (MapRoomNode node : nodes){
                if (node.getRoom() instanceof ShopRoom){
                    node.setRoom(new MonsterRoomElite());
                    changedNodes.add(node);
                }
            }
        }
    }

    @Override
    public void onEquip() {
        final EnergyManager energy = AbstractDungeon.player.energy;
        ++energy.energyMaster;
        for (ArrayList<MapRoomNode> nodes : AbstractDungeon.map){
            for (MapRoomNode node : nodes){
                if (node.getRoom() instanceof ShopRoom){
                    node.setRoom(new MonsterRoomElite());
                    changedNodes.add(node);
                }
            }
        }
    }

    @Override
    public void onUnequip() {
        final EnergyManager energy = AbstractDungeon.player.energy;
        --energy.energyMaster;
        for (MapRoomNode node : changedNodes){
            node.setRoom(new ShopRoom());
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
