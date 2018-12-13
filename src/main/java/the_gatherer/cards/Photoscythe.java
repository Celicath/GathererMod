package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import the_gatherer.GathererMod;
import the_gatherer.actions.PhotoscytheAction;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.patches.CustomTags;

import java.util.Iterator;

public class Photoscythe extends CustomCard {
	private static final String RAW_ID = "Photoscythe";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 3;
	private static final int UPGRADE_BONUS = 1;

	public Photoscythe() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;
		this.isInnate = true;
		this.exhaust = true;
	}

	public void applyPowers() {
		int count = 0;
		Iterator it = AbstractDungeon.player.masterDeck.group.iterator();
		while (it.hasNext()) {
			AbstractCard c = (AbstractCard) it.next();
			if (c.hasTag(CustomTags.FLOWER)) {
				++count;
			}
		}
		this.baseDamage = count * this.magicNumber;

		super.applyPowers();
		this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
		this.initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_DEFECT_BEAM"));
		AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new CleaveEffect(), 0.1F));
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
		AbstractDungeon.actionManager.addToBottom(new PhotoscytheAction());

		this.rawDescription = DESCRIPTION;
		this.initializeDescription();
	}

	public AbstractCard makeCopy() {
		return new Photoscythe();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
