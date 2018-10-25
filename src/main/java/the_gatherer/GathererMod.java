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
import the_gatherer.cards.*;
import the_gatherer.character.TheGatherer;
import the_gatherer.interfaces.OnObtainEffect;
import the_gatherer.interfaces.OnUsePotionEffect;
import the_gatherer.interfaces.OnceEffect;
import the_gatherer.interfaces.PostObtainCardSubscriber;
import the_gatherer.modules.CaseMod;
import the_gatherer.modules.PotionSack;
import the_gatherer.patches.AbstractCardEnum;
import the_gatherer.patches.AbstractPlayerEnum;
import the_gatherer.potions.*;
import the_gatherer.powers.SpareBottlePower;
import the_gatherer.relics.*;

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


	public static final String GATHERER_BADGE = "img/badge.png";
	public static final String GATHERER_BUTTON = "img/character/gatherer/button.png";
	public static final String GATHERER_PORTRAIT = "img/character/gatherer/PortraitBG.jpg";
	public static final String GATHERER_SHOULDER_1 = "img/character/gatherer/shoulder.png";
	public static final String GATHERER_SHOULDER_2 = "img/character/gatherer/shoulder2.png";
	public static final String GATHERER_CORPSE = "img/character/gatherer/corpse.png";

	public static PotionSack potionSack;
	public static Set<Class<?>> playedCardsCombat = new HashSet();

	public GathererMod() {
		logger.info("Constructor started.");
		BaseMod.subscribe(this);
		CaseMod.subscribe(this);

		BaseMod.addColor(AbstractCardEnum.LIME,
				Color.LIME, Color.LIME, Color.LIME, Color.LIME, Color.LIME, Color.LIME, Color.LIME,
				"img/cardui/512/bg_attack_lime.png",
				"img/cardui/512/bg_skill_lime.png",
				"img/cardui/512/bg_power_lime.png",
				"img/cardui/512/card_lime_orb.png",
				"img/cardui/1024/bg_attack_lime.png",
				"img/cardui/1024/bg_skill_lime.png",
				"img/cardui/1024/bg_power_lime.png",
				"img/cardui/1024/card_lime_orb.png",
				"img/cardui/512/card_lime_small_orb.png");

		logger.info("Constructor finished.");
	}

	public static void initialize() {
		logger.info("initialize started.");
		new GathererMod();
		logger.info("initialize finished.");
	}

	@Override
	public void receivePostInitialize() {
		logger.info("receivePostInitialize started.");
		Texture badgeTexture = new Texture(GATHERER_BADGE);
		ModPanel settingsPanel = new ModPanel();
		settingsPanel.addUIElement(new ModLabel("This mod does not have any settings.", 400.0f, 700.0f, settingsPanel, (me) -> {
		}));
		BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

		potionSack = new PotionSack();
		logger.info("receivePostInitialize finished.");
	}

	@Override
	public void receiveEditCharacters() {
		logger.info("receiveEditCharacters started.");
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

		logger.info("receiveEditCharacters finished.");
	}

	@Override
	public void receiveEditRelics() {
		logger.info("receiveEditRelics started.");
		BaseMod.addRelicToCustomPool(new AlchemyBag(), AbstractCardEnum.LIME);
		BaseMod.addRelicToCustomPool(new MiracleBag(), AbstractCardEnum.LIME);
		BaseMod.addRelicToCustomPool(new IronSlate(), AbstractCardEnum.LIME);
		BaseMod.addRelicToCustomPool(new SilentSlate(), AbstractCardEnum.LIME);
		logger.info("receiveEditRelics finished.");
	}

	@Override
	public void receiveEditCards() {
		logger.info("receiveEditCards started.");
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
		cards.add(new CollectorsShot());
		cards.add(new Convert());
		cards.add(new CoupDeGrace());
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
		cards.add(new LastResort());
		cards.add(new Light());
		cards.add(new Liquidism());
		cards.add(new MagicLamp());
		cards.add(new PoisonMastery());
		cards.add(new Pollute());
		cards.add(new QuickSynthesis());
		cards.add(new RustyPipe());
		cards.add(new SacredSoil());
		cards.add(new Salvage());
		cards.add(new SalvePotion());
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
		cards.add(new VenomBarrier());
		cards.add(new WitheringStrike());
		cards.add(new WoolGloves());

		for (CustomCard card : cards) {
			BaseMod.addCard(card);
			UnlockTracker.unlockCard(card.cardID);
		}
		logger.info("receiveEditCards finished.");
	}

	@Override
	public void receiveSetUnlocks() {

	}

	@Override
	public void receiveEditStrings() {
		logger.info("receiveEditStrings started.");

		// RelicStrings
		String relicStrings = Gdx.files.internal("localization/Gatherer-RelicStrings.json").readString(
				String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
		// CardStrings
		String cardStrings = Gdx.files.internal("localization/Gatherer-CardStrings.json").readString(
				String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
		// PotionStrings
		String potionStrings = Gdx.files.internal("localization/Gatherer-PotionStrings.json").readString(
				String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
		// PowerStrings
		String powerStrings = Gdx.files.internal("localization/Gatherer-PowerStrings.json").readString(
				String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
		// UIStrings
		String uiStrings = Gdx.files.internal("localization/Gatherer-UIStrings.json").readString(
				String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
		// EventStrings
		String eventStrings = Gdx.files.internal("localization/Gatherer-EventStrings.json").readString(
				String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(EventStrings.class, eventStrings);

		logger.info("receiveEditStrings finished.");
	}

	@Override
	public void receiveEditKeywords() {
		logger.info("receiveEditKeywords started.");

		BaseMod.addKeyword(new String[]{"unique", "Unique"}, "Cards with different IDs are considered unique. Whatever that means.");
		BaseMod.addKeyword(new String[]{"once", "Once"}, "Only activate once in combat, for each unique card.");
		BaseMod.addKeyword(new String[]{"flower", "Flower"}, "Card containing \"Flower\" in its name. It can be upgraded 3 times.");
		BaseMod.addKeyword(new String[]{"lep", "LEP", "LEPs"}, "Stands for Lesser Explosive Potion. Became a keyword because the text was too long.");

		logger.info("receiveEditKeywords finished.");
	}

	@Override
	public void receiveOnBattleStart(AbstractRoom room) {
		logger.info("receiveOnBattleStart started.");
		playedCardsCombat = new HashSet<>();
		potionSack.removeAllPotions();
		potionSack.show = false;
		LesserExplosivePotion.upgrades = 0;
		logger.info("receiveOnBattleStart finished.");
	}

	@Override
	public void receivePostEnergyRecharge() {
		logger.info("receivePostEnergyRecharge started.");
		logger.info("receivePostEnergyRecharge finished.");
	}

	@Override
	public void receivePowersModified() {
		logger.info("receivePowersModified started.");
		for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
			PoisonPower pp = (PoisonPower) monster.getPower(PoisonPower.POWER_ID);
			if (pp != null) {
				pp.updateDescription();
			}
		}
		logger.info("receivePowersModified finished.");
	}

	@Override
	public void receivePostPotionUse(AbstractPotion p) {
		for (AbstractPower r : AbstractDungeon.player.powers) {
			if (r instanceof OnUsePotionEffect) {
				((OnUsePotionEffect) r).onUsePotion();
			}
		}
	}

	@Override
	public void receiveCardUsed(AbstractCard c) {
		if (c instanceof OnceEffect) {
			OnceEffect oe = (OnceEffect) c;
			if (playedCardsCombat.contains(c.getClass())) {
				oe.notSingleEffect();
			} else {
				oe.singleEffect();
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

	public static boolean isBasicDefend(AbstractCard c) {
		return c.hasTag(BaseModCardTags.BASIC_DEFEND) || c.getClass() == Defend_Red.class || c.getClass() == Defend_Green.class || c.getClass() == Defend_Blue.class;
	}
}
