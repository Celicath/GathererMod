package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

public class MiningStrikeAction extends AbstractGameAction {
	public int[] damage;

	DamageInfo info;
	int div;

	public MiningStrikeAction(AbstractCreature target, DamageInfo info, AttackEffect effect, int div) {
		this.info = info;
		this.setValues(target, info);
		this.actionType = ActionType.DAMAGE;
		this.attackEffect = effect;
		this.duration = 0.1F;
		this.div = div;
	}

	public void update() {
		if (this.shouldCancelAction() && this.info.type != DamageInfo.DamageType.THORNS) {
			this.isDone = true;
		} else if (this.duration == 0.1F) {
			this.gainPlatedArmor();
			AbstractDungeon.actionManager.addToTop(new DamageAction(target, info, attackEffect));
		}

		this.tickDuration();
	}

	private void gainPlatedArmor() {
		int damaged = info.output - target.currentBlock;
		if (damaged > target.currentHealth)
			damaged = target.currentHealth;

		if (damaged >= div) {
			AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PlatedArmorPower(AbstractDungeon.player, damaged / div), damaged / div));
		}
	}
}
