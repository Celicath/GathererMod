package the_gatherer.patches;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.*;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import the_gatherer.interfaces.ColoredTextCard;

import static com.megacrit.cardcrawl.cards.AbstractCard.IMG_WIDTH;

public class ColoredTextCardPatch {
	@SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
	public static class InitDescription {
		private static GlyphLayout gl = new GlyphLayout();

		@SpirePrefixPatch
		public static SpireReturn Prefix(AbstractCard __instance) {
			if (__instance instanceof ColoredTextCard) {
				ShrinkLongDescription.ShrinkInitializeDescription.Prefix(__instance);
				__instance.keywords.clear();
				if (Settings.lineBreakViaCharacter) {
					initializeDescriptionCN(__instance);
				} else {
					StringBuilder sbuilder = new StringBuilder();
					StringBuilder sbuilder2 = new StringBuilder();
					float CARD_ENERGY_IMG_WIDTH = 24.0F * Settings.scale;
					float DESC_BOX_WIDTH = Settings.BIG_TEXT_MODE ? IMG_WIDTH * 0.95F : IMG_WIDTH * 0.79F;

					__instance.description.clear();
					sbuilder.setLength(0);
					float currentWidth = 0.0F;

					for (String word : __instance.rawDescription.split(" ")) {
						boolean isKeyword = false;
						sbuilder2.setLength(0);
						sbuilder2.append(" ");
						if (word.length() > 0 && word.charAt(word.length() - 1) != ']' && !Character.isLetterOrDigit(word.charAt(word.length() - 1))) {
							sbuilder2.insert(0, word.charAt(word.length() - 1));
							word = word.substring(0, word.length() - 1);
						}

						String[] var13 = new String[]{word};
						RenderDescriptionEnergy.AlterEnergyKeyword.Insert(__instance, var13);
						word = var13[0];
						String keywordTmp = word.toLowerCase();
						String retVal = GameDictionary.parentWord.get(keywordTmp);
						if (retVal != null) keywordTmp = retVal;

						if (GameDictionary.keywords.containsKey(keywordTmp)) {
							String[] var10 = new String[]{word};
							MultiwordKeywords.InitializeDescription.Insert(__instance, var10, keywordTmp);
							word = var10[0];
							if (!__instance.keywords.contains(keywordTmp)) {
								__instance.keywords.add(keywordTmp);
							}

							gl.reset();
							gl.setText(FontHelper.cardDescFont_N, sbuilder2);
							float tmp = gl.width;
							gl.setText(FontHelper.cardDescFont_N, word);
							gl.width += tmp;
							isKeyword = true;
						} else if (word.startsWith("[E]")) {
							gl.reset();
							gl.setText(FontHelper.cardDescFont_N, sbuilder2);
							gl.width += CARD_ENERGY_IMG_WIDTH;
							__instance.keywords.add("[E]");
							FixColoredTextOffset.Insert(__instance, gl, word);
						} else if (!word.equals("!D") && !word.equals("!B") && !word.equals("!M")) {
							if (word.equals("NL")) {
								gl.width = 0.0F;
								word = "";
								__instance.description.add(new DescriptionLine(sbuilder.toString().trim(), currentWidth));
								currentWidth = 0.0F;
								sbuilder.setLength(0);
							} else {
								gl.setText(FontHelper.cardDescFont_N, word + sbuilder2);
							}
						} else {
							gl.setText(FontHelper.cardDescFont_N, word);
						}

						GlyphLayout[] var11 = new GlyphLayout[]{gl};
						String[] var12 = new String[]{word};
						FixDescriptionWidthCustomDynamicVariable.Insert(__instance, var11, var12);
						gl = var11[0];
						word = var12[0];
						if (currentWidth + gl.width > DESC_BOX_WIDTH) {
							__instance.description.add(new DescriptionLine(sbuilder.toString().trim(), currentWidth));
							sbuilder.setLength(0);
							currentWidth = gl.width;
						} else {
							currentWidth += gl.width;
						}

						if (isKeyword) {
							sbuilder.append('*');
						}

						sbuilder.append(word).append(sbuilder2);
					}

					if (!sbuilder.toString().trim().isEmpty()) {
						__instance.description.add(new DescriptionLine(sbuilder.toString().trim(), currentWidth));
					}
				}

				RenderDescriptionEnergy.AlterEnergyKeyword.Postfix(__instance);
				ShrinkLongDescription.ShrinkInitializeDescription.Postfix(__instance);

				return SpireReturn.Return(null);
			}
			return SpireReturn.Continue();
		}

		public static void initializeDescriptionCN(AbstractCard __instance) {
			StringBuilder sbuilder = new StringBuilder();
			StringBuilder sbuilder2 = new StringBuilder();
			float CARD_ENERGY_IMG_WIDTH = 24.0F * Settings.scale;
			float CN_DESC_BOX_WIDTH = IMG_WIDTH * 0.72F;
			float MAGIC_NUM_W = 20.0F * Settings.scale;

			ShrinkLongDescription.ShrinkInitializeDescription.Prefix(__instance);
			__instance.description.clear();
			int numLines = 1;
			sbuilder.setLength(0);
			float currentWidth = 0.0F;

			for (String word : __instance.rawDescription.split(" ")) {
				word = word.trim();
				if (Settings.manualLineBreak || Settings.manualAndAutoLineBreak || !word.contains("NL")) {
					if ((!word.equals("NL") || sbuilder.length() != 0) && !word.isEmpty()) {
						String[] var25 = new String[]{word};
						RenderDescriptionEnergy.AlterEnergyKeyword.Insert(__instance, var25);
						word = var25[0];
						String keywordTmp = word.toLowerCase();
						String retVal = GameDictionary.parentWord.get(keywordTmp);
						if (retVal != null) keywordTmp = retVal;
						if (GameDictionary.keywords.containsKey(keywordTmp)) {
							if (!__instance.keywords.contains(keywordTmp)) {
								__instance.keywords.add(keywordTmp);
							}

							String[] var12 = new String[]{word};
							CNUniqueKeywords.Insert(__instance, var12);
							word = var12[0];
							gl.setText(FontHelper.cardDescFont_N, word);
							if (currentWidth + gl.width > CN_DESC_BOX_WIDTH) {
								++numLines;
								__instance.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
								sbuilder.setLength(0);
								currentWidth = gl.width;
								sbuilder.append(" *").append(word).append(" ");
							} else {
								sbuilder.append(" *").append(word).append(" ");
								currentWidth += gl.width;
							}
						} else if (word.startsWith("[E]")) {
							__instance.keywords.add("[E]");

							if (currentWidth + CARD_ENERGY_IMG_WIDTH > CN_DESC_BOX_WIDTH) {
								++numLines;
								__instance.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
								sbuilder.setLength(0);
								currentWidth = CARD_ENERGY_IMG_WIDTH;
								sbuilder.append(" ").append(word).append(" ");
							} else {
								sbuilder.append(" ").append(word).append(" ");
								currentWidth += CARD_ENERGY_IMG_WIDTH;
							}
						} else if (word.equals("!D!")) {
							if (currentWidth + MAGIC_NUM_W > CN_DESC_BOX_WIDTH) {
								++numLines;
								__instance.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
								sbuilder.setLength(0);
								currentWidth = MAGIC_NUM_W;
								sbuilder.append(" D ");
							} else {
								sbuilder.append(" D ");
								currentWidth += MAGIC_NUM_W;
							}
						} else if (word.equals("!B!")) {
							if (currentWidth + MAGIC_NUM_W > CN_DESC_BOX_WIDTH) {
								++numLines;
								__instance.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
								sbuilder.setLength(0);
								currentWidth = MAGIC_NUM_W;
								sbuilder.append(" ").append(word).append("! ");
							} else {
								sbuilder.append(" ").append(word).append("! ");
								currentWidth += MAGIC_NUM_W;
							}
						} else if (word.equals("!M!")) {
							if (currentWidth + MAGIC_NUM_W > CN_DESC_BOX_WIDTH) {
								++numLines;
								__instance.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
								sbuilder.setLength(0);
								currentWidth = MAGIC_NUM_W;
								sbuilder.append(" ").append(word).append("! ");
							} else {
								sbuilder.append(" ").append(word).append("! ");
								currentWidth += MAGIC_NUM_W;
							}
						} else if ((Settings.manualLineBreak || Settings.manualAndAutoLineBreak) && word.equals("NL") && sbuilder.length() != 0) {
							gl.width = 0.0F;
							word = "";
							String[] var17 = new String[]{word};
							float[] var18 = new float[]{currentWidth};
							int[] var19 = new int[]{numLines};
							StringBuilder[] var20 = new StringBuilder[]{sbuilder};
							CNCardTextColors.Insert(__instance, var17, var18, var19, var20, CN_DESC_BOX_WIDTH);
							word = var17[0];
							currentWidth = var18[0];
							numLines = var19[0];
							sbuilder = var20[0];
							__instance.description.add(new DescriptionLine(sbuilder.toString().trim(), currentWidth));
							currentWidth = 0.0F;
							++numLines;
							sbuilder.setLength(0);
						} else if (word.charAt(0) == '*') {
							gl.setText(FontHelper.cardDescFont_N, word.substring(1));
							if (currentWidth + gl.width > CN_DESC_BOX_WIDTH) {
								++numLines;
								__instance.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
								sbuilder.setLength(0);
								currentWidth = gl.width;
								sbuilder.append(" ").append(word).append(" ");
							} else {
								sbuilder.append(" ").append(word).append(" ");
								currentWidth += gl.width;
							}
						} else {
							word = word.trim();
							String[] var21 = new String[]{word};
							float[] var22 = new float[]{currentWidth};
							StringBuilder[] var23 = new StringBuilder[]{sbuilder};
							int[] var24 = new int[]{numLines};
							FixDescriptionWidthCustomDynamicVariableCN.Insert(__instance, var21, var22, var23, var24, CN_DESC_BOX_WIDTH);
							word = var21[0];
							currentWidth = var22[0];
							sbuilder = var23[0];
							numLines = var24[0];
							String[] var13 = new String[]{word};
							float[] var14 = new float[]{currentWidth};
							StringBuilder[] var15 = new StringBuilder[]{sbuilder};
							int[] var16 = new int[]{numLines};
							RenderDescriptionEnergy.FixEForChinese.Insert(__instance, var13, var14, var15, var16, CARD_ENERGY_IMG_WIDTH, CN_DESC_BOX_WIDTH);
							word = var13[0];
							currentWidth = var14[0];
							sbuilder = var15[0];
							numLines = var16[0];
							char[] wordArray = word.toCharArray();

							boolean insideBrackets = false;
							for (int i = 0; i < wordArray.length; ++i) {
								char c = wordArray[i];
								if (c == '[') {
									insideBrackets = true;
									sbuilder2.setLength(0);
								}
								if (insideBrackets) {
									sbuilder2.append(c);
								}
								if (c == ']') {
									insideBrackets = false;
									if (sbuilder2.length() == 2) {
										sbuilder2.setLength(0);
									}
								}
								gl.setText(FontHelper.cardDescFont_N, String.valueOf(c));
								sbuilder.append(c);
								if (!Settings.manualLineBreak && !insideBrackets) {
									if (currentWidth + gl.width > CN_DESC_BOX_WIDTH) {
										++numLines;
										__instance.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
										sbuilder.setLength(0);
										if (sbuilder2.length() != 0) {
											sbuilder.append(sbuilder2);
										}
										currentWidth = gl.width;
									} else {
										currentWidth += gl.width;
									}
								}
							}
						}
					} else {
						currentWidth = 0.0F;
					}
				}
			}

			if (sbuilder.length() != 0) {
				__instance.description.add(new DescriptionLine(sbuilder.toString(), currentWidth));
			}

			int removeLine = -1;

			for (int i = 0; i < __instance.description.size(); ++i) {
				if (__instance.description.get(i).text.equals(LocalizedStrings.PERIOD)) {
					StringBuilder var34 = new StringBuilder();
					DescriptionLine var10002 = __instance.description.get(i - 1);
					var10002.text = var34.append(var10002.text).append(LocalizedStrings.PERIOD).toString();
					removeLine = i;
				}
			}

			if (removeLine != -1) {
				__instance.description.remove(removeLine);
			}

			RenderDescriptionEnergy.AlterEnergyKeyword.Postfix(__instance);
			ShrinkLongDescription.ShrinkInitializeDescription.Postfix(__instance);
		}
	}
}
