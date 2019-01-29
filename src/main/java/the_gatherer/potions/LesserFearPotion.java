package the_gatherer.potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import javafx.util.Pair;
import the_gatherer.GathererMod;
import the_gatherer.actions.ChooseLesserPotionAction;
import the_gatherer.patches.PotionRarityEnum;

public class LesserFearPotion extends SackPotion {
	private static final String RAW_ID = "LesserFearPotion";
	public static final String POTION_ID = GathererMod.makeID(RAW_ID);
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public LesserFearPotion() {
		super(NAME, POTION_ID, PotionRarityEnum.LESSER, PotionSize.H, PotionColor.FEAR);
		this.isThrown = true;
		this.targetRequired = true;

		updateDescription();

		GathererMod.setLesserPotionColors(liquidColor, hybridColor, spotsColor);
	}

	public void use(AbstractCreature target) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new VulnerablePower(target, this.potency, false), this.potency));
	}

	public AbstractPotion makeCopy() {
		return new LesserFearPotion();
	}

	@Override
	public void updateDescription() {
		super.updateDescription(DESCRIPTIONS);
		this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.VULNERABLE.NAMES[0]), GameDictionary.keywords.get(GameDictionary.VULNERABLE.NAMES[0])));
	}

	@Override
	public int getBasePotency() {
		return 2;
	}

	@Override
	public Pair<Integer, String> getMindSearchResult() {
		int weight = 1;
		String thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[0];

		if (ChooseLesserPotionAction.enemyCount == 1 && ChooseLesserPotionAction.nonVulnerable) {
			weight += ChooseLesserPotionAction.eliteOrBoss * 3 + 4;
			thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[9];
		}

		return new Pair<>(weight, thought);
	}
}
