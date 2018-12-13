package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.actions.ObtainLesserPotionAction;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.patches.CustomTags;
import the_gatherer.potions.LesserSwiftPotion;
import the_gatherer.powers.DamageHalfPower;

public class SmartManeuver extends CustomCard {
	private static final String RAW_ID = "SmartManeuver";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	public SmartManeuver() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.magicNumber = this.baseMagicNumber;

		this.tags.add(CustomTags.POTION_GEN);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new DamageHalfPower(m, 1, false), 1));
		AbstractDungeon.actionManager.addToBottom(new ObtainLesserPotionAction(new LesserSwiftPotion(), true));
	}

	public AbstractCard makeCopy() {
		return new SmartManeuver();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
			AlwaysRetainField.alwaysRetain.set(this, true);
		}
	}
}
