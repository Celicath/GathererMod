package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.actions.ObtainLesserPotionAction;
import the_gatherer.interfaces.OnceEffect;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.patches.PotionRarityEnum;

public class Snatch extends CustomCard implements OnceEffect {
	private static final String RAW_ID = "Snatch";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.LIME;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 9;
	private static final int UPGRADE_BONUS = 3;

	public Snatch() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseDamage = POWER;
	}

	@Override
	public void applyPowers() {
		updateOnceText();
	}

	public void updateOnceText() {
		if (GathererMod.playedCardsCombat.contains(Snatch.class)) {
			this.rawDescription = EXTENDED_DESCRIPTION[0];
		} else {
			this.rawDescription = DESCRIPTION;
		}

		this.initializeDescription();
		GathererMod.updateAllOnceText(Snatch.class);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_VERTICAL));

		updateOnceText();
		GathererMod.updateAllOnceText(Snatch.class);
	}

	public AbstractCard makeCopy() {
		return new Snatch();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
		}
	}

	public void notFirstTimeEffect() {
	}

	public void firstTimeEffect() {
		AbstractDungeon.actionManager.addToBottom(new ObtainLesserPotionAction(AbstractDungeon.returnRandomPotion(PotionRarityEnum.LESSER, true)));
	}
}