package the_gatherer.modules;

import basemod.IUIElement;
import basemod.ModPanel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.function.Consumer;

public class ModFixedButton implements IUIElement {
	private static final float FIXED_X_EXTEND = 12.0f;

	private Consumer<ModFixedButton> click;
	public Hitbox hb;
	private Texture texture;
	private float x;
	private float y;
	private float w;
	public float h;

	public ModPanel parent;

	public ModFixedButton(float xPos, float yPos, ModPanel p, Consumer<ModFixedButton> c) {
		this(xPos, yPos, ImageMaster.loadImage("img/BlankButton.png"), p, c);
	}

	public ModFixedButton(float xPos, float yPos, Texture tex, ModPanel p, Consumer<ModFixedButton> c) {
		texture = tex;
		x = xPos * Settings.scale;
		y = yPos * Settings.scale;
		w = texture.getWidth() * 0.5f;
		h = texture.getHeight() * 0.5f;
		hb = new Hitbox(x, y, w * Settings.scale, h * Settings.scale);

		parent = p;
		click = c;
	}

	public void wrapHitboxToText(String text, float lineWidth, float lineSpacing, BitmapFont font) {
		float tWidth = FontHelper.getSmartWidth(font, text, lineWidth, lineSpacing);
		hb.width = tWidth + h * Settings.scale + FIXED_X_EXTEND;
	}

	public void render(SpriteBatch sb) {
		if (this.hb.hovered) {
			sb.setColor(Color.CYAN.cpy());
		} else {
			sb.setColor(Color.WHITE.cpy());
		}
		sb.draw(texture, x, y, w * Settings.scale, h * Settings.scale);
		hb.render(sb);
	}

	public void update() {
		hb.update();

		if (hb.justHovered) {
			CardCrawlGame.sound.playV("UI_HOVER", 0.75f);
		}

		if (hb.hovered) {
			if (InputHelper.justClickedLeft) {
				CardCrawlGame.sound.playA("UI_CLICK_1", -0.1f);
				hb.clickStarted = true;
			}
		}

		if (hb.clicked) {
			hb.clicked = false;
			onClick();
		}
	}

	private void onClick() {
		click.accept(this);
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