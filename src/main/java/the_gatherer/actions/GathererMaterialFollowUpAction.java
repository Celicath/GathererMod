package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class GathererMaterialFollowUpAction extends AbstractGameAction {
	private int block_per_attack;

	public GathererMaterialFollowUpAction(int block_per_attack) {
		this.duration = Settings.ACTION_DUR_FASTER;
		this.block_per_attack = block_per_attack;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FASTER) {
			for (int i = 0; i < GatherMaterialAction.drawnAttacks; i++)
				AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block_per_attack));

			GatherMaterialAction.drawnAttacks = 0;
		}

		this.tickDuration();
	}
}
