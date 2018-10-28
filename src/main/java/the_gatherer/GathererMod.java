package the_gatherer;

import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModPanel;
import basemod.abstracts.CustomCard;
import basemod.helpers.BaseModCardTags;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Defend_Blue;
import com.megacrit.cardcrawl.cards.green.Defend_Green;
import com.megacrit.cardcrawl.cards.red.Defend_Red;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.BloodPotion;
import com.megacrit.cardcrawl.potions.GhostInAJar;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_gatherer.actions.ScrollOfPurityAction;
import the_gatherer.cards.*;
import the_gatherer.character.TheGatherer;
import the_gatherer.interfaces.OnObtainEffect;
import the_gatherer.interfaces.OnUsePotionEffect;
import the_gatherer.interfaces.OnceEffect;
import the_gatherer.interfaces.PostObtainCardSubscriber;
import the_gatherer.modules.CaseMod;
import the_gatherer.modules.PotionSack;
import the_gatherer.patches.AbstractPlayerEnum;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.potions.*;
import the_gatherer.relics.AlchemyBag;
import the_gatherer.relics.IronSlate;
import the_gatherer.relics.MiracleBag;
import the_gatherer.relics.SilentSlate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	public static Set<Class<?>> playedCardsCombat = new HashSet<>();

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

		logger.debug("Constructor finished.");
	}

	public static void initialize() {
		logger.debug("initialize started.");
		new GathererMod();
		logger.debug("initialize finished.");
	}

	@Override
	public void receivePostInitialize() {
		logger.debug("receivePostInitialize started.");
		Texture badgeTexture = new Texture(GATHERER_BADGE);
		ModPanel settingsPanel = new ModPanel();
		settingsPanel.addUIElement(new ModLabel("This mod does not have any settings.", 400.0f, 700.0f, settingsPanel, (me) -> {
		}));
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

		BaseMod.addPotion(LesserAttackPotion.class, new Color(1.0f, 0.5f, 0.5f, 0.75f), new Color(1.0f, 0.82f, 0.5f, 0.75f), null, LesserAttackPotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(LesserBlockPotion.class, new Color(0.76f, 0.90f, 0.96f, 0.75f), null, null, LesserBlockPotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(LesserDexterityPotion.class, new Color(0.75f, 1.0f, 0.5f, 0.75f), null, null, LesserDexterityPotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(LesserEnergyPotion.class, new Color(1.0f, 0.92f, 0.5f, 0.75f), null, null, LesserEnergyPotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(LesserEssenceOfSteel.class, new Color(0.76f, 0.90f, 0.96f, 0.75f), null, null, LesserEssenceOfSteel.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(LesserExplosivePotion.class, new Color(1.0f, 0.82f, 0.5f, 0.75f), null, null, LesserExplosivePotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(LesserFearPotion.class, new Color(0.5f, 0.5f, 0.5f, 0.75f), new Color(1.0f, 0.6f, 0.55f, 0.75f), null, LesserFearPotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(LesserFirePotion.class, new Color(1.0f, 0.5f, 0.5f, 0.75f), new Color(1.0f, 0.82f, 0.5f, 0.75f), null, LesserFirePotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(LesserLiquidBronze.class, new Color(1.0f, 0.92f, 0.5f, 0.75f), new Color(0.5f, 1.0f, 1.0f, 0.75f), null, LesserLiquidBronze.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(LesserPoisonPotion.class, new Color(0.6f, 0.9f, 0.6f, 0.75f), null, new Color(0.56f, 0.77f, 0.56f, 0.75f), LesserPoisonPotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(LesserPowerPotion.class, new Color(0.76f, 0.9f, 0.96f, 0.75f), null, null, LesserPowerPotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(LesserSkillPotion.class, new Color(0.75f, 1.0f, 0.5f, 0.75f), null, null, LesserSkillPotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(LesserStrengthPotion.class, new Color(0.62f, 0.62f, 0.62f, 0.75f), null, new Color(1.0f, 0.75f, 0.65f, 0.75f), LesserStrengthPotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(LesserSwiftPotion.class, new Color(0.52f, 0.63f, 0.8f, 0.75f), null, new Color(0.5f, 1.0f, 1.0f, 0.75f), LesserSwiftPotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(LesserWeakPotion.class, new Color(0.96f, 0.75f, 0.96f, 0.75f), new Color(0.84f, 0.59f, 0.68f, 0.75f), null, LesserWeakPotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(FirstAidPotion.class, new Color(1.0f, 1.0f, 1.0f, 0.75f), new Color(0.875f, 0.875f, 0.875f, 0.75f), null, FirstAidPotion.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
		BaseMod.addPotion(FirstAidPotionPlus.class, new Color(1.0f, 1.0f, 1.0f, 0.75f), new Color(0.875f, 0.875f, 0.875f, 0.75f), null, FirstAidPotionPlus.POTION_ID, AbstractPlayerEnum.THE_GATHERER);
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
		cards.add(new BronzeBlade());
		cards.add(new Bulldoze());
		cards.add(new BomberForm());
		cards.add(new CarefulStrike());
		cards.add(new ChargingShot());
		cards.add(new CollectorsShot());
		cards.add(new Convert());
		cards.add(new CoupDeGrace());
		cards.add(new CursedBlade());
		cards.add(new FeelingFine());
		cards.add(new FirstAidKit());
		cards.add(new FlamingBottle());
		cards.add(new FlowerBeam());
		cards.add(new FlowerGuard());
		cards.add(new FlowerPower());
		cards.add(new FrenzyWhip());
		cards.add(new FruitForce());
		cards.add(new Fruitify());
		cards.add(new GatherMaterial());
		cards.add(new GlassHammer());
		cards.add(new HarmonicSymbol());
		cards.add(new Herbalism());
		cards.add(new Investigate());
		cards.add(new LastResort());
		cards.add(new Light());
		cards.add(new Liquidism());
		cards.add(new MagicLamp());
		cards.add(new Overflowing());
		cards.add(new PoisonMastery());
		cards.add(new Pollute());
		cards.add(new QuickSynthesis());
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
		cards.add(new Snatch());
		cards.add(new SolarBeam());
		cards.add(new SolidTechnique());
		cards.add(new TacticalStrike());
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

		BaseMod.addKeyword(new String[]{"unique", "Unique"}, "Cards with different IDs are considered unique. Whatever that means.");
		BaseMod.addKeyword(new String[]{"once", "Once"}, "Only activates when you play this unique card first time in the combat.");
		BaseMod.addKeyword(new String[]{"flower", "Flower"}, "Card containing \"Flower\" in its name. It can be upgraded 3 times.");
		BaseMod.addKeyword(new String[]{"lep", "LEP", "LEPs"}, "Stands for Lesser Explosive Potion. Became a keyword because the text is too long.");

		logger.debug("receiveEditKeywords finished.");
	}

	@Override
	public void receiveOnBattleStart(AbstractRoom room) {
		logger.debug("receiveOnBattleStart started.");
		playedCardsCombat = new HashSet<>();
		potionSack.removeAllPotions();
		potionSack.show = false;
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
		if(ScrollOfPurity.drawCount > 0) {
			AbstractDungeon.actionManager.addToBottom(new ScrollOfPurityAction(ScrollOfPurity.drawCount));
		}
	}

	@Override
	public void receiveCardUsed(AbstractCard c) {
		if (c instanceof OnceEffect) {
			OnceEffect oe = (OnceEffect) c;
			if (playedCardsCombat.contains(c.getClass())) {
				oe.notFirstTimeEffect();
			} else {
				oe.firstTimeEffect();
			}
		}
		playedCardsCombat.add(c.getClass());
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
}
