package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class HarmonicSymbolAction extends AbstractGameAction {
	private boolean freeToPlayOnce = false;
	private int damage;
	private int block;
	private AbstractPlayer p;
	private AbstractMonster m;
	private DamageType damageTypeForTurn;
	private int energyOnUse = -1;

	public HarmonicSymbolAction(AbstractPlayer p, AbstractMonster m, int damage, int block, DamageType damageTypeForTurn, boolean freeToPlayOnce, int energyOnUse) {
		this.p = p;
		this.m = m;
		this.damage = damage;
		this.block = block;
		this.freeToPlayOnce = freeToPlayOnce;
		this.duration = Settings.ACTION_DUR_XFAST;
		this.actionType = ActionType.SPECIAL;
		this.damageTypeForTurn = damageTypeForTurn;
		this.energyOnUse = energyOnUse;
	}

	public void update() {
		int effect = EnergyPanel.totalCount;
		if (this.energyOnUse != -1) {
			effect = this.energyOnUse;
		}

		if (this.p.hasRelic("Chemical X")) {
			effect += 2;
			this.p.getRelic("Chemical X").flash();
		}

		if (effect > 0) {
			for(int i = 0; i < effect; ++i) {
				AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.p, this.p, this.block));
			}
			for(int i = 0; i < effect; ++i) {
				AbstractDungeon.actionManager.addToBottom(new DamageAction(this.m, new DamageInfo(this.p, this.damage, this.damageTypeForTurn), AttackEffect.BLUNT_LIGHT));
			}
			if (!this.freeToPlayOnce) {
				this.p.energy.use(EnergyPanel.totalCount);
			}
		}

		this.isDone = true;
	}
}
