package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

public class GainPlatedArmorThresholdAction extends AbstractGameAction {
	private int num;
	private int threshold;

	public GainPlatedArmorThresholdAction(int num, int threshold) {
		this.num = num;
		this.threshold = threshold;
	}

	public void update() {
		if (AbstractDungeon.player.hasPower(PlatedArmorPower.POWER_ID)) {
			num = Math.min(num, threshold - AbstractDungeon.player.getPower(PlatedArmorPower.POWER_ID).amount);
		}
		if (num > 0) {
			AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(
					AbstractDungeon.player,
					AbstractDungeon.player,
					new PlatedArmorPower(AbstractDungeon.player, num), num));
		}
		this.isDone = true;
	}
}
