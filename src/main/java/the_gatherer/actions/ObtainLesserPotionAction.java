package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.Sozu;
import the_gatherer.GathererMod;
import the_gatherer.potions.LesserExplosivePotion;
import the_gatherer.potions.SackPotion;
import the_gatherer.powers.BomberFormPower;
import the_gatherer.powers.ExplodingPower;
import the_gatherer.powers.RecipeChangePower;


public class ObtainLesserPotionAction extends AbstractGameAction {
	private SackPotion potion;
	private boolean allowRecipeChange;

	public ObtainLesserPotionAction(SackPotion potion, boolean allowRecipeChange) {
		this.actionType = ActionType.SPECIAL;
		this.duration = Settings.ACTION_DUR_XFAST;
		this.potion = potion;
		this.allowRecipeChange = allowRecipeChange;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_XFAST) {
			if (AbstractDungeon.player.hasRelic(Sozu.ID)) {
				AbstractDungeon.player.getRelic(Sozu.ID).flash();
			} else {
				BomberFormPower bfp = (BomberFormPower) AbstractDungeon.player.getPower(BomberFormPower.POWER_ID);
				if (bfp != null) {
					potion = new LesserExplosivePotion();
				} else {
					RecipeChangePower rcp = (RecipeChangePower) AbstractDungeon.player.getPower(RecipeChangePower.POWER_ID);
					if (rcp != null && allowRecipeChange && AbstractDungeon.cardRandomRng.random(99) < rcp.ratio) {
						this.potion = (SackPotion) rcp.potion.makeCopy();
						rcp.flash();
					}
				}
				if (!GathererMod.potionSack.addPotion(this.potion)) {
					AbstractDungeon.actionManager.addToBottom(new ExcessPotionHandleAction(potion));
				}
			}
		}
		this.tickDuration();
	}
}
