package the_gatherer.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import the_gatherer.GathererMod;
import the_gatherer.actions.ExcessPotionHandleAction;
import the_gatherer.potions.SackPotion;

import java.util.ArrayList;

import static the_gatherer.modules.PotionSackPopUp.*;

public class ExcessPotionHandleScreen extends HandCardSelectScreen {

	private float yScale;

	private HandCardSelectScreen originalScreen = null;

	private String message;
	private ArrayList<SackPotion> potions = new ArrayList<>();
	private ArrayList<Hitbox> useButtons = new ArrayList<>();
	private ArrayList<Hitbox> discardButtons = new ArrayList<>();

	private Texture image;

	private final int WIDTH = 160;
	private final int HEIGHT = 84;

	private final int H_WIDTH = 142;
	private final int H_HEIGHT = 66;

	public Texture highlight;

	public ExcessPotionHandleScreen() {
		Pixmap px = new Pixmap(H_WIDTH, H_HEIGHT, Pixmap.Format.RGBA8888);
		for (int x = 0; x < H_WIDTH; x++) {
			for (int y = 0; y < H_HEIGHT; y++) {
				int t = Math.min(Math.min(x, H_WIDTH - 1 - x), Math.min(y, H_HEIGHT - 1 - y));
				if (t == 8)
					px.setColor(0.2f, 0.1f, 0.2f, 1f);
				else
					px.setColor(0.8f, 0.9f, 0.9f, 0.75f);

				// draw pixel
				px.drawPixel(x, y);
			}
		}
		highlight = new Texture(px);
	}

	public void open(String message) {
		originalScreen = AbstractDungeon.handCardSelectScreen;
		if (originalScreen == null) {
			GathererMod.logger.debug("originalScreen is NULL!");
		}
		AbstractDungeon.handCardSelectScreen = this;
		AbstractDungeon.screen = AbstractDungeon.CurrentScreen.HAND_SELECT;// 530
		AbstractDungeon.overlayMenu.proceedButton.hide();
		AbstractDungeon.overlayMenu.cancelButton.hide();

		AbstractDungeon.overlayMenu.showBlackScreen(0.4F);// 531

		this.message = message;

		if (image == null)
			image = new Texture("GathererMod/img/ui/Button.png");

		potions.clear();
		useButtons.clear();
		discardButtons.clear();

		for (SackPotion sp : ExcessPotionHandleAction.excessPotions) {
			potions.add(sp);
			useButtons.add(new Hitbox(WIDTH * Settings.scale, HEIGHT * Settings.scale));
			discardButtons.add(new Hitbox(WIDTH * Settings.scale, HEIGHT * Settings.scale));
		}
		ExcessPotionHandleAction.excessPotions.clear();
		this.yScale = 0.0f;

		setPotions();
	}

	void setPotions() {
		float baseXPos = Settings.WIDTH / 2f;
		float potionXPos = baseXPos - 300.0f * Settings.scale;
		float useXPos = baseXPos + 40.0f * Settings.scale;
		float discardXPos = baseXPos + 240.0f * Settings.scale;
		float baseYPos = Settings.HEIGHT - (450f * Settings.scale);

		int index = 0;
		for (SackPotion sp : potions) {
			if (sp != null) {
				Hitbox hb1 = useButtons.get(index);
				Hitbox hb2 = discardButtons.get(index);
				float yPos = baseYPos - (100 * Settings.scale) * index * yScale;

				sp.move(potionXPos, yPos);
				hb1.move(useXPos, yPos);
				hb2.move(discardXPos, yPos);
			}
			index++;
		}
	}

	@Override
	public void reopen() {
		AbstractDungeon.overlayMenu.showBlackScreen(0.4F);// 531
	}

	public void close() {
		if (originalScreen != null) {
			AbstractDungeon.handCardSelectScreen = originalScreen;
		}
	}

	public void update() {
		for (int i = 0; i < potions.size(); i++) {
			SackPotion potion = potions.get(i);
			if (potion != null) {
				Hitbox[] hbs = new Hitbox[]{useButtons.get(i), discardButtons.get(i)};
				for (Hitbox hb : hbs) {
					hb.update();
					if (hb.justHovered)
						CardCrawlGame.sound.play("UI_HOVER");
					if (InputHelper.justClickedLeft && hb.hovered && !AbstractDungeon.isScreenUp) {
						hb.clickStarted = true;
						CardCrawlGame.sound.play("UI_CLICK_1");
					}
					Hitbox hb2 = discardButtons.get(i);
					hb2.update();
					if (hb2.justHovered)
						CardCrawlGame.sound.play("UI_HOVER");
				}
				potion.update();
			}

		}
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setColor(Color.WHITE);

		FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, this.message, Settings.WIDTH / 2.0f, Settings.HEIGHT - 180.0F * Settings.scale, Settings.CREAM_COLOR);

		yScale = MathHelper.scaleLerpSnap(yScale, 1.0f);

		for (int i = 0; i < potions.size(); i++) {
			renderPotionStrip(i, sb);
		}
	}

	private void renderPotionStrip(int index, SpriteBatch sb) {
		SackPotion potion = potions.get(index);
		if (potion == null)
			return;

		float textXOffset = 30f * Settings.scale;
		float textYOffset = 0f * Settings.scale;

		Hitbox[] hbs = new Hitbox[]{useButtons.get(index), discardButtons.get(index)};
		potion.render(sb);
		if (potion.hb.hovered) {
			TipHelper.queuePowerTips((float) InputHelper.mX + 50.0F * Settings.scale, (float) InputHelper.mY, potion.tips);
		}

		// Renders the name of the potion
		FontHelper.renderFontLeft(sb, FontHelper.tipBodyFont, potion.name, potion.posX + textXOffset, potion.posY + textYOffset, Color.WHITE);

		int i = 0;
		for (Hitbox hb : hbs) {
			float y = hb.y;
			if (hb.clickStarted && !AbstractDungeon.isScreenUp) {
				y -= 2.0F * Settings.scale;
			} else if (hb.hovered && !AbstractDungeon.isScreenUp) {
				y += 2.0F * Settings.scale;
			}
			sb.draw(image, hb.x, y, 0, 0, WIDTH, HEIGHT, Settings.scale, Settings.scale, 0.0F, 0, 0, WIDTH, HEIGHT, false, false);

			if (hb.hovered) {
				sb.draw(highlight, hb.x + (WIDTH - H_WIDTH) / 2.0f * Settings.scale, y + (HEIGHT - H_HEIGHT) / 2.0f * Settings.scale, 0, 0, H_WIDTH, H_HEIGHT, Settings.scale, Settings.scale, 0.0F, 0, 0, H_WIDTH, H_HEIGHT, false, false);
			}
			hb.render(sb);

			if (i == 0) {
				String label = DRINK_LABEL;
				if (potion.isThrown) {
					label = THROW_LABEL;
				}
				FontHelper.renderFontCentered(sb, FontHelper.topPanelAmountFont, label, hbs[0].cX, y, Color.WHITE);
			} else {
				FontHelper.renderFontCentered(sb, FontHelper.topPanelAmountFont, DISCARD_LABEL, hbs[1].cX, y, Color.WHITE);
			}
			i++;
		}
	}
}
