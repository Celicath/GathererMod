package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ExamineAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Gatherer:ExamineAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;

	CardGroup tmpGroup;

	public ExamineAction(int amount) {
		this.p = AbstractDungeon.player;
		this.setValues(null, source, amount);
		this.actionType = ActionType.CARD_MANIPULATION;
		this.duration = Settings.ACTION_DUR_FASTER;
	}

	public void update() {
		if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
			this.isDone = true;
		} else {
			if (this.duration == Settings.ACTION_DUR_FASTER) {
				if (AbstractDungeon.player.drawPile.isEmpty()) {
					this.isDone = true;
					return;
				} else if (AbstractDungeon.player.drawPile.size() == 1) {
					AbstractDungeon.actionManager.addToBottom(new PlayTopCardAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster) null, true, AbstractDungeon.cardRandomRng), false));
					this.isDone = true;
					return;
				} else {
					tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

					for (int i = 0; i < Math.min(this.amount, AbstractDungeon.player.drawPile.size()); ++i) {
						tmpGroup.addToTop(AbstractDungeon.player.drawPile.group.get(AbstractDungeon.player.drawPile.size() - i - 1));
					}

					AbstractDungeon.gridSelectScreen.open(tmpGroup, 1, TEXT[0], false, false, false, false);
				}
			} else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {

				for (AbstractCard c : tmpGroup.group) {
					if (!AbstractDungeon.gridSelectScreen.selectedCards.contains(c)) {
						AbstractDungeon.player.drawPile.moveToDiscardPile(c);
					}
				}
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
				AbstractDungeon.actionManager.addToBottom(new PlayTopCardAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster) null, true, AbstractDungeon.cardRandomRng), false));
			}
		}
		this.tickDuration();
	}
}
