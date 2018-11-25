package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import the_gatherer.GathererMod;

public class ReplaceGrowBookAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Gatherer:ReplaceGrowBookAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;
	private AbstractCard card;
	private int growBookChar;
	private boolean upgraded;

	public ReplaceGrowBookAction(AbstractCard card, int growBookChar, boolean upgraded) {
		this.p = AbstractDungeon.player;
		this.card = card;
		this.growBookChar = growBookChar;
		this.upgraded = upgraded;
		this.actionType = ActionType.CARD_MANIPULATION;
		this.duration = Settings.ACTION_DUR_FASTER;
	}

	public void update() {
		if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
			this.isDone = true;
		} else {
			if (this.duration == Settings.ACTION_DUR_FASTER) {
				CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

				AbstractCard[] cards = GathererMod.growBookContent.get(growBookChar);
				for (int i = 0; i < (upgraded ? 4 : 3); i++) {
					AbstractCard c = cards[i].makeCopy();
					if (upgraded && i < 3)
						c.upgrade();
					group.addToTop(c);
				}
				AbstractDungeon.gridSelectScreen.open(group, 1, TEXT[0], false);

			} else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {

				AbstractDungeon.player.masterDeck.removeCard(card);

				for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
					AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(c, com.megacrit.cardcrawl.core.Settings.WIDTH / 2.0F, com.megacrit.cardcrawl.core.Settings.HEIGHT / 2.0F));
				}
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
			}
		}
		this.tickDuration();
	}
}
