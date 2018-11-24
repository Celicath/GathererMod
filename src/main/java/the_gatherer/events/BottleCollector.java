package the_gatherer.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.green.Alchemize;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import the_gatherer.GathererMod;
import the_gatherer.cards.SpareBottle;
import the_gatherer.patches.CustomTags;

public class BottleCollector extends AbstractImageEvent {
	private static final String RAW_ID = "BottleCollector";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
	private static final String NAME = eventStrings.NAME;
	private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
	private static final String[] OPTIONS = eventStrings.OPTIONS;

	private CurrentScreen curScreen = CurrentScreen.INTRO;
	private OptionChosen option = OptionChosen.NONE;

	private CardGroup cg;

	private enum CurrentScreen {
		INTRO, DONE
	}

	private enum OptionChosen {
		NONE, CARD
	}

	public BottleCollector() {
		super(NAME, DESCRIPTIONS[0], GathererMod.GetEventPath(RAW_ID));

		cg = getPotionGenCards();

		if (cg.isEmpty()) {
			imageEventText.setDialogOption(OPTIONS[1], true);
		} else {
			imageEventText.setDialogOption(OPTIONS[0], new SpareBottle());
		}
		imageEventText.setDialogOption(OPTIONS[2]);
	}

	@Override
	public void update() {
		super.update();

		if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
			switch (option) {
				case CARD:
					AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
					GathererMod.bottleCollector = false;
					AbstractDungeon.player.masterDeck.removeCard(c);

					SpareBottle bottle = SpareBottle.makeTransformedCopy(c);
					AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(bottle, com.megacrit.cardcrawl.core.Settings.WIDTH / 2.0F, com.megacrit.cardcrawl.core.Settings.HEIGHT / 2.0F));
					AbstractDungeon.gridSelectScreen.selectedCards.clear();

					imageEventText.updateBodyText(DESCRIPTIONS[1]);
					imageEventText.updateDialogOption(0, OPTIONS[2]);
					imageEventText.clearRemainingOptions();

					logMetricObtainCardAndLoseCard(ID, "Got a Spare Bottle", bottle, c);
					break;
			}

			option = OptionChosen.NONE;
		}
	}

	public CardGroup getPotionGenCards() {
		CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

		for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
			if (c.hasTag(CustomTags.POTION_GEN) || c.cardID.equals(Alchemize.ID)) {
				retVal.group.add(c);
			}
		}
		return CardGroup.getGroupWithoutBottledCards(retVal);
	}

	@Override
	protected void buttonEffect(int buttonPressed) {
		switch (curScreen) {
			case INTRO:
				switch (buttonPressed) {
					case 0:
						option = OptionChosen.CARD;
						GathererMod.bottleCollector = true;
						AbstractDungeon.gridSelectScreen.open(cg, 1, OPTIONS[3], true, false, false, false);
						break;
					case 1:
						imageEventText.updateBodyText(DESCRIPTIONS[2]);
						imageEventText.updateDialogOption(0, OPTIONS[2]);
						imageEventText.clearRemainingOptions();
						logMetricIgnored(ID);
						break;
				}
				curScreen = CurrentScreen.DONE;
				break;
			case DONE:
				openMap();
				break;
		}
	}
}
