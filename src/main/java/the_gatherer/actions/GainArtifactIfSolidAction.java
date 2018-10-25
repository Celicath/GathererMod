package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class GainArtifactIfSolidAction extends AbstractGameAction {
	private int threshold;
	private AbstractCreature owner;

	public GainArtifactIfSolidAction(AbstractCreature owner, int threshold) {
		this.duration = Settings.ACTION_DUR_FASTER;
		this.owner = owner;
		this.threshold = threshold;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FASTER) {
			if (owner.currentBlock >= threshold)
				AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(owner, owner, new ArtifactPower(owner, 1), 1));

			GatherMaterialAction.drawnAttacks = 0;
		}

		this.tickDuration();
	}
}
