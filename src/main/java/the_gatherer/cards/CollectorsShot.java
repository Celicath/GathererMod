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
import the_gatherer.GathererMod;
import the_gatherer.patches.AbstractCardEnum;

import java.util.HashSet;
import java.util.Iterator;

public class CollectorsShot extends CustomCard {
	private static final String CardID = "CollectorsShot";
	public static final String ID = GathererMod.makeID(CardID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = "img/cards/" + CardID + ".png";
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = AbstractCardEnum.LIME;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 3;
	private static final int UPGRADE_BONUS = 1;

	public CollectorsShot() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;
	}

	public void applyPowers() {
		Iterator it = AbstractDungeon.player.masterDeck.group.iterator();
		HashSet<String> ids1 = new HashSet<>();
		HashSet<String> ids2 = new HashSet<>();
		while (it.hasNext()) {
			AbstractCard c = (AbstractCard) it.next();
			if (ids1.contains(c.cardID))
				ids2.add(c.cardID);
			else ids1.add(c.cardID);
		}

		this.baseDamage = ids2.size() * this.magicNumber;

		super.applyPowers();
		this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
		this.initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));

		this.rawDescription = DESCRIPTION;
		this.initializeDescription();
	}

	public AbstractCard makeCopy() {
		return new CollectorsShot();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
