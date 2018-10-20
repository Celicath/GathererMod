package the_gatherer.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import the_gatherer.patches.CustomTags;
import the_gatherer.potions.PlaceHolderPotion;

import java.util.Iterator;

public class PotionUsedAction extends AbstractGameAction {
	private AbstractPlayer p;

	public PotionUsedAction() {
		this.actionType = ActionType.SPECIAL;
		this.p = AbstractDungeon.player;
		this.duration = Settings.ACTION_DUR_FAST;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {
			for (AbstractRelic relic : AbstractDungeon.player.relics) {
				relic.onUsePotion();
			}
			BaseMod.publishPostPotionUse(new PlaceHolderPotion());
		}

		this.tickDuration();
	}
}
