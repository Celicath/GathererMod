package the_gatherer.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class BigPouchAction extends AbstractGameAction {

	public BigPouchAction() {
		this.duration = Settings.ACTION_DUR_FASTER;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FASTER) {
			int sum = 0;
			for (AbstractCard c : AbstractDungeon.player.hand.group) {
				int cost = c.costForTurn;
				if (cost > 0) {
					sum += cost;
				} else if (cost == -1) {
					sum += EnergyPanel.getCurrentEnergy();
				}
			}
			float tmp = sum;
			for (final AbstractPower p : AbstractDungeon.player.powers) {
				tmp = p.modifyBlock(tmp);
			}
			if (tmp < 0.0f) {
				tmp = 0.0f;
			}
			int block = MathUtils.floor(tmp);
			AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
		}

		this.tickDuration();
	}
}
