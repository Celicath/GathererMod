package the_gatherer.modules;

import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

public class EnergyOrbGatherer extends CustomEnergyOrb {
	private static final float ORB_IMG_SCALE = 1.15F * Settings.scale;

	public EnergyOrbGatherer(String[] orbTexturePaths, String orbVfxPath) {
		super(orbTexturePaths, orbVfxPath, null);
	}

	@Override
	public void updateOrb(int orbCount) {
		if (orbCount == 0) {
			this.angles[0] += Gdx.graphics.getDeltaTime() * 5.0F;
			this.angles[3] += Gdx.graphics.getDeltaTime() * -8.0F;
//			this.angles[3] += Gdx.graphics.getDeltaTime() * 8.0F;
		} else {
			this.angles[0] += Gdx.graphics.getDeltaTime() * 20.0F;
			this.angles[3] += Gdx.graphics.getDeltaTime() * -40.0F;
			this.angles[1] += Gdx.graphics.getDeltaTime() * 40.0F;
		}
	}

	@Override
	public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
		if (enabled) {
			for (int i = 0; i < this.energyLayers.length; ++i) {
				sb.draw(this.energyLayers[i], current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
			}
		} else {
			for (int i = 0; i < this.noEnergyLayers.length; ++i) {
				sb.draw(this.noEnergyLayers[i], current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
			}
		}

		sb.draw(this.baseLayer, current_x - 128.0F, current_y - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F, 0, 0, 256, 256, false, false);
	}
}
