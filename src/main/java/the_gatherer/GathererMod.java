package the_gatherer;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.abstracts.CustomCard;
import basemod.helpers.BaseModCardTags;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.blue.Defend_Blue;
import com.megacrit.cardcrawl.cards.green.Defend_Green;
import com.megacrit.cardcrawl.cards.red.Defend_Red;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.BloodPotion;
import com.megacrit.cardcrawl.potions.GhostInAJar;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.ToyOrnithopter;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_gatherer.actions.ScrollOfPurityAction;
import the_gatherer.cards.*;
import the_gatherer.cards.Helper.AbstractNumberedCard;
import the_gatherer.character.TheGatherer;
import the_gatherer.interfaces.OnObtainEffect;
import the_gatherer.interfaces.OnUsePotionEffect;
import the_gatherer.interfaces.PostObtainCardSubscriber;
import the_gatherer.modules.CaseMod;
import the_gatherer.modules.ModLabeledButton;
import the_gatherer.modules.PotionSack;
import the_gatherer.patches.AbstractPlayerEnum;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.potions.*;
import the_gatherer.powers.HandcraftedFencePower;
import the_gatherer.relics.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.lwjgl.input.Keyboard.*;

@SpireInitializer
public class GathererMod implements PostInitializeSubscriber,
		EditCardsSubscriber, EditRelicsSubscriber, EditCharactersSubscriber,
		EditStringsSubscriber, SetUnlocksSubscriber, EditKeywordsSubscriber,
		OnStartBattleSubscriber, OnPowersModifiedSubscriber,
		PostPotionUseSubscriber, PostObtainCardSubscriber,
		PostBattleSubscriber, OnCardUseSubscriber, RenderSubscriber,
		PostEnergyRechargeSubscriber {

	public static final Logger logger = LogManager.getLogger(GathererMod.class.getName());

	public static final String MODNAME = "GathererMod";
	public static final String AUTHOR = "Celicath";
	public static final String DESCRIPTION = "Adds The Gatherer as a new playable character.";


	public static final String GATHERER_BADGE = "GathererMod/img/badge.png";
	public static final String GATHERER_BUTTON = "GathererMod/img/character/gatherer/button.png";
	public static final String GATHERER_PORTRAIT = "GathererMod/img/character/gatherer/PortraitBG.png";
	public static final String GATHERER_SHOULDER_1 = "GathererMod/img/character/gatherer/shoulder.png";
	public static final String GATHERER_SHOULDER_2 = "GathererMod/img/character/gatherer/shoulder2.png";
	public static final String GATHERER_CORPSE = "GathererMod/img/character/gatherer/corpse.png";

	public static PotionSack potionSack;
	public static Set<Class<? extends AbstractCard>> playedCardsCombat = new HashSet<>();
	public static int blockExpired = 0;

	public static ArrayList<Class<? extends AbstractPotion>> lesserPotionPool = new ArrayList<>();
	public static HashSet<AbstractCard> statusesToExhaust = new HashSet<>();

	public static Properties gathererDefaults = new Properties();
	public static int[] potionSackKeys = new int[]{KEY_I, KEY_O, KEY_P};
	public static boolean potionSackPopupFlipped = false;

	public GathererMod() {
		logger.debug("Constructor started.");
		BaseMod.subscribe(this);
		CaseMod.subscribe(this);

		BaseMod.addColor(CardColorEnum.LIME,
				Color.LIME, Color.LIME, Color.LIME, Color.LIME, Color.LIME, Color.LIME, Color.LIME,
				"GathererMod/img/cardui/512/bg_attack_lime.png",
				"GathererMod/img/cardui/512/bg_skill_lime.png",
				"GathererMod/img/cardui/512/bg_power_lime.png",
				"GathererMod/img/cardui/512/card_lime_orb.png",
				"GathererMod/img/cardui/1024/bg_attack_lime.png",
				"GathererMod/img/cardui/1024/bg_skill_lime.png",
				"GathererMod/img/cardui/1024/bg_power_lime.png",
				"GathererMod/img/cardui/1024/card_lime_orb.png",
				"GathererMod/img/cardui/512/card_lime_small_orb.png");

		gathererDefaults.setProperty("phoenixStart", "FALSE");
		gathererDefaults.setProperty("contentSharing", "TRUE");
		gathererDefaults.setProperty("contentSharing_potions", "TRUE");
		gathererDefaults.setProperty("overheatedBETA", "FALSE");
		loadConfig();

		logger.debug("Constructor finished.");
	}

	public static void loadConfig() {
		logger.debug("loadConfig started.");
		try {
			SpireConfig config = new SpireConfig("GathererMod", "GathererSaveData", gathererDefaults);
			config.load();
			potionSackKeys[0] = config.getInt("potionSackKey0");
			potionSackKeys[1] = config.getInt("potionSackKey1");
			potionSackKeys[2] = config.getInt("potionSackKey2");
			potionSackPopupFlipped = config.getBool("potionSackPopupFlipped");
		} catch (Exception e) {
			e.printStackTrace();
			clearConfig();
		}
		logger.debug("loadConfig finished.");
	}

	public static void saveConfig() {
		logger.debug("saveConfig started.");
		try {
			SpireConfig config = new SpireConfig("GathererMod", "GathererSaveData", gathererDefaults);
			config.setInt("potionSackKey0", potionSackKeys[0]);
			config.setInt("potionSackKey1", potionSackKeys[1]);
			config.setInt("potionSackKey2", potionSackKeys[2]);
			config.setBool("potionSackPopupFlipped", potionSackPopupFlipped);
			config.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("saveConfig finished.");
	}

	public static void clearConfig() {
		saveConfig();
	}

	public static void initialize() {
		logger.debug("initialize started.");
		new GathererMod();
		logger.debug("initialize finished.");
	}


	private InputProcessor oldInputProcessor;
	private int keyToConfig = -1;

	@Override
	public void receivePostInitialize() {
		logger.debug("receivePostInitialize started.");
		Texture badgeTexture = new Texture(GATHERER_BADGE);

		ModPanel settingsPanel = new ModPanel();

		ModLabeledToggleButton flipButton = new ModLabeledToggleButton("Flip Potion Sack popup orientation",
				400.0f, 720.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
				potionSackPopupFlipped, settingsPanel, (label) -> {
		}, (button) -> {
			potionSackPopupFlipped = button.enabled;
			saveConfig();
		});

		ModLabeledButton[] potionSackKeysButton = new ModLabeledButton[3];
		for (int i = 0; i < potionSackKeysButton.length; i++) {
			int k = i;
			potionSackKeysButton[i] = new ModLabeledButton("Key shortcut potion sack slot #" + (k + 1) + " : " + Input.Keys.toString(potionSackKeys[k]),
					380.0f, 600.0f - i * 60, Settings.CREAM_COLOR, FontHelper.charDescFont,
					settingsPanel, (label) -> {
				if (keyToConfig == k) {
					label.text = "Press key for slot #" + (k + 1);
				} else {
					label.text = "Key shortcut potion sack slot #" + (k + 1) + " : " + Input.Keys.toString(potionSackKeys[k]);
				}
				potionSackKeysButton[k].toggle.wrapHitboxToText(label.text, 1000.0f, 0.0f, label.font);
			}, (button) -> {
				if (keyToConfig == k) {
					keyToConfig = -1;
					Gdx.input.setInputProcessor(oldInputProcessor);
				} else {
					keyToConfig = k;
					oldInputProcessor = Gdx.input.getInputProcessor();
					Gdx.input.setInputProcessor(new InputAdapter() {
						@Override
						public boolean keyUp(int keycode) {
							potionSackKeys[k] = keycode;
							saveConfig();
							keyToConfig = -1;
							Gdx.input.setInputProcessor(oldInputProcessor);
							return true;
						}
					});
				}
			});
		}

		settingsPanel.addUIElement(flipButton);
		for (int i = 0; i < potionSackKeysButton.length; i++) {
			settingsPanel.addUIElement(potionSackKeysButton[i]);
		}

		BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

		potionSack = new PotionSack();
		logger.debug("receivePostInitialize finished.");
	}

	@Override
	public void receiveEditCharacters() {
		logger.debug("receiveEditCharacters started.");
		BaseMod.addCharacter(
				new TheGatherer(TheGatherer.CLASS_NAME, AbstractPlayerEnum.THE_GATHERER),
				GATHERER_BUTTON,
				GATHERER_PORTRAIT,
				AbstractPlayerEnum.THE_GATHERER);

		lesserPotionPool.add(LesserAttackPotion.class);
		lesserPotionPool.add(LesserBlockPotion.class);
		lesserPotionPool.add(LesserDexterityPotion.class);
		lesserPotionPool.add(LesserEnergyPotion.class);
		lesserPotionPool.add(LesserEssenceOfSteel.class);
		lesserPotionPool.add(LesserExplosivePotion.class);
		lesserPotionPool.add(LesserFearPotion.class);
		lesserPotionPool.add(LesserFirePotion.class);
		lesserPotionPool.add(LesserLiquidBronze.class);
		lesserPotionPool.add(LesserPoisonPotion.class);
		lesserPotionPool.add(LesserPowerPotion.class);
		lesserPotionPool.add(LesserSkillPotion.class);
		lesserPotionPool.add(LesserStrengthPotion.class);
		lesserPotionPool.add(LesserSwiftPotion.class);
		lesserPotionPool.add(LesserWeakPotion.class);

		BaseMod.addPotion(BloodPotion.class, Color.WHITE.cpy(), Color.LIGHT_GRAY.cpy(), null, BloodPotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(GhostInAJar.class, Color.WHITE.cpy(), Color.LIGHT_GRAY.cpy(), null, BloodPotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);

		logger.debug("receiveEditCharacters finished.");
	}

	@Override
	public void receiveEditRelics() {
		logger.debug("receiveEditRelics started.");
		BaseMod.addRelicToCustomPool(new AlchemyBag(), CardColorEnum.LIME);
		BaseMod.addRelicToCustomPool(new MiracleBag(), CardColorEnum.LIME);
		BaseMod.addRelicToCustomPool(new IronSlate(), CardColorEnum.LIME);
		BaseMod.addRelicToCustomPool(new SilentSlate(), CardColorEnum.LIME);
		BaseMod.addRelicToCustomPool(new FlowerGarden(), CardColorEnum.LIME);
		logger.debug("receiveEditRelics finished.");
	}

	@Override
	public void receiveEditCards() {
		logger.debug("receiveEditCards started.");
		List<CustomCard> cards = new ArrayList<>();
		cards.add(new Strike_Gatherer());
		cards.add(new Defend_Gatherer());
		cards.add(new FlowerWhip());
		cards.add(new Centralize());
		cards.add(new SpareBottle());

		cards.add(new AcidicSpray());
		cards.add(new BalancedGrowth());
		cards.add(new BigHands());
		cards.add(new BomberForm());
		cards.add(new BronzeBlade());
		cards.add(new Bulldoze());
		cards.add(new CarefulStrike());
		cards.add(new ChargingShot());
		cards.add(new CollectorsShot());
		cards.add(new Convert());
		cards.add(new CoupDeGrace());
		cards.add(new CursedBlade());
		cards.add(new Enchant());
		cards.add(new FeelingFine());
		cards.add(new FirstAidKit());
		cards.add(new FlamingBottle());
		cards.add(new FlowerBeam());
		cards.add(new FlowerGuard());
		cards.add(new FlowerPower());
		cards.add(new Frenzy());
		cards.add(new FruitForce());
		cards.add(new Fruitify());
		cards.add(new GatherMaterial());
		cards.add(new GlassHammer());
		cards.add(new HandcraftedFence());
		cards.add(new HarmonicSymbol());
		cards.add(new Herbalism());
		cards.add(new Investigate());
		cards.add(new LastResort());
		cards.add(new Light());
		cards.add(new Liquidism());
		cards.add(new MagicLamp());
		cards.add(new MiningStrike());
		cards.add(new Misfortune());
		cards.add(new Overflowing());
		cards.add(new PoisonMastery());
		cards.add(new Pollute());
		cards.add(new Polymorphism());
		cards.add(new QuickSynthesis());
		cards.add(new RainbowPower());
		cards.add(new Repair());
		cards.add(new RustyPipe());
		cards.add(new SacredSoil());
		cards.add(new Salvage());
		cards.add(new SalvePotion());
		cards.add(new ScrollOfPurity());
		cards.add(new ScrollOfWall());
		cards.add(new SealedBomb());
		cards.add(new SecretPlan());
		cards.add(new Shadow());
		cards.add(new SimpleSwing());
		cards.add(new SmartManeuver());
		cards.add(new Snatch());
		cards.add(new SolarBeam());
		cards.add(new SolidTechnique());
		cards.add(new TacticalStrike());
		cards.add(new TheCone());
		cards.add(new Transmute());
		cards.add(new TreeGrowth());
		cards.add(new TrickStyle());
		cards.add(new Uplift());
		cards.add(new VenomBarrier());
		cards.add(new WitheringStrike());
		cards.add(new WoolGloves());

		for (CustomCard card : cards) {
			BaseMod.addCard(card);
			UnlockTracker.unlockCard(card.cardID);
		}
		logger.debug("receiveEditCards finished.");
	}

	@Override
	public void receiveSetUnlocks() {

	}

	@Override
	public void receiveEditStrings() {
		logger.debug("receiveEditStrings started.");

		// RelicStrings
		String relicStrings = GetLocString("Gatherer-RelicStrings");
		BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
		// CardStrings
		String cardStrings = GetLocString("Gatherer-CardStrings");
		BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
		// PotionStrings
		String potionStrings = GetLocString("Gatherer-PotionStrings");
		BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
		// PowerStrings
		String powerStrings = GetLocString("Gatherer-PowerStrings");
		BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
		// UIStrings
		String uiStrings = GetLocString("Gatherer-UIStrings");
		BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
		// EventStrings
		String eventStrings = GetLocString("Gatherer-EventStrings");
		BaseMod.loadCustomStrings(EventStrings.class, eventStrings);

		logger.debug("receiveEditStrings finished.");
	}

	@Override
	public void receiveEditKeywords() {
		logger.debug("receiveEditKeywords started.");
		Gson gson = new Gson();
		String json = GetLocString("Gatherer-KeywordStrings");
		the_gatherer.modules.Keyword[] keywords = gson.fromJson(json, the_gatherer.modules.Keyword[].class);

		if (keywords != null) {
			for (the_gatherer.modules.Keyword keyword : keywords) {
				BaseMod.addKeyword(keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
			}
		}
		logger.debug("receiveEditKeywords finished.");
	}

	@Override
	public void receiveOnBattleStart(AbstractRoom room) {
		logger.debug("receiveOnBattleStart started.");
		playedCardsCombat = new HashSet<>();
		potionSack.removeAllPotions();
		potionSack.show = false;
		potionSack.loadKeySettings();
		statusesToExhaust.clear();

		for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
			if (c instanceof AbstractNumberedCard) {
				((AbstractNumberedCard) c).initializeNumberedCard();
			}
		}
		for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
			if (c instanceof AbstractNumberedCard) {
				((AbstractNumberedCard) c).initializeNumberedCard();
			}
		}
		for (AbstractCard c : AbstractDungeon.player.hand.group) {
			if (c instanceof AbstractNumberedCard) {
				((AbstractNumberedCard) c).initializeNumberedCard();
			}
		}
		logger.debug("receiveOnBattleStart finished.");
	}

	@Override
	public void receivePostEnergyRecharge() {
		logger.debug("receivePostEnergyRecharge started.");
		logger.debug("receivePostEnergyRecharge finished.");
	}

	@Override
	public void receivePowersModified() {
		logger.debug("receivePowersModified started.");
		for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
			PoisonPower pp = (PoisonPower) monster.getPower(PoisonPower.POWER_ID);
			if (pp != null) {
				pp.updateDescription();
			}
		}

		if (GathererMod.potionSack.potions != null) {
			for (AbstractPotion p : GathererMod.potionSack.potions) {
				if (p instanceof LesserExplosivePotion) {
					((LesserExplosivePotion) p).updateDescription();
				}
			}
		}
		logger.debug("receivePowersModified finished.");
	}

	@Override
	public void receivePostPotionUse(AbstractPotion p) {
		for (AbstractPower r : AbstractDungeon.player.powers) {
			if (r instanceof OnUsePotionEffect) {
				((OnUsePotionEffect) r).onUsePotion(p);
			}
		}

		ScrollOfPurity.drawCount = 0;
		if (AbstractDungeon.player.hand != null) {
			for (AbstractCard c : AbstractDungeon.player.hand.group) {
				if (c instanceof OnUsePotionEffect) {
					((OnUsePotionEffect) c).onUsePotion(p);
				}
			}
		}
		if (ScrollOfPurity.drawCount > 0) {
			AbstractDungeon.actionManager.addToBottom(new ScrollOfPurityAction(ScrollOfPurity.drawCount));
		}
	}

	@Override
	public void receiveCardUsed(AbstractCard c) {
		playedCardsCombat.add(c.getClass()); // might be obsoleted?
	}

	@Override
	public void receiveRender(SpriteBatch sb) {
	}

	@Override
	public void receivePostObtainCard(AbstractCard card) {
		if (card instanceof OnObtainEffect)
			((OnObtainEffect) card).onObtain();
	}

	@Override
	public void receivePostBattle(AbstractRoom room) {
		potionSack.removeAllPotions();
		potionSack.show = false;
	}

	public static String makeID(String text) {
		return "Gatherer:" + text;
	}

	public static String GetCardPath(String id) {
		return "GathererMod/img/cards/" + id + ".png";
	}

	public static String GetPowerPath(String id) {
		return "GathererMod/img/powers/" + id + ".png";
	}

	public static String GetRelicPath(String id) {
		return "GathererMod/img/relics/" + id + ".png";
	}

	private static String GetLocString(String name) {
		return Gdx.files.internal("GathererMod/localization/" + name + ".json").readString(
				String.valueOf(StandardCharsets.UTF_8));
	}

	public static boolean isBasicDefend(AbstractCard c) {
		return c.hasTag(BaseModCardTags.BASIC_DEFEND) || c.getClass() == Defend_Red.class || c.getClass() == Defend_Green.class || c.getClass() == Defend_Blue.class;
	}

	public static AbstractPotion returnRandomLesserPotion() {
		Class<? extends AbstractPotion> cls = lesserPotionPool.get(AbstractDungeon.potionRng.random.nextInt(lesserPotionPool.size()));
		try {
			return cls.newInstance();
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return new LesserFirePotion();
		}
	}

	private static void setLesserPotionColor(Color color) {
		color.r = (color.r * 3 + 2) / 5;
		color.g = (color.g * 3 + 2) / 5;
		color.b = (color.b * 3 + 2) / 5;
		color.a = 0.8f;
	}

	public static void setLesserPotionColors(Color liquidColor, Color hybridColor, Color spotsColor) {
		setLesserPotionColor(liquidColor);
		if (hybridColor != null) {
			setLesserPotionColor(hybridColor);
		}
		if (spotsColor != null) {
			setLesserPotionColor(spotsColor);
		}
	}

	public static int countUnique(CardGroup group) {
		HashSet<String> ids = new HashSet<>();
		for (AbstractCard c : group.group) {
			ids.add(c.cardID);
		}
		return ids.size();
	}

	public static void onBlockExpired() {
		if (GathererMod.blockExpired > 0 && AbstractDungeon.player != null) {
			AbstractPower p = AbstractDungeon.player.getPower(HandcraftedFencePower.POWER_ID);
			if (p instanceof HandcraftedFencePower) {
				((HandcraftedFencePower) p).onBlockExpired(GathererMod.blockExpired);
				GathererMod.blockExpired = 0;
			}
		}
	}

	public static void ActivatePotionUseEffects(AbstractPotion p) {
		for (AbstractRelic relic : AbstractDungeon.player.relics) {
			if (relic.relicId != ToyOrnithopter.ID) {
				relic.onUsePotion();
			}
		}
		BaseMod.publishPostPotionUse(p);
	}
}
