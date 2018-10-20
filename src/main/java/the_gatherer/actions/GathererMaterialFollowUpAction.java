package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class GathererMaterialFollowUpAction extends AbstractGameAction {
	private int block_per_unique;
	public GathererMaterialFollowUpAction(int block_per_unique) {
		this.duration = Settings.ACTION_DUR_FASTER;
		this.block_per_unique = block_per_unique;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FASTER) {
			AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, GatherMaterialAction.uniqueCards.size() * block_per_unique));

			GatherMaterialAction.uniqueCards.clear();
		}

		this.tickDuration();
	}
}
