package the_gatherer.cards;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.cards.Helper.AbstractTaggedCard;
import the_gatherer.patches.CardColorEnum;

import java.util.ArrayList;

public class Duality extends AbstractTaggedCard {
	private static final String RAW_ID = "Duality";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_POWER = 18;
	private static final int ATTACK_UPGRADE = 4;
	private static final int BLOCK_POWER = 16;
	private static final int BLOCK_UPGRADE = 4;

	private ArrayList<AbstractCard> tooltips;

	@Override
	public void setTag(int tagNo) {
		if (this.misc == 1) {
			this.type = CardType.SKILL;
			this.target = CardTarget.SELF;
			this.rawDescription = EXTENDED_DESCRIPTION[0];
			this.initializeDescription();
		} else {
			this.type = CardType.ATTACK;
			this.target = CardTarget.ENEMY;
			this.rawDescription = DESCRIPTION;
			this.initializeDescription();
		}
		if (tooltips != null) {
			for (AbstractCard tooltip : tooltips) {
				if (tooltip instanceof Duality) {
					((Duality) tooltip).setTag(tagNo == 1 ? 0 : 1);
				}
			}
		}

		super.setTag(tagNo);
	}

	@Override
	public String getTagName(int tag) {
		switch (tag) {
			case 0:
				return "<ATK>";
			case 1:
				return "<DEF>";
			default:
				return "<?>";
		}
	}

	public Duality() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.baseDamage = ATTACK_POWER;
		this.baseBlock = BLOCK_POWER;

		AlwaysRetainField.alwaysRetain.set(this, true);

		tooltips = new ArrayList<>();
		Duality tooltip = new Duality(true, 1);
		tooltips.add(tooltip);
		setTag(0);
	}

	public Duality(boolean disableTooltip, int tag) {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		if (!disableTooltip) {
			GathererMod.logger.info("Nope, it's still considered as disable Tooltip.");
		}

		this.baseDamage = ATTACK_POWER;
		this.baseBlock = BLOCK_POWER;
		setTag(tag);
	}

	@Override
	public void initializeDescription() {
		super.initializeDescription();
		keywords.remove("switch");
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (this.type == CardType.ATTACK)
			AbstractDungeon.actionManager.addToBottom(new DamageAction(m,
					new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_HEAVY));
		else
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
	}

	@Override
	public void triggerOnEndOfTurnForPlayingCard() {
		transform();
	}

	private void transform() {
		setTag(this.misc == 1 ? 0 : 1);
	}

	public AbstractCard makeCopy() {
		return new Duality();
	}

	@Override
	public void upgradeEffect() {
		this.upgradeDamage(ATTACK_UPGRADE);
		this.upgradeBlock(BLOCK_UPGRADE);
		if (tooltips != null) {
			for (AbstractCard tooltip : tooltips) {
				tooltip.upgrade();
			}
		}
	}

	@Override
	public void renderCardTip(SpriteBatch sb) {
		super.renderCardTip(sb);
		boolean renderTip = (boolean) ReflectionHacks.getPrivate(this, AbstractCard.class, "renderTip");

		int count = 0;
		if (!Settings.hideCards && renderTip) {
			if (AbstractDungeon.player != null && AbstractDungeon.player.isDraggingCard) {
				return;
			}
			for (AbstractCard c : tooltips) {
				float dx = (AbstractCard.IMG_WIDTH * 0.9f - 5f) * drawScale;
				float dy = (AbstractCard.IMG_HEIGHT * 0.4f - 5f) * drawScale;
				if (current_x > Settings.WIDTH * 0.75f) {
					c.current_x = current_x + dx;
				} else {
					c.current_x = current_x - dx;
				}
				if (count == 0) {
					c.current_y = current_y + dy;
				} else {
					c.current_y = current_y - dy;
				}
				c.drawScale = drawScale * 0.8f;
				c.render(sb);
				count++;
			}
		}
	}
}
