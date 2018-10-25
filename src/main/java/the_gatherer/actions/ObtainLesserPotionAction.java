package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.ExplosivePotion;
import com.megacrit.cardcrawl.relics.Sozu;
import the_gatherer.GathererMod;
import the_gatherer.potions.LesserExplosivePotion;
import the_gatherer.powers.BomberFormPower;


public class ObtainLesserPotionAction extends AbstractGameAction {
	private AbstractPotion potion;

	public ObtainLesserPotionAction(AbstractPotion potion) {
		this.actionType = ActionType.SPECIAL;
		this.duration = Settings.ACTION_DUR_XFAST;
		this.potion = potion;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_XFAST) {
			if (AbstractDungeon.player.hasRelic(Sozu.ID)) {
				AbstractDungeon.player.getRelic(Sozu.ID).flash();
			} else if (AbstractDungeon.player.hasPower(BomberFormPower.POWER_ID)) {
				if (GathererMod.potionSack.addPotion(new LesserExplosivePotion())) {
					AbstractDungeon.player.getPower(BomberFormPower.POWER_ID).flash();
					BomberFormPower bfp = (BomberFormPower)AbstractDungeon.player.getPower(BomberFormPower.POWER_ID);
					LesserExplosivePotion.upgrades += bfp.amount;

					for (AbstractPotion p : GathererMod.potionSack.potions) {
						if (p instanceof LesserExplosivePotion) {
							((LesserExplosivePotion)p).UpdateDescription();
						}
					}
				}
			} else {
				GathererMod.potionSack.addPotion(this.potion);
			}
		}

		this.tickDuration();
	}
}
