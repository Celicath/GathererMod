package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;
import java.util.UUID;

import static the_gatherer.cards.GrowBook.TRANSFORM_PLAYS;

public class IncreaseGrowBookMiscAction extends AbstractGameAction {
	private int miscIncrease;
	private UUID uuid;
	private boolean upgraded;

	public IncreaseGrowBookMiscAction(UUID targetUUID, int miscValue, int miscIncrease, boolean upgraded) {
		this.miscIncrease = miscIncrease;
		this.uuid = targetUUID;
		this.upgraded = upgraded;
	}

	public void update() {
		Iterator var1 = AbstractDungeon.player.masterDeck.group.iterator();

		AbstractCard c;
		while (var1.hasNext()) {
			c = (AbstractCard) var1.next();
			if (c.uuid.equals(this.uuid)) {
				c.misc += this.miscIncrease;
				if (c.misc % TRANSFORM_PLAYS == 0) {
					AbstractDungeon.actionManager.addToTop(new ReplaceGrowBookAction(c, (c.misc - 1) / TRANSFORM_PLAYS, this.uuid, upgraded));
				}
				c.applyPowers();
			}
		}

		this.isDone = true;
	}
}
