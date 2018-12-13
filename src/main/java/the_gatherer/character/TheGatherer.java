package the_gatherer.character;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import the_gatherer.GathererMod;
import the_gatherer.cards.Centralize;
import the_gatherer.cards.Defend_Gatherer;
import the_gatherer.cards.FlowerWhip;
import the_gatherer.cards.Strike_Gatherer;
import the_gatherer.patches.AbstractPlayerEnum;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.relics.AlchemyBag;

import java.util.ArrayList;

public class TheGatherer extends CustomPlayer {
	public static final int ENERGY_PER_TURN = 3;
	public static final int START_HP = 72;
	public static final int START_GOLD = 99;
	public static final String CLASS_NAME = "The Gatherer";

	public static final String[] orbTextures = {
			"GathererMod/img/character/gatherer/orb/layer1.png",
			"GathererMod/img/character/gatherer/orb/layer2.png",
			"GathererMod/img/character/gatherer/orb/layer3.png",
			"GathererMod/img/character/gatherer/orb/layer4.png",
			"GathererMod/img/character/gatherer/orb/layer5.png",
			"GathererMod/img/character/gatherer/orb/layer6.png",
			"GathererMod/img/character/gatherer/orb/layer1d.png",
			"GathererMod/img/character/gatherer/orb/layer2d.png",
			"GathererMod/img/character/gatherer/orb/layer3d.png",
			"GathererMod/img/character/gatherer/orb/layer4d.png",
			"GathererMod/img/character/gatherer/orb/layer5d.png",
	};

	public TheGatherer(String name, PlayerClass setClass) {
		super(name, setClass, orbTextures, "GathererMod/img/character/gatherer/orb/vfx.png", (String) null, null);

		initializeClass(null, GathererMod.GATHERER_SHOULDER_2,
				GathererMod.GATHERER_SHOULDER_1,
				GathererMod.GATHERER_CORPSE,
				getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));

		this.loadAnimation("GathererMod/img/character/gatherer/idle/skeleton.atlas", "GathererMod/img/character/gatherer/idle/skeleton.json", 1.0f);
		AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
		e.setTime(e.getEndTime() * MathUtils.random());
	}

	public ArrayList<String> getStartingRelics() {
		ArrayList<String> retVal = new ArrayList<>();
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
		retVal.add(FlowerWhip.ID);
		return retVal;
	}

	public CharSelectInfo getLoadout() {
		return new CharSelectInfo(
				"The Gatherer",
				"A wandering adventurer who gathers alchemy materials. NL Specialized in potion and nature power.",
				START_HP,
				START_HP,
				0,
				START_GOLD,
				5,
				this,
				getStartingRelics(),
				getStartingDeck(),
				false);
	}

	@Override
	public String getTitle(PlayerClass playerClass) {
		return CLASS_NAME;
	}

	@Override
	public AbstractCard.CardColor getCardColor() {
		return CardColorEnum.GATHERER_LIME;
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
		return 5;
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

	@Override
	public String getSpireHeartText() {
		return CardCrawlGame.languagePack.getEventString("Gatherer:SpireHeart").DESCRIPTIONS[0];
	}

	@Override
	public Color getSlashAttackColor() {
		return Color.LIME;
	}

	@Override
	public String getVampireText() {
		return CardCrawlGame.languagePack.getEventString("Gatherer:Vampires").DESCRIPTIONS[0];
	}

	@Override
	public Color getCardRenderColor() {
		return Color.LIME;
	}

	@Override
	public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
		return new AttackEffect[]{AttackEffect.SLASH_HEAVY, AttackEffect.POISON, AttackEffect.SLASH_DIAGONAL, AttackEffect.SLASH_HEAVY, AttackEffect.FIRE, AttackEffect.BLUNT_LIGHT};
	}
}
