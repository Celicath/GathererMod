package the_gatherer.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import the_gatherer.GathererMod;

public class ExplorersTrail extends CustomRelic {
	private static final String RelicID = "ExplorersTrail";
	public static final String ID = GathererMod.makeID(RelicID);
	private boolean active = false;

	public ExplorersTrail() {
		super(ID, new Texture(GathererMod.GetRelicPath(RelicID)),
				RelicTier.BOSS, LandingSound.FLAT);
		this.counter = 0;
	}

	@Override
	public void atBattleStartPreDraw() {
		if (this.counter > 0) {
			this.flash();
			active = true;
			++AbstractDungeon.player.energy.energyMaster;
		}
	}

	@Override
	public void onVictory() {
		if (active) {
			this.counter = 0;
			--AbstractDungeon.player.energy.energyMaster;
		}
	}

	@Override
	public String getUpdatedDescription() {
		if (this.counter > 0) {
			return DESCRIPTIONS[0] + DESCRIPTIONS[1];
		} else {
			return DESCRIPTIONS[0];
		}
	}

	@Override
	public AbstractRelic makeCopy() {
		return new ExplorersTrail();
	}
}
