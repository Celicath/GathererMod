package the_gatherer.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DrawHalfAction extends AbstractGameAction {
	AbstractCreature source;

	public DrawHalfAction(AbstractCreature source) {
		this.source = source;
	}

	public void update() {
		if (MathUtils.random(0, 1) == 0)
			AbstractDungeon.actionManager.addToTop(new DrawCardAction(source, 1));

		this.isDone = true;
	}
}
