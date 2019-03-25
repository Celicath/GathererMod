package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import the_gatherer.GathererMod;
import the_gatherer.cards.Helper.GathererCardHelper;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.patches.CustomTags;
import the_gatherer.vfx.FlowerBeamFollowUpEffect;

public class FlowerBeam extends CustomCard {
	private static final String RAW_ID = "FlowerBeam";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 7;
	private static final int UPGRADE_BONUS = 2;

	public FlowerBeam() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseDamage = POWER;
		this.isMultiDamage = true;
		this.tags.add(CustomTags.FLOWER);

		this.rawDescription = GetRawDescription();
		this.initializeDescription();
	}

	private String GetRawDescription() {
		return DESCRIPTION + GathererCardHelper.FlowerSuffix(timesUpgraded);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
		AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SKY.cpy())));
		AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(m.hb.cX, m.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.3F));
		DamageInfo info = new DamageInfo(p, this.baseDamage, this.damageTypeForTurn);
		info.applyPowers(p, m);
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, info, AbstractGameAction.AttackEffect.NONE));
		AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlowerBeamFollowUpEffect(m.hb.cX, m.hb.cY), FlowerBeamFollowUpEffect.EFFECT_DUR_CARD));
		AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		int before = this.damage;
		super.calculateCardDamage(mo);

		int aliveMonsters = 0;
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
			if ((!m.isDying) && (m.currentHealth > 0)) {
				aliveMonsters++;
			}
		}

		if (aliveMonsters != 1) {
			this.damage = before;
		}
	}

	@Override
	public void initializeDescription() {
		super.initializeDescription();
		if (!keywords.contains("flower beam tooltip")) {
			keywords.add("flower beam tooltip");
		}
	}

	public AbstractCard makeCopy() {
		return new FlowerBeam();
	}

	@Override
	public boolean canUpgrade() {
		return timesUpgraded < 3;
	}

	public void upgrade() {
		if (timesUpgraded < 3) {
			++this.timesUpgraded;
			if (timesUpgraded == 3) {
				upgradeBaseCost(COST - 1);
			} else {
				upgradeDamage(UPGRADE_BONUS);
			}
			this.upgraded = true;
			this.name = NAME + "+" + this.timesUpgraded;
			this.initializeTitle();
			this.rawDescription = GetRawDescription();
			this.initializeDescription();
		}
	}
}
