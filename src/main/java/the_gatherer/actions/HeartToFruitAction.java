package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class HeartToFruitAction extends AbstractGameAction {
	private int increaseHpAmount;
	private DamageInfo info;
	private static final float DURATION = 0.1F;

	public HeartToFruitAction(AbstractCreature target, DamageInfo info, int maxHPAmount) {
		this.info = info;
		this.setValues(target, info);
		this.increaseHpAmount = maxHPAmount;
		this.actionType = ActionType.DAMAGE;
		this.duration = 0.1F;
	}

	public void update() {
		if (this.duration == 0.1F && this.target != null) {
			AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.SLASH_VERTICAL));
			this.target.damage(this.info);
			if ((((AbstractMonster) this.target).isDying || this.target.currentHealth <= 0) && !this.target.halfDead && !this.target.hasPower("Minion")) {

				AbstractDungeon.player.increaseMaxHp(this.increaseHpAmount, false);
			}
			if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
				AbstractDungeon.actionManager.clearPostCombatActions();
			}
		}

		this.tickDuration();
	}
}
