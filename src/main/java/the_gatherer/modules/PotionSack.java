package the_gatherer.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;

import java.util.ArrayList;

import static the_gatherer.GathererMod.logger;

public class PotionSack {
	private static UIStrings uiStrings = null;
	public static String[] TEXT = null;
	public static Texture panel = null;
	public ArrayList<AbstractPotion> potions = null;

	public Hitbox hb;
	float flashRedTimer = 0.0f;
	public PotionSackPopUp potionUi;
	boolean init = false;
	public boolean show = false;

	private final int width = 240;
	private final int height = 100;
	private final float above = 200.0f;

	public PotionSack() {
		this.potionUi = new PotionSackPopUp();
		hb = new Hitbox(width * Settings.scale, height * Settings.scale);
		uiStrings = CardCrawlGame.languagePack.getUIString("Gatherer:PotionSack");
		TEXT = uiStrings.TEXT;
		loadImage();
	}

	public static void loadImage() {
		if (panel == null)
			panel = new Texture("img/PotionSack.png");
	}

	public void update() {
		if (!init) {
			hb.move(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY + above * Settings.scale);

			potions = new ArrayList();
			for (int i = 0; i < 3; i++) {
				potions.add(newPotionSlot(i));
			}

			init = true;
		}
		if (this.flashRedTimer != 0.0F) {
			this.flashRedTimer -= Gdx.graphics.getDeltaTime();
			if (this.flashRedTimer < 0.0F) {
				this.flashRedTimer = 0.0F;
			}
		}
		hb.update();
		for (AbstractPotion p : potions) {
			p.update();
			if (!(p instanceof PotionSlot)) {
				if (p.hb.justHovered) {
					if (MathUtils.randomBoolean()) {
						CardCrawlGame.sound.play("POTION_1", 0.1F);
					} else {
						CardCrawlGame.sound.play("POTION_3", 0.1F);
					}
				}
				if (p.hb.hovered) {
					p.scale = Settings.scale * 1.4F;
					if (AbstractDungeon.player.hoveredCard == null && InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed()) {
						CInputActionSet.select.unpress();
						InputHelper.justClickedLeft = false;
						this.potionUi.open(p.slot, p);
					}
				} else {
					p.scale = MathHelper.scaleLerpSnap(p.scale, Settings.scale);
				}
			}
		}
		this.potionUi.update();
	}

	public void render(SpriteBatch sb) {
		if (!init || !show || potions == null) return;
		if (this.hb.hovered) {
			sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
		} else {
			sb.setColor(new Color(0.6F, 0.6F, 0.6F, 0.8F));
		}
		sb.draw(panel, hb.cX - hb.width, hb.cY - hb.height, width, height, width, height, Settings.scale, Settings.scale, 0.0F, 0, 0, width, height, false, false);

		boolean potion_hovered = false;
		for (AbstractPotion p : potions) {
			p.render(sb);
			if (p.hb.hovered && !(p instanceof PotionSlot)) {
				TipHelper.renderGenericTip((float) InputHelper.mX + 50.0F * Settings.scale, (float) InputHelper.mY, p.name, p.description);
				potion_hovered = true;
			}
		}
		if (this.hb.hovered && !potion_hovered) {
			TipHelper.renderGenericTip((float) InputHelper.mX + 50.0F * Settings.scale, (float) InputHelper.mY, TEXT[1], TEXT[0]);
		}
		hb.render(sb);
	}

	public boolean addPotion(AbstractPotion potion) {
		show = true;
		int index = 0;
		for (AbstractPotion p : this.potions) {
			if ((p instanceof PotionSlot)) {
				break;
			}
			index++;
		}
		if (index < this.potions.size()) {
			this.potions.set(index, potion);
			setPotionPosition(index, potion);
			potion.flash();
			AbstractPotion.playPotionSound();
			return true;
		} else {
			flashRed();
			logger.info("Potion Sack is full");
			return false;
		}
	}

	public void removePotion(int slot) {
		potions.set(slot, newPotionSlot(slot));
	}

	public void removeAllPotions() {
		if (potions != null)
			for (int i = 0; i < potions.size(); i++)
				removePotion(i);
	}

	PotionSlot newPotionSlot(int slot) {
		PotionSlot ps = new PotionSlot(slot);
		setPotionPosition(slot, ps);
		return ps;
	}

	public void flashRed() {
		this.flashRedTimer = 1.0F;
	}

	private void setPotionPosition(int index, AbstractPotion potion) {
		potion.setAsObtained(index);
		float x = hb.cX + (index - 1) * Settings.POTION_W;
		float y = hb.cY;
		potion.move(x, y);
		potion.hb.move(x, y);
	}
}
