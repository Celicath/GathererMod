package the_gatherer.actions;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.BookOfStabbing;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import the_gatherer.GathererMod;
import the_gatherer.cards.BalancedGrowth;
import the_gatherer.cards.FeelingFine;
import the_gatherer.cards.LesserPotionOption;
import the_gatherer.cards.PoisonMastery;
import the_gatherer.potions.*;
import the_gatherer.powers.*;

import java.util.ArrayList;
import java.util.HashSet;

public class ChooseLesserPotionAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Gatherer:ChooseLesserPotionAction");
	public static final String[] TEXT = uiStrings.TEXT;

	private static final UIStrings mindSearchStrings = CardCrawlGame.languagePack.getUIString("Gatherer:MindSearchAction");
	public static String[] MIND_SEARCH_TEXT = mindSearchStrings.TEXT;

	private static int recipeChangeRatio = 25;

	private AbstractPlayer p;
	private int pick, num;
	private boolean recipeChange;
	private boolean upgraded;

	public static ArrayList<SackPotion> potionList;

	static {
		for (int i = 0; i < MIND_SEARCH_TEXT.length; i++) {
			MIND_SEARCH_TEXT[i] = MIND_SEARCH_TEXT[i].replaceAll("(?<=\\s|^)(?=\\S)", "[#f8e8a0]");
		}
	}

	public ChooseLesserPotionAction(int pick, int num, boolean recipeChange, boolean upgraded) {
		this.p = AbstractDungeon.player;
		this.setValues(null, p, pick);
		this.pick = pick;
		this.num = num;
		this.recipeChange = recipeChange;
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
					if (recipeChange) {
						LesserExplosivePotion lep = new LesserExplosivePotion();
						group.addToTop(new LesserPotionOption(lep, lep.description));
					} else {
						ArrayList<Integer> lineNo = new ArrayList<>();
						for (int i = 1; i <= 5; i++)
							lineNo.add(i);

						for (int n = 0; n < num; n++) {
							LesserExplosivePotion lep = new LesserExplosivePotion();
							int k = MathUtils.random(lineNo.size() - 1);
							group.addToTop(new LesserPotionOption(lep, lep.description + " NL " + MIND_SEARCH_TEXT[MIND_SEARCH_TEXT.length - lineNo.get(k)]));
							lineNo.remove(k);
						}
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

					if (recipeChange) {
						for (SackPotion sp : potionList) {
							group.addToTop(new LesserPotionOption(sp, sp.description));
						}
					} else {
						for (SackPotion sp : potionList) {
							int enemyCount = 0;
							int enemyAttackCount = 0;
							int unweakenedDamage = 0;
							int totalDamage = 0;
							int lowestEnemyHP = 999;
							boolean nonVulnerable = false;
							int maxPoison = 0;
							boolean isBYRD = false;
							boolean isBook = false;

							int eliteOrBoss = 0;

							for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
								if (!m.halfDead && !m.isDying && !m.isEscaping) {
									enemyCount++;

									if (m.id.equals(Byrd.ID))
										isBYRD = true;
									else if (m.id.equals(BookOfStabbing.ID))
										isBook = true;
									if (m.type == AbstractMonster.EnemyType.ELITE)
										eliteOrBoss = 1;
									else if (m.type == AbstractMonster.EnemyType.BOSS)
										eliteOrBoss = 2;

									if (!m.hasPower(VulnerablePower.POWER_ID)) {
										nonVulnerable = true;
									}
									if (m.hasPower(PoisonPower.POWER_ID)) {
										int amt = m.getPower(PoisonPower.POWER_ID).amount;
										if (amt > maxPoison)
											maxPoison = amt;
									}
									if (m.intent == AbstractMonster.Intent.ATTACK || m.intent == AbstractMonster.Intent.ATTACK_BUFF || m.intent == AbstractMonster.Intent.ATTACK_DEBUFF || m.intent == AbstractMonster.Intent.ATTACK_DEFEND) {
										int damage = (int) ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentBaseDmg");
										int mult = 1;
										if ((boolean) ReflectionHacks.getPrivate(m, AbstractMonster.class, "isMultiDmg")) {
											mult = (int) ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentMultiAmt");
										}
										damage *= mult;
										enemyAttackCount += mult;

										totalDamage += damage;
										if (!m.hasPower(WeakPower.POWER_ID) && unweakenedDamage < damage) {
											unweakenedDamage = damage;
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
							boolean feelingFine = p.hasPower(FeelingFinePower.POWER_ID);
							boolean balancedGrowth = p.hasPower(BalancedGrowthPower.POWER_ID);
							for (AbstractCard c : p.hand.group) {
								if (c.type == AbstractCard.CardType.SKILL) skillCount++;
								else if (c.type == AbstractCard.CardType.ATTACK) attackCount++;

								totalCost += c.costForTurn;
								if (c instanceof PoisonMastery)
									poisonMastery = true;
								else if (c instanceof FeelingFine)
									feelingFine = true;
								else if (c instanceof BalancedGrowth)
									balancedGrowth = true;
							}
							for (AbstractCard c : p.drawPile.group) {
								if (c.type == AbstractCard.CardType.SKILL) skillCount++;
								else if (c.type == AbstractCard.CardType.ATTACK) attackCount++;

								totalCost += c.costForTurn;
								if (c instanceof PoisonMastery)
									poisonMastery = true;
								else if (c instanceof FeelingFine)
									feelingFine = true;
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
									weight += 10;
								}
							} else if (sp instanceof LesserDexterityPotion) {
								if (skillCount > 0) {
									thought = MIND_SEARCH_TEXT[3];
									weight += skillCount;
								}
								if (balancedGrowth) {
									thought = MIND_SEARCH_TEXT[4];
									weight += 8;
								}
							} else if (sp instanceof LesserEnergyPotion) {
								weight += totalCost * 2;
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
									int now = p.getPower(PlatedArmorPower.POWER_ID).amount * 3;
									weight += now;
									if (now >= highest) {
										thought = MIND_SEARCH_TEXT[6];
									}
								}
							} else if (sp instanceof LesserExplosivePotion) {
								if (enemyCount > 1) {
									weight += enemyCount * 2;
									thought = MIND_SEARCH_TEXT[7];
								}
								if (lowestEnemyHP <= sp.getPotency()) {
									weight += 12;
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
									weight += 14;
									thought = MIND_SEARCH_TEXT[8];
								}
							} else if (sp instanceof LesserLiquidBronze) {
								weight += 4 * enemyAttackCount;
								if (enemyAttackCount >= 3) {
									thought = MIND_SEARCH_TEXT[10];
								}
								if (isBook) {
									weight += 10;
									thought = MIND_SEARCH_TEXT[10];
								}
								if (isBYRD) {
									weight += 25;
									thought = MIND_SEARCH_TEXT[11];
								}
							} else if (sp instanceof LesserPoisonPotion) {
								weight += maxPoison;
								if (maxPoison >= 2) {
									thought = MIND_SEARCH_TEXT[6];
								}
								if (poisonMastery) {
									weight += 14;
									thought = MIND_SEARCH_TEXT[12];
								}
							} else if (sp instanceof LesserPowerPotion) {
								if (eliteOrBoss > 0) {
									weight += eliteOrBoss * 3 + 5;
									thought = MIND_SEARCH_TEXT[13];
								}
							} else if (sp instanceof LesserSpeedPotion) {
								if (skillCount > 0) {
									thought = MIND_SEARCH_TEXT[17];
									weight += skillCount * 2;
								}
								if (feelingFine) {
									thought = MIND_SEARCH_TEXT[19];
									weight += 12;
								}
							} else if (sp instanceof LesserSteroidPotion) {
								if (attackCount > 0) {
									thought = MIND_SEARCH_TEXT[18];
									weight += attackCount * 2;
								}
								if (feelingFine) {
									thought = MIND_SEARCH_TEXT[19];
									weight += 12;
								}
							} else if (sp instanceof LesserStrengthPotion) {
								if (attackCount > 0) {
									thought = MIND_SEARCH_TEXT[14];
									weight += attackCount;
								}
								if (balancedGrowth) {
									thought = MIND_SEARCH_TEXT[4];
									weight += 8;
								}
							} else if (sp instanceof LesserSwiftPotion) {
								weight += EnergyPanel.totalCount * 4;
								if (EnergyPanel.totalCount > 1) {
									thought = MIND_SEARCH_TEXT[15];
								}
							} else if (sp instanceof LesserWeakPotion) {
								if (eliteOrBoss > 0) {
									weight += eliteOrBoss * 3 + 2;
									thought = MIND_SEARCH_TEXT[16];
								}
								weight += unweakenedDamage / 4;
								if (unweakenedDamage >= 20) {
									weight += 2;
									thought = MIND_SEARCH_TEXT[16];
								}
							}
							if (!upgraded) {
								weight += 2;
							}
							weights.add(weight);
							thoughts.add(thought);
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
								if (AbstractDungeon.cardRandomRng.random(thisWeightSum - 1) < weight) {
									weightSum -= weight;
									picked.add(cls);
									group.addToTop(new LesserPotionOption(sp, sp.description + " NL " + thoughts.get(i)));
									break;
								}
								thisWeightSum -= weight;
							}
						}
					}
				}
				AbstractDungeon.gridSelectScreen.open(group, pick, recipeChange ? TEXT[0] : TEXT[1], false);

			} else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {

				if (recipeChange) {
					LesserPotionOption lpo = (LesserPotionOption) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
					for (int i = 0; i < GathererMod.potionSack.potions.size(); i++) {
						if (!(GathererMod.potionSack.potions.get(i) instanceof PotionSlot)) {
							GathererMod.potionSack.setPotion(i, (SackPotion) lpo.potion.makeCopy());
						}
					}
					RecipeChangePower rcp = (RecipeChangePower) AbstractDungeon.player.getPower(RecipeChangePower.POWER_ID);
					if (rcp != null) {
						if (rcp.potion == lpo.potion && rcp.ratio >= recipeChangeRatio) {
							this.tickDuration();
							return;
						}
						AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(
								AbstractDungeon.player, AbstractDungeon.player, RecipeChangePower.POWER_ID));
					}
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
							AbstractDungeon.player, AbstractDungeon.player, new RecipeChangePower(lpo.potion, recipeChangeRatio)));
				} else {
					for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
						AbstractDungeon.actionManager.addToBottom(new ObtainLesserPotionAction(((LesserPotionOption) c).potion.makeCopy(), false));
					}
				}
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
			}
		}
		this.tickDuration();
	}
}
