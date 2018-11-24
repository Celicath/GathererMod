package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.PotionSlot;
import the_gatherer.GathererMod;
import the_gatherer.cards.LesserPotionOption;
import the_gatherer.potions.LesserExplosivePotion;
import the_gatherer.potions.SackPotion;
import the_gatherer.powers.BomberFormPower;
import the_gatherer.powers.RecipeChangePower;

import java.util.ArrayList;
import java.util.HashSet;

public class ChooseLesserPotionAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Gatherer:ChooseLesserPotionAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;
	private int pick, num;
	private int recipeRatio;

	public static ArrayList<SackPotion> potionList;

	public ChooseLesserPotionAction(int pick, int num, int recipeRatio) {
		this.p = AbstractDungeon.player;
		this.setValues(null, p, pick);
		this.pick = pick;
		this.num = num;
		this.recipeRatio = recipeRatio;
		this.actionType = ActionType.CARD_MANIPULATION;
		this.duration = Settings.ACTION_DUR_FASTER;
	}

	public void update() {
		if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
			this.isDone = true;
		} else {
			if (this.duration == Settings.ACTION_DUR_FASTER) {
				CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

				if (p.hasPower(BomberFormPower.POWER_ID)) {
					for (int n = 0; n < num; n++) {
						LesserExplosivePotion lep = new LesserExplosivePotion();
						group.addToTop(new LesserPotionOption(lep, lep.description));
					}
				} else {
					if (potionList == null) {
						potionList = new ArrayList<>();
						for (SackPotion sp : GathererMod.lesserPotionPool) {
							potionList.add((SackPotion) sp.makeCopy());
						}
					}

					ArrayList<Integer> weights = new ArrayList<>();
					int weightSum = 0;
					for (SackPotion sp : potionList) {
						// TODO: do something with sp
						int weight = 1;
						weights.add(weight);
						weightSum += weight;
					}

					HashSet<Class<? extends SackPotion>> picked = new HashSet<>();
					for (int n = 0; n < num; n++) {
						int thisWeightSum = weightSum;
						for (int i = 0; i < potionList.size(); i++) {
							SackPotion sp = potionList.get(i);
							Class<? extends SackPotion> cls = sp.getClass();
							if (picked.contains(cls)) continue;
							int weight = weights.get(i);
							if (AbstractDungeon.cardRandomRng.random(thisWeightSum) < weight) {
								weightSum -= weight;
								picked.add(cls);
								sp.updateDescription();
								group.addToTop(new LesserPotionOption(sp, sp.description));
								break;
							}
							thisWeightSum -= weight;
						}
					}
				}
				AbstractDungeon.gridSelectScreen.open(group, pick, recipeRatio > 0 ? TEXT[0] : TEXT[1], false);

			} else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {

				if (recipeRatio > 0) {
					LesserPotionOption lpo = (LesserPotionOption) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
					for (int i = 0; i < GathererMod.potionSack.potions.size(); i++) {
						if (!(GathererMod.potionSack.potions.get(i) instanceof PotionSlot)) {
							GathererMod.potionSack.setPotion(i, lpo.potion.makeCopy());
						}
						RecipeChangePower rcp = (RecipeChangePower) AbstractDungeon.player.getPower(RecipeChangePower.POWER_ID);
						if (rcp != null) {
							if (rcp.potion == lpo.potion && rcp.ratio >= this.recipeRatio) {
								this.tickDuration();
								return;
							}
							AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(
									AbstractDungeon.player, AbstractDungeon.player, RecipeChangePower.POWER_ID));
						}
						AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
								AbstractDungeon.player, AbstractDungeon.player, new RecipeChangePower(lpo.potion, recipeRatio)));
					}
				} else {
					for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
						GathererMod.potionSack.addPotion(((LesserPotionOption) c).potion.makeCopy());
					}
				}
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
			}
		}
		this.tickDuration();
	}
}
