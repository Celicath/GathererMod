package the_gatherer.potions;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import basemod.Pair;
import the_gatherer.GathererMod;
import the_gatherer.actions.ChooseLesserPotionAction;
import the_gatherer.patches.PotionRarityEnum;

public class LesserEnergyPotion extends SackPotion {
	private static final String RAW_ID = "LesserEnergyPotion";
	public static final String POTION_ID = GathererMod.makeID(RAW_ID);
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public LesserEnergyPotion() {
		super(NAME, POTION_ID, PotionRarityEnum.LESSER, PotionSize.BOLT, PotionColor.ENERGY);
		this.isThrown = false;

		updateDescription();

		GathererMod.setLesserPotionColors(liquidColor, hybridColor, spotsColor);
	}

	public void use(AbstractCreature target) {
		AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.potency));
	}

	public AbstractPotion makeCopy() {
		return new LesserEnergyPotion();
	}

	@Override
	public void updateDescription() {
		super.updateDescription(DESCRIPTIONS);
	}

	@Override
	public int getBasePotency() {
		return 1;
	}

	@Override
	public Pair<Integer, String> getMindSearchResult() {
		int weight = 1;
		String thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[0];

		int missing = ChooseLesserPotionAction.totalCost - EnergyPanel.totalCount;
		if (missing > 0) {
			weight += missing * (AbstractDungeon.player.energy.energyMaster <= 3 ? 6 : 4);
			thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[5];
		}

		return new Pair<>(weight, thought);
	}
}
