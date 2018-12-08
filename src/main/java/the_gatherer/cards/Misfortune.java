package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import the_gatherer.GathererMod;
import the_gatherer.actions.ObtainLesserPotionAction;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.patches.CustomTags;
import the_gatherer.potions.LesserFearPotion;
import the_gatherer.potions.LesserPoisonPotion;
import the_gatherer.potions.LesserWeakPotion;
import the_gatherer.potions.SackPotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Misfortune extends CustomCard {
	private static final String RAW_ID = "Misfortune";
	public static final String ID = GathererMod.makeID(RAW_ID);

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 0;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 3;
	private static final int UPGRADE_BONUS = 2;
	private static final int POTION_GET = 1;
	private static final int POTION_BONUS = 1;

	public static HashMap<String, SackPotion> debuffPotionMap;

	static {
		debuffPotionMap = new HashMap<>();
		debuffPotionMap.put(PoisonPower.POWER_ID, new LesserPoisonPotion());
		debuffPotionMap.put(WeakPower.POWER_ID, new LesserWeakPotion());
		debuffPotionMap.put(VulnerablePower.POWER_ID, new LesserFearPotion());
	}

	public Misfortune() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseDamage = POWER;
		this.baseMagicNumber = POTION_GET;
		this.magicNumber = this.baseMagicNumber;

		this.tags.add(CustomTags.POTION_GEN);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		ArrayList<SackPotion> potions = new ArrayList<>();
		for (Map.Entry<String, SackPotion> entry : debuffPotionMap.entrySet()) {
			if (m.hasPower(entry.getKey())) {
				potions.add(entry.getValue());
			}
		}
		Collections.shuffle(potions, AbstractDungeon.cardRandomRng.random);

		for(int i = 0; i < this.magicNumber && i < potions.size(); i++) {
			AbstractDungeon.actionManager.addToBottom(new ObtainLesserPotionAction(potions.get(i).makeCopy(), true));
		}
	}

	public AbstractCard makeCopy() {
		return new Misfortune();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
			this.upgradeMagicNumber(POTION_BONUS);
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
