package the_gatherer.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.QuestionCard;
import the_gatherer.GathererMod;

import java.nio.charset.StandardCharsets;

public class ExplorersPath extends CustomRelic {
	private static final String RelicID = "ExplorersPath";
	public static final String ID = GathererMod.makeID(RelicID);

	public static final JsonObject pickRatioData;

	static {
		String jsonString = Gdx.files.internal("GathererMod/data/PickRatio-" + "Gatherer" + ".json").readString(
				String.valueOf(StandardCharsets.UTF_8));
		JsonParser parser = new JsonParser();
		pickRatioData = parser.parse(jsonString).getAsJsonObject();
	}

	public ExplorersPath() {
		super(ID, new Texture(GathererMod.GetRelicPath(RelicID)),
				RelicTier.BOSS, LandingSound.FLAT);
		this.counter = 0;
		refreshDescription();
	}

	@Override
	public void onEquip() {
		for (int i = 0; i < 2; i++)
			chooseCorrectly();
	}


	@Override
	public void onUnequip() {
		if (counter > 0)
			--AbstractDungeon.player.energy.energyMaster;
	}

	@Override
	public void onVictory() {
		if (this.counter > 0) {
			setCounter(this.counter - 1);
			if (counter == 0)
				--AbstractDungeon.player.energy.energyMaster;
			refreshDescription();
		}
	}

	public void chooseCorrectly() {
		this.flash();
		if (counter == 0)
			++AbstractDungeon.player.energy.energyMaster;
		setCounter(counter + 1);
		refreshDescription();
	}

	private void refreshDescription() {
		this.description = getUpdatedDescription();
		this.tips.clear();
		this.tips.add(new PowerTip(this.name, this.description));
		this.tips.add(new PowerTip(DESCRIPTIONS[1], DESCRIPTIONS[2] + FontHelper.colorString(new QuestionCard().name, "y") + DESCRIPTIONS[3]));
		this.initializeTips();
	}

	// returns ratio gets used by Explorers Path.
	public static double getRatio(String cardID) {
		JsonElement element = pickRatioData.get(cardID);
		if (element == null) return 100;
		try {
			double r = element.getAsFloat();
			if (r <= 0.01f) {
				// if ratio is too low, treat it as ??. ?? has lower priority over cards with actual values.
				r = r + 99.99 + r * 0.4;
			} else if (r >= 99.99f) {
				// make it so r <= 0.01f is always lower than r >= 99.99f
				r = r * 0.4 + 60.0;
			}
			return r;
		} catch (Exception e) {
			GathererMod.logger.error("Exception occurred when getting pick ratio of " + cardID, e);
		}
		return 100;
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + new QuestionCard().name;
	}

	@Override
	public AbstractRelic makeCopy() {
		return new ExplorersPath();
	}
}
