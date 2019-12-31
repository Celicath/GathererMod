package the_gatherer.actions;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import javafx.util.Pair;
import the_gatherer.GathererMod;
import the_gatherer.cards.BalancedGrowth;
import the_gatherer.cards.FakeCards.LesserPotionOption;
import the_gatherer.cards.FeelingFine;
import the_gatherer.cards.PoisonMastery;
import the_gatherer.potions.LesserExplosivePotion;
import the_gatherer.potions.SackPotion;
import the_gatherer.powers.BalancedGrowthPower;
import the_gatherer.powers.BomberFormPower;
import the_gatherer.powers.FeelingFinePower;
import the_gatherer.powers.PoisonMasteryPower;

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

	// These values can used in getMindSearchResult() of SackPotion.
	// You don't NEED to use these; feel free to use your own calculations.
	public static int enemyCount;
	public static int enemyAttackCount;
	public static int unweakenedDamage;
	public static int totalDamage;
	public static int lowestEnemyHP;
	public static boolean nonVulnerable;
	public static int maxPoison;
	public static boolean isBYRD;
	public static boolean isBook;
	public static int skillCount;
	public static int attackCount;
	public static int totalCost;
	public static boolean poisonMastery;
	public static boolean feelingFine;
	public static boolean balancedGrowth;
	public static int eliteOrBoss;

	static {
		for (int i = 0; i < MIND_SEARCH_TEXT.length; i++) {
			MIND_SEARCH_TEXT[i] = MIND_SEARCH_TEXT[i].replaceAll("(?<=\\s|^)(\\S+)(?=\\s|$)", "[#f8e8a0]$1[]");
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
						enemyCount = 0;
						enemyAttackCount = 0;
						unweakenedDamage = 0;
						totalDamage = 0;
						lowestEnemyHP = 999;
						nonVulnerable = false;
						maxPoison = 0;
						isBYRD = false;
						isBook = false;
						skillCount = 0;
						attackCount = 0;
						totalCost = 0;
						poisonMastery = p.hasPower(PoisonMasteryPower.POWER_ID);
						feelingFine = p.hasPower(FeelingFinePower.POWER_ID);
						balancedGrowth = p.hasPower(BalancedGrowthPower.POWER_ID);
						eliteOrBoss = 0;

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
							if (c instanceof PoisonMastery)
								poisonMastery = true;
							else if (c instanceof FeelingFine)
								feelingFine = true;
							else if (c instanceof BalancedGrowth)
								balancedGrowth = true;
						}

						for (SackPotion sp : potionList) {
							Pair<Integer, String> result = sp.getMindSearchResult();
							int weight = result.getKey();
							String thought = result.getValue();

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
				} else {
					for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
						AbstractDungeon.actionManager.addToBottom(new ObtainLesserPotionAction((SackPotion) ((LesserPotionOption) c).potion.makeCopy(), false));
					}
				}
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
			}
		}
		this.tickDuration();
	}
}
