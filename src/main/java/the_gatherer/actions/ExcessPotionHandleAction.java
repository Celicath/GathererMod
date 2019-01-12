package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import the_gatherer.GathererMod;
import the_gatherer.potions.SackPotion;
import the_gatherer.screens.ExcessPotionHandleScreen;

public class ExcessPotionHandleAction extends AbstractGameAction {
	private AbstractPlayer p;

	public ExcessPotionHandleAction(SackPotion excessPotion) {
		this.p = AbstractDungeon.player;
		excessPotion.isObtained = true;
		ExcessPotionHandleScreen.potions.add(excessPotion);
		this.setValues(null, source, 1);
	}

	public ExcessPotionHandleAction() {
		this.p = AbstractDungeon.player;
		this.setValues(null, source, 1);
	}

	public void update() {
		if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
			this.isDone = true;
		} else {
			GathererMod.excessPotionHandleScreen.open();
		}
		if (AbstractDungeon.handCardSelectScreen != GathererMod.excessPotionHandleScreen) {
			this.isDone = true;
		}
	}
}
