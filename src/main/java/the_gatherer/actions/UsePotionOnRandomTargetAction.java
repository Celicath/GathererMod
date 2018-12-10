package the_gatherer.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import java.util.ArrayList;

public class UsePotionOnRandomTargetAction extends AbstractGameAction {
	private AbstractPotion p;

	public UsePotionOnRandomTargetAction(AbstractPotion p) {
		this.p = p;
		this.duration = Settings.ACTION_DUR_FAST;
	}

	public void update() {
		if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			this.isDone = true;
			return;
		}
		AbstractCreature target = null;
		ArrayList<AbstractMonster> tmp = new ArrayList<>();
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
			if (!m.halfDead && !m.isDying && !m.isEscaping) {
				tmp.add(m);
			}
		}
		if (tmp.size() > 0) {
			target = tmp.get(MathUtils.random(0, tmp.size() - 1));
		}

		this.target = target;

		p.use(target);
		this.isDone = true;
		this.tickDuration();
	}
}
