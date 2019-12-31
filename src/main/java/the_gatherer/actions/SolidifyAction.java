package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.Sozu;
import the_gatherer.GathererMod;
import the_gatherer.potions.LesserBlockPotion;

public class SolidifyAction extends AbstractGameAction {
	private int convert;
	private AbstractCreature owner;

	public SolidifyAction(AbstractCreature owner, int convert) {
		this.duration = Settings.ACTION_DUR_FASTER;
		this.owner = owner;
		this.convert = convert;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FASTER) {
			if (AbstractDungeon.player.hasRelic(Sozu.ID)) {
				AbstractDungeon.player.getRelic(Sozu.ID).flash();
			} else {
				int maxCount = owner.currentBlock / convert;
				if (maxCount > 0) {
					int count = 0;
					for (AbstractPotion p : GathererMod.potionSack.potions) {
						if (p instanceof PotionSlot) {
							AbstractDungeon.actionManager.addToTop(new LoseBlockAction(owner, owner, convert));
							AbstractDungeon.actionManager.addToTop(new ObtainLesserPotionAction(new LesserBlockPotion(), true));
							if (++count >= maxCount) {
								break;
							}
						}
					}
				}
			}
		}

		this.tickDuration();
	}
}
