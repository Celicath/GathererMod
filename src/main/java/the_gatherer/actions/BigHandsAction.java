package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class BigHandsAction extends AbstractGameAction {

	public BigHandsAction() {
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
			AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, sum));
		}

		this.tickDuration();
	}
}
