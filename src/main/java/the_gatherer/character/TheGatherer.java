package the_gatherer.character;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import the_gatherer.GathererMod;
import the_gatherer.cards.*;
import the_gatherer.patches.AbstractPlayerEnum;
import the_gatherer.relics.AlchemyBag;

import java.util.ArrayList;

public class TheGatherer extends CustomPlayer {
	public static final int ENERGY_PER_TURN = 3;
	public static final int START_HP = 64;
	public static final int START_GOLD = 160;
	public static final String CLASS_NAME = "The Gatherer";

	public static final String[] orbTextures = {
		"img/character/gatherer/orb/layer1.png",
		"img/character/gatherer/orb/layer2.png",
		"img/character/gatherer/orb/layer3.png",
		"img/character/gatherer/orb/layer4.png",
		"img/character/gatherer/orb/layer5.png",
		"img/character/gatherer/orb/layer6.png",
		"img/character/gatherer/orb/layer1d.png",
		"img/character/gatherer/orb/layer2d.png",
		"img/character/gatherer/orb/layer3d.png",
		"img/character/gatherer/orb/layer4d.png",
		"img/character/gatherer/orb/layer5d.png",
	};

	public TheGatherer(String name, PlayerClass setClass) {
		super(name, setClass, orbTextures, "img/character/gatherer/orb/vfx.png", (String)null, null);

		initializeClass(null, GathererMod.GATHERER_SHOULDER_2,
				GathererMod.GATHERER_SHOULDER_1,
				GathererMod.GATHERER_CORPSE,
				getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));

		this.loadAnimation("img/character/gatherer/idle/skeleton.atlas", "img/character/gatherer/idle/skeleton.json", 1.0f);
		AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
		e.setTime(e.getEndTime() * MathUtils.random());
	}

	public ArrayList<String> getStartingRelics() {
		ArrayList<String> retVal = new ArrayList();
		retVal.add(AlchemyBag.ID);
		return retVal;
	}

	public ArrayList<String> getStartingDeck() {
		ArrayList<String> retVal = new ArrayList<>();
		retVal.add(Strike_Gatherer.ID);
		retVal.add(Strike_Gatherer.ID);
		retVal.add(Strike_Gatherer.ID);
		retVal.add(Strike_Gatherer.ID);
		retVal.add(Strike_Gatherer.ID);
		retVal.add(Defend_Gatherer.ID);
		retVal.add(Defend_Gatherer.ID);
		retVal.add(Defend_Gatherer.ID);
		retVal.add(Defend_Gatherer.ID);
		retVal.add(Defend_Gatherer.ID);
		retVal.add(Centralize.ID);
		retVal.add(SpareBottle.ID);
		retVal.add(FlowerWhip.ID);
		return retVal;
	}
	public CharSelectInfo getLoadout() {
		CharSelectInfo selectInfo = new CharSelectInfo(
			"The Gatherer",
			"A wandering adventurer who gathers alchemy materials. NL Specializes potion and nature power.",
			START_HP,
			START_HP,
			0, //Possibly have 1 orb to represent type
			START_GOLD,
			5,
			this,
			getStartingRelics(),
			getStartingDeck(),
			false);
		return selectInfo;
	}
	@Override
	public String getTitle(PlayerClass playerClass) {
		return CLASS_NAME;
	}
	@Override
	public Color getCardColor() {
		return Color.LIME;
	}
	@Override
	public AbstractCard getStartCardForEvent() {
		return new FlowerWhip();
	}
	@Override
	public Color getCardTrailColor() {
		return Color.LIME;
	}
	@Override
	public int getAscensionMaxHPLoss() {
		return 4;
	}
	@Override
	public BitmapFont getEnergyNumFont() {
		return FontHelper.energyNumFontRed;
	}
	@Override
	public void doCharSelectScreenSelectEffect() {
		CardCrawlGame.sound.playA("POTION_1", MathUtils.random(-0.2f, 0.2f));
		CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
	}
	@Override
	public String getCustomModeCharacterButtonSoundKey() {
		return "POTION_2";
	}
	@Override
	public String getLocalizedCharacterName() {
		return CLASS_NAME;
	}

	@Override
	public AbstractPlayer newInstance() {
		return new TheGatherer(CLASS_NAME, AbstractPlayerEnum.THE_GATHERER);
	}
}
