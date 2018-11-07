package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import the_gatherer.potions.LesserBlockPotion;

public class GainPotionIfSolidAction extends AbstractGameAction {
	private int threshold;
	private AbstractCreature owner;

	public GainPotionIfSolidAction(AbstractCreature owner, int threshold) {
		this.duration = Settings.ACTION_DUR_FASTER;
		this.owner = owner;
		this.threshold = threshold;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FASTER) {
			if (owner.currentBlock >= threshold) {
				AbstractDungeon.actionManager.addToTop(new LoseBlockAction(owner, owner, 5));
				AbstractDungeon.actionManager.addToTop(new ObtainLesserPotionAction(new LesserBlockPotion()));
			}
		}

		this.tickDuration();
	}
}
