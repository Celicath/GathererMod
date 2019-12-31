package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import the_gatherer.cards.FakeCards.DoNotReplaceOption;

import java.util.ArrayList;
import java.util.UUID;

public class GrowBookAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Gatherer:ReplaceGrowBookAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;
	private int block;
	private AbstractCard card;
	private boolean upgraded;
	private boolean deckCardUpgraded;

	public GrowBookAction(UUID uuid, int block, boolean upgraded) {
		this.p = AbstractDungeon.player;
		this.block = block;

		this.deckCardUpgraded = this.upgraded = upgraded;
		for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
			if (c.uuid.equals(uuid)) {
				this.card = c;
				deckCardUpgraded = c.upgraded;
				break;
			}
		}
		this.actionType = ActionType.CARD_MANIPULATION;
		this.duration = Settings.ACTION_DUR_FASTER;
	}

	public void update() {
		if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
			this.isDone = true;
		} else {
			if (this.duration == Settings.ACTION_DUR_FASTER) {
				AbstractDungeon.cardRewardScreen.customCombatOpen(generateCardChoices(), CardRewardScreen.TEXT[1], false);

			} else if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
				AbstractCard c = AbstractDungeon.cardRewardScreen.discoveryCard;
				if (c instanceof DoNotReplaceOption) {
					c.use(p, null);
				} else {
					if (card != null) {
						AbstractDungeon.player.masterDeck.removeCard(card);
						AbstractCard cCopy1 = c.makeCopy();
						if (deckCardUpgraded) {
							cCopy1.upgrade();
						}
						AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(cCopy1, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
					}
					AbstractCard cCopy2 = c.makeCopy();
					if (upgraded) {
						cCopy2.upgrade();
					}
					AbstractDungeon.topLevelEffects.add(new ShowCardAndAddToHandEffect(cCopy2, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
				}
				AbstractDungeon.cardRewardScreen.discoveryCard = null;
			}
		}
		this.tickDuration();
	}

	private ArrayList<AbstractCard> generateCardChoices() {
		ArrayList<AbstractCard> allAvailableCards = new ArrayList<>();
		for (AbstractCard c : CardLibrary.cards.values()) {
			if (!c.hasTag(AbstractCard.CardTags.HEALING) &&
					c.type != AbstractCard.CardType.CURSE &&
					c.type != AbstractCard.CardType.STATUS &&
					c.color != AbstractDungeon.player.getCardColor() &&
					(c.rarity == AbstractCard.CardRarity.COMMON ||
							c.rarity == AbstractCard.CardRarity.UNCOMMON ||
							c.rarity == AbstractCard.CardRarity.RARE)
			) {
				allAvailableCards.add(c);
			}
		}
		ArrayList<AbstractCard> tmp = new ArrayList<>();

		while (tmp.size() < 3) {
			AbstractCard c = allAvailableCards.get(AbstractDungeon.cardRandomRng.random(allAvailableCards.size() - 1));

			if (!tmp.contains(c)) {
				tmp.add(c);
			}
		}

		ArrayList<AbstractCard> result = new ArrayList<>();
		for (AbstractCard c : tmp) {
			AbstractCard cCopy = c.makeCopy();
			if (upgraded && deckCardUpgraded) {
				cCopy.upgrade();
			}
			result.add(cCopy);
		}

		result.add(new DoNotReplaceOption(block));
		return result;
	}
}
