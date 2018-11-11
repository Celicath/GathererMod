package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import the_gatherer.patches.CustomTags;

import java.util.Iterator;

public class SolarBeamAction extends AbstractGameAction {
	private AbstractPlayer p;

	public SolarBeamAction() {
		this.actionType = ActionType.CARD_MANIPULATION;
		this.p = AbstractDungeon.player;
		this.duration = Settings.ACTION_DUR_FAST;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {
			AbstractCard c;
			Iterator it = AbstractDungeon.player.hand.group.iterator();

			while (it.hasNext()) {
				c = (AbstractCard) it.next();
				if (c.hasTag(CustomTags.FLOWER) && c.canUpgrade()) {
					c.upgrade();
					c.superFlash();
				}
			}

			it = AbstractDungeon.player.drawPile.group.iterator();

			while (it.hasNext()) {
				c = (AbstractCard) it.next();
				if (c.hasTag(CustomTags.FLOWER) && c.canUpgrade()) {
					c.upgrade();
					c.superFlash();
				}
			}

			it = AbstractDungeon.player.discardPile.group.iterator();

			while (it.hasNext()) {
				c = (AbstractCard) it.next();
				if (c.hasTag(CustomTags.FLOWER) && c.canUpgrade()) {
					c.upgrade();
					c.superFlash();
				}
			}

			this.isDone = true;
			return;
		}

		this.tickDuration();
	}
}
