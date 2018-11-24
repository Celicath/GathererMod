package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.patches.CardColorEnum;

import java.util.HashSet;

public class OddSpoils extends CustomCard {
	private static final String RAW_ID = "OddSpoils";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 10;
	private static final int UPGRADE_BONUS = 3;
	private static final int CARD = 1;
	private static final int CARD_BONUS = 1;

	public OddSpoils() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.baseDamage = POWER;
		this.baseMagicNumber = CARD;
		this.magicNumber = this.baseMagicNumber;

		this.tags.add(CardTags.STRIKE);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_HEAVY));

		AbstractCard prevCard = null;

		for (int i = 0; i < magicNumber; i++) {
			AbstractCard card = null;
			HashSet<Class<? extends AbstractCard>> set1 = new HashSet<>();
			HashSet<Class<? extends AbstractCard>> set2 = new HashSet<>();

			for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
				set1.add(c.getClass());
			}
			for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
				set2.add(c.getClass());
			}

			for (int j = 0; j < 30; j++) {
				card = AbstractDungeon.returnTrulyRandomCardInCombat();
				if (card.cardID.equals(prevCard.cardID))
					continue;
				if (set2.contains(card.getClass()))
					continue;
				if (j < 15 && set1.contains(card.getClass()))
					continue;
				break;
			}
			prevCard = card;

			if (card.costForTurn > 0) {
				card.updateCost(-1);
				GathererMod.costs1LessUntilPlayed.add(card);
			}

			AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(card, 1));
		}
	}

	public AbstractCard makeCopy() {
		return new OddSpoils();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
			this.upgradeMagicNumber(CARD_BONUS);
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
