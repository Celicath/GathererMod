package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import the_gatherer.potions.SackPotion;

import java.util.ArrayList;

public class ExcessPotionHandleAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Gatherer:ExcessPotionHandleAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;

	public static ArrayList<SackPotion> excessPotions = new ArrayList<>();

	public ExcessPotionHandleAction(SackPotion excessPotion) {
		this.p = AbstractDungeon.player;
		excessPotions.add(excessPotion);
		this.setValues(null, source, 1);
	}

	public void update() {
		if (AbstractDungeon.getCurrRoom().isBattleEnding() || excessPotions.isEmpty()) {
			this.isDone = true;
		}
		this.tickDuration();
	}
}
