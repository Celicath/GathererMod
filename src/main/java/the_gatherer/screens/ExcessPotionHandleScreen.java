package the_gatherer.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import the_gatherer.GathererMod;
import the_gatherer.actions.ExcessPotionHandleAction;
import the_gatherer.modules.PotionSackPopUp;
import the_gatherer.potions.SackPotion;

import java.util.ArrayList;

import static the_gatherer.modules.PotionSackPopUp.*;

public class ExcessPotionHandleScreen extends HandCardSelectScreen {
	public final String[] TEXT;

	private float stripHeight;

	private HandCardSelectScreen originalScreen = null;

	private String message;
	public static ArrayList<SackPotion> potions = new ArrayList<>();
	private ArrayList<Hitbox> extendedPotionTootip = new ArrayList<>();
	private ArrayList<Hitbox> useButtons = new ArrayList<>();
	private ArrayList<Hitbox> discardButtons = new ArrayList<>();

	private Texture image;

	private final int WIDTH = 160;
	private final int HEIGHT = 84;

	private final int H_WIDTH = 142;
	private final int H_HEIGHT = 66;

	public Texture highlight;

	public ExcessPotionHandleScreen() {
		TEXT = CardCrawlGame.languagePack.getUIString("Gatherer:ExcessPotionHandleAction").TEXT;
		Pixmap px = new Pixmap(H_WIDTH, H_HEIGHT, Pixmap.Format.RGBA8888);
		for (int x = 0; x < H_WIDTH; x++) {
			for (int y = 0; y < H_HEIGHT; y++) {
				int t = Math.min(Math.min(x, H_WIDTH - 1 - x), Math.min(y, H_HEIGHT - 1 - y));
				if (t < 8)
					px.setColor(0.7f, 0.8f, 0.7f, 0.75f);
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

	public void open() {
		if (potions.isEmpty())
			return;

		if (AbstractDungeon.handCardSelectScreen != this) {
			originalScreen = AbstractDungeon.handCardSelectScreen;
		}
		AbstractDungeon.handCardSelectScreen = this;
		AbstractDungeon.screen = AbstractDungeon.CurrentScreen.HAND_SELECT;
		AbstractDungeon.overlayMenu.proceedButton.hide();
		AbstractDungeon.overlayMenu.cancelButton.hide();

		AbstractDungeon.overlayMenu.showBlackScreen(0.4F);

		this.message = (potions.size() > 1 ? TEXT[1] : TEXT[0]) + TEXT[2];

		if (image == null)
			image = new Texture("GathererMod/img/ui/Button.png");

		extendedPotionTootip.clear();
		useButtons.clear();
		discardButtons.clear();
		for (SackPotion sp : potions) {
			extendedPotionTootip.add(new Hitbox(400.0f * Settings.scale, 64.0f * Settings.scale));
			useButtons.add(new Hitbox(WIDTH * Settings.scale, HEIGHT * Settings.scale));
			discardButtons.add(new Hitbox(WIDTH * Settings.scale, HEIGHT * Settings.scale));
		}
		this.stripHeight = 100.0f;

		setPotions();
	}

	void setPotions() {
		float baseXPos = Settings.WIDTH / 2f;
		float potionXPos = baseXPos - 300.0f * Settings.scale;
		float extendedXPos = potionXPos + 64.0f * Settings.scale;
		float useXPos = baseXPos + 60.0f * Settings.scale;
		float discardXPos = baseXPos + 240.0f * Settings.scale;
		float baseYPos = Settings.HEIGHT - (450f * Settings.scale);

		int index = 0;
		for (SackPotion sp : potions) {
			if (sp != null) {
				Hitbox hb1 = useButtons.get(index);
				Hitbox hb2 = discardButtons.get(index);
				float yPos = baseYPos - (stripHeight * Settings.scale) * index;

				sp.move(potionXPos, yPos);
				sp.hb.move(sp.posX, sp.posY);
				hb1.move(useXPos, yPos);
				hb2.move(discardXPos, yPos);

				extendedPotionTootip.get(index).move(extendedXPos, yPos);
			}
			index++;
		}
	}

	@Override
	public void reopen() {
		AbstractDungeon.overlayMenu.showBlackScreen(0.4F);// 531
	}

	public void close() {
		if (AbstractDungeon.handCardSelectScreen == this) {
			AbstractDungeon.handCardSelectScreen = originalScreen;
		}
	}

	public void update() {
		SackPotion potionUse = null;
		boolean potionDiscard = false;
		for (int i = 0; i < potions.size(); i++) {
			SackPotion potion = potions.get(i);
			if (potion != null) {
				extendedPotionTootip.get(i).update();
				Hitbox[] hbs = new Hitbox[]{useButtons.get(i), discardButtons.get(i)};
				boolean discard = false;
				for (Hitbox hb : hbs) {
					hb.update();
					if (hb.justHovered)
						CardCrawlGame.sound.play("UI_HOVER");
					if (InputHelper.justClickedLeft && hb.hovered && !AbstractDungeon.isScreenUp) {
						hb.clickStarted = true;
						CardCrawlGame.sound.play("UI_CLICK_1");
					} else if (hb.clicked) {
						hb.clicked = false;
						potionUse = potion;
						potionDiscard = discard;
					}
					discard = true;
				}
				potion.update();
			}

		}
		if (potionUse != null) {
			if (!potionDiscard) {
				AbstractCreature target = null;
				ArrayList<AbstractMonster> tmp = new ArrayList<>();
				for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
					if (!m.halfDead && !m.isDying && !m.isEscaping) {
						tmp.add(m);
					}
				}
				if (tmp.size() > 0) {
					target = tmp.get(MathUtils.random(0, tmp.size() - 1));
				}
				PotionSackPopUp.usePotionInSack(target, potionUse);
			}
			potions.remove(potionUse);
			if (!potionDiscard || potions.isEmpty()) {
				close();
				AbstractDungeon.closeCurrentScreen();
				AbstractDungeon.actionManager.addToBottom(new ExcessPotionHandleAction());
			} else {
				open();
			}
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setColor(Color.WHITE);

		FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, this.message, Settings.WIDTH / 2.0f, Settings.HEIGHT - 180.0F * Settings.scale, Settings.CREAM_COLOR);

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
		extendedPotionTootip.get(index).render(sb);
		if (potion.hb.hovered || extendedPotionTootip.get(index).hovered) {
			TipHelper.queuePowerTips((float) InputHelper.mX + 50.0F * Settings.scale, (float) InputHelper.mY, potion.tips);
		}

		// Renders the name of the potion
		FontHelper.renderFontLeft(sb, FontHelper.tipBodyFont, potion.name, potion.posX + textXOffset, potion.posY + textYOffset, Color.WHITE);

		int i = 0;
		for (Hitbox hb : hbs) {
			float y = hb.y;
			float dy = 0;
			if (hb.clickStarted && !AbstractDungeon.isScreenUp) {
				dy = -2.0F * Settings.scale;
			} else if (hb.hovered && !AbstractDungeon.isScreenUp) {
				dy = 2.0F * Settings.scale;
			}
			y += dy;
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
				FontHelper.renderFontCentered(sb, FontHelper.topPanelAmountFont, label, hbs[0].cX, hbs[0].cY + dy, Color.WHITE);
			} else {
				FontHelper.renderFontCentered(sb, FontHelper.topPanelAmountFont, DISCARD_LABEL, hbs[1].cX, hbs[1].cY + dy, Color.WHITE);
			}
			i++;
		}
	}
}
