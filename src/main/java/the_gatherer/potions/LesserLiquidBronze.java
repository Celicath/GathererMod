package the_gatherer.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import basemod.Pair;
import the_gatherer.GathererMod;
import the_gatherer.actions.ChooseLesserPotionAction;
import the_gatherer.patches.PotionRarityEnum;

public class LesserLiquidBronze extends SackPotion {
	private static final String RAW_ID = "LesserLiquidBronze";
	public static final String POTION_ID = GathererMod.makeID(RAW_ID);
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public LesserLiquidBronze() {
		super(NAME, POTION_ID, PotionRarityEnum.LESSER, PotionSize.SPIKY, PotionEffect.NONE, new Color(-491249153), new Color(415023359), null);
		this.isThrown = false;

		updateDescription();

		GathererMod.setLesserPotionColors(liquidColor, hybridColor, spotsColor);
	}

	public void use(AbstractCreature target) {
		target = AbstractDungeon.player;
		if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new ThornsPower(target, this.potency), this.potency));
		}
	}

	public AbstractPotion makeCopy() {
		return new LesserLiquidBronze();
	}

	@Override
	public void updateDescription() {
		super.updateDescription(DESCRIPTIONS);
		this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.THORNS.NAMES[0]), GameDictionary.keywords.get(GameDictionary.THORNS.NAMES[0])));
	}

	@Override
	public int getBasePotency() {
		return 2;
	}

	@Override
	public Pair<Integer, String> getMindSearchResult() {
		int weight = 1;
		String thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[0];

		weight += 4 * ChooseLesserPotionAction.enemyAttackCount;
		if (ChooseLesserPotionAction.enemyAttackCount >= 3) {
			thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[10];
		}
		if (ChooseLesserPotionAction.isBook) {
			weight += 10;
			thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[10];
		}
		if (ChooseLesserPotionAction.isBYRD) {
			weight += 25;
			thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[11];
		}

		return new Pair<>(weight, thought);
	}
}
