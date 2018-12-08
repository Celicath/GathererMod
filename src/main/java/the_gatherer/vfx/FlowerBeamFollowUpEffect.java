package the_gatherer.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class FlowerBeamFollowUpEffect extends AbstractGameEffect {
	public static final float EFFECT_DUR = 0.6f;
	private static Texture[] imgs = null;
	private boolean init = false;

	float originX, originY;
	private ArrayList<Float> rotations;
	private ArrayList<Integer> frames;
	private ArrayList<Vector2> curPos;
	private ArrayList<Vector2> targets;

	public FlowerBeamFollowUpEffect(float originX, float originY) {
		if (imgs == null) {
			imgs = new Texture[4];
			imgs[0] = ImageMaster.loadImage("images/vfx/petal/petal1.png");
			imgs[1] = ImageMaster.loadImage("images/vfx/petal/petal2.png");
			imgs[2] = ImageMaster.loadImage("images/vfx/petal/petal3.png");
			imgs[3] = ImageMaster.loadImage("images/vfx/petal/petal4.png");
		}
		this.duration = EFFECT_DUR;

		this.originX = originX;
		this.originY = originY;

		rotations = new ArrayList<>();
		frames = new ArrayList<>();
		curPos = new ArrayList<>();
		targets = new ArrayList<>();
	}

	@Override
	public void update() {
		if (!this.init) {
			CardCrawlGame.sound.playV("RELIC_DROP_MAGICAL", 1.5f);

			for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
				if (!m.halfDead && !m.isDying && !m.isEscaping) {
					rotations.add(MathUtils.random(-10.0F, 10.0F));
					frames.add(MathUtils.random(8));
					curPos.add(new Vector2(originX, originY));
					targets.add(new Vector2(m.hb.cX, m.hb.cY));
				}
			}
			this.init = true;
		}

		this.duration -= Gdx.graphics.getDeltaTime();
		float t = (EFFECT_DUR - this.duration) / EFFECT_DUR;
		this.scale = 1.0f + 0.5f * t;

		if (t > 1) {
			this.isDone = true;
		} else {
			for (int i = 0; i < targets.size(); i++) {
				Vector2 v2 = targets.get(i);

				float x = Interpolation.linear.apply(originX, v2.x, t);
				float y = Interpolation.linear.apply(originY, v2.y, t);

				y += 200 * (1 - (0.5f - t) * (0.5f - t) * 4);

				curPos.set(i, new Vector2(x, y));
				frames.set(i, (frames.get(i) + MathUtils.random(2) / 2) % 12);
			}
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setColor(Color.WHITE);
		for (int i = 0; i < curPos.size(); i++) {
			switch (frames.get(i)) {
				case 0:
					this.renderImg(sb, imgs[0], false, false, i);
					break;
				case 1:
					this.renderImg(sb, imgs[1], false, false, i);
					break;
				case 2:
					this.renderImg(sb, imgs[2], false, false, i);
					break;
				case 3:
					this.renderImg(sb, imgs[3], false, false, i);
					break;
				case 4:
					this.renderImg(sb, imgs[2], true, true, i);
					break;
				case 5:
					this.renderImg(sb, imgs[1], true, true, i);
					break;
				case 6:
					this.renderImg(sb, imgs[0], true, true, i);
					break;
				case 7:
					this.renderImg(sb, imgs[1], true, true, i);
					break;
				case 8:
					this.renderImg(sb, imgs[2], true, true, i);
					break;
				case 9:
					this.renderImg(sb, imgs[3], true, true, i);
					break;
				case 10:
					this.renderImg(sb, imgs[2], false, false, i);
					break;
				case 11:
					this.renderImg(sb, imgs[1], false, false, i);
					break;
			}
		}
	}

	private void renderImg(SpriteBatch sb, Texture img, boolean flipH, boolean flipV, int loc) {
		Vector2 v = curPos.get(loc);
		sb.setBlendFunction(770, 1);
		sb.draw(img, v.x, v.y, 16.0F, 16.0F, 32.0F, 32.0F, this.scale, this.scale, rotations.get(loc), 0, 0, 32, 32, flipH, flipV);
		sb.setBlendFunction(770, 771);
	}

	public void dispose() {
	}
}
