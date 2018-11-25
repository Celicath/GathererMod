package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import the_gatherer.GathererMod;
import the_gatherer.cards.BalancedGrowth;
import the_gatherer.cards.LesserPotionOption;
import the_gatherer.cards.PoisonMastery;
import the_gatherer.potions.*;
import the_gatherer.powers.BalancedGrowthPower;
import the_gatherer.powers.BomberFormPower;
import the_gatherer.powers.PoisonMasteryPower;
import the_gatherer.powers.RecipeChangePower;

import java.util.ArrayList;
import java.util.HashSet;

public class ChooseLesserPotionAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Gatherer:ChooseLesserPotionAction");
	public static final String[] TEXT = uiStrings.TEXT;

	private static final UIStrings mindSearchStrings = CardCrawlGame.languagePack.getUIString("Gatherer:MindSearchAction");
	public static final String[] MIND_SEARCH_TEXT = mindSearchStrings.TEXT;

	private AbstractPlayer p;
	private int pick, num;
	private int recipeRatio;
	private boolean upgraded;

	public static ArrayList<SackPotion> potionList;

	public ChooseLesserPotionAction(int pick, int num, int recipeRatio, boolean upgraded) {
		this.p = AbstractDungeon.player;
		this.setValues(null, p, pick);
		this.pick = pick;
		this.num = num;
		this.recipeRatio = recipeRatio;
		this.actionType = ActionType.CARD_MANIPULATION;
		this.duration = Settings.ACTION_DUR_FASTER;
		this.upgraded = upgraded;
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

					for (SackPotion sp : potionList) {
						sp.updateDescription();
					}

					ArrayList<Integer> weights = new ArrayList<>();
					ArrayList<String> thoughts = new ArrayList<>();

					int weightSum = 0;

					if (recipeRatio == 0) {
						for (SackPotion sp : potionList) {
							int enemyCount = 0;
							int enemyAttackCount = 0;
							int unweakenedDamage = 0;
							int totalDamage = 0;
							int lowestEnemyHP = 999;
							boolean nonVulnerable = false;
							int maxPoison = 0;
							boolean isBYRD = false;

							int eliteOrBoss = 0;

							for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
								if (!m.halfDead && !m.isDying && !m.isEscaping) {
									enemyCount++;

									if (m.id == Byrd.ID)
										isBYRD = true;
									if (m.type == AbstractMonster.EnemyType.ELITE)
										eliteOrBoss = 1;
									else if (m.type == AbstractMonster.EnemyType.BOSS)
										eliteOrBoss = 2;

									if (m.intent == AbstractMonster.Intent.ATTACK || m.intent == AbstractMonster.Intent.ATTACK_BUFF || m.intent == AbstractMonster.Intent.ATTACK_DEBUFF || m.intent == AbstractMonster.Intent.ATTACK_DEFEND) {
										int damage = 0;
										for (DamageInfo di : m.damage) {
											enemyAttackCount++;
											damage += di.output;
										}
										totalDamage += damage;
										if (!m.hasPower(WeakPower.POWER_ID) && unweakenedDamage < damage) {
											unweakenedDamage = damage;
										}
										if (!m.hasPower(VulnerablePower.POWER_ID)) {
											nonVulnerable = true;
										}
										if (m.hasPower(PoisonPower.POWER_ID)) {
											int amt = m.getPower(PoisonPower.POWER_ID).amount;
											if (amt > maxPoison)
												maxPoison = amt;
										}
									}
									if (lowestEnemyHP > m.currentHealth + m.currentBlock) {
										lowestEnemyHP = m.currentHealth + m.currentBlock;
									}
								}
							}

							int skillCount = 0;
							int attackCount = 0;
							int totalCost = 0;
							boolean poisonMastery = p.hasPower(PoisonMasteryPower.POWER_ID);
							boolean balancedGrowth = p.hasPower(BalancedGrowthPower.POWER_ID);
							for (AbstractCard c : p.hand.group) {
								if (c.type == AbstractCard.CardType.SKILL) skillCount++;
								else if (c.type == AbstractCard.CardType.ATTACK) attackCount++;

								totalCost += c.costForTurn;
								if (c instanceof PoisonMastery)
									poisonMastery = true;
								else if (c instanceof BalancedGrowth)
									balancedGrowth = true;
							}

							int weight = 1;
							int highest = weight;
							String thought = MIND_SEARCH_TEXT[0];

							if (sp instanceof LesserBlockPotion) {
								int now = totalDamage - p.currentBlock;
								if (now > 0) {
									thought = MIND_SEARCH_TEXT[1];
									weight += Math.min(now, 8);
								}
								if (p.currentHealth <= p.maxHealth / 5) {
									thought = MIND_SEARCH_TEXT[2];
									weight += 8;
								}
							} else if (sp instanceof LesserDexterityPotion) {
								if (skillCount > 0) {
									thought = MIND_SEARCH_TEXT[2];
									weight += skillCount;
								}
								if (balancedGrowth) {
									thought = MIND_SEARCH_TEXT[4];
									weight += 6;
								}
							} else if (sp instanceof LesserEnergyPotion) {
								weight += totalCost;
								if (totalCost >= 4) {
									thought = MIND_SEARCH_TEXT[5];
								}
							} else if (sp instanceof LesserEssenceOfSteel) {
								if (eliteOrBoss > 0) {
									highest = 3 + eliteOrBoss * 2;
									weight += highest;
									thought = MIND_SEARCH_TEXT[3];
								}
								if (p.hasPower(PlatedArmorPower.POWER_ID)) {
									int now = p.getPower(PlatedArmorPower.POWER_ID).amount;
									weight += now;
									if (now >= highest) {
										thought = MIND_SEARCH_TEXT[6];
									}
								}
							} else if (sp instanceof LesserExplosivePotion) {
								if (enemyCount > 1) {
									weight += enemyCount * 3;
									thought = MIND_SEARCH_TEXT[7];
								}
								if (lowestEnemyHP <= sp.getPotency()) {
									weight += 10;
									thought = MIND_SEARCH_TEXT[8];
								}
							} else if (sp instanceof LesserFearPotion) {
								if (enemyCount == 1 && nonVulnerable) {
									weight += eliteOrBoss * 3 + 4;
									thought = MIND_SEARCH_TEXT[9];
								}
							} else if (sp instanceof LesserFirePotion) {
								weight++;
								if (lowestEnemyHP <= sp.getPotency()) {
									weight += 10;
									thought = MIND_SEARCH_TEXT[8];
								}
							} else if (sp instanceof LesserLiquidBronze) {
								weight += 4 * enemyAttackCount;
								if (enemyAttackCount >= 3) {
									thought = MIND_SEARCH_TEXT[10];
								}
								if (isBYRD) {
									weight += 20;
									thought = MIND_SEARCH_TEXT[11];
								}
							} else if (sp instanceof LesserPoisonPotion) {
								weight += maxPoison;
								if (maxPoison >= 2) {
									thought = MIND_SEARCH_TEXT[6];
								}
								if (poisonMastery) {
									weight += 12;
									thought = MIND_SEARCH_TEXT[12];
								}
							} else if (sp instanceof LesserPowerPotion) {
								if (eliteOrBoss > 0) {
									weight += eliteOrBoss * 3 + 4;
									thought = MIND_SEARCH_TEXT[13];
								}
							} else if (sp instanceof LesserStrengthPotion) {
								if (attackCount > 0) {
									thought = MIND_SEARCH_TEXT[14];
									weight += attackCount;
								}
								if (balancedGrowth) {
									thought = MIND_SEARCH_TEXT[4];
									weight += 6;
								}
							} else if (sp instanceof LesserSwiftPotion) {
								weight += EnergyPanel.totalCount * 3;
								if (EnergyPanel.totalCount > 1) {
									thought = MIND_SEARCH_TEXT[15];
								}
							} else if (sp instanceof LesserWeakPotion) {
								if (enemyCount == 1 && nonVulnerable) {
									weight += eliteOrBoss * 3 + 4;
									thought = MIND_SEARCH_TEXT[16];
								}
								weight += unweakenedDamage / 4;
								if (unweakenedDamage >= 20) {
									weight += 2;
									thought = MIND_SEARCH_TEXT[16];
								}
							}
							if (!upgraded) {
								weight += 3;
							}
							weights.add(weight);
							thoughts.add(thought);
							weightSum += weight;
						}
					} else {
						for (SackPotion sp : potionList) {
							int weight = 1;
							weights.add(weight);
							weightSum += weight;
						}
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
								if (recipeRatio == 0) {
									group.addToTop(new LesserPotionOption(sp, sp.description + " NL " + thoughts.get(i)));
								} else {
									group.addToTop(new LesserPotionOption(sp, sp.description));
								}
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
