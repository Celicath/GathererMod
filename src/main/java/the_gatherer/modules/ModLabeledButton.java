package the_gatherer.modules;

import basemod.IUIElement;
import basemod.ModLabel;
import basemod.ModPanel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

import java.util.function.Consumer;

public class ModLabeledButton implements IUIElement {
	private static final float TEXT_X_OFFSET = 60.0f;
	private static final float TEXT_Y_OFFSET = 24.0f;

	public ModFixedButton toggle;
	public ModLabel text;

	public ModLabeledButton(String labelText, float xPos, float yPos,
	                        Color color, BitmapFont font, ModPanel p,
	                        Consumer<ModLabel> labelUpdate, Consumer<ModFixedButton> c) {
		toggle = new ModFixedButton(xPos, yPos, p, c);
		text = new ModLabel(labelText, xPos + TEXT_X_OFFSET, yPos + TEXT_Y_OFFSET,
				color, font, p, labelUpdate);

		// TODO: implement multi-line text
		toggle.wrapHitboxToText(labelText, 1000.0f, 0.0f, font);
	}


	@Override
	public void render(SpriteBatch sb) {
		text.color = (toggle.hb.hovered ? Color.YELLOW : Settings.CREAM_COLOR);

		toggle.render(sb);
		text.render(sb);
	}

	@Override
	public void update() {
		toggle.update();
		text.update();
	}

	@Override
	public int renderLayer() {
		return ModPanel.MIDDLE_LAYER;
	}

	@Override
	public int updateOrder() {
		return ModPanel.DEFAULT_UPDATE;
	}

}
