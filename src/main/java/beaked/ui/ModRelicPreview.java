package beaked.ui;

import basemod.IUIElement;
import basemod.ModButton;
import basemod.ModPanel;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiwordKeywords;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ModRelicPreview implements IUIElement {
    private static final float HB_SHRINK = 14.0f;

    private Consumer<ModButton> click;
    private Hitbox hb;
    private Texture texture;
    private float x;
    private float y;
    private float w;
    private float h;
    public ArrayList<PowerTip> tips = new ArrayList<>();

    public ModPanel parent;

    public ModRelicPreview(float xPos, float yPos, AbstractRelic relic, ModPanel p) {
        texture = relic.img;
        x = xPos* Settings.scale;
        y = yPos*Settings.scale;
        w = texture.getWidth();
        h = texture.getHeight();
        hb = new Hitbox(x+(HB_SHRINK*Settings.scale), y+(HB_SHRINK*Settings.scale), (w-(2*HB_SHRINK))*Settings.scale, (h-(2*HB_SHRINK))*Settings.scale);
        this.tips.add(new PowerTip(relic.name, getRelicTierString(relic.tier) + relic.description));

        parent = p;
    }

    public String getRelicTierString(AbstractRelic.RelicTier tier){
        // not comprehensive obviously, more doing this for super-rares rather than the general case.
        switch (tier){
            case BOSS:
                return "#rBoss #rRelic NL ";
            case SPECIAL:
                return "#bSpecial #bRelic NL ";
            case SHOP:
                return "#yShop #yRelic NL ";
        }
        return "";
    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(texture, x, y, w*Settings.scale, h*Settings.scale);
        hb.render(sb);
        if (hb.hovered) {
            TipHelper.queuePowerTips(InputHelper.mX + 50.0f * Settings.scale, InputHelper.mY + 50.0f * Settings.scale, this.tips);
        }
    }

    public void update() {
        hb.update();
    }

    @Override
    public int renderLayer() {
        return ModPanel.MIDDLE_LAYER;
    }

    @Override
    public int updateOrder() {
        return ModPanel.DEFAULT_UPDATE;
    }
}