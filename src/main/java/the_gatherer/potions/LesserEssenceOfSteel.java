package the_gatherer.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import basemod.Pair;
import the_gatherer.GathererMod;
import the_gatherer.actions.ChooseLesserPotionAction;
import the_gatherer.patches.PotionRarityEnum;

public class LesserEssenceOfSteel extends SackPotion {
	private static final String RAW_ID = "LesserEssenceOfSteel";
	public static final String POTION_ID = GathererMod.makeID(RAW_ID);
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	// liquidColor is copied in the AbstractPotion constructor.
	public LesserEssenceOfSteel() {
		super(NAME, POTION_ID, PotionRarityEnum.LESSER, PotionSize.ANVIL, PotionEffect.NONE, Color.TEAL, null, null);
		this.isThrown = false;

		updateDescription();

		GathererMod.setLesserPotionColors(liquidColor, hybridColor, spotsColor);
	}

	public void use(AbstractCreature target) {
		target = AbstractDungeon.player;
		if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new PlatedArmorPower(target, this.potency), this.potency));
		}
	}

	public AbstractPotion makeCopy() {
		return new LesserEssenceOfSteel();
	}

	@Override
	public void updateDescription() {
		super.updateDescription(DESCRIPTIONS);
	}

	@Override
	public int getBasePotency() {
		return 2;
	}

	@Override
	public Pair<Integer, String> getMindSearchResult() {
		int weight = 1;
		String thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[0];

		int highest = 1;
		if (ChooseLesserPotionAction.eliteOrBoss > 0) {
			highest = 3 + ChooseLesserPotionAction.eliteOrBoss * 2;
			weight += highest;
			thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[3];
		}
		if (AbstractDungeon.player.hasPower(PlatedArmorPower.POWER_ID)) {
			int now = AbstractDungeon.player.getPower(PlatedArmorPower.POWER_ID).amount * 3;
			weight += now;
			if (now >= highest) {
				thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[6];
			}
		}

		return new Pair<>(weight, thought);
	}
}
