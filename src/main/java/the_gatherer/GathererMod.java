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
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.blue.Stack;
import com.megacrit.cardcrawl.cards.blue.*;
import com.megacrit.cardcrawl.cards.green.*;
import com.megacrit.cardcrawl.cards.red.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
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
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.ToyOrnithopter;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_gatherer.actions.ScrollOfPurityFollowUpAction;
import the_gatherer.cards.*;
import the_gatherer.cards.Helper.AbstractNumberedCard;
import the_gatherer.cards.Helper.AbstractTaggedCard;
import the_gatherer.character.TheGatherer;
import the_gatherer.interfaces.MyStartupCard;
import the_gatherer.interfaces.OnObtainEffect;
import the_gatherer.interfaces.OnUsePotionEffect;
import the_gatherer.interfaces.PostObtainCardSubscriber;
import the_gatherer.modules.CaseMod;
import the_gatherer.modules.ModLabeledButton;
import the_gatherer.modules.PotionSack;
import the_gatherer.patches.AbstractPlayerEnum;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.patches.CustomTags;
import the_gatherer.potions.*;
import the_gatherer.powers.PoisonMasteryPower;
import the_gatherer.powers.StoneFencePower;
import the_gatherer.relics.*;
import the_gatherer.screens.ExcessPotionHandleScreen;

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
		PreMonsterTurnSubscriber, PostUpdateSubscriber, PostEnergyRechargeSubscriber,
		PostCreateStartingDeckSubscriber {

	public static final Logger logger = LogManager.getLogger(GathererMod.class.getName());

	public static final String MODNAME = "GathererMod";
	public static final String AUTHOR = "Celicath";
	public static final String DESCRIPTION = "Adds The Gatherer as a new playable character.";

	public static final String GATHERER_BADGE = "GathererMod/img/badge.png";
	public static final String GATHERER_BUTTON = "GathererMod/img/character/gatherer/button.png";
	public static final String GATHERER_PORTRAIT = "GathererMod/img/character/gatherer/GathererBG.png";
	public static final String GATHERER_SHOULDER_1 = "GathererMod/img/character/gatherer/shoulder.png";
	public static final String GATHERER_SHOULDER_2 = "GathererMod/img/character/gatherer/shoulder2.png";
	public static final String GATHERER_CORPSE = "GathererMod/img/character/gatherer/corpse.png";

	public static PotionSack potionSack;
	public static Set<Class<? extends AbstractCard>> playedCardsCombat = new HashSet<>();
	public static int remainedBlock = 0;

	public static ArrayList<SackPotion> lesserPotionPool = new ArrayList<>();
	public static HashSet<AbstractGameEffect> gainCardEffectToExhaust = new HashSet<>();

	public static Properties gathererDefaults = new Properties();
	public static int[] potionSackKeys = new int[]{Input.Keys.I, Input.Keys.O, Input.Keys.P};
	public static boolean potionSackPopupFlipped = false;

	// Transmute & Enchant
	public static AbstractCard cardSelectScreenCard = null;
	public static int enchantAmount = 0;
	public static int transmuteAmount = 0;
	public static float transmuteAnimTimer = 0;
	public static boolean bottleCollector = false;

	// Drug Power
	public static AbstractPotion lastPotionUsedThisTurn = null;

	// Type Count
	public static int uniqueWithSingleCopyCount = -1;
	public static int flowerCount = -1;

	// Excess Potion (Not used now)
	public static ExcessPotionHandleScreen excessPotionHandleScreen = null;

	// Potion RNG
	public static Random lesserPotionObtainRNG = new Random();

	public static void setLastPotionUsedThisTurn(AbstractPotion p) {
		logger.debug("Last potion used: " + (p == null ? "null" : p.name));
		if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
			lastPotionUsedThisTurn = p;
			for (AbstractCard c : AbstractDungeon.player.hand.group) {
				c.applyPowers();
			}
		} else {
			lastPotionUsedThisTurn = null;
		}
	}

	// Drug Power Infinite HP Gain Prevention
	public static int drugPowerHPGain = 0;

	// GrowBook Content
	public static ArrayList<String> growBookCharacter;
	public static ArrayList<AbstractCard[]> growBookContent;

	// Explorers Path
	public static HashSet<AbstractCard> explorersPathBestOption = new HashSet<>();

	public GathererMod() {
		logger.debug("Constructor started.");
		BaseMod.subscribe(this);
		CaseMod.subscribe(this);

		BaseMod.addColor(CardColorEnum.GATHERER_LIME,
				Color.LIME.cpy(), Color.LIME.cpy(), Color.LIME.cpy(), Color.LIME.cpy(), Color.LIME.cpy(), Color.LIME.cpy(), Color.LIME.cpy(),
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

			if (potionSackKeys[0] == KEY_I && potionSackKeys[1] == KEY_O && potionSackKeys[2] == KEY_P) {
				potionSackKeys = new int[]{Input.Keys.I, Input.Keys.O, Input.Keys.P};
			}

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
		excessPotionHandleScreen = new ExcessPotionHandleScreen();

		addGrowBookContent("Iron", new AbstractCard[]{
				new Armaments(), new Havoc(), new TwinStrike(), new Feed()
		});
		addGrowBookContent("Silent", new AbstractCard[]{
				new Choke(), new WellLaidPlans(), new PoisonedStab(), new Alchemize()
		});
		addGrowBookContent("Defect", new AbstractCard[]{
				new Stack(), new SteamBarrier(), new AllForOne(), new Seek()
		});

		logger.debug("receivePostInitialize finished.");
	}

	private void addGrowBookContent(String name, AbstractCard[] cards) {
		if (growBookCharacter == null || growBookContent == null) {
			growBookCharacter = new ArrayList<>();
			growBookContent = new ArrayList<>();
		}
		growBookCharacter.add(name);
		growBookContent.add(cards);
	}


	@Override
	public void receiveEditCharacters() {
		logger.debug("receiveEditCharacters started.");
		BaseMod.addCharacter(
				new TheGatherer(TheGatherer.charStrings.NAMES[1], AbstractPlayerEnum.THE_GATHERER),
				GATHERER_BUTTON,
				GATHERER_PORTRAIT,
				AbstractPlayerEnum.THE_GATHERER);

		lesserPotionPool.add(new LesserBlockPotion());
		lesserPotionPool.add(new LesserDexterityPotion());
		lesserPotionPool.add(new LesserEnergyPotion());
		lesserPotionPool.add(new LesserEssenceOfSteel());
		lesserPotionPool.add(new LesserExplosivePotion());
		lesserPotionPool.add(new LesserFearPotion());
		lesserPotionPool.add(new LesserFirePotion());
		lesserPotionPool.add(new LesserLiquidBronze());
		lesserPotionPool.add(new LesserPoisonPotion());
		lesserPotionPool.add(new LesserPowerPotion());
		lesserPotionPool.add(new LesserSpeedPotion());
		lesserPotionPool.add(new LesserSteroidPotion());
		lesserPotionPool.add(new LesserStrengthPotion());
		lesserPotionPool.add(new LesserSwiftPotion());
		lesserPotionPool.add(new LesserWeakPotion());

		BaseMod.addPotion(BloodPotion.class, Color.WHITE.cpy(), Color.LIGHT_GRAY.cpy(), null, BloodPotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(GhostInAJar.class, Color.WHITE.cpy(), Color.LIGHT_GRAY.cpy(), null, GhostInAJar.POTION_ID, AbstractPlayerEnum.THE_GATHERER);

		logger.debug("receiveEditCharacters finished.");
	}

	@Override
	public void receiveEditRelics() {
		logger.debug("receiveEditRelics started.");
		BaseMod.addRelicToCustomPool(new AlchemyBag(), CardColorEnum.GATHERER_LIME);
		BaseMod.addRelicToCustomPool(new MiracleBag(), CardColorEnum.GATHERER_LIME);
		//BaseMod.addRelicToCustomPool(new IronSlate(), CardColorEnum.GATHERER_LIME);
		//BaseMod.addRelicToCustomPool(new SilentSlate(), CardColorEnum.GATHERER_LIME);
		BaseMod.addRelicToCustomPool(new FloralEgg(), CardColorEnum.GATHERER_LIME);
		BaseMod.addRelicToCustomPool(new Leftovers(), CardColorEnum.GATHERER_LIME);
		BaseMod.addRelicToCustomPool(new FlyingFruit(), CardColorEnum.GATHERER_LIME);
		BaseMod.addRelicToCustomPool(new ExplorersPath(), CardColorEnum.GATHERER_LIME);
		BaseMod.addRelicToCustomPool(new MistGenerator(), CardColorEnum.GATHERER_LIME);
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
		cards.add(new AlchemyTrial());
		cards.add(new AromaProtection());
		cards.add(new BalancedGrowth());
		cards.add(new BambuSword());
		cards.add(new BigPouch());
		cards.add(new BlackTea());
		cards.add(new BomberForm());
		cards.add(new Bulldoze());
		cards.add(new CarefulStrike());
		cards.add(new CollectorsShot());
		cards.add(new ColorfulGarden());
		cards.add(new Convert());
		cards.add(new CoupDeGrace());
		cards.add(new CursedBlade());
		cards.add(new DrugPower());
		cards.add(new Duality());
		cards.add(new Duplicator());
		cards.add(new EchoOfNature());
		cards.add(new Enchant());
		cards.add(new EnergyBasket());
		cards.add(new Examine());
		cards.add(new FeelingFine());
		cards.add(new FlamingBottle());
		cards.add(new FlowerBeam());
		cards.add(new FlowerFalling());
		cards.add(new FlowerPower());
		cards.add(new FlowerShield());
		cards.add(new Frenzy());
		cards.add(new FruitForce());
		cards.add(new GatherMaterial());
		cards.add(new Glitched());
		cards.add(new GlowingPlant());
		cards.add(new GrassHammer());
		cards.add(new GrowBook());
		cards.add(new HeartToFruit());
		cards.add(new Herbalism());
		cards.add(new Light());
		cards.add(new Liquidism());
		cards.add(new Liquidism());
		cards.add(new LuckyClover());
		cards.add(new MindSearch());
		cards.add(new MiningStrike());
		cards.add(new Misfortune());
		cards.add(new Nutrients());
		cards.add(new OddSpoils());
		cards.add(new Overflowing());
		cards.add(new Photoscythe());
		cards.add(new PoisonMastery());
		cards.add(new Pollute());
		cards.add(new Polymorphism());
		cards.add(new QuickSynthesis());
		cards.add(new RecipeChange());
		cards.add(new RecoveryHerb());
		cards.add(new RottenStipe());
		cards.add(new SaveValuables());
		cards.add(new ScentOfRosmari());
		cards.add(new ScherryThrow());
		cards.add(new ScrollOfPurity());
		cards.add(new ScrollOfWall());
		cards.add(new SealedBomb());
		cards.add(new Shadow());
		cards.add(new SmartManeuver());
		cards.add(new Snatch());
		cards.add(new Solidify());
		cards.add(new StarFruit());
		cards.add(new StoneFence());
		cards.add(new TacticalStrike());
		cards.add(new Thrower());
		cards.add(new Transmute());
		cards.add(new TreeGrowth());
		cards.add(new UpgradeBag());
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

	public static String getLocCode() {
		if (Settings.language == Settings.GameLanguage.KOR) {
			return "kor";
		} else if (Settings.language == Settings.GameLanguage.RUS) {
			return "rus";
		} else {
			return "eng";
		}
	}


	@Override
	public void receiveEditStrings() {
		logger.debug("receiveEditStrings started.");

		String loc = getLocCode();

		// RelicStrings
		String relicStrings = GetLocString(loc, "Gatherer-RelicStrings");
		BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
		// CardStrings
		String cardStrings = GetLocString(loc, "Gatherer-CardStrings");
		BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
		// PotionStrings
		String potionStrings = GetLocString(loc, "Gatherer-PotionStrings");
		BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
		// PowerStrings
		String powerStrings = GetLocString(loc, "Gatherer-PowerStrings");
		BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
		// UIStrings
		String uiStrings = GetLocString(loc, "Gatherer-UIStrings");
		BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
		// EventStrings
		String eventStrings = GetLocString(loc, "Gatherer-EventStrings");
		BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
		// CharacterStrings
		String characterStrings = GetLocString(loc, "Gatherer-CharacterStrings");
		BaseMod.loadCustomStrings(CharacterStrings.class, characterStrings);

		logger.debug("receiveEditStrings finished.");
	}

	@Override
	public void receiveEditKeywords() {
		logger.debug("receiveEditKeywords started.");
		Gson gson = new Gson();
		String loc = getLocCode();

		String json = GetLocString(loc, "Gatherer-KeywordStrings");
		Keyword[] keywords = gson.fromJson(json, Keyword[].class);

		if (keywords != null) {
			for (Keyword keyword : keywords) {
				BaseMod.addKeyword(keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
			}
		}
		logger.debug("receiveEditKeywords finished.");
	}

	@Override
	public void receiveOnBattleStart(AbstractRoom room) {
		logger.debug("receiveOnBattleStart started.");

		lesserPotionObtainRNG = new Random(Settings.seed, AbstractDungeon.cardRandomRng.counter + AbstractDungeon.floorNum * 100);
		CardGroup[] cardGroups = new CardGroup[]{
				AbstractDungeon.player.drawPile,
				AbstractDungeon.player.hand,
				AbstractDungeon.player.discardPile,
				AbstractDungeon.player.exhaustPile
		};
		for (CardGroup cardGroup : cardGroups) {
			for (AbstractCard c : cardGroup.group) {
				if (c instanceof MyStartupCard) {
					if (((MyStartupCard) c).myAtBattleStartPreDraw()) {
						AbstractDungeon.effectList.add(0, new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
					}
				}
			}
		}

		playedCardsCombat = new HashSet<>();
		potionSack.removeAllPotions();
		potionSack.hide();
		potionSack.loadKeySettings();
		gainCardEffectToExhaust.clear();
		enchantAmount = 0;
		transmuteAmount = 0;
		drugPowerHPGain = 0;
		setLastPotionUsedThisTurn(null);
		explorersPathBestOption.clear();
		excessPotionHandleScreen.close();
		ExcessPotionHandleScreen.potions.clear();

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
	public boolean receivePreMonsterTurn(AbstractMonster m) {
		logger.debug("receivePreMonsterTurn started.");
		setLastPotionUsedThisTurn(null);

		logger.debug("receivePreMonsterTurn finished.");
		return true;
	}

	@Override
	public void receivePowersModified() {
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
			for (AbstractPotion p : ExcessPotionHandleScreen.potions) {
				if (p instanceof LesserExplosivePotion) {
					((LesserExplosivePotion) p).updateDescription();
				}
			}
		}
	}

	@Override
	public void receivePostPotionUse(AbstractPotion p) {
		for (AbstractPower r : AbstractDungeon.player.powers) {
			if (r instanceof OnUsePotionEffect) {
				((OnUsePotionEffect) r).onUsePotion(p);
			}
		}

		AbstractDungeon.player.hand.applyPowers();

		ScrollOfPurity.exhaustCount = 0;
		if (AbstractDungeon.player.hand != null) {
			for (AbstractCard c : AbstractDungeon.player.hand.group) {
				if (c instanceof OnUsePotionEffect) {
					((OnUsePotionEffect) c).onUsePotion(p);
				}
			}
		}
		AbstractDungeon.actionManager.addToBottom(new ScrollOfPurityFollowUpAction(ScrollOfPurity.exhaustCount));

		setLastPotionUsedThisTurn(p);
	}

	@Override
	public void receiveCardUsed(AbstractCard c) {
		playedCardsCombat.add(c.getClass()); // might be obsoleted?
	}

	@Override
	public void receivePostUpdate() {
		if (AbstractDungeon.player == null) {
			if (potionSack != null && potionSack.showing) {
				potionSack.removeAllPotions();
				potionSack.show();
			}
			bottleCollector = false;
		}
		if (uniqueWithSingleCopyCount == -1 || flowerCount == -1) {
			updateTypeCount();
		}
	}

	public static void updateTypeCount() {
		if (AbstractDungeon.player != null) {
			flowerCount = 0;
			HashSet<String> ids = new HashSet<>();
			HashSet<String> idsDup = new HashSet<>();
			for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
				String uid = getUniqueID(c);
				if (ids.contains(uid)) {
					idsDup.add(uid);
				} else {
					ids.add(uid);
				}

				if (c.hasTag(CustomTags.FLOWER)) {
					flowerCount++;
				}
			}
			uniqueWithSingleCopyCount = ids.size() - idsDup.size();
		}
	}

	@Override
	public void receivePostCreateStartingDeck(AbstractPlayer.PlayerClass chosenClass, CardGroup addCardsToMe) {
		updateTypeCount();
	}

	@Override
	public void receiveRender(SpriteBatch sb) {
		if (AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
				AbstractDungeon.overlayMenu != null && AbstractDungeon.overlayMenu.combatPanelsShown) {
			GathererMod.potionSack.render(sb);
		}
	}

	@Override
	public void receivePostObtainCard(AbstractCard card) {
		if (card instanceof OnObtainEffect)
			((OnObtainEffect) card).onObtain();
		logger.debug("COUNT_TEST = " + AbstractDungeon.player.masterDeck.group.size());
		updateTypeCount();
	}

	@Override
	public void receivePostBattle(AbstractRoom room) {
		potionSack.removeAllPotions();
		potionSack.hide();
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

	public static String GetEventPath(String id) {
		return "GathererMod/img/events/" + id + ".png";
	}

	private static String GetLocString(String locCode, String name) {
		return Gdx.files.internal("GathererMod/localization/" + locCode + "/" + name + ".json").readString(
				String.valueOf(StandardCharsets.UTF_8));
	}

	public static boolean isBasic(AbstractCard c) {
		return c.hasTag(BaseModCardTags.BASIC_DEFEND) || c.getClass() == Defend_Red.class || c.getClass() == Defend_Green.class || c.getClass() == Defend_Blue.class ||
				c.hasTag(BaseModCardTags.BASIC_STRIKE) || c.getClass() == Strike_Red.class || c.getClass() == Strike_Green.class || c.getClass() == Strike_Blue.class;
	}

	public static SackPotion returnRandomLesserPotion() {
		SackPotion sp = lesserPotionPool.get(lesserPotionObtainRNG.random.nextInt(lesserPotionPool.size()));
		return (SackPotion) sp.makeCopy();
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

	public static String getUniqueID(AbstractCard c) {
		if (c instanceof AbstractTaggedCard) {
			return c.cardID + " " + ((AbstractTaggedCard) c).getTagName(c.misc);
		} else {
			return c.cardID;
		}
	}

	public static int countUnique(CardGroup group) {
		return countUnique(group.group);
	}

	public static int countUnique(Iterable<AbstractCard> group) {
		HashSet<String> ids = new HashSet<>();
		for (AbstractCard c : group) {
			ids.add(getUniqueID(c));
		}
		return ids.size();
	}

	public static void ActivatePotionUseEffects(AbstractPotion p, boolean disableToyOrnithopter) {
		for (AbstractRelic relic : AbstractDungeon.player.relics) {
			if (!disableToyOrnithopter || !relic.relicId.equals(ToyOrnithopter.ID)) {
				relic.onUsePotion();
			}
		}
		BaseMod.publishPostPotionUse(p);
	}

	public static int calcPoisonDamageWithPower(int poisonAmount) {
		PoisonMasteryPower pmp = (PoisonMasteryPower) AbstractDungeon.player.getPower(PoisonMasteryPower.POWER_ID);
		if (pmp != null) {
			return calcPoisonDamage(poisonAmount, pmp.amount);
		} else {
			return poisonAmount;
		}
	}

	public static int calcPoisonDamage(int poisonAmount, int powerAmount) {
		return poisonAmount * (1 + powerAmount);
	}

	public static String stripPrefix(String id) {
		if (id.equals(Strike_Gatherer.ID)) {
			return "Strike";
		} else if (id.equals(Defend_Gatherer.ID)) {
			return "Defend";
		} else {
			int index = id.lastIndexOf(':');
			if (index != -1 && index < id.length() - 1) {
				return id.substring(index + 1);
			}
			return id;
		}
	}

	public static void activateStoneFence() {
		if (AbstractDungeon.player != null) {
			StoneFencePower sfp = (StoneFencePower) AbstractDungeon.player.getPower(StoneFencePower.POWER_ID);
			if (sfp != null) {
				sfp.activate();
			}
		}
	}

	@Override
	public void receivePostEnergyRecharge() {
		logger.debug("receivePostEnergyRecharge");
	}
}
