package the_gatherer.potions;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import javafx.util.Pair;
import the_gatherer.GathererMod;
import the_gatherer.actions.ChooseLesserPotionAction;
import the_gatherer.patches.PotionRarityEnum;

public class LesserBlockPotion extends SackPotion {
	private static final String RAW_ID = "LesserBlockPotion";
	public static final String POTION_ID = GathererMod.makeID(RAW_ID);
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public LesserBlockPotion() {
		super(NAME, POTION_ID, PotionRarityEnum.LESSER, PotionSize.S, PotionColor.BLUE);
		this.isThrown = false;

		updateDescription();

		GathererMod.setLesserPotionColors(liquidColor, hybridColor, spotsColor);
	}

	public void use(AbstractCreature target) {
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.potency));
	}

	public AbstractPotion makeCopy() {
		return new LesserBlockPotion();
	}

	@Override
	public void updateDescription() {
		super.updateDescription(DESCRIPTIONS);
		this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.BLOCK.NAMES[0]), GameDictionary.keywords.get(GameDictionary.BLOCK.NAMES[0])));
	}

	@Override
	public int getBasePotency() {
		return 8;
	}

	@Override
	public Pair<Integer, String> getMindSearchResult() {
		int weight = 1;
		String thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[0];

		int now = ChooseLesserPotionAction.totalDamage - AbstractDungeon.player.currentBlock;

		if (now > 0) {
			thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[1];
			weight += Math.min(now, 8);
		}
		if (AbstractDungeon.player.currentHealth <= AbstractDungeon.player.maxHealth / 5) {
			thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[2];
			weight += 10;
		}

		return new Pair<>(weight, thought);
	}
}
