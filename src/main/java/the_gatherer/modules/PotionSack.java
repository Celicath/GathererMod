package the_gatherer.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import the_gatherer.GathererMod;
import the_gatherer.cards.Glitched;
import the_gatherer.potions.SackPotion;
import the_gatherer.powers.SackPotionPotencyPower;

import java.util.ArrayList;

import static the_gatherer.GathererMod.logger;
import static the_gatherer.GathererMod.potionSackPopupFlipped;

public class PotionSack {
	private static UIStrings uiStrings = null;
	public static String[] TEXT = null;
	private static Texture panel = null;
	public ArrayList<AbstractPotion> potions = null;

	private Hitbox hb;
	private float flashRedTimer = 0.0f;
	public PotionSackPopUp potionUi;
	private boolean init = false;
	public boolean show = false;

	private static final int width = 240;
	private static final int height = 100;
	private static final float above = 200.0f;

	private int moveState = 0;
	private float dx;
	private float dy;
	private float startx;
	private float starty;

	public static InputAction[] selectPotionActions = new InputAction[3];

	public static int potionPotency = 0;

	public PotionSack() {
		this.potionUi = new PotionSackPopUp();
		hb = new Hitbox(width * Settings.scale, height * Settings.scale);
		uiStrings = CardCrawlGame.languagePack.getUIString("Gatherer:PotionSack");
		TEXT = uiStrings.TEXT;
		loadImage();
		loadKeySettings();
	}

	public void loadKeySettings() {
		for (int i = 0; i < selectPotionActions.length; i++) {
			selectPotionActions[i] = new InputAction(GathererMod.potionSackKeys[i]);
		}
	}

	private static void loadImage() {
		if (panel == null)
			panel = new Texture("GathererMod/img/PotionSack.png");
	}

	public void update() {
		if (!init) {
			hb.move(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY + above * Settings.scale);

			potions = new ArrayList<>();
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

		this.potionUi.update();

		hb.update();
		AbstractPotion openP = null;
		boolean keyboard = false;
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
						openP = p;
					}
				} else {
					p.scale = MathHelper.scaleLerpSnap(p.scale, Settings.scale);
				}
			}
		}

		for (int i = 0; i < selectPotionActions.length; i++) {
			if (potions != null && !(potions.get(i) instanceof PotionSlot) && selectPotionActions[i].isJustPressed()) {
				openP = potions.get(i);
				keyboard = true;
			}
		}
		if (moveState == 0 && openP != null) {
			this.potionUi.open(openP.slot, openP, keyboard);
		}

		// dragging
		if (InputHelper.justClickedLeft) {
			if (hb.hovered && this.potionUi.isHidden) {
				dx = hb.cX - InputHelper.mX;
				dy = hb.cY - InputHelper.mY;
				moveState = 1;
				startx = InputHelper.mX;
				starty = InputHelper.mY;
			}
		}
		if (InputHelper.justClickedRight && hb.hovered) {
			potionSackPopupFlipped = !potionSackPopupFlipped;
			GathererMod.saveConfig();
			potionUi.setHitboxPosition();
		}
		if (moveState > 0) {
			if (InputHelper.justReleasedClickLeft) {
				moveState = 0;
			} else {
				float x = Math.min(Math.max(InputHelper.mX + dx, 0.04f * Settings.WIDTH), 0.6f * Settings.WIDTH);
				float y = Math.min(Math.max(InputHelper.mY + dy, 0.3f * Settings.HEIGHT), 0.8f * Settings.HEIGHT);

				if ((startx - InputHelper.mX) * (startx - InputHelper.mX) + (starty - InputHelper.mY) * (starty - InputHelper.mY) > 64) {
					moveState = 2;
				}

				if (moveState == 2) {
					hb.move(x, y);
					for (int i = 0; i < potions.size(); i++) {
						movePotion(i, potions.get(i));
					}
				}
			}
		}

		int potency = 0;
		SackPotionPotencyPower ubp = null;
		if (AbstractDungeon.player != null) {
			ubp = (SackPotionPotencyPower) AbstractDungeon.player.getPower(SackPotionPotencyPower.POWER_ID);
			if (ubp != null) {
				potency = ubp.amount;
				for (AbstractCard c : AbstractDungeon.player.hand.group) {
					if (c instanceof Glitched) {
						potency = 0;
						break;
					}
				}
			}
		}
		if (potency != potionPotency) {
			potionPotency = potency;
			if (ubp != null) {
				ubp.activated = (potency != 0);
				ubp.updateDescription();
			}
			for (AbstractPotion p : potions) {
				if (p instanceof SackPotion) {
					((SackPotion) p).updateDescription();
				}
			}
		}
	}

	public void render(SpriteBatch sb) {
		if (!init || !show || potions == null) return;
		float r = 0.0f;
		if (this.flashRedTimer != 0.0F) {
			r += this.flashRedTimer / 2.0f;
		}
		if (this.hb.hovered) {
			sb.setColor(new Color(1.0F, 1.0F * (1 - 0.4F * r), 1.0F * (1 - 0.4F * r), 1.0F));
		} else {
			sb.setColor(new Color(0.6F + 0.4f * r, 0.6F, 0.6F, 0.8F + 0.2F * r));
		}

		sb.draw(panel, hb.x, hb.y, 0, 0, width, height, Settings.scale, Settings.scale, 0.0F, 0, 0, width, height, false, potionSackPopupFlipped);

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

		for (int i = 0; i < potions.size(); i++) {
			AbstractPotion p = potions.get(i);
			if (!(p instanceof PotionSlot)) {
				float textSpacing = 35.0F * Settings.scale;
				float textY = p.hb.cY + (potionSackPopupFlipped ? -textSpacing : textSpacing);

				String shortcut = selectPotionActions[i].getKeyString();
				if (shortcut.length() > 3) shortcut = shortcut.substring(0, 3);

				FontHelper.renderFontCentered(
						sb,
						FontHelper.topPanelAmountFont,
						shortcut,
						p.posX,
						textY,
						Settings.CREAM_COLOR);
			}
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

	public void setPotion(int index, AbstractPotion potion) {
		this.potions.set(index, potion);
		setPotionPosition(index, potion);
		potion.flash();
		AbstractPotion.playPotionSound();
	}

	public void removePotion(int slot) {
		potions.set(slot, newPotionSlot(slot));
	}

	public void removeAllPotions() {
		if (potions != null)
			for (int i = 0; i < potions.size(); i++)
				removePotion(i);
	}

	private PotionSlot newPotionSlot(int slot) {
		PotionSlot ps = new PotionSlot(slot);
		setPotionPosition(slot, ps);
		return ps;
	}

	private void flashRed() {
		this.flashRedTimer = 1.0F;
	}

	private void setPotionPosition(int index, AbstractPotion potion) {
		potion.setAsObtained(index);
		movePotion(index, potion);
	}

	private void movePotion(int index, AbstractPotion potion) {
		float x = hb.cX + (index - 1) * Settings.POTION_W;
		float y = hb.cY;
		potion.move(x, y);
		potion.hb.move(x, y);
	}
}
